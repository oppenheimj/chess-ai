package pieces;

import game.*;
import java.util.List;
import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(Board board, String team, int[] location) {
        symbol = "r";
        this.board = board;
        this.location = location;
        setTeam(team);
        board.move(this, location);
    }

    public List<int[]> calculateMoves() {
        moves = new ArrayList<>();
        attackMoves = new ArrayList<>();

        int numDirections = 4;

        for (int direction = 0; direction < numDirections; direction++) {
            int i = 1;
            int[] nextLocation = locationGenerator(i, direction);
            while (board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                moves.add(nextLocation);
                i++;
                nextLocation = locationGenerator(i, direction);
            }
            if (board.validLocation(nextLocation) && board.teamPieceAtLocation(enemy, nextLocation) != null) {
                attackMoves.add(
                    board.teamPieceAtLocation(enemy, nextLocation)
                );
            }
        }

        return moves;
    }

    public int[] locationGenerator(int i, int index) {
        int[][] nextLocations = {
            {location[0]-i, location[1]},
            {location[0], location[1]+i},
            {location[0]+i, location[1]},
            {location[0], location[1]-i}
        };

        return nextLocations[index];
    }
}