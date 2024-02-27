package chessMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class MoveFinder {
  public static Collection<ChessMove> findMoves(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition myPosition, int rowChange, int colChange) {
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    ChessPosition newPos = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

    while(newPos.inBounds() && board.getPiece(newPos) == null){
      validMoves.add(new ChessMove(myPosition, newPos.copy(), null));
      newPos.updatePos(rowChange, colChange);
    }

    if(newPos.inBounds() && board.getPiece(newPos) != null && board.getPiece(newPos).getTeamColor() != pieceColor){
      validMoves.add(new ChessMove(myPosition, newPos.copy(), null));
    }
    return validMoves;
  }
}
