import game.*;

public class Main {
    public static void main(String[] args) {
        for (int j = 0; j < 100000; j++) {
            Game game = new Game();
            for (int i = 0; i < 10; i++) {
                game.nextState(false);
            }
        }
    }
}