package pieces;

import game.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Board board, String team, int[] location) {
        symbol = "p";
        this.board = board;
        this.location = location;
        setTeam(team);
        board.move(this, location);
    }

    public List<int[]> calculateMoves() {
        moves = new ArrayList<>();
        attackMoves = new ArrayList<>();

        //TODO: DRY
        if (team.equals("W")) {
            int[] nextLocation = new int[]{location[0]-1, location[1]};
            if (board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                moves.add(nextLocation);
                nextLocation = new int[] {location[0]-2, location[1]};
                if (location[0] == 6 && board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                    moves.add(nextLocation);
                }
            }

            int[][] attackLocations = {
                {location[0]-1, location[1]-1},
                {location[0]-1, location[1]+1}
            };

            for (int[] attackLocation : attackLocations) {
                if (board.validLocation(attackLocation) && board.teamPieceAtLocation(enemy, attackLocation) != null) {
                    attackMoves.add(board.teamPieceAtLocation(enemy, attackLocation));
                }
            }
        } else {
            int[] nextLocation = new int[] {location[0]+1, location[1]};
            if (board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                moves.add(nextLocation);
                nextLocation = new int[] {location[0]+2, location[1]};
                if (location[0] == 1 && board.validLocation(nextLocation) && board.unoccupiedLocation(nextLocation)) {
                    moves.add(nextLocation);
                }
            }
            
            int[][] attackLocations = {
                {location[0]+1, location[1]-1},
                {location[0]+1, location[1]+1}
            };

            for (int[] attackLocation : attackLocations) {
                if (board.validLocation(attackLocation) && board.teamPieceAtLocation(enemy, attackLocation) != null) {
                    attackMoves.add(board.teamPieceAtLocation(enemy, attackLocation));
                }
            }
        }

        return moves;
    }
}