package WebSocket;

import com.google.gson.Gson;
import ui.ChessBoard;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WSClient extends Endpoint {
  private Session session;
  private ChessBoard chessBoard;

  public WSClient(String url) throws Exception {
    url = url.replace("http", "ws") + "connect";
    System.out.println(url);
    URI uri = new URI(url);
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, uri);

    this.session.addMessageHandler(new MessageHandler.Whole<String>() {
      public void onMessage(String message) {
        try {
          ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
//          observer.notify(serverMessage);
          switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME:
              loadGame();
            case ERROR:
              error();
            case NOTIFICATION:
              notify();
          }
        } catch(Exception ex) {
//          observer.notify(new ErrorMessage(ex.getMessage()));
        }
      }

    });
  }

  private void loadGame() {

  }

  private void error() {

  }

  public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}
  public void onOpen(Session session, EndpointConfig endpointConfig) {}
  public void onClose(Session session, EndpointConfig endpointConfig) {}
  public void onError(Session session, EndpointConfig endpointConfig) {}
}

