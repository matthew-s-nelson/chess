package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMove {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor pieceColor;

  public KingMove(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
    this.board = board;
    this.myPosition = myPosition;
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> findMoves(int rowChange, int colChange){
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    ChessPosition newPos = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

    if (newPos.inBounds() && board.getPiece(newPos) == null){
      validMoves.add(new ChessMove(myPosition, newPos.copy(), null));
    } else if(newPos.inBounds() && board.getPiece(newPos) != null && board.getPiece(newPos).getTeamColor() != pieceColor){
      validMoves.add(new ChessMove(myPosition, newPos.copy(), null));
    }
    return validMoves;
  }

  public Collection<ChessMove> move(){
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    validMoves.addAll(findMoves(1, 1));
    validMoves.addAll(findMoves(-1, 1));
    validMoves.addAll(findMoves(1, -1));
    validMoves.addAll(findMoves(-1, -1));
    validMoves.addAll(findMoves(1, 0));
    validMoves.addAll(findMoves(-1, 0));
    validMoves.addAll(findMoves(0, 1));
    validMoves.addAll(findMoves(0, -1));

    return validMoves;
  }
}
