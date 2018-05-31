package pieces;

import game.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Board board, String team, int[] location) {
        this.board = board;
        this.location = location;
        setTeam(team);
        board.movePiece(this, location);
    }

    public String pieceCode() {
        return "B";
    }

    public List<int[]> calculateMoves() {
        moves = new ArrayList<int[]>();

        int i = 1;
        while (board.validLocation(new int[]{location[0]+i, location[1]+i})) {
            moves.add(new int[]{location[0]+i, location[1]+i});
            i++;
        }

        i = 1;
        while (board.validLocation(new int[]{location[0]-i, location[1]+i})) {
            moves.add(new int[]{location[0]-i, location[1]+i});
            i++;
        }

        i = 1;
        while (board.validLocation(new int[]{location[0]+i, location[1]-i})) {
            moves.add(new int[]{location[0]+i, location[1]-i});
            i++;
        }

        i = 1;
        while (board.validLocation(new int[]{location[0]-i, location[1]-i})) {
            moves.add(new int[]{location[0]-i, location[1]-i});
            i++;
        }

        return moves;
    }
}