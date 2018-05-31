package game;

import pieces.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Board {
    Piece[][] board = new Piece[8][8];
    List<Piece> blackPieces = new ArrayList<>();
    List<Piece> whitePieces = new ArrayList<>();
    String turn;
    Random rand;

    public Board() {
        rand = new Random();
        turn = "W";
    }

    public void initialize() {
        blackPieces.addAll(generatePowerRow("B", 0));
        blackPieces.addAll(generatePawnRow("B", 1));

        whitePieces.addAll(generatePawnRow("W", 6));
        whitePieces.addAll(generatePowerRow("W", 7));
    }

    private List<Piece> generatePawnRow(String team, int row) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            int[] location = new int[]{row, i};
            pieces.add(new Pawn(this, team, location));
        }
        return pieces;
    }

    private List<Piece> generatePowerRow(String team, int row) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            int[] location = new int[]{row, i};
            if (i == 0 || i == 7) {
                pieces.add(new Rook(this, team, location));
            } else if (i == 1 || i == 6) {
                pieces.add(new Knight(this, team, location));
            } else if (i == 2 || i == 5) {
                pieces.add(new Bishop(this, team, location));
            } else if (i == 3) {           
                if (team.equals("W")) {
                    pieces.add(new Queen(this, team, location));
                } else {
                    pieces.add(new King(this, team, location));
                }
            } else {
                if (team.equals("W")) {
                    pieces.add(new King(this, team, location));
                } else {
                    pieces.add(new Queen(this, team, location));
                }
            }
        }
        return pieces;
    }

    public void display() {
        System.out.println("------------------------");
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if (piece != null) {
                    System.out.print(piece.pieceCode() + (piece.pieceCode().length() == 2 ? " " : "  "));
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }

    public void nextState() {
        int piece = rand.nextInt(16);
        while (whitePieces.get(piece).calculateMoves() == null) {
            piece = rand.nextInt(16);
        }
        whitePieces.get(piece).randomMove();
    }

    public boolean validLocation(int[] location) {
        return location[0] >= 0 && location[0] < 8 &&
            location[1] >= 0 && location[1] < 8 &&
            board[location[0]][location[1]] == null;
    }

    public void movePiece(Piece piece, int[] newLocation) {
        board[piece.location[0]][piece.location[1]] = null;
        board[newLocation[0]][newLocation[1]] = piece;
        piece.setLocation(newLocation);
    }
}