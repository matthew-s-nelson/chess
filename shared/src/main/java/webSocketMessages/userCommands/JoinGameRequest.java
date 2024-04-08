package webSocketMessages.userCommands;

public class JoinGameRequest extends UserGameCommand {
  private int gameID;
  private String color;
  public JoinGameRequest(String authToken, int gameID, String playerColor) {
    super(authToken);
    this.gameID = gameID;
    this.commandType = CommandType.JOIN_PLAYER;
    this.color = playerColor;

  }

  public int getGameID() {
    return gameID;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String newColor) {
    color = newColor;
  }
}
