package game;

import logic.*;
import pieces.*;

import java.util.ArrayList;
import java.util.List;

public class State {
    public Board board;
    public Pieces pieces;

    private String turn = "W";
    private Boolean check = false;
    private Boolean terminal = false;
    private List<Piece> checkers = null;
    private List<State> futureStates = new ArrayList<>();
    private String statusText = "";

    public double derivedValue = 0;
    float value;

    State() {
        board = new Board();
        pieces = new Pieces(board);
        pieces.calculate();
        value = pieces.getValue(turn);
    }

    public State(State previousState, Piece pieceToMove, int[] targetLocation) {
        board = new Board();
        pieces = previousState.pieces.clone(board);
        turn = previousState.turn;

        pieces.resetMovedThisTurnFlags();
        Piece targetPiece = board.pieceAtLocation(pieceToMove.getLocation());

        if (board.unoccupiedLocation(targetLocation)) {
            move(targetPiece, targetLocation);
        } else {
            kill(targetPiece, board.pieceAtLocation(targetLocation));
        }

        changeTurn();
        pieces.calculate();
        updateCheckState();
        value = check ? 0 : pieces.getValue(turn);
        updateStatusText();
    }

    boolean isTerminal() {
        return terminal;
    }

    void displayStatusText() {
        System.out.println(statusText);
    }

    void display() {
        System.out.println(getState() + "\n");
    }

    State getNextState() {
        return DecisionMaker.pickBestNextState(futureStates);
    }

    public List<State> getFutureStates() {
        return futureStates;
    }

    void clearFutureStates() {
        futureStates = new ArrayList<>();
    }

    void calculateFutureStates() {
        if (check) {
            List<State> checkResolutionStates = checkResolutionFutureStates();
            if (checkResolutionStates.isEmpty()) {
                value = 0;
                terminal = true;
            } else {
                futureStates.addAll(checkResolutionStates);
            }
        } else {
            List<State> otherFutureStates = otherFutureStates();
            if (otherFutureStates.isEmpty()) {
                terminal = true;
            } else {
                futureStates.addAll(otherFutureStates);
            }
        }
    }

    private List<State> checkResolutionFutureStates() {
        List<State> nextStates = null;

        if (check) {
            Piece king = pieces.getKingOfTeam(turn);
            nextStates = new ArrayList<>(CheckManager.checkResolutionStates(king, this, checkers));
        }

        return nextStates;
    }

    private List<State> otherFutureStates()  {
        List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);

        return DecisionMaker.getFutureStates(currentTurnPieces, this);
    }

    private void updateCheckState() {
        List<Piece> enemyTeamPieces = pieces.getPiecesBelongingToTeam(otherTeam());

        check = CheckManager.checkDetected(pieces.getKingOfTeam(turn), enemyTeamPieces);
        checkers = CheckManager.getCheckers(pieces.getKingOfTeam(turn), enemyTeamPieces);
    }

    private String getState() {
        int r = 8;
        StringBuilder sb = new StringBuilder();
        sb.append("    a   b   c   d   e   f   g   h\n");
        sb.append("  ┌───┬───┬───┬───┬───┬───┬───┬───┐\n");
        for (Piece[] row : board.getBoard()) {
            sb.append(r + " ");
            for (Piece piece : row) {
                sb.append("│" + (piece == null ? "   " : piece.getSymbol()));
            }
            sb.append("│ " + r-- +"\n");
            if (row == board.getBoard()[board.getBoard().length-1]) {
                sb.append("  └───┴───┴───┴───┴───┴───┴───┴───┘\n");
            } else {
                sb.append("  ├───┼───┼───┼───┼───┼───┼───┼───┤\n");
            }
        }
        sb.append("    a   b   c   d   e   f   g   h\n");

        return sb.toString();
    }

    private void move(Piece piece, int[] newLocation) {
        board.move(piece, newLocation);
        piece.setLocation(newLocation);
        piece.movedThisTurn = true;
    }

    private void kill(Piece attacker, Piece victim) {
        board.move(attacker, victim.getLocation());
        attacker.setLocation(victim.getLocation());
        pieces.deletePiece(victim);
        attacker.movedThisTurn = true;
    }

    private void changeTurn() {
        turn = otherTeam();
    }

    private String otherTeam() {
        return turn.equals("W") ? "B" : "W";
    }

    private void updateStatusText() {
        statusText += turn + "; " + value;

        if (check) {
            statusText += " CHECK!";
        }
    }
}