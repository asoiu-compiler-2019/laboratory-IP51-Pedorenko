package edu.pedorenko.interpreter.lexer;

import edu.pedorenko.interpreter.model.KeyWords;
import edu.pedorenko.interpreter.model.Token;
import edu.pedorenko.interpreter.model.TokenType;
import edu.pedorenko.interpreter.util.CharUtil;
import edu.pedorenko.interpreter.util.FSM;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private String input;
    private int position;
    private int line;
    private int column;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 0;
        this.column = 0;
    }

    public List<Token> allTokens() {

        List<Token> tokens = new ArrayList<>();

        Token token;
        do {
            token = nextToken();
            tokens.add(token);
        } while (!token.getTokenType().equals(TokenType.END_OF_INPUT));

        return tokens;
    }

    public Token nextToken() {

        skipWhitespacesAndNewLines();

        if (position >= input.length()) {
            return new Token(TokenType.END_OF_INPUT);
        }

        char character = input.charAt(position);

        if (CharUtil.isLetter(character)) {
            return recognizeIdentifier();
        }

        if (CharUtil.isDigit(character)) {
            return recognizeDigit();
        }

        if (CharUtil.isParenthesis(character)) {
            return recognizeParenthesis();
        }

        if (CharUtil.isDash(character)) {
            return recognizeConnection();
        }

        if (CharUtil.isComma(character)) {
            position++;
            column++;
            return new Token(TokenType.COMMA, character, line, column);
        }

        throw new RuntimeException("Unrecognized character " + character +
                "at line " + line + " and column " + column);
    }

    private Token recognizeIdentifier() {

        String sfmInput = input.substring(position);

        for (TokenType tokenType : KeyWords.keyWords.keySet()) {
            String keyWord = KeyWords.keyWords.get(tokenType);
            if (sfmInput.startsWith(keyWord)) {
                position += keyWord.length();
                column += keyWord.length();
                return new Token(tokenType, keyWord, line, column);
            }
        }

        StringBuilder identifier = new StringBuilder();
        int i = position;
        while (i < input.length()) {
            char character = input.charAt(i);
            if (!(CharUtil.isLetter(character) || CharUtil.isDigit(character) || character == '_')) {
                break;
            }

            identifier.append(character);
            i++;
        }

        position += identifier.length();
        column += identifier.length();

        return new Token(TokenType.IDENTIFIER, identifier.toString(), line, column);
    }

    private Token recognizeDigit() {

        FSM fsm = NumberRecognizerBuilder.build();

        String sfmInput = input.substring(position);

        String number = fsm.run(sfmInput);

        if (number != null) {
            position += number.length();
            column += number.length();

            TokenType tokenType = number.contains(".") ? TokenType.FLOAT : TokenType.INT;

            return new Token(tokenType, number, line, column);
        }

        throw new RuntimeException("Malformed number at line " + line + " and column " + column);
    }

    private Token recognizeParenthesis() {

        char character = input.charAt(position);

        position++;
        column++;

        if (character == '(') {
            return new Token(TokenType.LEFT_BRACKET, character, line, column);
        }

        if (character == ')') {
            return new Token(TokenType.RIGHT_BRACKET, character, line, column);
        }

        if (character == '{') {
            return new Token(TokenType.LEFT_BRACE, character, line, column);
        }

        return new Token(TokenType.RIGHT_BRACE, character, line, column);
    }

    private Token recognizeConnection() {

        char dash = input.charAt(position);
        char more = input.charAt(position + 1);

        position += 2;
        column += 2;

        if (more != '>') {
            throw new RuntimeException("Unrecognized character " + more +
                    "at line " + line + " and column " + column);
        }

        return new Token(TokenType.CONNECTION, String.valueOf(dash) + more, position, column);
    }

    private void skipWhitespacesAndNewLines() {
        while (position < input.length() && CharUtil.isWhitespaceOrNewLine(input.charAt(position))) {

            if (CharUtil.isNewLine(input.charAt(position))) {
                position++;
                line++;
                column = 0;
            } else {
                position++;
                column ++;
            }
        }
    }
}
