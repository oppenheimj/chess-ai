import pieces.*;

class Board {
    Piece[][] board = new Piece[8][8];

    Board() {}

    public void initialize() {
        generatePowerRow("B", 0);
        generatePawnRow("B", 1);

        generatePawnRow("W", 6);
        generatePowerRow("W", 7);
    }

    private void generatePawnRow(String team, int row) {
        for (int i = 0; i < board.length; i++) {
            int[] location = new int[]{row, i};
            board[row][i] = new Pawn(team, location);
        }
    }

    private void generatePowerRow(String team, int row) {
        for (int i = 0; i < board.length; i++) {
            int[] location = new int[]{row, i};
            if (i == 0 || i == 7) {
                board[row][i] = new Rook(team, location);
            } else if (i == 1 || i == 6) {
                board[row][i] = new Knight(team, location);
            } else if (i == 2 || i == 5) {
                board[row][i] = new Bishop(team, location);
            } else if (i == 3) {
                board[row][i] = team.equals("W") ? new Queen(team, location) : new King(team, location);
            } else {
                board[row][i] = team.equals("W") ? new King(team, location) : new Queen(team, location);
            }
        }
    }

    public void display() {
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if (piece != null) {
                    System.out.print(piece.pieceCode() + (piece.pieceCode().length() == 2 ? " " : "  "));
                }
            }
            System.out.println();
        }
    }
}