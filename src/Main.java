import game.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Game> previousStates = new ArrayList<>();
        previousStates.add(new Game());

        List<Game> nextFutureStates = new ArrayList<>();

        int depth = 4;
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
        System.out.println(numGames);
    }
}