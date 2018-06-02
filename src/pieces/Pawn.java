package pieces;

import game.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Board board, String team, int[] location) {
        symbol = "p";
        this.board = board;
        this.location = location;
        setTeam(team);
        board.move(this, location);
    }

    public List<int[]> calculateMoves() {
        clearPostures();

        //TODO: DRY this up!
        if (team.equals("W")) {
            int[] nextLocation = {location[0]-1, location[1]};
            if (board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                moves.add(nextLocation);
                nextLocation = new int[] {location[0]-2, location[1]};
                if (location[0] == 6 && board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                    moves.add(nextLocation);
                }
            }

            int[][] attackLocations = {
                {location[0]-1, location[1]-1},
                {location[0]-1, location[1]+1}
            };

            for (int[] attackLocation : attackLocations) {
                if (board.validLocation(attackLocation) && !board.unoccupiedLocation(attackLocation)) {
                    if (board.teamPieceAtLocation(enemy, attackLocation) != null) {
                        threatening.add(board.teamPieceAtLocation(enemy, attackLocation));
                    } else {
                        defending.add(board.pieceAtLocation(attackLocation));
                    }
                }
            }
        } else {
            int[] nextLocation = {location[0]+1, location[1]};
            if (board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                moves.add(nextLocation);
                nextLocation = new int[] {location[0]+2, location[1]};
                if (location[0] == 1 && board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                    moves.add(nextLocation);
                }
            }
            
            int[][] attackLocations = {
                {location[0]+1, location[1]-1},
                {location[0]+1, location[1]+1}
            };

            for (int[] attackLocation : attackLocations) {
                if (board.validLocation(attackLocation) && !board.unoccupiedLocation(attackLocation)) {
                    if (board.teamPieceAtLocation(enemy, attackLocation) != null) {
                        threatening.add(board.teamPieceAtLocation(enemy, attackLocation));
                    } else {
                        defending.add(board.pieceAtLocation(attackLocation));
                    }
                }
            }
        }

        return moves;
    }
}