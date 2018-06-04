package game;

import pieces.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Pieces {
    private List<Piece> blackPieces = new ArrayList<>();
    private List<Piece> whitePieces = new ArrayList<>();
    private List<List> pieceSets = new ArrayList<>();

    private Board board;

    public Pieces(Board board) {
        this.board = board;
        initializePieces();
    }

    public Pieces(Pieces pieces) {
        blackPieces = pieces.blackPieces;
        whitePieces = pieces.whitePieces;
        pieceSets = pieces.pieceSets;
    }

    private List<King> getKings() {
        List<King> kings = new ArrayList<>();

        kings.add((King)getKingOfTeam("W"));
        kings.add((King)getKingOfTeam("B"));

        kings.removeAll(Collections.singleton(null));

        return kings;
    }


    void deletePiece(Piece piece) {
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

    void calculate() {
        calculateMovesThreateningDefending();
        calculateThreatenedByDefendedBy();

        correctKingsPostures();
        correctionAlgorithm();
    }

    private void calculateMovesThreateningDefending() {
        for (List<Piece> pieceSet : pieceSets) {
            for (Piece piece : pieceSet) {
                piece.calculateMoves();
            }
        }
    }

    private void calculateThreatenedByDefendedBy() {
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

    private void correctKingsPostures() {
        List<King> kings = getKings();
        for (King king : kings) {
            king.correctKingPosture();
        }
    }

    //TODO rename this
    private void correctionAlgorithm() {
        List<King> kings = getKings();

        for (King king : kings) {
            for (int i = 0; i < 8; i++) {
                boolean friendlySeen = false;
                boolean enemySeen = false;
                Piece friendly = null;
                Piece enemy = null;
                List<int[]> locationsTraversed = new ArrayList<>();
                for (int j = 1; j < board.BOARD_DIMENSION; j++) {
                    int[] nextLocation = locationGenerator(king, j, i);
                    if (board.locationInBounds(nextLocation)) {
                        Piece pieceAtLocation = board.anyPieceAtLocation(nextLocation);
                        if (pieceAtLocation != null) {
                            if (pieceAtLocation.getTeam().equals(king.getTeam())) {
                                if (friendlySeen) {
                                    break;
                                } else {
                                    friendlySeen = true;
                                    friendly = pieceAtLocation;
                                }
                            } else {
                                if ((i < 4 && (pieceAtLocation instanceof Rook || pieceAtLocation instanceof Queen)) ||
                                        (i >= 4 && (pieceAtLocation instanceof Bishop || pieceAtLocation instanceof Queen)) && friendlySeen) {
                                    enemySeen = true;
                                    enemy = pieceAtLocation;
                                    break;
                                } else {
                                    break;
                                }
                            }
                        } else {
                            locationsTraversed.add(nextLocation);
                        }
                    }
                }
                if (friendlySeen && enemySeen) {
                    friendly.moves = Piece.intersectLocationSets(friendly.moves, locationsTraversed);
                    boolean threateningEnemy = friendly.threatening.contains(enemy);
                    friendly.undoPostures();
                    if (threateningEnemy) {
                        friendly.threatening.add(enemy);
                        enemy.threatenedBy.add(friendly);
                    }
                }
            }
        }
    }

    private int[] locationGenerator(Piece piece, int spaces, int index) {
        int[] location = piece.getLocation();

        int[][] nextLocations = {
                {location[0]-spaces, location[1]},
                {location[0], location[1]+spaces},
                {location[0]+spaces, location[1]},
                {location[0], location[1]-spaces},
                {location[0]-spaces, location[1]+spaces},
                {location[0]+spaces, location[1]+spaces},
                {location[0]+spaces, location[1]-spaces},
                {location[0]-spaces, location[1]-spaces}
        };

        return nextLocations[index];
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
                    pieces.add(new King(board, team, location, this));
                }
            } else {
                if (team.equals("W")) {
                    pieces.add(new King(board, team, location, this));
                } else {
                    pieces.add(new Queen(board, team, location));
                }
            }
        }

        return pieces;
    }
}