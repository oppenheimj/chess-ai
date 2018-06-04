package pieces;

import game.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends PointThreatPiece {
    List<int[]> corners;

    public Pawn(Board board, String team, int[] location) {
        symbol = "p";
        this.board = board;
        this.location = location;
        setTeam(team);
        board.move(this, location);
    }

    private int[] locationGenerator(int spaces, int index) {
        int[][] nextLocations = {
                {location[0]-spaces, location[1]},
                {location[0]+spaces, location[1]},
        };

        return nextLocations[index];
    }

    public List<int[]> calculateMoves() {
        clearPostures();
        corners = new ArrayList<>();

        int[][] whiteAttackLocations = {
                {location[0]-1, location[1]-1},
                {location[0]-1, location[1]+1}
        };

        int[][] blackAttackLocations = {
                {location[0]+1, location[1]-1},
                {location[0]+1, location[1]+1}
        };

        for (int i = 0; i < 2; i++) {
            int[] nextLocation = locationGenerator(1, i);
            if (board.locationInBounds(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                moves.add(nextLocation);
                nextLocation = locationGenerator(2, i);
                if ((location[0] == (i == 0 ? 6 : 1)) && board.locationInBounds(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                    moves.add(nextLocation);
                }
            }

            int[][] attackLocations = (i == 0 ? whiteAttackLocations : blackAttackLocations);
            for (int[] attackLocation : attackLocations) {
                if (board.locationInBounds(attackLocation)) {
                    if (board.unoccupiedLocation(attackLocation)) {
                        corners.add(attackLocation);
                    } else if (board.teamPieceAtLocation(enemy, attackLocation) != null) {
                        threatening.add(board.teamPieceAtLocation(enemy, attackLocation));
                    } else if (!(board.anyPieceAtLocation(attackLocation) instanceof King)){
                        defending.add(board.anyPieceAtLocation(attackLocation));
                    }
                }
            }
        }

        return moves;
    }
}