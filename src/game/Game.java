package game;

import java.util.List;

public class Game {
    static int maxDepth = 5;
    static int currentDepth = 0;

    public Game() {}

    public static void play() {
        int turns = 100;

        State previousState = new State();
        State nextState;

        for (int i = 0; i < turns; i++) {
            previousState.display();
            previousState.displayStatusText();

            nextState = previousState.getNextState();
            previousState = nextState;
        }
    }

    public static void advancedPlay() {

        State previousState = new State();
        State nextState;
        int turns = 20;

        for (int i = 0; i < turns; i++) {
            previousState.display();
            previousState.displayStatusText();
            analyze(previousState);

            nextState = previousState.getNextState();
            previousState = nextState;
        }
    }

    public static void depthAnalyzer() {
        State startState = new State();
        analyze(startState);
    }

    private static void analyze(State state) {
        if (currentDepth == maxDepth) {
            state.derivedValue = state.value;
        } else {
            currentDepth++;
            state.calculateFutureStates();
            for (State futureState : state.getAllPossibleFutureStates()) {
                analyze(futureState);
            }
            currentDepth--;
            state.derivedValue = lowestDerivedValue(state.getAllPossibleFutureStates());
            if (currentDepth > 0) {
                state.clearFutureStates();
            }
        }
    }

    private static double lowestDerivedValue(List<State> states) {
        double lowestDerivedValue = states.get(0).derivedValue;

        for (State state : states) {
            if (state.derivedValue < lowestDerivedValue) {
                lowestDerivedValue = state.derivedValue;
            }
        }

        return lowestDerivedValue;
    }
}