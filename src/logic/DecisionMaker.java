package logic;

import game.*;
import pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DecisionMaker  {

    public static List<State> getFutureStates(List<Piece> pieceSet, State state) {
        List<State> futureStates = new ArrayList<>();
        for (Piece piece : pieceSet) {
            for (Piece threatenedPiece : piece.threatening) {
                futureStates.add(new State(state, piece.getLocation(), threatenedPiece.getLocation()));
            }
            for (int[] location : piece.moves) {
                futureStates.add(new State(state, piece.getLocation(), location));
            }
        }
        return futureStates;
    }

    public static State pickBestNextState(List<State> futureStates) {
        double greatestStateValue = futureStates.get(0).derivedValue;

        for (State futureState : futureStates) {
            if (futureState.derivedValue > greatestStateValue) {
                greatestStateValue = futureState.derivedValue;
            }
        }

        List<State> tiedBestStates = new ArrayList<>();
        for (State futureState : futureStates) {
            if (futureState.derivedValue == greatestStateValue) {
                tiedBestStates.add(futureState);
            }
        }
        Random rand = new Random();

        return tiedBestStates.get(rand.nextInt(tiedBestStates.size()));
    }
}