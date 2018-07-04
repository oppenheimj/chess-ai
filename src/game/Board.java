package game;

import pieces.*;

public class Board {

    private Piece[][] board = new Piece[8][8];
    final int BOARD_DIMENSION = 8;

    public Board() {}

    public Piece[][] getBoard() {
        return board;
    }

    public void move(Piece piece, int[] newLocation) {
        int[] oldLocation = piece.getLocation();
        board[oldLocation[0]][oldLocation[1]] = null;
        board[newLocation[0]][newLocation[1]] = piece;
    }

    public boolean locationInBounds(int[] location) {
        return location[0] >= 0 && location[0] < 8 &&
            location[1] >= 0 && location[1] < 8;
    }

    public boolean unoccupiedLocation(int[] location) {
        return board[location[0]][location[1]] == null;
    }

    public Piece teamPieceAtLocation(String team, int[] location) {
        Piece piece = board[location[0]][location[1]];
        return (piece != null && piece.getTeam().equals(team)) ? piece : null;
    }

    public Piece pieceAtLocation(int[] location) {
        return board[location[0]][location[1]];
    }
}