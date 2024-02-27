package chessMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chessMoves.MoveFinder.findMovesMultipleSpaces;

public class QueenMove {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor pieceColor;

  public QueenMove(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
    this.board = board;
    this.myPosition = myPosition;
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> move(){
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    validMoves.addAll(findMovesMultipleSpaces(pieceColor, board, myPosition, 1, 1));
    validMoves.addAll(findMovesMultipleSpaces(pieceColor, board, myPosition, -1, 1));
    validMoves.addAll(findMovesMultipleSpaces(pieceColor, board, myPosition,1, -1));
    validMoves.addAll(findMovesMultipleSpaces(pieceColor, board, myPosition,-1, -1));
    validMoves.addAll(findMovesMultipleSpaces(pieceColor, board, myPosition,0, 1));
    validMoves.addAll(findMovesMultipleSpaces(pieceColor, board, myPosition,0, -1));
    validMoves.addAll(findMovesMultipleSpaces(pieceColor, board, myPosition,1, 0));
    validMoves.addAll(findMovesMultipleSpaces(pieceColor, board, myPosition, -1, 0));

    return validMoves;
  }
}
