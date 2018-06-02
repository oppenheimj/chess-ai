package pieces;

import game.*;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public abstract class Piece {
    protected String symbol;
    protected int[] location;

    protected String team;
    protected String enemy;

    public List<int[]> moves;
    public List<Piece> attackMoves;

    private Random rand = new Random();
    public Board board;

    public boolean threatened;
    public boolean defended;

    public boolean threatening;
    public boolean defending;

    public abstract List<int[]> calculateMoves();

    public String getSymbol() {
        return team.equals("B") ? symbol.toUpperCase() : symbol;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
        enemy = team.equals("W") ? "B" : "W";
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] newLocation) {
        location = newLocation;
    }

    public void randomMove() {
        System.out.println("Moving piece " + symbol + ", which has " + moves.size() + " possible moves");
        if (moves.size() > 0) {
            int moveChoice = rand.nextInt(moves.size());
            board.move(this, moves.get(moveChoice));
        }
    }
}