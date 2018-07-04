package game;

import pieces.Piece;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Game {
    private static int maxDepth = 5;
    private static int currentDepth = 0;
    private static HashMap<String, Integer> letterToNumber;
    private static Scanner scan = new Scanner(System.in);

    static {
        letterToNumber = new HashMap<>();
        letterToNumber.put("a", 0);
        letterToNumber.put("b", 1);
        letterToNumber.put("c", 2);
        letterToNumber.put("d", 3);
        letterToNumber.put("e", 4);
        letterToNumber.put("f", 5);
        letterToNumber.put("g", 6);
        letterToNumber.put("h", 7);
    }

    public static void start() {
        State humanTurnState = new State();
        State computerTurnState;

        humanTurnState.display();

        //TODO introduce haulting condition
        while (true) {
            computerTurnState = humanTurn(humanTurnState);
            computerTurnState.displayStatusText();
            computerTurnState.display();

            humanTurnState = computerTurn(computerTurnState);
            humanTurnState.displayStatusText();
            humanTurnState.display();
        }
    }

    private static State humanTurn(State state) {
        //TODO clean up this disaster
        System.out.print("Enter move: ");
        String input = scan.nextLine();
        String[] splitString = input.split(" ");

        int[] location = {
                8 - Integer.parseInt(Character.toString(splitString[0].charAt(1))),
                letterToNumber.get(Character.toString(splitString[0].charAt(0)))
        };
        int[] targetLocation = {
                8 - Integer.parseInt(Character.toString(splitString[1].charAt(1))),
                letterToNumber.get(Character.toString(splitString[1].charAt(0)))
        };

        Piece pieceToMove = state.board.pieceAtLocation(location);
        return new State(state, pieceToMove, targetLocation);
    }

    private static State computerTurn(State state) {
        analyze(state);
        return state.getNextState();
    }

    private static void analyze(State state) {
        if (currentDepth == maxDepth) {
            state.derivedValue = state.value;
        } else {
            currentDepth++;
            state.calculateFutureStates();
            for (State futureState : state.getFutureStates()) {
                analyze(futureState);
            }
            currentDepth--;
            state.derivedValue = state.isTerminal() ? state.value : lowestDerivedValue(state.getFutureStates());
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

    public static void advancedPlay() {
        State previousState = new State();
        State nextState;
        int turns = 100;

        for (int i = 0; i < turns; i++) {
            System.out.println("Turn " + i);
            previousState.displayStatusText();
            previousState.display();

            analyze(previousState);

            nextState = previousState.getNextState();
            previousState = nextState;
        }
    }
}