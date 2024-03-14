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

  // color key: 0 = white, 1 = black
  public void drawBoard() {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    drawHeader(out, 0);
    drawRows(out, 0);
    drawHeader(out, 0);

    out.println();
    drawHeader(out, 1);
    drawRows(out, 1);
    drawHeader(out, 1);
  }

  public void drawHeader(PrintStream out, int color) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
    String[] headers;
    if (color == 0) {
      headers =new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
    } else {
      headers =new String[]{"H", "G", "F", "E", "D", "C", "B", "A"};
    }
    out.print(EMPTY_SQUARE);
    for (String header : headers) {
      out.print(BORDER_SPACE);
      out.print(header);
      out.print(BORDER_SPACE);
    }
    out.print("   ");
    out.println(RESET_BG_COLOR);
  }

  public void drawRows(PrintStream out, int color) {
    int[] startEnd = this.setStartAndEnd(color);
    int row = startEnd[0];
    int end = startEnd[1];
    while (row != end) {
      out.print(SET_BG_COLOR_BLACK);
      drawSideHeader(out, row);
      drawRowSquares(out, row, color);
      drawSideHeader(out, row);
      out.println(RESET_BG_COLOR);
      row = incrementOrDecrement(color, row);
    }
  }

  public void drawSideHeader(PrintStream out, int rowNum) {
    out.print(SET_TEXT_COLOR_WHITE);
    out.print(BORDER_SPACE);
    out.print(rowNum);
    out.print(BORDER_SPACE);
  }

  public void drawRowSquares(PrintStream out, int rowNum, int color) {
    int col;
    int end;
    if (color == 1) {
      col = 1;
      end = 9;
    } else {
      col = 8;
      end = 0;
    }
    while (col != end) {
      if ((col + rowNum) % 2 == 1) {
        out.print(SET_BG_COLOR_WHITE);
      } else {
        out.print(SET_BG_COLOR_DARK_GREY);
      }
      out.print(BORDER_SPACE);
      drawChessPiece(out, rowNum, col);
      out.print(BORDER_SPACE);
      col = this.incrementOrDecrement(color, col);
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

  public int[] setStartAndEnd(int i) {
    int start;
    int end;
    if (i == 1) {
      start = 1;
      end = 9;
    } else {
      start = 8;
      end = 0;
    }
    int[] result = {start, end};
    return result;
  }

  public int incrementOrDecrement(int i, int currentNum) {
    if (i == 1) {
      currentNum++;
    } else {
      currentNum--;
    }
    return currentNum;
  }
}
