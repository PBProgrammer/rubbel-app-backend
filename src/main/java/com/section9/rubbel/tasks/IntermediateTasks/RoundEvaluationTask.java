package com.section9.rubbel.tasks.IntermediateTasks;

import com.section9.rubbel.models.*;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.Action;
import com.section9.rubbel.tasks.base.IntermediateTask;

import java.util.List;

public class RoundEvaluationTask extends IntermediateTask {

    public RoundEvaluationTask(String id, List<Action> actions) {
        super(id, actions);
    }

    @Override
    public Action getResolvedAction(RubbelGameSession gameSession) {
        return getActions().stream().findFirst().orElseThrow();
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        // TODO handle gemäß gesetzten optionen
        if(gameSession.getVotingContainer().isAccepted()) {
            gameSession.fixLetterTokens();
            WordsOnBoardContainer wordsOnBoard = gameSession.getNewWordsOnBoardContainer();
            if (wordsOnBoard != null) {
                wordsOnBoard.getWords().forEach(word -> highlightWord(word, false));
                int roundScore = wordsOnBoard.calculateTotalScore();
                if(gameSession.getRackByPlayerId(wordsOnBoard.getOwner()).size() == 0) {
                    roundScore += GameConfigOptions.BONUS_POINTS;
                }
                Player player = gameSession.getPlayerById(wordsOnBoard.getOwner());
                int totalPlayerScore = player.getScore() + roundScore;
                player.setScore(totalPlayerScore);
                gameSession.setNewWordsOnBoardContainer(null);
            }
            gameSession.setFixedGameBoard((List<Square>) Util.deepCopy(gameSession.getGameBoard()));
        } else {
            gameSession.transferUnfixedLetterTokensOnBoardBackToActivePlayer();
            gameSession.resetGameBoardToFixedState();
        }
        gameSession.setVotingContainer(null);

//        if (PlayerAction.CONFIRM_WORD_ACTION_ID.equals(resolvedAction.getId())) {
//            gameSession.fixLetterTokens();
//            if (gameSession.getNewWordsOnBoard() != null) {
//                gameSession.getNewWordsOnBoard().getWords().forEach(word -> highlightWord(word, false));
//                gameSession.setNewWordsOnBoard(null);
//            }
//            gameSession.setFixedGameBoard((List<Square>) Util.deepCopy(gameSession.getGameBoard()));
//            return;
//        }
//
//        if (PlayerAction.VETO_ACTION_ID.equals(resolvedAction.getId())) {
//            if (resolvedAction.getPayload() != null) {
//                //TODO publish answer for next task e.g. override newSquaresOnBoard;
//            }
//            return;
//        }
    }

    private void highlightWord(Word word, boolean value) {
        word.getSquares().forEach(square -> square.setHighlighted(value));
    }
}
