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
    
    update();
  }

  public State(State previousState, int[] fromLocation, int[] toLocation) {
    board = new Board();
    pieces = previousState.pieces.clone(board);
    turn = previousState.turn;

    pieces.resetMovedThisTurnFlags();

    Piece targetPiece = board.pieceAtLocation(fromLocation);

    if (board.unoccupiedLocation(toLocation)) {
      move(targetPiece, toLocation);
    } else {
      kill(targetPiece, board.pieceAtLocation(toLocation));
    }

    changeTurn();
    update();
  }

  private void update() {
    pieces.calculate();
    updateCheckState();
    value = check ? 0 : pieces.getValue(turn);
    updateStatusText();
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

  void calculateFutureStates() {
    List<State> nextStates = check ? checkResolutionFutureStates() : otherFutureStates();

    if (nextStates.isEmpty()) {
      value = 0;
      terminal = true;
    } else {
      futureStates.addAll(nextStates);
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

    checkers = CheckManager.getCheckers(pieces.getKingOfTeam(turn), enemyTeamPieces);
    check = !checkers.isEmpty();
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

  private String getState() {
    StringBuilder stringBuilder = new StringBuilder();
    int rowNumber = 8;

    stringBuilder.append("    a   b   c   d   e   f   g   h\n");
    stringBuilder.append("  ┌───┬───┬───┬───┬───┬───┬───┬───┐\n");

    for (Piece[] rowOfPieces : board.getBoard()) {
      stringBuilder.append(rowNumber + " ");

      for (Piece piece : rowOfPieces) {
        stringBuilder.append("│" + (piece == null ? "   " : piece.getSymbol()));
      }

      stringBuilder.append("│ " + rowNumber-- + "\n");

      if (rowOfPieces == board.getBoard()[board.getBoard().length - 1]) {
        stringBuilder.append("  └───┴───┴───┴───┴───┴───┴───┴───┘\n");
      } else {
        stringBuilder.append("  ├───┼───┼───┼───┼───┼───┼───┼───┤\n");
      }
    }

    stringBuilder.append("    a   b   c   d   e   f   g   h\n");

    return stringBuilder.toString();
  }

  void display() {
    System.out.println(getState() + "\n");
  }

  private void updateStatusText() {
    statusText += turn + "; " + value;

    if (check) {
      statusText += " CHECK!";
    }
  }

  void displayStatusText() {
    System.out.println(statusText);
  }

  private void changeTurn() {
    turn = otherTeam();
  }

  private String otherTeam() {
    return turn.equals("W") ? "B" : "W";
  }

  boolean isTerminal() {
    return terminal;
  }
}