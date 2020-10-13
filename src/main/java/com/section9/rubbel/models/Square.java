package com.section9.rubbel.models;

import java.io.Serializable;

public class Square implements Serializable {
    private int index;
    private LetterToken letterToken;
    private int points;
    private Position position;
    private String rule;
    private boolean fixed = false;
    private boolean highlighted = false;

    public boolean isEmpty() {
        return letterToken == null;
    }

    public boolean hasUnfixedLetterToken(){
        return !isEmpty() && !isFixed();
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LetterToken getLetterToken() {
        return letterToken;
    }

    public void setLetterToken(LetterToken letterToken) {
        this.letterToken = letterToken;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
