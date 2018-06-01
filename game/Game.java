package game;

import pieces.*;
import java.util.Random;
import java.util.List;

public class Game {
    Board board;
    Pieces pieces;
    String turn = "W";
    Random rand = new Random();
    Boolean check = false;

    public Game() {
        board = new Board();
        pieces = new Pieces(board);
        board.display();
    }

    public void nextState(Boolean showResult) {
        if (turn.equals("W")) {
            movePieceFromSet(pieces.whitePieces);
            turn = "B";
        } else {
            movePieceFromSet(pieces.blackPieces);
            turn = "W";
        }

        if (showResult) {
            board.display();
        }
    }

    private void movePieceFromSet(List<Piece> pieceSet) {
        int numPieces = pieceSet.size();
        int piece = rand.nextInt(numPieces);
        while (pieceSet.get(piece).calculateMoves().size() == 0) {
            piece = rand.nextInt(numPieces);
        }
        pieceSet.get(piece).randomMove();
        
    }
}