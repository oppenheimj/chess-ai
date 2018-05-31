package pieces;

public abstract class Piece {
    int[] location = new int[2];
    String team;

    public abstract String pieceCode();

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] newLocation) {
        location = newLocation;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    
}