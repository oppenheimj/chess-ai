package pieces;

import game.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected String symbol;
    protected int[] location;

    protected String team;
    protected String enemy;

    public List<int[]> moves;
    public List<Piece> threatening;
    public List<Piece> threatenedBy;
    public List<Piece> defending;
    public List<Piece> defendedBy;

    public Board board;

    public abstract List<int[]> calculateMoves();

    protected void clearPostures() {
        moves = new ArrayList<>();
        threatening = new ArrayList<>();
        threatenedBy = new ArrayList<>();
        defending = new ArrayList<>();
        defendedBy = new ArrayList<>();
    }

    public void undoPostures() {
        for (Piece threatenedPiece : threatening) {
            threatenedPiece.threatenedBy.remove(this);
        }
        for (Piece defendedPiece : defending) {
            defendedPiece.defendedBy.remove(this);
        }
        threatening = new ArrayList<>();
        defending = new ArrayList<>();
    }

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

    public String getEnemy() {
        return enemy;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] newLocation) {
        location = newLocation;
    }
}