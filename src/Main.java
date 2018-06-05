import game.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.calculateFutureStates();
        List<Game> futureStates = game.getFutureStates();
        game.displayFutureStates();
        int numGames = 0;

        for (Game nextGame : futureStates) {
            nextGame.calculateFutureStates();
            numGames += nextGame.getFutureStates().size();
            nextGame.displayFutureStates();
        }
        System.out.println("num games: " + numGames);

    }
}