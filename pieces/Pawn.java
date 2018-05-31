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
        move(location);
    }

    public List<int[]> calculateMoves() {
        moves = new ArrayList<int[]>();

        if (team.equals("W")) {
            if (board.validLocation(new int[] {location[0]-1, location[1]} )) {
                moves.add(new int[]{location[0]-1, location[1]});
                if (location[0] == 6 && board.validLocation(new int[] {location[0]-2, location[1]} )) {
                    moves.add(new int[] {location[0]-2, location[1]});
                }
            }
        } else {
            if (board.validLocation(new int[] {location[0]+1, location[1]} )) {
                moves.add(new int[]{location[0]+1, location[1]});
                if (location[0] == 1 && board.validLocation(new int[] {location[0]+2, location[1]} )) {
                    moves.add(new int[] {location[0]+2, location[1]});
                }
            }
        }

        return moves;
    }
}