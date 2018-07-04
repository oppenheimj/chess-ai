package pieces;

import java.util.ArrayList;
import java.util.List;

public abstract class ZoneThreatPiece extends Piece {

    int NUMBER_OF_ZONES;

    public void calculateMoves() {
        clearPostures();

        for (int direction = 0; direction < NUMBER_OF_ZONES; direction++) {
            List<int[]> path = new ArrayList<>();
            int i = 1;
            int[] nextLocation = locationGenerator(i, direction);
            while (board.locationInBounds(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                path.add(nextLocation);
                moves.add(nextLocation);
                i++;
                nextLocation = locationGenerator(i, direction);
            }
            if (board.locationInBounds(nextLocation)) {
                if (board.teamPieceAtLocation(enemy, nextLocation) != null) {
                    Piece enemyPiece = board.teamPieceAtLocation(enemy, nextLocation);
                    threatening.add(enemyPiece);
                    if (enemyPiece instanceof King) {
                        pathToEnemyKing = path;
                        i++;
                        nextLocation = locationGenerator(i, direction);
                        if (board.locationInBounds(nextLocation)) {
                            if (board.teamPieceAtLocation(team, nextLocation) != null) {
                                defending.add(board.teamPieceAtLocation(team, nextLocation));
                            } else if (board.teamPieceAtLocation(enemy, nextLocation) == null) {
                                moves.add(nextLocation);
                            }
                        }
                    }
                } else if (!(board.pieceAtLocation(nextLocation) instanceof King)) {
                    defending.add(board.pieceAtLocation(nextLocation));
                }
            }
        }
    }

    abstract int[] locationGenerator(int space, int index);
}
