package game;

import pieces.*;

import java.util.List;

public class Game {
    public Board board;
    public Pieces pieces;
    String turn = "W";
    Boolean check = false;
    Piece checker = null;

    public Game() {
        board = new Board();
        pieces = new Pieces(board);
        pieces.calculate();
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
        List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);
        if (check) {
            Piece king = pieces.getKingOfTeam(turn);
            if (!DecisionMaker.checkResolution(king, this, checker)) {
                System.out.println("GG! " + (turn.equals("W") ? "B" : "W") + " wins!");
                System.exit(0);
            } else {
                pieces.calculate();
                checker = DecisionMaker.checkDetection(pieces.getKingOfTeam(turn.equals("W") ? "B" : "W"), currentTurnPieces);
                check = checker != null;
            }
        } else {
            DecisionMaker.makeMove(currentTurnPieces, this);
            pieces.calculate();
            checker = DecisionMaker.checkDetection(pieces.getKingOfTeam(turn.equals("W") ? "B" : "W"), currentTurnPieces);
            check = checker != null;
        }
        turn = turn == "W" ? "B" : "W";

        if (showResult) {
            board.display();
        }
    }
}