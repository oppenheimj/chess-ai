package pieces;

import game.*;
import java.util.List;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(Board board, String team, int[] location) {
        symbol = "kn";
        this.board = board;
        this.location = location;
        setTeam(team);
        move(location);
    }

    public List<int[]> calculateMoves() {
        moves = new ArrayList<>();
        attackMoves = new ArrayList<>();
    
        int[][] nextLocations = {
            {location[0]-1, location[1]+2},
            {location[0]-2, location[1]+1},
            {location[0]+1, location[1]-2},
            {location[0]+2, location[1]-1},
            {location[0]+1, location[1]+2},
            {location[0]+2, location[1]+1},
            {location[0]-1, location[1]-2},
            {location[0]-2, location[1]-1}
        };

        for (int[] nextLocation : nextLocations) {
            if (board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                moves.add(nextLocation);
            } else if (board.validLocation(nextLocation) && board.teamPieceAtLocation(enemy, nextLocation) != null) {
                attackMoves.add(board.teamPieceAtLocation(enemy, nextLocation));
            }
        }

        return moves;
    }
}