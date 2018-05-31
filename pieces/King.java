package pieces;

import game.*;
import java.util.List;

public class King extends Piece {
    public King(Board board, String team, int[] location) {
        this.board = board;
        this.location = location;
        setTeam(team);
        board.movePiece(this, location);
    }

    public String pieceCode() {
        return "Ki";
    }

    public List<int[]> calculateMoves() {
        return moves;
    }
}