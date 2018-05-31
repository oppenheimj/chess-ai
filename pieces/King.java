package pieces;

public class King extends Piece {
    public King(String team, int[] location) {
        setTeam(team);
        setLocation(location);
    }

    public String pieceCode() {
        return "Ki";
    }
}