package edu.pedorenko.interpreter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWords {

    public static final Map<TokenType, String> keyWords = new HashMap<TokenType, String>() {{
        put(TokenType.PLACES, "places");
        put(TokenType.TRANSITIONS, "transitions");
        put(TokenType.ARCS, "arcs");

        put(TokenType.DELAY, "delay");
        put(TokenType.DEVIATION, "deviation");
        put(TokenType.DISTRIBUTION, "distribution");
        put(TokenType.PRIORITY, "priority");
        put(TokenType.PROBABILITY, "probability");

        put(TokenType.CONST, "const");
        put(TokenType.EXP, "exp");
        put(TokenType.NORM, "norm");
        put(TokenType.UNIFORM, "uniform");

        put(TokenType.INF, "inf");

        put(TokenType.SIMULATE, "simulate");

        put(TokenType.VALUE, "value");
        put(TokenType.MIN, "min");
        put(TokenType.MAX, "max");
        put(TokenType.MEAN, "mean");
    }};

    public static final List<TokenType> reportTypes = new ArrayList<TokenType>() {{
        add(TokenType.VALUE);
        add(TokenType.MIN);
        add(TokenType.MAX);
        add(TokenType.MEAN);
    }};

    public static final List<TokenType> numbers = new ArrayList<TokenType>() {{
        add(TokenType.INT);
        add(TokenType.FLOAT);
    }};

    public static final List<TokenType> distributionTypes = new ArrayList<TokenType>() {{
        add(TokenType.CONST);
        add(TokenType.EXP);
        add(TokenType.NORM);
        add(TokenType.UNIFORM);
    }};
}
