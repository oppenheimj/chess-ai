package game;

import pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public Board board;
    public Pieces pieces;
    String turn = "W";
    Boolean check = false;

    public Game() {
        board = new Board();
        pieces = new Pieces(board);
        board.display();
    }

    public Game(Game otherGame) {
        board = otherGame.board;
        pieces = otherGame.pieces;
        turn = otherGame.turn;
        check = otherGame.check;
    }

    public void kill(Piece attacker, Piece victim) {
        if (victim instanceof King) {
            System.out.println("KILLING KING");
        }
        board.move(attacker, victim.getLocation());
        pieces.deletePiece(victim);
    }

    public void nextState(Boolean showResult) {
        pieces.calculate();

        if (check) {
            Piece king = pieces.getKingOfTeam(turn);
            if (!DecisionMaker.checkResolution(king, this)) {
                System.out.println("GG! " + (turn.equals("W") ? "B" : "W") + " wins!");
                System.exit(0);
            } else {
                check = false;
            }
        } else {
            List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);
            DecisionMaker.makeMove(currentTurnPieces, this);
            pieces.calculate();

            check = DecisionMaker.checkDetection(pieces.getKingOfTeam(turn.equals("W") ? "B" : "W"), currentTurnPieces);
        }
        turn = turn == "W" ? "B" : "W";

        if (showResult) {
            board.display();
        }
    }
}