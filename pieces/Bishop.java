package pieces;

public class Bishop extends Piece {
    public Bishop(String team, int[] location) {
        setTeam(team);
        setLocation(location);
    }

    public String pieceCode() {
        return "B";
    }
}