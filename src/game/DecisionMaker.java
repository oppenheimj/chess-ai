package game;

import pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class DecisionMaker  {
    private static Random rand = new Random();

    static List<Game> checkResolutions(Piece king, Game game, List<Piece> checkers) {
        List<Game> futureStates = new ArrayList<>();
        if (checkers.size() > 1) {
            if (!king.moves.isEmpty()) {
                for (int[] move : king.moves) {
                    futureStates.add(new Game(game, king, move));
                }
            }
            for (Piece checker : checkers) {
                if (checker.defendedBy.isEmpty()) {
                    futureStates.add(new Game(game, king, checker.getLocation()));
                }
            }
            return futureStates;
        } else {
            Piece soleChecker = checkers.get(0);
            for (Piece threat : soleChecker.threatenedBy) {
                futureStates.add(new Game(game, threat, soleChecker.getLocation()));
            }
            for (int[] move : king.moves) {
                futureStates.add(new Game(game, king, move));
            }
            if (soleChecker instanceof Queen || soleChecker instanceof Rook || soleChecker instanceof Bishop) {
                List[] data = checkForBlockers(game, king, soleChecker);
                List<Piece> pieces = data[0];
                List<int[]> locations = data[1];
                for (int i = 0; i < pieces.size(); i++) {
                    futureStates.add(new Game(game, pieces.get(i), locations.get(i)));
                }
            }
        }

        return futureStates;
    }

    static boolean checkResolution(Piece king, Game game, List<Piece> checkers) {
        Board board = game.board;
        if (checkers.size() > 1) {
            if (!king.moves.isEmpty()) {
                board.move(king, king.moves.get(0));
            }
            return !king.moves.isEmpty();
        } else {
            Piece soleChecker = checkers.get(0);
            if (!soleChecker.threatenedBy.isEmpty()) {
                game.kill(soleChecker.threatenedBy.get(0), soleChecker);
                return true;
            } else if (!king.moves.isEmpty()) {
                board.move(king, king.moves.get(0));
                return true;
            } else {
                return (soleChecker instanceof Queen || soleChecker instanceof Rook || soleChecker instanceof Bishop) &&
                        checkForBlock(game, king, soleChecker);
            }
        }
    }

    private static List[] checkForBlockers(Game game, Piece king, Piece checker) {
        List<Piece> pieces = new ArrayList<>();
        List<int[]> locations = new ArrayList<>();

        List<int[]> lineOfSight = checker.pathToEnemyKing;
        List<Piece> friendlyPieces = game.pieces.getPiecesBelongingToTeam(king.getTeam());
        for (Piece friendlyPiece : friendlyPieces) {
            List<int[]> intersection = Piece.intersectLocationSets(friendlyPiece.moves, lineOfSight);
            if (!intersection.isEmpty()) {
                for (int[] location: intersection) {
                    pieces.add(friendlyPiece);
                    locations.add(location);
                }
            }
        }

        return new List[] {pieces, locations};
    }

    private static boolean checkForBlock(Game game, Piece king, Piece checker) {
        boolean canBlock = false;
        List<int[]> lineOfSight = checker.pathToEnemyKing;
        List<Piece> friendlyPieces = game.pieces.getPiecesBelongingToTeam(king.getTeam());
        for (Piece friendlyPiece : friendlyPieces) {
            List<int[]> intersection = Piece.intersectLocationSets(friendlyPiece.moves, lineOfSight);
            if (!intersection.isEmpty()) {
                canBlock = true;
                game.board.move(friendlyPiece, intersection.get(0));
            }
        }

        return canBlock;
    }

    static List<Piece> checkDetection(Piece king, List<Piece> otherTeamsPieces) {
        List<Piece> checkers = new ArrayList<>();
        for (Piece piece : otherTeamsPieces) {
            if (piece.threatening.contains(king)) {
                checkers.add(piece);
            }
        }

        return checkers;
    }

    static List<Game> makeMoves(List<Piece> pieceSet, Game game) {
        List<Game> nextStates = new ArrayList<>();
        for (Piece piece : pieceSet) {
            for (Piece threatenedPiece : piece.threatening) {
                nextStates.add(new Game(game, piece, threatenedPiece.getLocation()));
            }
            for (int[] location : piece.moves) {
                nextStates.add(new Game(game, piece, location));
            }
        }
        return nextStates;
    }

    static void makeMove(List<Piece> pieceSet, Game game) {
        boolean killMade = false;
        for (Piece piece : pieceSet) {
            if (!piece.threatening.isEmpty()) {
                game.kill(piece, piece.threatening.get(0));
                killMade = true;
                break;
            }
        }
        if (!killMade) {
            //TODO remove once I figure out how to handle this case
            if (pieceSet.size() == 1 && pieceSet.get(0) instanceof King && pieceSet.get(0).moves.isEmpty()) {
                System.out.println("GG, ONLY KING LEFT");
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