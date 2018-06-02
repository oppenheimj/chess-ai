package game;

import pieces.*;
import java.util.List;

public class Game {
    Board board;
    Pieces pieces;
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
        board.move(attacker, victim.getLocation());
        pieces.deletePiece(victim);
    }

    public void nextState(Boolean showResult) {
        pieces.calculate();

        if (check) {
            Piece king = pieces.getKingOfTeam(turn);
            DecisionMaker.checkResolution(king, this);
            if (check) {
                System.out.println("GG");
            }
        } else {
            List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);
            DecisionMaker.makeMove(currentTurnPieces, this);
            pieces.calculate();

            for (Piece piece : currentTurnPieces) {
                if (piece.threatening.contains(pieces.getKingOfTeam(turn.equals("W") ? "B" : "W"))) {
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