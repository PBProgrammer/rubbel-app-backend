package com.section9.rubbel.enums;

public enum BoardUpdateType
{
    R2GB("R2GB"),
    GB2R("GB2R"),
    GB2GB("GB2GB");


    private String type;

    BoardUpdateType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}