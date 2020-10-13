package com.section9.rubbel.models;

import com.section9.rubbel.enums.BoardUpdateType;

import java.util.UUID;


public class BoardUpdate {
    UUID executor;
    BoardUpdateType type;
    Square square;
    int index;

    public UUID getExecutor() {
        return executor;
    }

    public void setExecutor(UUID executor) {
        this.executor = executor;
    }

    public BoardUpdateType getType() {
        return type;
    }

    public void setType(BoardUpdateType type) {
        this.type = type;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static BoardUpdate create(UUID executor,
                                     BoardUpdateType type,
                                     Square square,
                                     int index) {
        BoardUpdate boardUpdate = new BoardUpdate();
        boardUpdate.setExecutor(executor);
        boardUpdate.setType(type);
        boardUpdate.setSquare(square);
        boardUpdate.setIndex(index);
        return boardUpdate;
    }
}
