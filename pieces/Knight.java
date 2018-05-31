package pieces;

import game.*;
import java.util.List;

public class Knight extends Piece {
    public Knight(Board board, String team, int[] location) {
        this.board = board;
        this.location = location;
        setTeam(team);
        board.movePiece(this, location);
    }

    public String pieceCode() {
        return "Kn";
    }

    public List<int[]> calculateMoves() {
        return moves;
    }
}