## Overview
The objective of this project is to create a chess program that slaughters me. The program currently generates a graph of states into the future and assigns values to states in order to "decide" the next move.

## Setup
Download and install the latest [Java Development Kit](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) (JDK). If using Windows, make sure to add Java to your path by going to `Control Panel > Edit Environment Variables For Your Account > Edit Path` and add `C:\Program Files\Java\jdk-11.0.1\bin\`, for example.
### Compile
Browse to the project root (`chess-ai/`) and run
```
javac -cp src -d out src/Main.java
```
This creates all of the `*.class` files inside `out/`, organized by package.

Note: If you run into problems related to encoding, create the following environment variable:
```
JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8
```
### Run
Browse to `chess-ai/out/` and run
```
java Main
```
This executes the program.

## Next steps
* Develop algorithm for pruning tree of possible state transitions

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
