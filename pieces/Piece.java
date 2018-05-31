package pieces;

import game.*;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public abstract class Piece {
    public int[] location;
    public String team;
    public List<int[]> moves;
    private Random rand = new Random();
    public Board board; 

    public abstract String pieceCode();

    public abstract List<int[]> calculateMoves();

    public void randomMove() {
        if (moves.size() > 0) {
            int move = rand.nextInt(moves.size());
            board.movePiece(this, moves.get(move));
        }
    }

    public void setLocation(int[] newLocation) {
        location = newLocation;        
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }
}