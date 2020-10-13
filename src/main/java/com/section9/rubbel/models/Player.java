package com.section9.rubbel.models;

import com.section9.rubbel.services.StaticValues;
import com.section9.rubbel.services.Util;

import java.util.List;
import java.util.UUID;

public class Player {
    private String name;
    private UUID id;
    private int score;
    private boolean ready;
    private boolean host;
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static Player create(Player basePlayer) {
        return create(basePlayer, false);
    }

    public static Player create(Player basePlayer, boolean keepIcon) {
        Player player = new Player();
        player.setHost(basePlayer.isHost());
        player.setScore(0);
        if(keepIcon) {
            player.setIcon(basePlayer.getIcon());
        }else{
            player.setIcon(StaticValues.getRandomIcon());
        }
        if(basePlayer.getName() != null) {
            player.setName(basePlayer.getName());
        }else{
            player.setName(Util.getRandomPlayerName());
        }

        if(basePlayer.getId() != null) {
            player.setId(basePlayer.getId());
        }else{
            player.setId(UUID.randomUUID());
        }

        return player;
    }
}
