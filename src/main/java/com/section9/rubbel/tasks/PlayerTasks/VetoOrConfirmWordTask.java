package com.section9.rubbel.tasks.PlayerTasks;

import com.section9.rubbel.enums.Axis;
import com.section9.rubbel.enums.Direction;
import com.section9.rubbel.models.*;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class VetoOrConfirmWordTask extends PlayerTask {

    public static int WORD_LENGTH_LOWER_LIMIT = 2; // could be part of customization -> lobby settings

    public VetoOrConfirmWordTask(String id, List<UUID> owners, List<PlayerAction> actions) {
        super(id, owners, actions);
        setModal(true);
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        setEndTimestamp(Util.getEndTimestamp(gameSession.getGameConfig().getVetoTime()));
        List<Square> unfixedSquares = gameSession.getSquaresWithUnfixedLetterTokens();
        Axis unfixedLetterTokensAxis = getUnfixedLetterTokensAxis(unfixedSquares);
        List<Word> detectedWordCandidates = new ArrayList<>();
        if (unfixedLetterTokensAxis == Axis.SINGLE_TOKEN) {
            detectedWordCandidates.add(getWordOnAxis(unfixedSquares.get(0), Axis.HORIZONTAL, gameSession.getGameBoard()));
            detectedWordCandidates.add(getWordOnAxis(unfixedSquares.get(0), Axis.VERTICAL, gameSession.getGameBoard()));
        } else {
            detectedWordCandidates.add(getWordOnAxis(unfixedSquares.get(0), unfixedLetterTokensAxis, gameSession.getGameBoard()));
            for (Square square : unfixedSquares) {
                detectedWordCandidates.add(getWordOnAxis(square, getOrthogonalAxis(unfixedLetterTokensAxis), gameSession.getGameBoard()));
            }
        }
        gameSession.updateNewWordsOnBoardContainer(gameSession.getActivePlayer(), detectedWordCandidates.stream().filter(word -> word.getSquares().size() > 0).collect(Collectors.toList()));
    }

    @Override
    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction) {
        if (resolvedAction.getId().equals(PlayerAction.VETO_ACTION_ID)) {
            List<UUID> challengedWordsIds = ((List<String>) resolvedAction.getPayload()).stream().map(UUID::fromString).collect(Collectors.toList());
            gameSession.getNewWordsOnBoardContainer().getWords().forEach(word -> {
                for (UUID id : challengedWordsIds) {
                    if (id.equals(word.getId())) {
                        word.setChallenged(true);
                    }
                }
            });
        }
    }

    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        Optional<PlayerAction> vetoAction = getReceivedActions().values().stream().filter(receivedAction -> PlayerAction.VETO_ACTION_ID.equals(receivedAction.getId())).findFirst();
        VotingContainer votingContainer = new VotingContainer();
        gameSession.setVotingContainer(votingContainer);
        if (vetoAction.isPresent()) {
            votingContainer.setVetoInitiator(vetoAction.get().getExecutor());
            votingContainer.setActivePlayer(gameSession.getActivePlayer());
            return vetoAction.get();
        }
        votingContainer.setAccepted(true);
        return actions
                .stream()
                .filter(action -> action.getId().equals(PlayerAction.CONFIRM_WORD_ACTION_ID))
                .findFirst().orElseThrow();
    }

    @Override
    public boolean taskCompletable(RubbelGameSession gameSession) {
        if (isAutoComplete()) {
            return true;
        }

        Optional<PlayerAction> vetoAction = getReceivedActions().values().stream().filter(receivedAction -> PlayerAction.VETO_ACTION_ID.equals(receivedAction.getId())).findFirst();
        if (vetoAction.isPresent() && vetoAction.get().getPayload() != null) {
            return true;
        }

        if (owners.isEmpty() && getReceivedActions().values().stream().allMatch(receivedAction -> PlayerAction.CONFIRM_WORD_ACTION_ID.equals(receivedAction.getId()))) {
            return true;
        }

        return false;
    }

    private Word getWordOnAxis(Square rootSquare, Axis axis, List<Square> gameBoard) {
        Direction wordHeadPart;
        Direction wordTailPart;
        if (axis == Axis.VERTICAL) {
            wordHeadPart = Direction.NORTH;
            wordTailPart = Direction.SOUTH;
        } else {
            wordHeadPart = Direction.WEST;
            wordTailPart = Direction.EAST;
        }
        Square currentSquare = rootSquare;
        Word newWord = new Word();
        newWord.getSquares().add(currentSquare);
        while (true) {
            // get head part
            Square neighbor = getNeighbor(currentSquare, wordHeadPart, gameBoard);
            if (neighbor != null && !neighbor.isEmpty()) {
                newWord.add(0, neighbor);
                currentSquare = neighbor;
            } else {
                break;
            }
        }
        currentSquare = rootSquare;
        while (true) {
            // get tail part
            Square neighbor = getNeighbor(currentSquare, wordTailPart, gameBoard);
            if (neighbor != null && !neighbor.isEmpty()) {
                newWord.add(neighbor);
                currentSquare = neighbor;
            } else {
                break;
            }
        }
        if (newWord.getSquares().size() >= WORD_LENGTH_LOWER_LIMIT) {
            highlightWord(newWord, true);
            String label = newWord.getSquares().stream().map(square -> square.getLetterToken().getSymbol()).collect(Collectors.joining(""));
            newWord.setLabel(label);
            return newWord;
        }
        return new Word();
//        return null;
    }

    private void highlightWord(Word word, boolean value) {
        word.getSquares().forEach(square -> square.setHighlighted(value));
    }

    private Square getNeighbor(Square square, Direction direction, List<Square> gameBoard) {
        int index = square.getIndex();
        switch (direction) {
            case NORTH:
                boolean isNotAtTopBorder = index > 14;
                if (isNotAtTopBorder)
                    return gameBoard.get(index - 15);
            case EAST:
                boolean isNotAtRightBorder = (index + 1) % 15 != 0;
                if (isNotAtRightBorder)
                    return gameBoard.get(index + 1);
            case SOUTH:
                boolean isNotAtBottomBorder = index < 210;
                if (isNotAtBottomBorder)
                    return gameBoard.get(index + 15);
            case WEST:
                boolean isNotAtLeftBorder = index % 15 != 0;
                if (isNotAtLeftBorder)
                    return gameBoard.get(index - 1);
            default:
                return null;
        }
    }

    private Axis getOrthogonalAxis(Axis axis) {
        if (Axis.HORIZONTAL == axis) {
            return Axis.VERTICAL;
        }
        if (Axis.VERTICAL == axis) {
            return Axis.HORIZONTAL;
        }
        return Axis.SINGLE_TOKEN;
    }

    private Axis getUnfixedLetterTokensAxis(List<Square> unfixedSquares) {
        if (unfixedSquares.size() == 1) {
            return Axis.SINGLE_TOKEN;
        }
        Position position = unfixedSquares.get(0).getPosition();
        AtomicBoolean xAxisConsistent = new AtomicBoolean(true);
        AtomicBoolean yAxisConsistent = new AtomicBoolean(true);
        unfixedSquares.stream().map(Square::getPosition).forEach(nextPosition -> {
            if (nextPosition.getX() != position.getX()) {
                xAxisConsistent.set(false);
            }
            if (nextPosition.getY() != position.getY()) {
                yAxisConsistent.set(false);
            }
        });
        if (xAxisConsistent.get()) {
            return Axis.VERTICAL;
        } else if (yAxisConsistent.get()) {
            return Axis.HORIZONTAL;
        }
        throw new EnumConstantNotPresentException(Axis.class, "No axis found. You should check this.");
    }
}
