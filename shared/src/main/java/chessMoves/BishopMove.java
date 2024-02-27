package chessMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static chessMoves.MoveFinder.findMoves;

public class BishopMove {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor pieceColor;

  public BishopMove(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
    this.board = board;
    this.myPosition = myPosition;
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> move(){
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    validMoves.addAll(findMoves(pieceColor, board, myPosition, 1, 1));
    validMoves.addAll(findMoves(pieceColor, board, myPosition, -1, 1));
    validMoves.addAll(findMoves(pieceColor, board, myPosition, 1, -1));
    validMoves.addAll(findMoves(pieceColor, board, myPosition, -1, -1));

    return validMoves;
  }
}
