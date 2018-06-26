package logic;

import game.State;
import pieces.*;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    public static boolean checkDetected(Piece king, List<Piece> otherTeamsPieces) {
        for (Piece piece : otherTeamsPieces) {
            if (piece.threatening.contains(king)) {
                return true;
            }
        }
        return false;
    }

    public static List<Piece> getCheckers(Piece king, List<Piece> otherTeamsPieces) {
        List<Piece> checkers = new ArrayList<>();
        for (Piece piece : otherTeamsPieces) {
            if (piece.threatening.contains(king)) {
                checkers.add(piece);
            }
        }

        return checkers;
    }

    public static List<State> checkResolutionStates(Piece king, State state, List<Piece> checkers) {
        List<State> validFutureStates = new ArrayList<>();

        if (checkers.size() > 1) {
            if (!king.moves.isEmpty()) {
                for (int[] move : king.moves) {
                    validFutureStates.add(new State(state, king, move));
                }
            }
            for (Piece checker : checkers) {
                if (checker.defendedBy.isEmpty() && king.threatening.contains(checker)) {
                    validFutureStates.add(new State(state, king, checker.getLocation()));
                }
            }
        } else {
            Piece soleChecker = checkers.get(0);

            for (Piece threat : soleChecker.threatenedBy) {
                validFutureStates.add(new State(state, threat, soleChecker.getLocation()));
            }
            for (int[] move : king.moves) {
                validFutureStates.add(new State(state, king, move));
            }
            if (soleChecker instanceof Queen || soleChecker instanceof Rook || soleChecker instanceof Bishop) {
                List[] blockersAndLocations = checkForBlockers(state, king, soleChecker);
                List<Piece> pieces = blockersAndLocations[0];
                List<int[]> locations = blockersAndLocations[1];

                for (int i = 0; i < pieces.size(); i++) {
                    validFutureStates.add(new State(state, pieces.get(i), locations.get(i)));
                }
            }
        }

        return validFutureStates;
    }

    private static List[] checkForBlockers(State state, Piece king, Piece checker) {
        List<Piece> pieces = new ArrayList<>();
        List<int[]> locations = new ArrayList<>();
        List<int[]> lineOfSight = checker.pathToEnemyKing;
        List<Piece> friendlyPieces = state.pieces.getPiecesBelongingToTeam(king.getTeam());

        for (Piece friendlyPiece : friendlyPieces) {
            List<int[]> intersection = Piece.intersectLocationSets(friendlyPiece.moves, lineOfSight);
            if (!intersection.isEmpty()) {
                for (int[] location : intersection) {
                    pieces.add(friendlyPiece);
                    locations.add(location);
                }
            }
        }

        return new List[]{pieces, locations};
    }
}
