package game;

import pieces.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class DecisionMaker  {
    static Random rand = new Random();

    public static Boolean pieceUnderThreat(Pieces pieces, Piece piece) {
        String enemyTeam = piece.getTeam().equals("W") ? "B" : "W";
        List<Piece> enemyPieces = pieces.getPiecesBelongingToTeam(enemyTeam);
        pieces.calculateMoves();

        List<Piece> targets = new ArrayList<>();
        for (Piece enemyPiece : enemyPieces) {
            targets.addAll(enemyPiece.attackMoves);
        }

        return targets.contains(piece);
    }

    public static void checkResolution(Piece king, Game game) {
        Board board = game.board;
        Pieces pieces = game.pieces;

        king.calculateMoves();
        List<int[]> kingsMoves = king.moves;
        List<Piece> kingsKills = king.attackMoves;

        List<int[]> validMoves = new ArrayList<>();
        List<Piece> validKills = new ArrayList<>();

        int[] originalLocation = king.getLocation();

        for (int[] location : kingsMoves) {
            board.move(king, location);
            if (!pieceUnderThreat(pieces, king)) {
                validMoves.add(location);
            }
            board.move(king, originalLocation);
        }

        for (Piece targetKill : kingsKills) {
            int[] location = targetKill.getLocation();
            board.move(king, targetKill.getLocation());
            if (!pieceUnderThreat(game.pieces, king)) {
                validKills.add(targetKill);
            }
            board.move(king, originalLocation);
            board.move(targetKill, location);
        }

        if (validKills.size() > 0) {
            System.out.println("Resolving by kill");
            game.kill(king, validKills.get(0));
            game.check = false;
        } else if (validMoves.size() > 0) {
            System.out.println("Resolving by move");
            board.move(king, validMoves.get(0));
            game.check = false;
        } else {
            System.out.println("GG");
        }
        //Pieces.calculateMoves(Pieces.getPiecesOfTeam(king.team));
    }

    public static void makeMove(List<Piece> pieceSet, Game game) {
        boolean killMade = false;
        for (Piece piece : pieceSet) {
            if (piece.attackMoves.size() > 0) {
                game.kill(piece, piece.attackMoves.get(0));
                killMade = true;
                System.out.println("KILL");
                break;
            }
        }
        if (!killMade) {
            int chosenPiece = rand.nextInt(pieceSet.size());
            while (pieceSet.get(chosenPiece) instanceof King || pieceSet.get(chosenPiece).moves.size() == 0) {
                chosenPiece = rand.nextInt(pieceSet.size());
            }
            Piece piece = pieceSet.get(chosenPiece);
            int chosenMove = rand.nextInt(piece.moves.size());
            game.board.move(piece, piece.moves.get(chosenMove));
        }
    }
}