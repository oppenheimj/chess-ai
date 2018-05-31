import game.*;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.initialize();
        board.display();

        for (int i = 0; i < 10; i++) {
            board.nextState();
            board.display();
        }
    }
}