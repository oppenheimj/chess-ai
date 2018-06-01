package game;

import pieces.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Board {
    public Piece[][] board = new Piece[8][8];

    public Board() {}

    public void display() {
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if (piece != null) {
                    System.out.print(piece.symbol() + (piece.symbol().length() == 2 ? " " : "  "));
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println( );
    }

    public boolean validLocation(int[] location) {
        return location[0] >= 0 && location[0] < 8 &&
            location[1] >= 0 && location[1] < 8;
    }

    public boolean unoccupiedLocation(int[] location) {
        return board[location[0]][location[1]] == null;
    }

    public Piece teamPieceAtLocation(String team, int[] location) {
        Piece piece = board[location[0]][location[1]];
        return (piece != null && piece.team.equals(team)) ? piece : null; 
    }
}