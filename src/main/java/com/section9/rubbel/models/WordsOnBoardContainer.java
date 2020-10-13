package com.section9.rubbel.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WordsOnBoardContainer {

    private UUID owner;
    private List<Word> words;

    public WordsOnBoardContainer(UUID owner, List<Word> words) {
        this.owner = owner;
        this.words = words;
        setWordValues();
    }

    public WordsOnBoardContainer() {
        this.words = new ArrayList<>();
    }

    private void setWordValues() {
        for (Word word : words) {
            word.setValue(calculateWordValue(word));
        }
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public int calculateWordValue(Word word) {
        int wordValue = 0;
        int wordMultiplier = 1;
        for (Square square : word.getSquares()) {
            int letterValue = square.getLetterToken().getValue();
            if (square.isFixed()) {
                wordValue += letterValue;
            } else {
                String rule = square.getRule();
                if (rule.isBlank()) {
                    wordValue += letterValue;
                } else if (rule.contains("L")) {
                    // letter multiplier
                    String mult = rule.replace("L", "");
                    wordValue += letterValue * Integer.parseInt(mult);
                } else if (rule.contains("W")) {
                    // word multiplier
                    String mult = rule.replace("W", "");
                    wordValue += letterValue;
                    wordMultiplier *= Integer.parseInt(mult);
                } else if (rule.contains("C")) {
                    wordValue += letterValue;
                    wordMultiplier *= 2;
                }
            }
        }
        return wordValue * wordMultiplier;
    }

    public int calculateTotalScore() {
        int totalScore = 0;
        for (Word word : words) {
            totalScore += word.getValue();
        }
        return totalScore;
    }
}
