package com.section9.rubbel.models;

import java.util.ArrayList;
import java.util.List;

public class GameBoardFactory {

    public static int BOARD_SIZE = 15; // tiles

    public static List<Square> buildInitialSquares(GameBoardConfig gameBoardConfig) {

        List<List<String>> currentRules = gameBoardConfig.rules;
        List<Square> squares = new ArrayList<>();
        int index = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                String rule = currentRules.get(row).get(col);
                Square square = new Square();
                square.setIndex(index);
                square.setPosition(new Position(col, row));
                square.setRule(rule);
                squares.add(square);
                index += 1;
            }
        }
        return squares;
    }
}

