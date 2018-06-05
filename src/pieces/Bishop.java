package pieces;

import game.*;

public class Bishop extends ZoneThreatPiece {

    public Bishop(Board board, String team, int[] location) {
        symbol = "b";
        NUMBER_OF_ZONES = 4;

        this.board = board;
        this.location = location;

        setTeam(team);
        board.move(this, location);
    }

    public Bishop clone(Board board) {
        int[] newLocation = new int[]{location[0], location[1]};
        return new Bishop(board, team, newLocation);
    }

    public int[] locationGenerator(int spaces, int index) {
        int[][] nextLocations = {
            {location[0]+spaces, location[1]+spaces},
            {location[0]-spaces, location[1]+spaces},
            {location[0]+spaces, location[1]-spaces},
            {location[0]-spaces, location[1]-spaces}
        };

        return nextLocations[index];
    }
}