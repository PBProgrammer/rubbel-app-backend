package com.section9.rubbel.models;

import java.util.List;

public class GameBoardConfig {
    String name;
    List<List<String>> rules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getRules() {
        return rules;
    }

    public void setRules(List<List<String>> rules) {
        this.rules = rules;
    }
}
