## Overview
The objective of this project is to create a chess program that slaughters me. In its current form, the computer plays both sides. Pieces wander the board, killing each other by chance, and checkmate situations arise by chance. The check resolution algorithm seems to work flawlessly.

## Next steps
* Develop algorithm for pruning tree of possible state transitions
* Allow moves to be entered via command line so as to allow a human to play against the computer

## Code map
```
├── LICENSE
├── README.md
└── src
   ├── Main.java
   ├── game
   │  ├── Board.java
   │  ├── Game.java
   │  ├── Pieces.java
   │  └── State.java
   ├── logic
   │  ├── CheckManager.java
   │  └── DecisionMaker.java
   └── pieces
      ├── Bishop.java
      ├── King.java
      ├── Knight.java
      ├── Pawn.java
      ├── Piece.java                (abstract)
      ├── PointThreatPiece.java     (abstract)
      ├── Queen.java
      ├── Rook.java
      └── ZoneThreatPiece.java      (abstract)
```
