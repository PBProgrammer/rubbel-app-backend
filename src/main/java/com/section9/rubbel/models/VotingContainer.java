package com.section9.rubbel.models;

import java.util.UUID;

public class VotingContainer {
    private UUID activePlayer;
    private UUID vetoInitiator;
    private String proof;
    private boolean accepted;

    public UUID getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(UUID activePlayer) {
        this.activePlayer = activePlayer;
    }

    public UUID getVetoInitiator() {
        return vetoInitiator;
    }

    public void setVetoInitiator(UUID vetoInitiator) {
        this.vetoInitiator = vetoInitiator;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
