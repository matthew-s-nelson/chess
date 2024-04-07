package webSocketMessages.serverMessages;

public class NotificationResponse extends ServerMessage {
  String message;
  public NotificationResponse(String message) {
    super(ServerMessageType.NOTIFICATION);
    this.message = message;
  }

  public String message() {
    return message;
  }
}
