package edu.pedorenko.interpreter.util;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;

public class CharUtil {

    private CharUtil() {
    }

    public static boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    public static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    public static boolean isParenthesis(char c) {
        return c == '{' || c == '}' || c == '(' || c == ')';
    }

    public static boolean isComma(char c) {
        return c == ',';
    }

    public static boolean isWhitespaceOrNewLine(char c) {
        return Character.isWhitespace(c) || EncodingHelper.isNewLine(c);
    }

    public static boolean isNewLine(char c) {
        return EncodingHelper.isNewLine(c);
    }

    public static boolean isDash(char character) {
        return character == '-';
    }
}
