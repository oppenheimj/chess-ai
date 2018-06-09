package pieces;

import game.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Piece {
    String symbol;
    int[] location;
    int value;

    String team;
    String enemy;

    public boolean movedThisTurn;

    public List<int[]> moves;
    public List<Piece> threatening;
    public List<Piece> threatenedBy;
    public List<Piece> defending;
    public List<Piece> defendedBy;

    public List<int[]> pathToEnemyKing;

    public Board board;

    public abstract void calculateMoves();

    public abstract Piece clone(Board board);


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
        moves.clear();
        threatening.clear();
        threatenedBy.clear();
        defending.clear();
        defendedBy.clear();
        pathToEnemyKing.clear();
    }

    public void undoPostures() {
        for (Piece threatenedPiece : threatening) {
            threatenedPiece.threatenedBy.remove(this);
        }
        for (Piece defendedPiece : defending) {
            defendedPiece.defendedBy.remove(this);
        }
        if (this instanceof Pawn) {
            ((Pawn) this).corners.clear();
        }
        threatening.clear();
        defending.clear();
    }

    public String getSymbol() {
        String formattedSymbol = team.equals("B") ? symbol.toUpperCase() : symbol;
        if (formattedSymbol.length() == 2) {
            formattedSymbol += (movedThisTurn ? "." : " ");
        } else {
            formattedSymbol += (movedThisTurn ? ". " : "  ");
        }

        return formattedSymbol;
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

    public int getValue() {
        return value;
    }
}