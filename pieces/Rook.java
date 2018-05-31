package pieces;

public class Rook extends Piece {
    public Rook(String team, int[] location) {
        setTeam(team);
        setLocation(location);
    }

    public String pieceCode() {
        return "R";
    }
}