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
    private List<Game> futureStates = new ArrayList<>();
    boolean terminal = false;
    private Game previousState = null;

    public Game() {
        board = new Board();
        pieces = new Pieces(board);
        pieces.calculate();
    }

    public Game(Game otherGame, Piece pieceToMove, int[] targetLocation) {
        board = new Board();
        pieces = otherGame.pieces.clone(board);
        turn = otherGame.turn == "W" ? "W" : "B";
        check = otherGame.check;
        checkers = otherGame.checkers;
        states = otherGame.states;
        previousState = otherGame;

        pieces.resetMovedThisTurnFlags();

        Piece targetPiece = board.anyPieceAtLocation(pieceToMove.getLocation());

        if (board.unoccupiedLocation(targetLocation)) {
            move(targetPiece, targetLocation);
        } else {
            kill(targetPiece, board.anyPieceAtLocation(targetLocation));
        }

        turn = turn == "W" ? "B" : "W";

        states.add(board.getState());
        pieces.calculate();
        List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);
        updateCheckState(currentTurnPieces);
    }

    public void calculateFutureStates() {
        if (!terminal) {
            List<Game> checkResolutionStates = checkResolutionStates();
            if (checkResolutionStates == null) {
                List<Game> regularMoveStates = regularMoveStates();
                if (regularMoveStates.isEmpty()) {
                    terminal = true;
                } else {
                    futureStates.addAll(regularMoveStates);
                }
            } else if (checkResolutionStates.isEmpty()) {
                terminal = true;
            } else {
                futureStates = checkResolutionStates;
            }
        }
    }

    public List<Game> getFutureStates() {
        return futureStates;
    }

    public void move(Piece piece, int[] newLocation) {
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

    public List<Game> checkResolutionStates() {
        pieces.resetMovedThisTurnFlags();
        List<Game> nextStates = null;
        if (check) {
            nextStates = new ArrayList<>();
            pieces.calculate();
            Piece king = pieces.getKingOfTeam(turn);
            nextStates.addAll(DecisionMaker.checkResolutions(king, this, checkers));
        }

        return nextStates;
    }

    public List<Game> regularMoveStates()  {
        List<Piece> currentTurnPieces = pieces.getPiecesBelongingToTeam(turn);
        List<Game> nextStates = DecisionMaker.makeMoves(currentTurnPieces, this);

        return nextStates;

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

    public void displayFutureStates() {
        for (Game state : futureStates) {
            state.board.display();
            System.out.println("***************");
        }
    }
}