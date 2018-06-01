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

        MoveCalculator moveCalculator = new MoveCalculator();
    }

    public void kill(Piece attacker, Piece victim) {
        attacker.move(victim.location);
        pieces.deletePiece(victim);
    }

    public void nextState(Boolean showResult) {
        if (check) {
            Piece king = pieces.kingOfTeam(turn);
            DecisionMaker.checkResolution(king, this);
            if (check) {
                System.out.println("GG");
            }
        } else {
            List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);
            pieces.calculateMoves();
            DecisionMaker.makeMove(currentTurnPieces, this);
            pieces.calculateMoves();
            for (Piece piece : currentTurnPieces) {
                if (piece.attackMoves.contains(pieces.kingOfTeam(turn.equals("W") ? "B" : "W"))) {
                    System.out.println("CHECK!");
                    check = true;
                }
            }
        }
        turn = turn == "W" ? "B" : "W";

        if (showResult) {
            board.display();
        }
    }
}