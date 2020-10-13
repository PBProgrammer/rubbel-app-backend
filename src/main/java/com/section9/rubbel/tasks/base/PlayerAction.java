package com.section9.rubbel.tasks.base;

import com.section9.rubbel.enums.PlayerActionType;

import java.util.UUID;

public class PlayerAction extends Action {

    public static final String START_GAME_ACTION_ID = "START_GAME_ACTION";
    public static final String WAIT_AFTER_START_GAME_ACTION_ID = "WAIT_AFTER_START_ACTION";
    public static final String RETURN_LETTER_TOKENS_ACTION_ID = "RETURN_LETTER_TOKENS_ACTIONS";
    public static final String PASS_TURN_ACTION_ID = "PASS_TURN_ACTION";
    public static final String FINALIZE_PLACED_LETTERS_ACTION_ID = "FINALIZE_PLACED_LETTERS_ACTION";
    public static final String OPEN_SWITCH_LETTERS_ACTION_ID = "OPEN_SWITCH_LETTERS_ACTION";
    public static final String CONFIRM_SWITCH_LETTERS_ACTION_ID = "CONFIRM_SWITCH_LETTERS_ACTION";
    public static final String TEMPORARY_BOARD_UPDATES_ID = "TEMPORARY_BOARD_UPDATES";
    public static final String ABORT_ACTION_ID = "ABORT_ACTION";
    public static final String VETO_ACTION_ID = "VETO_ACTION";
    public static final String CONFIRM_WORD_ACTION_ID = "CONFIRM_WORD_ACTION";
    public static final String PROVIDE_PROOF_ACTION_ID = "PROVIDE_PROOF_ACTION";

    public static final String ACCEPT_PROOF_ACTION_ID = "ACCEPT_PROOF_ACTION";
    public static final String REJECT_PROOF_ACTION_ID = "REJECT_PROOF_ACTION";
    public static final String HAS_NO_PROOF_ACTION_ID = "HAS_NO_PROOF_ACTION";



    PlayerActionType type;
    UUID executor;
    Object payload;

    public PlayerAction() { }

    public PlayerAction(String id, String originTaskId,  String targetTaskId, PlayerActionType type) {
        super(id, originTaskId, targetTaskId);
        this.type = type;
    }

    public PlayerAction(String id,  String targetTaskId, PlayerActionType type) {
        super(id, targetTaskId);
        this.type = type;
    }

    public UUID getExecutor() {
        return executor;
    }

    public void setExecutor(UUID executor) {
        this.executor = executor;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public PlayerActionType getType() {
        return type;
    }

    public void setType(PlayerActionType type) {
        this.type = type;
    }
}
