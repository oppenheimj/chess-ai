package pieces;

import game.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class King extends PointThreatPiece {

    public King(Board board, String team, int[] location) {
        symbol = "ki";
        value = 0;
        this.board = board;
        this.location = location;
        setTeam(team);
        clearPostures();
        board.move(this, location);
    }

    public King clone(Board board) {
        int[] newLocation = location.clone();
        return new King(board, team, newLocation);
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

    public void correctKingPosture(Pieces pieces) {
        List<int[]> movesToRemove = new ArrayList<>();

        for (int[] move : moves) {
            if (cancelSpaceWithOtherKing(move, pieces) || !validSpaceForKing(move, pieces)) {
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

    private boolean cancelSpaceWithOtherKing(int[] space, Pieces pieces) {
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

    private boolean validSpaceForKing(int[] space, Pieces pieces) {
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