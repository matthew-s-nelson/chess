package webSocketMessages.serverMessages;

public class ErrorResponse extends ServerMessage {
  String errorMessage;
  public ErrorResponse(String message) {
    super(ServerMessageType.ERROR);
    this.errorMessage = message;
  }

  public String getMessage() {
    return errorMessage;
  }
}
