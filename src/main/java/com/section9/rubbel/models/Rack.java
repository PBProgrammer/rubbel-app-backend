package com.section9.rubbel.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rack extends ArrayList<LetterToken> {
    public static final int maxSize = 7;

    public Rack(List<LetterToken> tokens){
        super(tokens);
    }


}