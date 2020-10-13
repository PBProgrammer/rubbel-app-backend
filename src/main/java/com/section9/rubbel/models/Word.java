package com.section9.rubbel.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Word {

    private List<Square> squares;
    private int value = 0;
    private String label;
    private boolean challenged;
    private UUID id;

    public Word() {
        id = UUID.randomUUID();
        squares = new ArrayList<>();
    }

    public Word(List<Square> squares) {
        id = UUID.randomUUID();
        this.squares = squares;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public void add(int index, Square square) {
        this.squares.add(index, square);
    }

    public void add(Square square) {
        this.squares.add(square);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChallenged() {
        return challenged;
    }

    public void setChallenged(boolean challenged) {
        this.challenged = challenged;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
