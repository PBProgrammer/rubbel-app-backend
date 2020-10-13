package com.section9.rubbel.models;

import java.util.List;

public class GameConfigOptions {

    public static final int BONUS_POINTS = 50; // to setting / options?

    private List<ConfigSelectOption> languageOptions;
    private List<ConfigSelectOption> startDelays;
    private List<ConfigSelectOption> timePerTurns;
    private List<ConfigSelectOption> vetoTimes;
    private List<ConfigSelectOption> proofTimes;

    public List<ConfigSelectOption> getLanguageOptions() {
        return languageOptions;
    }

    public void setLanguageOptions(List<ConfigSelectOption> languageOptions) {
        this.languageOptions = languageOptions;
    }

    public List<ConfigSelectOption> getStartDelays() {
        return startDelays;
    }

    public void setStartDelays(List<ConfigSelectOption> startDelays) {
        this.startDelays = startDelays;
    }

    public List<ConfigSelectOption> getTimePerTurns() {
        return timePerTurns;
    }

    public void setTimePerTurns(List<ConfigSelectOption> timePerTurns) {
        this.timePerTurns = timePerTurns;
    }

    public List<ConfigSelectOption> getVetoTimes() {
        return vetoTimes;
    }

    public void setVetoTimes(List<ConfigSelectOption> vetoTimes) {
        this.vetoTimes = vetoTimes;
    }

    public List<ConfigSelectOption> getProofTimes() {
        return proofTimes;
    }

    public void setProofTimes(List<ConfigSelectOption> proofTimes) {
        this.proofTimes = proofTimes;
    }
}
