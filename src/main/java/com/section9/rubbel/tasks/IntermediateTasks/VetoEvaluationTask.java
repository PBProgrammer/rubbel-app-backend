package com.section9.rubbel.tasks.IntermediateTasks;

import com.section9.rubbel.models.LetterToken;
import com.section9.rubbel.models.Rack;
import com.section9.rubbel.models.RubbelGameSession;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.Action;
import com.section9.rubbel.tasks.base.IntermediateTask;

import java.util.List;

public class VetoEvaluationTask extends IntermediateTask {

    public VetoEvaluationTask(String id, List<Action> actions) {
        super(id, actions);
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        // TODO handle gemäß gesetzten optionen
        if(!gameSession.getVotingContainer().isAccepted()) {
            gameSession.transferUnfixedLetterTokensOnBoardBackToActivePlayer();
            gameSession.resetGameBoardToFixedState();
            gameSession.setVotingContainer(null);
        } else {

        }
    }

    @Override
    public Action getResolvedAction(RubbelGameSession gameSession) {
        return getActions().stream().findFirst().orElseThrow();
    }
}
