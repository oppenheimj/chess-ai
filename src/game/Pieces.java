package game;

import pieces.*;

import java.util.List;
import java.util.ArrayList;

public class Pieces {
    public List<Piece> blackPieces = new ArrayList<>();
    public List<Piece> whitePieces = new ArrayList<>();
    public List<List> pieceSets = new ArrayList<>();

    private Board board;

    public Pieces(Board board) {
        this.board = board;
        initializePieces();
    }

    public void calculate() {
        calculateMovesThreateningDefending();
        calculateThreatenedByDefendedBy();
    }

    public void calculateMovesThreateningDefending() {
        for (List<Piece> pieceSet : pieceSets) {
            for (Piece piece : pieceSet) {
                piece.calculateMoves();
            }
        }
    }

    public void calculateThreatenedByDefendedBy() {
        for (List<Piece> friendlies : pieceSets) {
            for (Piece friendly : friendlies) {
                if (!friendly.threatening.isEmpty()) {
                    for (Piece enemy : friendly.threatening) {
                        enemy.threatenedBy.add(friendly);
                    }
                }
                if (!friendly.defending.isEmpty()) {
                    for (Piece defendedFriendly : friendly.defending) {
                        defendedFriendly.defendedBy.add(friendly);
                    }
                }
            }
        }
    }

    public void deletePiece(Piece piece) {
        if (piece.getTeam().equals("W")) {
            whitePieces.remove(piece);
        } else {
            blackPieces.remove(piece);
        }
    }

    public List<Piece> getPiecesBelongingToTeam(String team) {
        return team.equals("W") ? whitePieces : blackPieces;
    }

    public Piece getKingOfTeam(String team) {
        List<Piece> pieces = getPiecesBelongingToTeam(team);
        Piece king = null;
        for (Piece piece : pieces) {
            if (piece instanceof King) {
                king = piece;
            }
        }
        return king;
    }

    private void initializePieces() {
        blackPieces.addAll(generatePowerRow("B", 0));
        blackPieces.addAll(generatePawnRow("B", 1));

        whitePieces.addAll(generatePawnRow("W", 6));
        whitePieces.addAll(generatePowerRow("W", 7));

        pieceSets.add(blackPieces);
        pieceSets.add(whitePieces);
    }

    private List<Piece> generatePawnRow(String team, int row) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < board.BOARD_DIMENSION; i++) {
            int[] location = new int[]{row, i};
            pieces.add(new Pawn(board, team, location));
        }
        return pieces;
    }

    private List<Piece> generatePowerRow(String team, int row) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < board.BOARD_DIMENSION; i++) {
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