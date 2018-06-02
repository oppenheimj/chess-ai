package pieces;

import game.*;
import java.util.List;
import java.util.ArrayList;

public class King extends Piece {

    public King(Board board, String team, int[] location) {
        symbol = "ki";
        this.board = board;
        this.location = location;
        setTeam(team);
        board.move(this, location);
    }

    public List<int[]> calculateMoves() {
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
            if (board.validLocation(nextLocation)) {
                if (board.unoccupiedLocation(nextLocation)) {
                    moves.add(nextLocation);
                } else if (board.teamPieceAtLocation(enemy, nextLocation) != null) {
                    threatening.add(board.teamPieceAtLocation(enemy, nextLocation));
                } else {
                    defending.add(board.pieceAtLocation(nextLocation));
                }
            }
        }

        return moves;
    }
}