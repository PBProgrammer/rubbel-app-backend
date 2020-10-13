package com.section9.rubbel.models;

import java.util.Map;

public class LanguageConfig {
    private String key;
    private boolean readLeftToRight;
    private Map<String, LetterConfig> letters;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isReadLeftToRight() {
        return readLeftToRight;
    }

    public void setReadLeftToRight(boolean readLeftToRight) {
        this.readLeftToRight = readLeftToRight;
    }

    public Map<String, LetterConfig> getLetters() {
        return letters;
    }

    public void setLetters(Map<String, LetterConfig> letters) {
        this.letters = letters;
    }
}
