package com.section9.rubbel.tasks.base;

import com.section9.rubbel.models.RubbelGameSession;

import java.util.List;

public class Action{

    public static final String FINISH_GAME_ACTION_ID = "FINISH_GAME_ACTION";
    public static final String GAME_CONTINUES_ACTION_ID = "GAME_CONTINUES_ACTION";
    public static final String TO_ROUND_EVALUATION_ACTION_ID = "TO_ROUND_EVALUATION_ACTION";
    public static final String TO_GAME_IS_FINISHED_ACTION_ID = "TO_GAME_IS_FINISHED_ACTION";


    String id;
    String targetTaskId;
    String originTaskId;

    public Action() {

    }

    public Action(String id, String originTaskId, String targetTaskId) {
        this.id = id;
        this.originTaskId = originTaskId;
        this.targetTaskId = targetTaskId;
    }

    public Action(String id, String targetTaskId) {
        this.id = id;
        this.targetTaskId = targetTaskId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetTaskId() {
        return targetTaskId;
    }

    public void setTargetTaskId(String targetTaskId) {
        this.targetTaskId = targetTaskId;
    }

    public String getOriginTaskId() {
        return originTaskId;
    }

    public void setOriginTaskId(String originTaskId) {
        this.originTaskId = originTaskId;
    }


}
