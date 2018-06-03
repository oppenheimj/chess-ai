package game;

import pieces.*;

import java.util.Arrays;
import java.util.Collections;
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

        correctKingsPostures();
        correctionAlgorithm();
    }

    //TODO rename this
    public void correctionAlgorithm() {
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
                    if (board.validLocation(nextLocation)) {
                        Piece pieceAtLocation = board.pieceAtLocation(nextLocation);
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
                    friendly.moves = intersectLocationSets(friendly.moves, locationsTraversed);
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

    public static List<int[]> intersectLocationSets(List<int[]> currentMoves, List<int[]> acceptableMoves) {
        List<int[]> intersection = new ArrayList<>();

        for (int[] currentMove : currentMoves) {
            for (int[] acceptableMove : acceptableMoves) {
                if (Arrays.equals(currentMove, acceptableMove)) {
                    intersection.add(currentMove);
                }
            }
        }
        return intersection;
    }

    public int[] locationGenerator(Piece piece, int i, int index) {
        int[] location = piece.getLocation();

        int[][] nextLocations = {
                {location[0]-i, location[1]},
                {location[0], location[1]+i},
                {location[0]+i, location[1]},
                {location[0], location[1]-i},
                {location[0]-i, location[1]+i},
                {location[0]+i, location[1]+i},
                {location[0]+i, location[1]-i},
                {location[0]-i, location[1]-i}
        };

        return nextLocations[index];
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

    public List<King> getKings() {
        List<King> kings = new ArrayList<>();

        kings.add((King)getKingOfTeam("W"));
        kings.add((King)getKingOfTeam("B"));

        kings.removeAll(Collections.singleton(null));

        return kings;
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

    public void correctKingsPostures() {
        List<King> kings = getKings();
        for (King king : kings) {
            king.correctKingPosture();
        }
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