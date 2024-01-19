package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PawnMove {
  private ChessGame.TeamColor pieceColor;
  public PawnMove(ChessGame.TeamColor pieceColor) {
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> moveForward(ChessBoard board, ChessPosition myPosition, int rowChange, int colChange) {

    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (nextPosition.getColumn() < 9 && nextPosition.getRow() < 9 && nextPosition.getColumn() > 0 && nextPosition.getRow() > 0 && board.getPiece(nextPosition) == null) {
        ChessMove move = new ChessMove(myPosition, new ChessPosition(nextPosition.getRow(), nextPosition.getColumn()), null);
        validMoves.add(move);
    }

    return validMoves;
  }

  public Collection<ChessMove> capture(ChessBoard board, ChessPosition myPosition, int rowChange, int colChange){
    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (board.getPiece(nextPosition) != null && board.getPiece(nextPosition).getTeamColor() != pieceColor) {
      ChessMove move = new ChessMove(myPosition, nextPosition, null);
      validMoves.add(move);
    }
    return validMoves;
  }

  public Collection<ChessMove> promote(ChessBoard board, ChessPosition myPosition, int rowChange, int colChange) {
    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (board.getPiece(nextPosition) == null) {
      ArrayList<ChessPiece.PieceType> colors = new ArrayList<>(Arrays.asList(ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KING, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK));
      for
    }
  }

  public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.WHITE) {
      validMoves.addAll(moveForward(board, myPosition, 2, 0));
      validMoves.addAll(moveForward(board, myPosition, 1, 0));
    } else if (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.BLACK) {
      validMoves.addAll(moveForward(board, myPosition, -2, 0));
      validMoves.addAll(moveForward(board, myPosition, -1, 0));
    } else if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.BLACK) {
//      validMoves.addAll(promote)
    }

    else if (pieceColor == ChessGame.TeamColor.BLACK) {
      validMoves.addAll(moveForward(board, myPosition, -1, 0));
    } else {
      validMoves.addAll(moveForward(board, myPosition, 1, 0));
    }

    if (myPosition.getColumn() > 1) {
      if (pieceColor == ChessGame.TeamColor.BLACK) {
        validMoves.addAll(capture(board, myPosition, -1, -1));
      } else {
        validMoves.addAll(capture(board, myPosition, 1, -1));
      }
    }
    if (myPosition.getColumn() < 8) {
      if (pieceColor == ChessGame.TeamColor.BLACK) {
        validMoves.addAll(capture(board, myPosition, -1, 1));
      } else {
        validMoves.addAll(capture(board, myPosition, 1, 1));
      }
    }

    return validMoves;
  }
}
