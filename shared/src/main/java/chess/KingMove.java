package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMove {
  private ChessGame.TeamColor pieceColor;
  private ChessBoard board;
  private ChessPosition myPosition;
  public KingMove(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition myPosition) {
    this.pieceColor = pieceColor;
    this.board = board;
    this.myPosition = myPosition;
  }

  public Collection<ChessMove> move(int rowChange, int colChange) {
    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (nextPosition.getColumn() < 9 && nextPosition.getRow() < 9 && nextPosition.getColumn() > 0 && nextPosition.getRow() > 0) {
      if (board.getPiece(nextPosition) == null) {
        ChessMove move=new ChessMove(myPosition, new ChessPosition(nextPosition.getRow(), nextPosition.getColumn()), null);
        validMoves.add(move);
      }else {
        ChessPiece blockingPiece = board.getPiece(nextPosition);

        if (blockingPiece.getTeamColor() != pieceColor) {
          validMoves.add(new ChessMove(myPosition, nextPosition, null));
        }
      }
    }

    return validMoves;
  }

  public Collection<ChessMove> kingMoves() {
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    validMoves.addAll(move(1, 0));
    validMoves.addAll(move(-1, 0));
    validMoves.addAll(move(0, 1));
    validMoves.addAll(move(0, -1));
    validMoves.addAll(move(-1, -1));
    validMoves.addAll(move(-1, 1));
    validMoves.addAll(move(1, 1));
    validMoves.addAll(move(1, -1));

    return validMoves;
  }
}
