package pieces;

import game.*;
import java.util.List;
import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(Board board, String team, int[] location) {
        symbol = "r";
        this.board = board;
        this.location = location;
        setTeam(team);
        board.move(this, location);
    }

    public List<int[]> calculateMoves() {
        clearPostures();

        int numDirections = 4;

        for (int direction = 0; direction < numDirections; direction++) {
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
                } else if (!(board.anyPieceAtLocation(nextLocation) instanceof King)) {
                    defending.add(board.anyPieceAtLocation(nextLocation));
                }
            }
        }

        return moves;
    }

    public int[] locationGenerator(int i, int index) {
        int[][] nextLocations = {
            {location[0]-i, location[1]},
            {location[0], location[1]+i},
            {location[0]+i, location[1]},
            {location[0], location[1]-i}
        };

        return nextLocations[index];
    }
}