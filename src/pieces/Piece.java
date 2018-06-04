package pieces;

import game.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Piece {
    String symbol;
    int[] location;
    String team;
    String enemy;

    public List<int[]> moves;
    public List<Piece> threatening;
    public List<Piece> threatenedBy;
    public List<Piece> defending;
    public List<Piece> defendedBy;

    public List<int[]> pathToEnemyKing;

    public Board board;

    public abstract List<int[]> calculateMoves();

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

    void clearPostures() {
        moves = new ArrayList<>();
        threatening = new ArrayList<>();
        threatenedBy = new ArrayList<>();
        defending = new ArrayList<>();
        defendedBy = new ArrayList<>();
        pathToEnemyKing = new ArrayList<>();
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