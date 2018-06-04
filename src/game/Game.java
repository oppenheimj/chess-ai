package game;

import pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public Board board;
    public Pieces pieces;

    private String turn = "W";
    private Boolean check = false;
    private List<Piece> checkers = null;
    private List<String> states = new ArrayList<>();

    public Game() {
        board = new Board();
        pieces = new Pieces(board);
        pieces.calculate();
    }

    public Game(Game otherGame) {
        board = otherGame.board;
        pieces = otherGame.pieces;
        turn = otherGame.turn;
        check = otherGame.check;
        checkers = otherGame.checkers;
        states = otherGame.states;
    }

    void kill(Piece attacker, Piece victim) {
        //TODO remove after bug fixed
        if (victim instanceof King) {
            displayLastStates();
            System.out.println("ERROR: KILLING KING");
            System.exit(0);
        }
        board.move(attacker, victim.getLocation());
        pieces.deletePiece(victim);
        attacker.movedThisTurn = true;
    }

    public void nextState(Boolean showResult) {
        pieces.resetMovedThisTurnFlags();
        List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);

        if (check) {
            pieces.calculate();
            Piece king = pieces.getKingOfTeam(turn);
            if (!DecisionMaker.checkResolution(king, this, checkers)) {
                displayLastStates();
                System.out.println("GG! " + (turn.equals("W") ? "B" : "W") + " wins!");
                System.exit(0);
            } else {
                pieces.calculate();
                updateCheckState(currentTurnPieces);
            }
        } else {
            DecisionMaker.makeMove(currentTurnPieces, this);
            pieces.calculate();
            updateCheckState(currentTurnPieces);
        }

        turn = turn == "W" ? "B" : "W";
        states.add(board.getState());

        if (showResult) {
            board.display();
        }
    }

    private void updateCheckState(List<Piece> currentTurnPieces) {
        checkers = DecisionMaker.checkDetection(pieces.getKingOfTeam(turn.equals("W") ? "B" : "W"), currentTurnPieces);
        check = !checkers.isEmpty();
    }

    private void displayLastStates() {
        //TODO will break if game was absurdly short
        for (int i = states.size()-6; i < states.size(); i++) {
            System.out.println(states.get(i) + "\n**********************\n");
        }
    }
}