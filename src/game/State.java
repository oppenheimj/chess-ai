package game;

import pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class State {
    private Random rand = new Random();

    public Board board;
    public Pieces pieces;

    private String turn = "W";
    private Boolean check = false;
    private List<Piece> checkers = null;

    private List<State> futureStates = new ArrayList<>();

    private String statusText = "";

    public State() {
        board = new Board();
        pieces = new Pieces(board);
        pieces.calculate();
    }

    State(State previousState, Piece pieceToMove, int[] targetLocation) {
        board = new Board();
        pieces = previousState.pieces.clone(board);
        turn = previousState.turn.equals("W") ? "W" : "B";

        pieces.resetMovedThisTurnFlags();

        Piece targetPiece = board.anyPieceAtLocation(pieceToMove.getLocation());

        if (board.unoccupiedLocation(targetLocation)) {
            move(targetPiece, targetLocation);
        } else {
            kill(targetPiece, board.anyPieceAtLocation(targetLocation));
        }

        pieces.calculate();
        changeTurn();
        List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn.equals("W") ? "B" : "W");
        updateCheckState(currentTurnPieces);
        updateStatusText();
    }

    void displayStatusText() {
        System.out.println(statusText);
    }

    private void updateStatusText() {
        statusText += turn + "; ";

        if (check) {
            statusText += "CHECK!";
        }
    }

    void display() {
         System.out.println("------------------------\n" + getState() + "\n------------------------");
    }

    private String getState() {
        StringBuilder sb = new StringBuilder();
        for (Piece[] row : board.getBoard()) {
            for (Piece piece : row) {
                sb.append(piece == null ? "   " : piece.getSymbol());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void move(Piece piece, int[] newLocation) {
        board.move(piece, newLocation);
        piece.setLocation(newLocation);
        piece.movedThisTurn = true;
    }

    void kill(Piece attacker, Piece victim) {
        board.move(attacker, victim.getLocation());
        attacker.setLocation(victim.getLocation());
        pieces.deletePiece(victim);
        attacker.movedThisTurn = true;
    }

    private void changeTurn() {
        turn = turn.equals("W") ? "B" : "W";
    }

    private void updateCheckState(List<Piece> enemyTeamPieces) {
        checkers = DecisionMaker.checkDetection(pieces.getKingOfTeam(turn), enemyTeamPieces);
        check = !checkers.isEmpty();
    }

    State getNextState() {
        calculateFutureStates();
        int nextStateIndex = rand.nextInt(futureStates.size());
        return futureStates.get(nextStateIndex);
    }

    private void calculateFutureStates() {
        if (check) {
            List<State> checkResolutionStates = checkResolutionFutureStates();
            if (checkResolutionStates.isEmpty()) {
                System.out.println("CHECKMATE A");
                System.exit(0);
            } else {
                futureStates.addAll(checkResolutionStates);
            }
        } else {
            List<State> otherFutureStates = otherFutureStates();
            if (otherFutureStates.isEmpty()) {
                System.out.println("NO MORE MOVES A");
                System.exit(0);
            } else {
                futureStates.addAll(otherFutureStates);
            }
        }
    }

    private List<State> checkResolutionFutureStates() {
        List<State> nextStates = null;
        if (check) {
            nextStates = new ArrayList<>();
            pieces.calculate();
            Piece king = pieces.getKingOfTeam(turn);
            nextStates.addAll(DecisionMaker.checkResolutions(king, this, checkers));
        }

        return nextStates;
    }

    private List<State> otherFutureStates()  {
        List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);
        return DecisionMaker.makeMoves(currentTurnPieces, this);
    }
}