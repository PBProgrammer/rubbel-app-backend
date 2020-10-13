package com.section9.rubbel.models;

import java.io.Serializable;
import java.util.UUID;

public class LetterToken implements Serializable {
    private UUID id;
    private String symbol;
    private int value;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
