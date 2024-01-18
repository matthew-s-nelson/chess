package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMove {
  private ChessGame.TeamColor pieceColor;
  public KingMove(ChessGame.TeamColor pieceColor) {
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> move(ChessBoard board, ChessPosition myPosition, int rowChange, int colChange) {
    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (nextPosition.getColumn() < 9 && nextPosition.getRow() < 9 && nextPosition.getColumn() > 0 && nextPosition.getRow() > 0 && board.getPiece(nextPosition) == null) {
      ChessMove move = new ChessMove(myPosition, new ChessPosition(nextPosition.getRow(), nextPosition.getColumn()), null);
      validMoves.add(move);
    } else if (nextPosition.getColumn() < 9 && nextPosition.getRow() < 9 && nextPosition.getColumn() > 0 && nextPosition.getRow() > 0) {
      ChessPiece blockingPiece = board.getPiece(nextPosition);

      if (blockingPiece.getTeamColor() != pieceColor) {
        validMoves.add(new ChessMove(myPosition, nextPosition, null));
      }
    }

    return validMoves;
  }

  public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    validMoves.addAll(move(board, myPosition, 1, 0));
    validMoves.addAll(move(board, myPosition, -1, 0));
    validMoves.addAll(move(board, myPosition, 0, 1));
    validMoves.addAll(move(board, myPosition, 0, -1));
    validMoves.addAll(move(board, myPosition, -1, -1));
    validMoves.addAll(move(board, myPosition, -1, 1));
    validMoves.addAll(move(board, myPosition, 1, 1));
    validMoves.addAll(move(board, myPosition, 1, -1));

    return validMoves;
  }
}
