package webSocketMessages.userCommands;

public class LeaveRequest extends UserGameCommand {
  int gameID;
  public LeaveRequest(String authToken, int gameID) {
    super(authToken);
    commandType = CommandType.LEAVE;
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
}
