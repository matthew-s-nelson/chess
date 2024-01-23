package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PawnMove {
  private ChessGame.TeamColor pieceColor;
  private ChessBoard board;
  private ChessPosition myPosition;
  public PawnMove(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition myPosition) {
    this.pieceColor = pieceColor;
    this.board = board;
    this.myPosition = myPosition;
  }

  public Collection<ChessMove> moveForward(int rowChange, int colChange) {

    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (nextPosition.getColumn() < 9 && nextPosition.getRow() < 9 && nextPosition.getColumn() > 0 && nextPosition.getRow() > 0 && board.getPiece(nextPosition) == null) {
        ChessMove move = new ChessMove(myPosition, new ChessPosition(nextPosition.getRow(), nextPosition.getColumn()), null);
        validMoves.add(move);
    }

    return validMoves;
  }

  public Collection<ChessMove> starting(int rowChange1) {

    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange1, myPosition.getColumn());
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (board.getPiece(nextPosition) == null) {
      ChessMove move = new ChessMove(myPosition, new ChessPosition(nextPosition.getRow(), nextPosition.getColumn()), null);
      validMoves.add(move);

      if (board.getPiece(new ChessPosition(nextPosition.getRow() + rowChange1, nextPosition.getColumn())) == null) {
        ChessMove move2 = new ChessMove(myPosition, new ChessPosition(nextPosition.getRow() + rowChange1, nextPosition.getColumn()), null);
        validMoves.add(move2);
      }
    };

    return validMoves;
  }

  public Collection<ChessMove> capture(int rowChange, int colChange){
    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    if (board.getPiece(nextPosition) != null && board.getPiece(nextPosition).getTeamColor() != pieceColor) {
      if ((pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) || (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2)) {
        validMoves.addAll(promote(rowChange, colChange));
      } else {
        ChessMove move= new ChessMove(myPosition, nextPosition, null);
        validMoves.add(move);
      }
    }
    return validMoves;
  }

  // Add in an if statement to see if it is about to be promoted. If it is, promote it too.
  public Collection<ChessMove> promote(int rowChange, int colChange) {
    ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if ((board.getPiece(nextPosition) == null &&  colChange == 0) || colChange != 0) {
      ArrayList<ChessPiece.PieceType> colors = new ArrayList<>(Arrays.asList(ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK));
      for (ChessPiece.PieceType promotionType : colors) {
        ChessMove move = new ChessMove(myPosition, nextPosition, promotionType);
        validMoves.add(move);
      }
    }
    return validMoves;
  }

  public Collection<ChessMove> pawnMoves() {
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.WHITE) {  // Starting position White
      validMoves.addAll(starting(1));
    } else if (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.BLACK) { // Starting position Black
      validMoves.addAll(starting(-1));
    } else if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.BLACK) {  // Promote Black
      validMoves.addAll(promote(-1, 0));
    } else if (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.WHITE) { // Promote White
      validMoves.addAll(promote(1, 0));
    } else if (pieceColor == ChessGame.TeamColor.BLACK) { // Move Black
      validMoves.addAll(moveForward(-1, 0));
    } else { // Move White
      validMoves.addAll(moveForward(1, 0));
    }

    if (myPosition.getColumn() > 1) {
      if (pieceColor == ChessGame.TeamColor.BLACK) { // Capture left Black
          validMoves.addAll(capture(-1, -1));
      } else { // Capture left White
          validMoves.addAll(capture(1, -1));
      }
    }
    if (myPosition.getColumn() < 8) {
      if (pieceColor == ChessGame.TeamColor.BLACK) { // Capture right black
          validMoves.addAll(capture(-1, 1));
      } else { // Capture right white
        validMoves.addAll(capture(1, 1));
      }
    }

    return validMoves;
  }
}
