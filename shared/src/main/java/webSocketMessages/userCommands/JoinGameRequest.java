package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinGameRequest extends UserGameCommand {
  private int gameID;
  private ChessGame.TeamColor playerColor;
  public JoinGameRequest(String authToken, int gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.gameID = gameID;
    this.commandType = CommandType.JOIN_PLAYER;
    this.playerColor = playerColor;

  }

  public int getGameID() {
    return gameID;
  }

  public ChessGame.TeamColor getColor() {
    return playerColor;
  }

  public void setColor(ChessGame.TeamColor newColor) {
    playerColor = newColor;
  }
}
