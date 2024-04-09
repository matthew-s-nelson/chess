package webSocketMessages.userCommands;

public class JoinObserverRequest extends UserGameCommand {
  int gameID;
  public JoinObserverRequest(String authToken, int gameID) {
    super(authToken);
    this.commandType = CommandType.JOIN_OBSERVER;
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
}
