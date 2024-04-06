package webSocketMessages.userCommands;

public class JoinGameRequest extends UserGameCommand {
  private int gameID;
  public JoinGameRequest(String authToken, int gameID) {
    super(authToken);
    this.gameID = gameID;
    this.commandType = CommandType.JOIN_PLAYER;
  }

  public int getGameID() {
    return gameID;
  }
}
