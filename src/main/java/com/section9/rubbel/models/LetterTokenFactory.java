package com.section9.rubbel.models;

import java.util.*;


public class LetterTokenFactory {

    public static int BOARD_SIZE = 15; // tiles

    public static List<LetterToken> getLetterTokensByConfig(LanguageConfig languageConfig) {

        List<LetterToken> letterTokens = new ArrayList<>();
        languageConfig.getLetters().forEach((key, value) -> {
            for(int i = 0; i < value.tiles; i++) {
                LetterToken token = new LetterToken();
                token.setId(UUID.randomUUID());
                token.setSymbol(key);
                token.setValue(value.getPoints());
                letterTokens.add(token);
            }
        });
       return letterTokens;
    }

//    public static LetterToken map(LetterToken source) {
//        LetterToken letterToken = new LetterToken();
//        letterToken.setId(source.getId());
//        letterToken.setSymbol(source.getSymbol());
//        letterToken.setValue(source.getValue());
//        return letterToken;
//    }

    public static LetterToken map(Map<String, Object> source) {
        LetterToken letterToken = new LetterToken();
        letterToken.setId(UUID.fromString((String) source.get("id")));
        letterToken.setSymbol((String) source.get("symbol"));
        letterToken.setValue((int) source.get("value"));
        return letterToken;
    }
}

