package game;

public class Game {
    public Game() {}

    public static void play() {
        int turns = 40;


        State previousState = new State();
        State nextState;

        for (int i = 0; i < turns; i++) {
            previousState.display();
            previousState.displayStatusText();

            nextState = previousState.getNextState();
            previousState = nextState;
        }
    }
}
