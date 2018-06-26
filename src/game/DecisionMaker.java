package game;

import pieces.*;

import java.util.ArrayList;
import java.util.List;

class DecisionMaker  {

    private static int NUMBER_OF_PRUNED_FUTURE_STATES = 3;

    static List<State> getFutureStates(List<Piece> pieceSet, State state) {
        List<State> futureStates = new ArrayList<>();
        for (Piece piece : pieceSet) {
            for (Piece threatenedPiece : piece.threatening) {
                futureStates.add(new State(state, piece, threatenedPiece.getLocation()));
            }
            for (int[] location : piece.moves) {
                futureStates.add(new State(state, piece, location));
            }
        }
        return futureStates;
    }

    static State pickBestNextState(List<State> futureStates) {
        double greatestStateValue = futureStates.get(0).derivedValue;
        State greatestState = futureStates.get(0);

        for (State futureState : futureStates) {
            if (futureState.derivedValue > greatestStateValue) {
                greatestStateValue = futureState.derivedValue;
                greatestState = futureState;
            }
        }

        return greatestState;
    }

    static State getLowestValueFutureStates(State state) {
        List<State> prunedFutureStates = new ArrayList<>();

        for (State futureState : state.getAllPossibleFutureStates()) {
            prunedFutureStates = insertStateToList(prunedFutureStates, futureState);
        }

        state.prunedFutureStates = prunedFutureStates;
        return pickBestNextState(prunedFutureStates);
    }

    private static List<State> insertStateToList(List<State> prunedFutureStates, State stateToAdd) {
        if (prunedFutureStates.size() < NUMBER_OF_PRUNED_FUTURE_STATES) {
            prunedFutureStates.add(stateToAdd);
        } else {
            State stateToRemove = prunedFutureStates.get(0);
            double lowestValue = stateToRemove.derivedValue;

            for (State state : prunedFutureStates) {
                if (state.derivedValue < lowestValue) {
                    lowestValue = state.derivedValue;
                    stateToRemove = state;
                }
            }

            prunedFutureStates.remove(stateToRemove);
            prunedFutureStates.add(stateToAdd);
        }

        return prunedFutureStates;
    }
}