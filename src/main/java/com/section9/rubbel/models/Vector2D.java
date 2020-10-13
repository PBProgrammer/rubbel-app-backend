package com.section9.rubbel.models;

import java.io.Serializable;

public class Vector2D implements Serializable {
    private int x;
    private int y;
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
