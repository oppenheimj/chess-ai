package pieces;

import game.*;
import java.util.List;

public class Queen extends Piece {
    public Queen(Board board, String team, int[] location) {
        this.board = board;
        this.location = location;
        setTeam(team);
        board.movePiece(this, location);
    }

    public String pieceCode() {
        return "Q";
    }

    public List<int[]> calculateMoves() {
        return moves;
    }
}