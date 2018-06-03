package game;

import pieces.*;

import java.util.List;
import java.util.Random;

public class DecisionMaker  {
    static Random rand = new Random();

    public static boolean checkResolution(Piece king, Game game, Piece checker) {
        Board board = game.board;
        if (!checker.threatenedBy.isEmpty()) {
            game.kill(checker.threatenedBy.get(0), checker);
            return true;
        } else if (!king.moves.isEmpty()) {
            board.move(king, king.moves.get(0));
            return true;
        } else if (checker instanceof Queen || checker instanceof Rook || checker instanceof Bishop) {
            return checkForBlock(game, king, checker);
        } else {
            System.out.println("GG");
            return false;
        }
    }

    public static boolean checkForBlock(Game game, Piece king, Piece checker) {
        boolean canBlock = false;
        List<int[]> lineOfSight = checker.pathToEnemyKing;
        List<Piece> friendlyPieces = game.pieces.getPiecesBelongingToTeam(king.getTeam());
        for (Piece friendlyPiece : friendlyPieces) {
            List<int[]> intersection = Pieces.intersectLocationSets(friendlyPiece.moves, lineOfSight);
            if (!intersection.isEmpty()) {
                canBlock = true;
                game.board.move(friendlyPiece, intersection.get(0));
            }
        }
        return canBlock;
    }

    public static Piece checkDetection(Piece king, List<Piece> otherTeamsPieces) {
        Piece checker = null;
        for (Piece piece : otherTeamsPieces) {
            if (piece.threatening.contains(king)) {
                System.out.println("CHECK!");
                checker = piece;
                break;
            }
        }
        return checker;
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