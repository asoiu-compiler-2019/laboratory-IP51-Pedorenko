package edu.pedorenko.interpreter.lexer;

import edu.pedorenko.interpreter.util.CharUtil;
import edu.pedorenko.interpreter.util.FSM;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class NumberRecognizerBuilder {

    //   digit  .
    // 1   2    -
    // 2   2    3
    // 3   4    -
    // 4   4    -
    public static FSM build() {

        int initialState = 1;
        int noNextState = -1;
        Set<Integer> acceptingStates = new HashSet<Integer>() {{add(2); add(4);}};
        BiFunction<Integer, Character, Integer> nextStateProvider = (state, character) -> {
            switch (state) {
                case 1:
                    if (CharUtil.isDigit(character)) {
                        return 2;
                    }
                    break;

                case 2:
                    if (CharUtil.isDigit(character)) {
                        return 2;
                    }

                    if (character == '.') {
                        return 3;
                    }

                    break;

                case 3:
                    if (CharUtil.isDigit(character)) {
                        return 4;
                    }

                    break;

                case 4:

                    if (CharUtil.isDigit(character)) {
                        return 4;
                    }

                    break;

                default:
                    break;
            }

            return -1;
        };

        return new FSM(initialState, noNextState, acceptingStates, nextStateProvider);
    }

}
