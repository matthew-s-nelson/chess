package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoard {
  private static final String BORDER_SPACE = " ";
  private static final String EMPTY_SQUARE = "   ";
  private chess.ChessBoard board;
  private int playerColor;
  private PrintStream printStream;

  public ChessBoard(int playerColor) {
    this.playerColor = playerColor;
  }

  // color key: 0 = white, 1 = black
  public void drawBoard(chess.ChessBoard board, Collection<ChessPosition> legalMoves) {
    if (board != null) {
      this.board = board;
    }
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    printStream = out;
    drawHeader(out, playerColor-1);
    drawRows(out, playerColor-1, legalMoves);
    drawHeader(out, playerColor-1);

    out.println();
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

  public void drawRows(PrintStream out, int color, Collection<ChessPosition> legalMoves) {
    int[] startEnd = this.setStartAndEnd(color);
    int row = startEnd[0];
    int end = startEnd[1];
    while (row != end) {
      out.print(SET_BG_COLOR_BLACK);
      drawSideHeader(out, row);
      drawRowSquares(out, row, color, legalMoves);
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

  public void drawRowSquares(PrintStream out, int rowNum, int color, Collection<ChessPosition> legalMoves) {
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
      ChessPosition posToCheck = new ChessPosition(rowNum, Math.abs(9-col));
      if ((col + rowNum) % 2 == 1) {
        if (legalMoves != null && legalMoves.contains(posToCheck)) {
          out.print(SET_BG_COLOR_LIGHT_YELLOW);
        } else {
          out.print(SET_BG_COLOR_WHITE);
        }
      } else {
        if (legalMoves != null && legalMoves.contains(posToCheck)) {
          out.print(SET_BG_COLOR_YELLOW);
        } else {
          out.print(SET_BG_COLOR_DARK_GREY);
        }
      }
      out.print(BORDER_SPACE);
      drawChessPiece(out, rowNum, col);
      out.print(BORDER_SPACE);
      col = this.incrementOrDecrement(color, col);
    }
    out.print(RESET_BG_COLOR);
  }

  public void drawChessPiece(PrintStream out, int rowNum, int colNum) {
    chess.ChessPiece piece = board.getPiece(new ChessPosition(rowNum, Math.abs(9-colNum)));
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

  public void notify(String message) {
    printStream.println(message);
  }

  public void highlightLegalMoves(ChessPosition piecePos, ChessGame game) {
    Collection<ChessMove> validMoves = game.validMoves(piecePos);
    Collection<ChessPosition> validEndingPos = getEndingPosFromMoves(validMoves);
    drawBoard(game.getBoard(), validEndingPos);
  }

  public void highlightAllLegalMoves(ChessGame game) {
    Collection<ChessMove> allValidMoves = game.getAllValidMoves();
    Collection<ChessPosition> validEndingPos = getEndingPosFromMoves(allValidMoves);
    drawBoard(game.getBoard(), validEndingPos);
  }

  public Collection<ChessPosition> getEndingPosFromMoves(Collection<ChessMove> moves) {
    Collection<ChessPosition> validEndingPos = new ArrayList<>();
    for (ChessMove validMove: moves) {
      validEndingPos.add(validMove.getEndPosition());
    }
    return validEndingPos;
  }
}
