package com.section9.rubbel.tasks.PlayerTasks;

import com.section9.rubbel.models.RubbelGameSession;
import com.section9.rubbel.models.VotingContainer;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.List;
import java.util.UUID;

public class ShareProofAfterVetoTask extends PlayerTask {

    public ShareProofAfterVetoTask(String id, UUID owner, List<PlayerAction> actions) {
        super(id, List.of(owner), actions);
        setModal(true);
    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        setEndTimestamp(Util.getEndTimestamp(gameSession.getGameConfig().getProofTime()));
    }

    @Override
    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction) {
        if (resolvedAction.getId().equals(PlayerAction.HAS_NO_PROOF_ACTION_ID)) {
            gameSession.getVotingContainer().setAccepted(false);
        } else if (resolvedAction.getId().equals(PlayerAction.PROVIDE_PROOF_ACTION_ID)) {
            gameSession.getVotingContainer().setProof((String) resolvedAction.getPayload());
        }
    }


    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        if(autoComplete) {
            return getActions()
                    .stream()
                    .filter(action -> action.getId().equals(PlayerAction.HAS_NO_PROOF_ACTION_ID))
                    .findFirst()
                    .orElseThrow();
        }
        PlayerAction action = getReceivedActions()
                .values()
                .stream()
                .findFirst()
                .orElseThrow();
        return action;
    }

    @Override
    public boolean taskCompletable(RubbelGameSession gameSession) {
        if (autoComplete) {
            return true;
        }
        PlayerAction receivedAction = getReceivedActions()
                .values()
                .stream()
                .findFirst()
                .orElseThrow();


        if (receivedAction.getId().equals(PlayerAction.PROVIDE_PROOF_ACTION_ID)) {
            Object receivedProof = receivedAction.getPayload();
            if (receivedProof == null || !(receivedProof instanceof String)) {
                return false;
            } else {
                return !((String) receivedProof).trim().equals("");
            }
        } else if (receivedAction.getId().equals(PlayerAction.HAS_NO_PROOF_ACTION_ID)) {
            return true;
        }
        return false;
    }
}
