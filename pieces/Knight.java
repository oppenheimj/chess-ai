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
        moves = new ArrayList<int[]>();

        if (board.validLocation(new int[] {location[0]-1, location[1]+2} )) {
            moves.add(new int[]{location[0]-1, location[1]+2});
        }
        if (board.validLocation(new int[] {location[0]-2, location[1]+1} )) {
            moves.add(new int[]{location[0]-2, location[1]+1});
        }

        if (board.validLocation(new int[] {location[0]+1, location[1]-2} )) {
            moves.add(new int[]{location[0]+1, location[1]-2});
        }
        if (board.validLocation(new int[] {location[0]+2, location[1]-1} )) {
            moves.add(new int[]{location[0]+2, location[1]-1});
        }

        if (board.validLocation(new int[] {location[0]+1, location[1]+2} )) {
            moves.add(new int[]{location[0]+1, location[1]+2});
        }
        if (board.validLocation(new int[] {location[0]+2, location[1]+1} )) {
            moves.add(new int[]{location[0]+2, location[1]+1});
        }

        if (board.validLocation(new int[] {location[0]-1, location[1]-2} )) {
            moves.add(new int[]{location[0]-1, location[1]-2});
        }
        if (board.validLocation(new int[] {location[0]-2, location[1]-1} )) {
            moves.add(new int[]{location[0]-2, location[1]-1});
        }
        return moves;
    }
}