package pieces;

import game.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class King extends PointThreatPiece {

    private Pieces pieces;

    public King(Board board, String team, int[] location, Pieces pieces) {
        symbol = "ki";
        this.board = board;
        this.location = location;
        this.pieces = pieces;
        setTeam(team);
        clearPostures();
        board.move(this, location);
    }

    public void calculateMoves() {
        clearPostures();

        int[][] nextLocations = {
            {location[0]-1, location[1]},
            {location[0]-1, location[1]+1},
            {location[0], location[1]+1},
            {location[0]+1, location[1]+1},
            {location[0]+1, location[1]},
            {location[0]+1, location[1]-1},
            {location[0], location[1]-1},
            {location[0]-1, location[1]-1}
        };

        for (int[] nextLocation : nextLocations) {
            if (board.locationInBounds(nextLocation)) {
                if (board.unoccupiedLocation(nextLocation)) {
                    moves.add(nextLocation);
                } else if (board.teamPieceAtLocation(enemy, nextLocation) != null) {
                    threatening.add(board.teamPieceAtLocation(enemy, nextLocation));
                } else {
                    defending.add(board.anyPieceAtLocation(nextLocation));
                }
            }
        }
    }

    public void correctKingPosture() {
        List<int[]> movesToRemove = new ArrayList<>();

        for (int[] move : moves) {
            if (cancelSpaceWithOtherKing(move) || !validSpaceForKing((move))) {
                movesToRemove.add(move);
            }
        }
        moves.removeAll(movesToRemove);

        List<Piece> threateningToRemove = new ArrayList<>();

        for (Piece threatenedPiece : threatening) {
            if (!threatenedPiece.defendedBy.isEmpty()) {
                threateningToRemove.add(threatenedPiece);
                threatenedPiece.threatenedBy.remove(this);
            }
        }
        threatening.removeAll(threateningToRemove);
    }

    private boolean cancelSpaceWithOtherKing(int[] space) {
        Piece enemyKing = pieces.getKingOfTeam(enemy);
        List<int[]> moves = enemyKing.moves;
        for (int[] move : moves) {
            if (Arrays.equals(move, space)) {
                enemyKing.moves.remove(move);
                return true;
            }
        }
        return false;
    }

    private boolean validSpaceForKing(int[] space) {
        List<Piece> enemyPieces = pieces.getPiecesBelongingToTeam(enemy);
        for (Piece enemyPiece : enemyPieces) {
            List<int[]> moves = enemyPiece instanceof Pawn ? ((Pawn) enemyPiece).corners : enemyPiece.moves;
            for (int[] move : moves) {
                if (Arrays.equals(move, space)) {
                    return false;
                }
            }
        }
        return true;
    }
}