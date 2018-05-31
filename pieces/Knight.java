package pieces;

public class Knight extends Piece {
    public Knight(String team, int[] location) {
        setTeam(team);
        setLocation(location);
    }

    public String pieceCode() {
        return "Kn";
    }
}