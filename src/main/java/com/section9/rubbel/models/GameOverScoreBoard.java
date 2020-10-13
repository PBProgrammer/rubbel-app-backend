package com.section9.rubbel.models;

import java.util.List;
import java.util.UUID;

public class GameOverScoreBoard {
    //TODO could be extended by player statistics collected while playing
    // e.g. gelegte buchstaben, wie oft what man buchstaben getauascht und wieviele insgesamt, gespielte runden etc
    public List<UUID> winner;

    public List<UUID> getWinner() {
        return winner;
    }

    public void setWinner(List<UUID> winner) {
        this.winner = winner;
    }
}
