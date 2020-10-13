package com.section9.rubbel.tasks.PlayerTasks;

import com.section9.rubbel.models.*;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerTurnTask extends PlayerTask {


    public PlayerTurnTask(String id, List<UUID> owners, List<PlayerAction> actions) {
        super(id, owners, actions);
        setModal(false);
    }

    private void saveInitialBoardState(List<Square> boardState) {
        getTaskData().put("boardState", boardState);
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        saveInitialBoardState(gameSession.getGameBoard());
        long endTimeStamp = Util.getEndTimestamp(gameSession.getGameConfig().getTimePerTurn());
        setEndTimestamp(endTimeStamp);
    }


    @Override
    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction) {
        if (resolvedAction.getId().equals(PlayerAction.FINALIZE_PLACED_LETTERS_ACTION_ID)) {
            intermediateBlankSquaresUpdate(gameSession, resolvedAction);
        } else {
            gameSession.transferUnfixedLetterTokensOnBoardBackToActivePlayer();
            gameSession.resetGameBoardToFixedState();
            if (resolvedAction.getId().equals(PlayerAction.PASS_TURN_ACTION_ID)) {
                gameSession.incrementConsecutivePasses();
            }
        }
    }

    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        if (isAutoComplete()) {
            return getActions().stream().filter(action -> action.getId().equals(PlayerAction.PASS_TURN_ACTION_ID)).findFirst().orElseThrow();
        }
        return getReceivedActions().values().stream().findFirst().orElseThrow();
    }

    private void intermediateBlankSquaresUpdate(RubbelGameSession gameSession, PlayerAction resolvedAction) {
        if(resolvedAction.getPayload() != null) {
            ArrayList<Map<String, Object>> squaresUpdate = (ArrayList<Map<String, Object>>) resolvedAction.getPayload();
            squaresUpdate.forEach(squareUpdateMap -> {
                int squareIndex = (Integer) squareUpdateMap.get("index");
                String letter = (String) squareUpdateMap.get("letter");
                gameSession.updateSquareSymbol(squareIndex, letter);
            });
        }
    }

}
