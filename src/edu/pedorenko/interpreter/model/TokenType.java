package edu.pedorenko.interpreter.model;

public enum TokenType {

    //Petri net parts
    PLACES, //places
    TRANSITIONS, //transitions
    ARCS, // arcs

    // Identifiers and literals
    IDENTIFIER,
    INT,
    FLOAT,

    //Transitions properties
    DELAY,
    DEVIATION,
    DISTRIBUTION,
    PRIORITY,
    PROBABILITY,

    //Distribution types
    CONST,
    EXP,
    NORM,
    UNIFORM,

    //arc props
    CONNECTION, //->
    INF,

    LEFT_BRACKET,
    RIGHT_BRACKET,
    LEFT_BRACE,
    RIGHT_BRACE,

    COMMA,

    SIMULATE,

    //Report type
    VALUE,
    MIN,
    MAX,
    MEAN,

    END_OF_INPUT,
}
