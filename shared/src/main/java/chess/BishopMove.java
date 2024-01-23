package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMove {
  private ChessGame.TeamColor pieceColor;
  private ChessBoard board;
  private ChessPosition myPosition;
  public BishopMove(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition position) {
    this.pieceColor = pieceColor;
    this.board = board;
    this.myPosition = position;
  }

  public Collection<ChessMove> move(int rowChange, int colChange) {
    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    while (nextPosition.getColumn() < 9 && nextPosition.getRow() < 9 && nextPosition.getColumn() > 0 && nextPosition.getRow() > 0 && board.getPiece(nextPosition) == null) {
      ChessMove move = new ChessMove(myPosition, new ChessPosition(nextPosition.getRow(), nextPosition.getColumn()), null);
      validMoves.add(move);
      nextPosition.updatePosition(nextPosition.getRow() + rowChange, nextPosition.getColumn() + colChange);
    }

    if (nextPosition.getColumn() < 9 && nextPosition.getRow() < 9 && nextPosition.getColumn() > 0 && nextPosition.getRow() > 0) {
      ChessPiece blockingPiece = board.getPiece(nextPosition);

      if (blockingPiece.getTeamColor() != pieceColor) {
        validMoves.add(new ChessMove(myPosition, nextPosition, null));
      }
    }

    return validMoves;
  }

  public Collection<ChessMove> bishopMoves() {
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    validMoves.addAll(move(-1, -1));
    validMoves.addAll(move(-1, 1));
    validMoves.addAll(move(1, 1));
    validMoves.addAll(move(1, -1));

    return validMoves;
  }
}
