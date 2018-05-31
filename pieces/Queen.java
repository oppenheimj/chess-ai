package pieces;

public class Queen extends Piece {
    public Queen(String team, int[] location) {
        setTeam(team);
        setLocation(location);
    }

    public String pieceCode() {
        return "Q";
    }
}