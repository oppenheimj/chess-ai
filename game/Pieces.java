package game;

import pieces.*;
import java.util.List;
import java.util.ArrayList;

public class Pieces {
    List<Piece> blackPieces = new ArrayList<>();
    List<Piece> whitePieces = new ArrayList<>();
    Board board;
    public final int BOARD_DIMENSION = 8;

    public Pieces(Board board) {
        this.board = board;
        initializePieces();
    }

    private void initializePieces() {
        blackPieces.addAll(generatePowerRow("B", 0));
        blackPieces.addAll(generatePawnRow("B", 1));

        whitePieces.addAll(generatePawnRow("W", 6));
        whitePieces.addAll(generatePowerRow("W", 7));
    }

    private List<Piece> generatePawnRow(String team, int row) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            int[] location = new int[]{row, i};
            pieces.add(new Pawn(board, team, location));
        }
        return pieces;
    }

    private List<Piece> generatePowerRow(String team, int row) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            int[] location = new int[]{row, i};
            if (i == 0 || i == 7) {
                pieces.add(new Rook(board, team, location));
            } else if (i == 1 || i == 6) {
                pieces.add(new Knight(board, team, location));
            } else if (i == 2 || i == 5) {
                pieces.add(new Bishop(board, team, location));
            } else if (i == 3) {           
                if (team.equals("W")) {
                    pieces.add(new Queen(board, team, location));
                } else {
                    pieces.add(new King(board, team, location));
                }
            } else {
                if (team.equals("W")) {
                    pieces.add(new King(board, team, location));
                } else {
                    pieces.add(new Queen(board, team, location));
                }
            }
        }
        return pieces;
    }
}