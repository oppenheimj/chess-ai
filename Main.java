import game.*;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();

        for (int i = 0; i < 5; i++) {
            game.nextState(true);
        }
    }
}