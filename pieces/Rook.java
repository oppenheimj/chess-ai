package pieces;

import game.*;
import java.util.List;

public class Rook extends Piece {
    public Rook(Board board, String team, int[] location) {
        this.board = board;
        this.location = location;
        setTeam(team);
        board.movePiece(this, location);
    }

    public String pieceCode() {
        return "R";
    }

    public List<int[]> calculateMoves() {
        return moves;
    }
}