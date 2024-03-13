package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {
  private final String BORDER_SPACE = " ";
  private final String EMPTY_SQUARE = "   ";
  private chess.ChessBoard board;

  public ChessBoard() {
    board = new chess.ChessBoard();
    board.resetBoard();
  }

  public void drawBoard() {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    drawHeader(out);
    drawRows(out);
    drawHeader(out);
  }

  public void drawHeader(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
    String[] headers = { "A", "B", "C", "D", "E", "F", "G", "H" };
    out.print(EMPTY_SQUARE);
    for (String header : headers) {
      out.print(BORDER_SPACE);
      out.print(header);
      out.print(BORDER_SPACE);
    }
    out.print("   ");
    out.println(RESET_BG_COLOR);
  }

  public void drawRows(PrintStream out) {
    for (int i = 1; i < 9; i++) {
      out.print(SET_BG_COLOR_BLACK);
      drawSideHeader(out, i);
      drawRowSquares(out, i);
      drawSideHeader(out, i);
      out.println(RESET_BG_COLOR);
    }
  }

  public void drawSideHeader(PrintStream out, int rowNum) {
    out.print(SET_TEXT_COLOR_WHITE);
    out.print(BORDER_SPACE);
    out.print(rowNum);
    out.print(BORDER_SPACE);
  }

  public void drawRowSquares(PrintStream out, int rowNum) {
    for (int i = 1; i < 9; i++) {
      if (i % 2 == rowNum % 2) {
        out.print(SET_BG_COLOR_WHITE);
      } else {
        out.print(SET_BG_COLOR_DARK_GREY);
      }
      out.print(BORDER_SPACE);
      drawChessPiece(out, rowNum, i);
      out.print(BORDER_SPACE);
    }
    out.print(RESET_BG_COLOR);
  }

  public void drawChessPiece(PrintStream out, int rowNum, int colNum) {
    chess.ChessPiece piece = board.getPiece(new ChessPosition(rowNum, colNum));
    chess.ChessPiece.PieceType pieceType;
    if (piece != null){
       pieceType = piece.getPieceType();
      setPieceColor(out, piece);
    } else {
      pieceType = null;
    }

    switch(pieceType) {
      case BISHOP:
        out.print("B");
        break;
      case PAWN:
        out.print("P");
        break;
      case ROOK:
        out.print("R");
        break;
      case KNIGHT:
        out.print("N");
        break;
      case KING:
        out.print("K");
        break;
      case QUEEN:
        out.print("Q");
        break;
      case null:
        out.print(" ");
    }

    out.print(RESET_TEXT_COLOR);
  }

  public void setPieceColor(PrintStream out, chess.ChessPiece piece) {
    ChessGame.TeamColor pieceColor = piece.getTeamColor();

    switch (pieceColor) {
      case BLACK:
        out.print(SET_TEXT_COLOR_RED);
        break;
      case WHITE:
        out.print(SET_TEXT_COLOR_BLUE);
        break;
    }
  }
}
