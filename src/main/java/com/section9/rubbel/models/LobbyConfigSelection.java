package com.section9.rubbel.models;

public class LobbyConfigSelection {
    ConfigSelectOption languageSelection;
    ConfigSelectOption startDelaySelection;
    ConfigSelectOption timePerTurnSelection;
    ConfigSelectOption vetoTimeSelection;
    ConfigSelectOption proofTimeSelection;

    public ConfigSelectOption getLanguageSelection() {
        return languageSelection;
    }

    public void setLanguageSelection(ConfigSelectOption languageSelection) {
        this.languageSelection = languageSelection;
    }

    public ConfigSelectOption getStartDelaySelection() {
        return startDelaySelection;
    }

    public void setStartDelaySelection(ConfigSelectOption startDelaySelection) {
        this.startDelaySelection = startDelaySelection;
    }

    public ConfigSelectOption getTimePerTurnSelection() {
        return timePerTurnSelection;
    }

    public void setTimePerTurnSelection(ConfigSelectOption timePerTurnSelection) {
        this.timePerTurnSelection = timePerTurnSelection;
    }

    public ConfigSelectOption getVetoTimeSelection() {
        return vetoTimeSelection;
    }

    public void setVetoTimeSelection(ConfigSelectOption vetoTimeSelection) {
        this.vetoTimeSelection = vetoTimeSelection;
    }

    public ConfigSelectOption getProofTimeSelection() {
        return proofTimeSelection;
    }

    public void setProofTimeSelection(ConfigSelectOption proofTimeSelection) {
        this.proofTimeSelection = proofTimeSelection;
    }
}
