package game;

import pieces.*;

import java.util.ArrayList;
import java.util.List;

public class State {

    public Board board;
    public Pieces pieces;

    private String turn = "W";
    private Boolean check = false;
    private List<Piece> checkers = null;
    private List<State> allPossibleFutureStates = new ArrayList<>();
    public List<State> prunedFutureStates;
    private String statusText = "";
    public double derivedValue = 0;

    float value;

    State() {
        board = new Board();
        pieces = new Pieces(board);
        pieces.calculate();
        value = pieces.getValue(turn);
    }

    State(State previousState, Piece pieceToMove, int[] targetLocation) {
        board = new Board();
        pieces = previousState.pieces.clone(board);
        turn = previousState.turn;

        pieces.resetMovedThisTurnFlags();

        Piece targetPiece = board.anyPieceAtLocation(pieceToMove.getLocation());

        if (board.unoccupiedLocation(targetLocation)) {
            move(targetPiece, targetLocation);
        } else {
            kill(targetPiece, board.anyPieceAtLocation(targetLocation));
        }

        changeTurn();
        value = pieces.getValue(turn);

        pieces.calculate();
        updateCheckState();
        updateStatusText();


    }

    State getNextState() {
        return DecisionMaker.pickBestNextState(allPossibleFutureStates);
    }

    List<State> getAllPossibleFutureStates() {
        return allPossibleFutureStates;
    }

    void displayStatusText() {
        System.out.println(statusText);
    }

    void clearFutureStates() {
        allPossibleFutureStates = new ArrayList<>();
    }

    private void updateStatusText() {
        statusText += turn + "; " + value;

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

    private void updateCheckState() {
        List<Piece> enemyTeamPieces = pieces.getPiecesBelongingToTeam(otherTeam());

        check = CheckManager.checkDetected(pieces.getKingOfTeam(turn), enemyTeamPieces);
        checkers = CheckManager.getCheckers(pieces.getKingOfTeam(turn), enemyTeamPieces);
    }

    void calculateFutureStates() {
        if (check) {
            List<State> checkResolutionStates = checkResolutionFutureStates();
            if (checkResolutionStates.isEmpty()) {
                System.out.println("CHECKMATE A");
                System.exit(0);
            } else {
                allPossibleFutureStates.addAll(checkResolutionStates);
            }
        } else {
            List<State> otherFutureStates = otherFutureStates();
            if (otherFutureStates.isEmpty()) {
                System.out.println("NO MORE MOVES A");
                System.exit(0);
            } else {
                allPossibleFutureStates.addAll(otherFutureStates);
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
}