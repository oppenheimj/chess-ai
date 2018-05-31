package pieces;

public class Pawn extends Piece {
    public Pawn(String team, int[] location) {
        setTeam(team);
        setLocation(location);
    }

    public String pieceCode() {
        return "P";
    }
}