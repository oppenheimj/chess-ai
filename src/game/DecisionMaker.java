package game;

import pieces.*;
import java.util.List;
import java.util.Random;

public class DecisionMaker  {
    static Random rand = new Random();

    public static Boolean pieceUnderThreat(Piece piece) {
        return !piece.threatenedBy.isEmpty();
    }

    public static boolean checkResolution(Piece king, Game game) {
        Board board = game.board;
        if (!king.threatening.isEmpty()) {
            game.kill(king, king.threatening.get(0));
            return true;
        } else if (!king.moves.isEmpty()) {
            board.move(king, king.moves.get(0));
            return true;
        } else {
            System.out.println("GG");
            return false;
        }
    }

    public static boolean checkDetection(Piece king, List<Piece> otherTeamsPieces) {
        for (Piece piece : otherTeamsPieces) {
            if (piece.threatening.contains(king)) {
                System.out.println("CHECK!");
                return true;
            }
        }
        return false;
    }

    public static void makeMove(List<Piece> pieceSet, Game game) {
        boolean killMade = false;
        for (Piece piece : pieceSet) {
            if (!piece.threatening.isEmpty()) {
                game.kill(piece, piece.threatening.get(0));
                killMade = true;
                System.out.println("KILL");
                break;
            }
        }
        if (!killMade) {
            if (pieceSet.size() == 1 && pieceSet.get(0) instanceof King && pieceSet.get(0).moves.isEmpty()) {
                System.out.println("GG");
            } else {
                int chosenPiece = rand.nextInt(pieceSet.size());
                while (pieceSet.get(chosenPiece).moves.isEmpty()) {
                    chosenPiece = rand.nextInt(pieceSet.size());
                }
                Piece piece = pieceSet.get(chosenPiece);
                int chosenMove = rand.nextInt(piece.moves.size());
                game.board.move(piece, piece.moves.get(chosenMove));
            }
        }
    }
}