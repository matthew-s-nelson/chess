package server.websocket;

import chess.ChessBoard;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.gson;
import service.GameService;
import service.UserService;
import spark.Spark;
import webSocketMessages.serverMessages.LoadGameResponse;
import webSocketMessages.serverMessages.NotificationResponse;
import webSocketMessages.userCommands.JoinGameRequest;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebSocket
public class WSServer {
  private ConnectionManager connections = new ConnectionManager();
  private final GameService gameService = new GameService(new SqlUserDAO(), new SqlAuthDAO(), new SqlGameDAO());
  private final UserService userService = new UserService(new SqlUserDAO(), new SqlAuthDAO());

//  public static void main(String[] args) {
//    Spark.port(8080);
//    Spark.webSocket("/connect", WSServer.class);
//    Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//  }

  @OnWebSocketMessage
  public void onMessage(Session session, String msg) throws Exception {
    UserGameCommand command =new Gson().fromJson(msg, UserGameCommand.class);

    if (command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER || command.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
      connections.addConnection(command.getAuthString(), session);
    }

    Session conn = connections.getSession(command.getAuthString());
    if (conn != null) {
      switch (command.getCommandType()) {
        case JOIN_PLAYER -> join(conn, msg);
        case JOIN_OBSERVER -> observe(conn, msg);
        case MAKE_MOVE -> move(conn, msg);
        case LEAVE -> leave(conn, msg);
        case RESIGN -> resign(conn, msg);
      }
    } else {
//      Connection.sendError(session.getRemote(), "unknown user");
    }
  }

  public void join(Session session, String msg) {
    JoinGameRequest joinRequest = new Gson().fromJson(msg, JoinGameRequest.class);
    String authToken =joinRequest.getAuthString();
    int gameID =joinRequest.getGameID();
    try {
      gameService.checkIfUserInGame(authToken, gameID);
      GameData gameData = gameService.getGameData(gameID);
      connections.addUserToGame(joinRequest.getGameID(), joinRequest.getAuthString());
      ChessBoard chessBoard = gameData.game().getBoard();
      LoadGameResponse loadGameResponse = new LoadGameResponse(chessBoard);
      String msgToSend = new Gson().toJson(loadGameResponse);

      session.getRemote().sendString(msgToSend);

      AuthData authData = userService.getUser(authToken);
      String broadcastMessage = String.format("%s joined the game", authData.username());
      connections.broadcast(broadcastMessage, authToken, gameID);

    } catch (DataAccessException e) {

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void observe(Session session, String msg) {

  }

  public void move(Session session, String msg) {

  }

  public void leave(Session session, String msg) {

  }

  public void resign(Session session, String msg) {

  }

}
