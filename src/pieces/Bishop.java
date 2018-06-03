package pieces;

import game.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(Board board, String team, int[] location) {
        symbol = "b";
        this.board = board;
        this.location = location;
        setTeam(team);
        board.move(this, location);;
    }

     public List<int[]> calculateMoves() {
         clearPostures();

        int numDirections = 4;

        for (int direction = 0; direction < numDirections; direction++) {
            int i = 1;
            int[] nextLocation = locationGenerator(i, direction);
            //TODO move needs to make sure piece won't reveal king
            while (board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                moves.add(nextLocation);
                i++;
                nextLocation = locationGenerator(i, direction);
            }
            if (board.validLocation(nextLocation)) {
                if (board.teamPieceAtLocation(enemy, nextLocation) != null) {
                    Piece enemyPiece = board.teamPieceAtLocation(enemy, nextLocation);
                    //TODO Can't threaten unless won't put king in check
                    threatening.add(enemyPiece);
                    if (enemyPiece instanceof King) {
                        i++;
                        nextLocation = locationGenerator(i, direction);
                        if (board.validLocation(nextLocation)) {
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

        return moves;
    }

    public int[] locationGenerator(int i, int index) {
        int[][] nextLocations = {
            {location[0]+i, location[1]+i},
            {location[0]-i, location[1]+i},
            {location[0]+i, location[1]-i},
            {location[0]-i, location[1]-i}
        };

        return nextLocations[index];
    }
}