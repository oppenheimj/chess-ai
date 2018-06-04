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

    public void calculateMoves() {
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

        int color = team.equals("W") ? 0 : 1;
        int[] nextLocation = locationGenerator(1, color);
        if (board.locationInBounds(nextLocation) && board.unoccupiedLocation(nextLocation)) {
            moves.add(nextLocation);

            if ((location[0] == (color == 0 ? 6 : 1))) {
                nextLocation = locationGenerator(2, color);
                if (board.unoccupiedLocation(nextLocation)) {
                    moves.add(nextLocation);
                }
            }
        }

        int[][] attackLocations = (color == 0 ? whiteAttackLocations : blackAttackLocations);
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
}