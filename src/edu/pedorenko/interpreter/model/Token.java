package edu.pedorenko.interpreter.model;

public class Token {

    private TokenType tokenType;
    private String value;
    int line;
    int column;

    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Token(TokenType tokenType, char c, int line, int column) {
        this.tokenType = tokenType;
        value = String.valueOf(c);
        this.line = line;
        this.column = column;
    }

    public Token(TokenType tokenType, String value, int line, int column) {
        this.tokenType = tokenType;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String toString() {
        return tokenType + " " + value + " " + line + " " + column;
    }
}
