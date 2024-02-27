package chessMoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMove {
  private ChessBoard board;
  private ChessPosition myPosition;
  private ChessGame.TeamColor pieceColor;

  public PawnMove(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
    this.board = board;
    this.myPosition = myPosition;
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> findMoves(int rowChange, int colChange){
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    ChessPosition newPos = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

    if (newPos.inBounds() && board.getPiece(newPos) == null){
      validMoves.add(new ChessMove(myPosition, newPos.copy(), null));
    }

    return validMoves;
  }

  public Collection<ChessMove> capture(int rowChange, int colChange){
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    ChessPosition newPos = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + colChange);

    if(newPos.inBounds() && board.getPiece(newPos) != null && board.getPiece(newPos).getTeamColor() != pieceColor){
      if (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.WHITE){
        validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.KNIGHT));
        validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.ROOK));
      } else if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.BLACK) {
        validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.KNIGHT));
        validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.ROOK));
      } else {
        validMoves.add(new ChessMove(myPosition, newPos.copy(), null));
      }
    }

    return validMoves;
  }

  public Collection<ChessMove> starting(int rowChange){
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    ChessPosition newPos = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn());

    if (newPos.inBounds() && board.getPiece(newPos) == null) {
      validMoves.add(new ChessMove(myPosition, newPos.copy(), null));
      newPos.updatePos(rowChange, 0);
      if (newPos.inBounds() && board.getPiece(newPos) == null){
        validMoves.add(new ChessMove(myPosition, newPos.copy(), null));
      }
    }

    return validMoves;
  }

  public Collection<ChessMove> promote(int rowChange){
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    ChessPosition newPos = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn());

    if (newPos.inBounds() && board.getPiece(newPos) == null){
      validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.BISHOP));
      validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.KNIGHT));
      validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.QUEEN));
      validMoves.add(new ChessMove(myPosition, newPos.copy(), ChessPiece.PieceType.ROOK));
    }

    return validMoves;
  }

  public Collection<ChessMove> move(){
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.WHITE){
      validMoves.addAll(starting(1));
    } else if (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.BLACK){
      validMoves.addAll(starting(-1));
    } else if (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.WHITE){
      validMoves.addAll(promote(1));
    } else if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.BLACK){
      validMoves.addAll(promote(-1));
    } else if (pieceColor == ChessGame.TeamColor.WHITE){
      validMoves.addAll(findMoves(1, 0));
    } else if (pieceColor == ChessGame.TeamColor.BLACK){
      validMoves.addAll(findMoves(-1, 0));
    }

    if (pieceColor == ChessGame.TeamColor.WHITE){
      validMoves.addAll(capture(1, 1));
      validMoves.addAll(capture(1, -1));
    } else {
      validMoves.addAll(capture(-1, 1));
      validMoves.addAll(capture(-1, -1));
    }

    return validMoves;
  }
}
