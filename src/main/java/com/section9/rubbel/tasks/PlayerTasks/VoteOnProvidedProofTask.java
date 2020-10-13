package com.section9.rubbel.tasks.PlayerTasks;

import com.section9.rubbel.models.RubbelGameSession;
import com.section9.rubbel.services.Util;
import com.section9.rubbel.tasks.base.PlayerAction;
import com.section9.rubbel.tasks.base.PlayerTask;

import java.util.List;
import java.util.UUID;

public class VoteOnProvidedProofTask extends PlayerTask {

    public VoteOnProvidedProofTask(String id, List<UUID> owners, List<PlayerAction> actions) {
        super(id, owners, actions);
        setModal(true);
    }

    @Override
    public void executeEndOperations(RubbelGameSession gameSession, PlayerAction resolvedAction) {

    }

    @Override
    public void executeStartOperations(RubbelGameSession gameSession) {
        setEndTimestamp(Util.getEndTimestamp(gameSession.getGameConfig().getVetoTime()));
    }

    @Override
    public boolean taskCompletable(RubbelGameSession gameSession) {
        return autoComplete || owners.size() == 0;
    }

    @Override
    public PlayerAction resolveFinalPlayerAction(RubbelGameSession gameSession) {
        if(autoComplete) {
            gameSession.getVotingContainer().setAccepted(true);
            return getActions()
                    .stream()
                    .filter(action -> action.getId().equals(PlayerAction.ACCEPT_PROOF_ACTION_ID))
                    .findFirst()
                    .orElseThrow();
        }

        long rejectedCount = getReceivedActions().values().stream().filter(playerAction -> playerAction.getId().equals(PlayerAction.REJECT_PROOF_ACTION_ID)).count();
        if (rejectedCount > getReceivedActions().size() / 2) {
            gameSession.getVotingContainer().setAccepted(false);
            return getActions().get(1);
        }
        gameSession.getVotingContainer().setAccepted(true);
        return getActions().get(0);
    }

}
