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
        move(location);
    }

    public List<int[]> calculateMoves() {
        moves = new ArrayList<>();

        if (board.validLocation(new int[] {location[0]-1, location[1]} )) {
            moves.add(new int[]{location[0]-1, location[1]});
        }
        if (board.validLocation(new int[] {location[0]-1, location[1]+1} )) {
            moves.add(new int[]{location[0]-1, location[1]+1});
        }
        if (board.validLocation(new int[] {location[0], location[1]+1} )) {
            moves.add(new int[]{location[0], location[1]+1});
        }
        if (board.validLocation(new int[] {location[0]+1, location[1]+1} )) {
            moves.add(new int[]{location[0]+1, location[1]+1});
        }
        if (board.validLocation(new int[] {location[0]+1, location[1]} )) {
            moves.add(new int[]{location[0]+1, location[1]});
        }
        if (board.validLocation(new int[] {location[0]+1, location[1]-1} )) {
            moves.add(new int[]{location[0]+1, location[1]-1});
        }
        if (board.validLocation(new int[] {location[0], location[1]-1} )) {
            moves.add(new int[]{location[0], location[1]-1});
        }
        if (board.validLocation(new int[] {location[0]-1, location[1]-1} )) {
            moves.add(new int[]{location[0]-1, location[1]-1});
        }

        return moves;
    }
}