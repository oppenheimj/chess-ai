import game.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        play();
    }

    public static void play() {
        int turns = 1;

        Game previousState = new Game();
        Game nextState;

        for (int i = 0; i < turns; i++) {
            calculateFuture(previousState);
            nextState = previousState.getNextState();
            previousState = nextState;
        }
    }

    public static void calculateFuture(Game game) {
        List<Game> previousStates = new ArrayList<>();
        previousStates.add(game);

        List<Game> nextFutureStates = new ArrayList<>();

        int depth = 2;
        long numGames = 0;

        for (int i = 0; i < depth; i++) {
            for (Game previousState : previousStates) {
                previousState.calculateFutureStates();
                nextFutureStates.addAll(previousState.getFutureStates());
            }
            previousStates.clear();
            previousStates.addAll(nextFutureStates);

            numGames = nextFutureStates.size();
            nextFutureStates.clear();
        }
        System.out.println("There are " + numGames + " possible future states.");
    }
}