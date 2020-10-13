package com.section9.rubbel.tasks.IntermediateTasks;

import com.section9.rubbel.models.LetterToken;
import com.section9.rubbel.models.Rack;
import com.section9.rubbel.models.RubbelGameSession;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.Action;
import com.section9.rubbel.tasks.base.IntermediateTask;

import java.util.List;

public class IsGameFinishedTask extends IntermediateTask {

    private boolean rackNotFull = false;

    public IsGameFinishedTask(String id, List<Action> actions) {
        super(id, actions);
    }

    @Override
    public Action getResolvedAction(RubbelGameSession gameSession) {
        boolean maxPassesReached = gameSession.getConsecutivePasses() >= gameSession.getPlayerIds().size() * 2;
        if (maxPassesReached || rackNotFull) {
            return getActions().stream().filter(action -> action.getId().equals(Action.FINISH_GAME_ACTION_ID)).findFirst().orElseThrow();
        }
        gameSession.determineAndSetNextPlayer();
        return getActions().stream().filter(action -> action.getId().equals(Action.GAME_CONTINUES_ACTION_ID)).findFirst().orElseThrow();
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        Rack rack = gameSession.getRackByPlayerId(gameSession.getActivePlayer());
        while(rack.size() < 7) { // TODO Konstante?
            LetterToken newLetterToken = Util.getAndRemoveRandomLetterTokenFromBank(gameSession.getLetterTokenBank());
            if(newLetterToken == null) {
                rackNotFull = true;
                break;
            }
            rack.add(newLetterToken);
        }
    }
}
