package WebSocket;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import ui.ChessBoard;
import webSocketMessages.serverMessages.ErrorResponse;
import webSocketMessages.serverMessages.LoadGameResponse;
import webSocketMessages.serverMessages.NotificationResponse;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WSClient extends Endpoint {
  private Session session;
  private ChessBoard chessBoard;
  private ChessGame game;

  public WSClient(String url, ChessBoard chessBoard) throws Exception {
    this.chessBoard = chessBoard;

    url = url.replace("http", "ws") + "connect";
    URI uri = new URI(url);
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, uri);

    this.session.addMessageHandler(new MessageHandler.Whole<String>() {
      @Override
      public void onMessage(String message) {
        try {
          ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
          switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME:
              loadGame(message);
              break;
            case ERROR:
              error(message);
            case NOTIFICATION:
              this.notify(message);
          }
        } catch(Exception ex) {
//          observer.notify(new ErrorMessage(ex.getMessage()));
        }
      }

      private void notify(String message) {
        NotificationResponse notification = new Gson().fromJson(message, NotificationResponse.class);
        chessBoard.notify(notification.message());
      }

    });
  }

  private void loadGame(String message) {
    LoadGameResponse loadGameResponse = new Gson().fromJson(message, LoadGameResponse.class);
    game = loadGameResponse.game();
    chessBoard.drawBoard(game.getBoard(), null);
  }

  private void error(String message) {
    ErrorResponse errorResponse = new Gson().fromJson(message, ErrorResponse.class);
    chessBoard.notify(errorResponse.getMessage());
  }

  public void redrawBoard(){
    chessBoard.drawBoard(game.getBoard(), null);
  }

  public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}

  public void highlightMoves(ChessPosition piecePos) {
    chessBoard.highlightLegalMoves(piecePos, game);
  }

  public void highlightAllMoves() {
    chessBoard.highlightAllLegalMoves(game);
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
  }

}

