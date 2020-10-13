package com.section9.rubbel.enums;

public enum PlayerActionType
{
    AUTO_COMPLETE("AUTO_COMPLETE"),
    COMPLETE("COMPLETE"),
    ABORT("ABORT");

    private String type;

    PlayerActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}