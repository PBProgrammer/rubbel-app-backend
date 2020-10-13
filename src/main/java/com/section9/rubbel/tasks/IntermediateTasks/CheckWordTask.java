package com.section9.rubbel.tasks.IntermediateTasks;

import com.section9.rubbel.models.RubbelGameSession;
import com.section9.rubbel.services.GameSessionManager;
import com.section9.rubbel.tasks.base.Action;
import com.section9.rubbel.tasks.base.IntermediateTask;

import java.util.List;

public class CheckWordTask extends IntermediateTask {
    public CheckWordTask(String id, List<Action> actions) {
        super(id, actions);
    }

    @Override
    public Action getResolvedAction(RubbelGameSession gameSession) {
        return null;
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {

    }
}
