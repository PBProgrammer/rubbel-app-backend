package com.section9.rubbel.tasks.base;

import com.section9.rubbel.enums.PlayerActionType;
import com.section9.rubbel.models.RubbelGameSession;

import java.util.*;
import java.util.stream.Collectors;

public abstract class PlayerTask extends Task implements IPlayerTask {

    //START
    public static final String EMPTY_TASK_ID = "EMPTY_TASK";
    public static final String LOBBY_TASK_ID = "LOBBY_TASK";
    public static final String PLAYER_TURN_TASK_ID = "PLAYER_TURN";
    public static final String WAIT_AFTER_START_TASK_ID = "WAIT_AFTER_START";
    public static final String VETO_OR_CONFIRM_WORD_TASK = "VETO_OR_CONFIRM_WORD";
    public static final String SHARE_PROOF_AFTER_VETO_TASK = "SHARE_PROVE_AFTER_VETO";
    public static final String VOTE_ON_PROVIDED_PROOF_TASK = "VOTE_ON_PROVIDED_PROOF";
    public static final String GAME_OVER_TASK = "GAME_OVER";


    //WHILE GAME IS RUNNING
    public static final String SWITCH_LETTER_TOKENS_TASK_ID = "SWITCH_LETTER_TOKENS";

    public UUID instanceId;
    public boolean aborted;
    public boolean autoComplete;
    public boolean modal = false;
    public boolean takeOverTime;
    public List<UUID> owners;
    public List<PlayerAction> actions;
    Map<UUID, PlayerAction> receivedActions;
    public long endTimestamp = 0;

    public PlayerTask(String id, List<UUID> owners, List<PlayerAction> actions) {
        super(id);
        this.owners = owners;
        actions.forEach(action -> action.setOriginTaskId(id));
        this.actions = actions;
        this.receivedActions = new HashMap<>();
        aborted = false;
        instanceId = UUID.randomUUID();
    }

    public PlayerTask(String id, List<UUID> owners, List<PlayerAction> actions, HashMap<UUID, PlayerAction> receivedActions, boolean aborted, UUID instanceId) {
        super(id);
        this.owners = owners;
        this.actions = actions;
        this.receivedActions = receivedActions;
        this.aborted = aborted;
        this.instanceId = instanceId;
    }

    public UUID getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }

    public List<UUID> getOwners() {
        return owners;
    }

    public void setOwners(List<UUID> owners) {
        this.owners = owners;
    }

    public List<PlayerAction> getActions() {
        return actions;
    }

    public void setActions(List<PlayerAction> actions) {
        this.actions = actions;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public boolean isTakeOverTime() {
        return takeOverTime;
    }

    public void setTakeOverTime(boolean takeOverTime) {
        this.takeOverTime = takeOverTime;
    }

    public Map<UUID, PlayerAction> getReceivedActions() {
        return receivedActions;
    }

    public void setReceivedActions(Map<UUID, PlayerAction> receivedActions) {
        this.receivedActions = receivedActions;
    }

    public boolean isModal() {
        return modal;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public boolean isAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    @Override
    public boolean taskCompletable(RubbelGameSession gameSession) {
        if (autoComplete) {
            return true;
        }
        long completedActionsCount = receivedActions.values()
                .stream()
                .filter(action -> action.getType().equals(PlayerActionType.COMPLETE))
                .count();
        return completedActionsCount >= owners.size();
    }

    public void addReceivedPlayerAction(PlayerAction receivedAction) {
        if(this.getOwners().contains(receivedAction.getExecutor())) {
            List<UUID> updatedOwners = this.getOwners().stream().filter(id -> !id.equals(receivedAction.executor)).collect(Collectors.toList());
            this.setOwners(updatedOwners);
            this.receivedActions.put(receivedAction.executor, receivedAction);
        }
    }

    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        if (actions.size() == 1 && receivedActions.size() == 1) {
            return receivedActions.values().stream().findFirst().orElseThrow();
        }
        return actions
                .stream()
                .filter(action -> action.getType().equals(PlayerActionType.COMPLETE))
                .findFirst()
                .orElseThrow();
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }
}
