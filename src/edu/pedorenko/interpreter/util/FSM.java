package edu.pedorenko.interpreter.util;

import java.util.Set;
import java.util.function.BiFunction;

public class FSM {

    private int initialState;

    private int noNextState;

    private Set<Integer> acceptingStates;

    private BiFunction<Integer, Character, Integer> nextStateProvider;

    public FSM(int initialState, int noNextState, Set<Integer> acceptingStates, BiFunction<Integer, Character, Integer> nextStateProvider) {
        this.initialState = initialState;
        this.noNextState = noNextState;
        this.acceptingStates = acceptingStates;
        this.nextStateProvider = nextStateProvider;
    }

    public String run(String input) {

        StringBuilder output = new StringBuilder();

        int currentState = initialState;

        for (int i = 0; i < input.length(); ++i) {

            char character = input.charAt(i);

            int nextState = nextStateProvider.apply(currentState, character);

            if (nextState == noNextState) {
                break;
            }

            currentState = nextState;
            output.append(character);
        }

        if (acceptingStates.contains(currentState)) {
            return output.toString();
        } else {
            return null;
        }
    }
}
