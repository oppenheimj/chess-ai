import game.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static int movesDeep = 0;
    public static long gamesTotal = 0;
    public static void main(String[] args) {
        play();
//        recursivelyTraverse(new Game());
//        System.out.println(gamesTotal);
    }


    public static void recursivelyTraverse(Game game) {
        game.calculateFutureStates();
        List<Game> futureStates = game.getFutureStates();
        if (movesDeep < 2) {
            movesDeep++;
            for (Game futureState : futureStates) {
                recursivelyTraverse(futureState);
            }
            movesDeep--;
        } else {
            gamesTotal++;
        }
    }

    public static void play() {
        Game startState = new Game();
        int turns = 10;

        Game previousState = startState;
        Game nextState = null;

        for (int i = 0; i < turns; i++) {
            System.out.println(previousState.board.getState() + "\n*******************\n");
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