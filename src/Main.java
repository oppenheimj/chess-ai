import game.*;

public class Main {
    public static void main(String[] args) {
        for (int j = 0; j < 1000; j++) {
            Game game = new Game();
            for (int i = 0; i < 250; i++) {
                game.nextState(false);
            }
        }
    }
}