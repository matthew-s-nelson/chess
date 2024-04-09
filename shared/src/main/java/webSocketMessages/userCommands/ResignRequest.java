package webSocketMessages.userCommands;

public class ResignRequest extends UserGameCommand {

  int gameID;
  public ResignRequest(String authToken, int gameID) {
    super(authToken);
    this.commandType = CommandType.RESIGN;
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
}
