package com.section9.rubbel.tasks.base;

import com.section9.rubbel.models.RubbelGameSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IntermediateTask extends Task implements IIntermediateTask{

    public static final String RESET_AFTER_PASS_TASK_ID = "RESET_AFTER_PASS";
    public static final String IS_GAME_FINISHED_TASK_ID = "IS_GAME_FINISHED";
    public static final String VETO_EVALUATION_ID = "VETO_EVALUATION";
    public static final String ROUND_EVALUATION_ID = "ROUND_EVALUATION";


    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    List<Action> actions;

    public IntermediateTask(String id, List<Action> actions) {
        super(id);
        actions.forEach(action -> action.setOriginTaskId(id));
        setActions(actions);
    }


    @Override
    public boolean taskCompletable(RubbelGameSession gameSession) {
        return true;
    }

}
