package com.section9.rubbel.models;

public class GameConfig {

    private LanguageConfig languageConfig;
    private int startDelay;
    private int timePerTurn;
    private int vetoTime;
    private int proofTime;

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public void setLanguageConfig(LanguageConfig languageConfig) {
        this.languageConfig = languageConfig;
    }

    public int getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public int getTimePerTurn() {
        return timePerTurn;
    }

    public void setTimePerTurn(int timePerTurn) {
        this.timePerTurn = timePerTurn;
    }

    public int getVetoTime() {
        return vetoTime;
    }

    public void setVetoTime(int vetoTime) {
        this.vetoTime = vetoTime;
    }

    public int getProofTime() {
        return proofTime;
    }

    public void setProofTime(int proofTime) {
        this.proofTime = proofTime;
    }
}
