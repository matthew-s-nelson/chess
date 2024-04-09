package webSocketMessages.userCommands;

import chess.ChessPosition;

public class MakeMoveRequest extends UserGameCommand {
  String startPos;
  String endPos;
  int gameID;
  public MakeMoveRequest(String authToken, String startPos, String endPos, int gameID) {
    super(authToken);
    this.commandType = CommandType.MAKE_MOVE;
    this.startPos = startPos;
    this.endPos = endPos;
    this.gameID = gameID;
  }

  public ChessPosition getStartPos() {
    return new ChessPosition(parseRow(startPos.charAt(1)), parseColumn(startPos.charAt(0)));
  }

  public ChessPosition getEndPos() {
    return new ChessPosition(parseRow(endPos.charAt(1)), parseColumn(endPos.charAt(0)));
  }

  public int getGameID() {
    return gameID;
  }

  public int parseColumn(char col) {
    switch (col) {
      case 'A':
        return 1;
      case 'B':
        return 2;
      case 'C':
        return 3;
      case 'D':
        return 4;
      case 'E':
        return 5;
      case 'F':
        return 6;
      case 'G':
        return 7;
      case 'H':
        return 8;
    }
    return 0;
  }

  public int parseRow(char row) {
    switch (row) {
      case '1':
        return 1;
      case '2':
        return 2;
      case '3':
        return 3;
      case '4':
        return 4;
      case '5':
        return 5;
      case '6':
        return 6;
      case '7':
        return 7;
      case '8':
        return 8;
    }
    return 0;
  }
}
