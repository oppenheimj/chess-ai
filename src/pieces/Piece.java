package pieces;

import game.*;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public abstract class Piece {
    public int[] location;
    public String team;
    public String enemy;
    public List<int[]> moves;
    public List<Piece> attackMoves;
    private Random rand = new Random();
    public Board board;
    public String symbol;

    public String symbol() {
        return team.equals("B") ? symbol.toUpperCase() : symbol;
    }

    public abstract List<int[]> calculateMoves();

    public void randomMove() {
        System.out.println("Moving piece " + symbol() + ", which has " + moves.size() + " possible moves");
        if (moves.size() > 0) {
            int moveChoice = rand.nextInt(moves.size());
            move(moves.get(moveChoice));
        }
    }

    public void move(int[] newLocation) {
        board.board[location[0]][location[1]] = null;
        board.board[newLocation[0]][newLocation[1]] = this;
        location = newLocation;
    }

    public void setTeam(String team) {
        this.team = team;
        enemy = team.equals("W") ? "B" : "W";
    }

    public String getTeam() {
        return team;
    }
}