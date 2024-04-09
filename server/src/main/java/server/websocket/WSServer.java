package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.gson;
import service.GameJoinException;
import service.GameService;
import service.UserService;
import spark.Spark;
import webSocketMessages.serverMessages.ErrorResponse;
import webSocketMessages.serverMessages.LoadGameResponse;
import webSocketMessages.serverMessages.NotificationResponse;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

  @OnWebSocketError
  public void onError(Session session, Throwable throwable) {
    // Handle the error here
    System.err.println("WebSocket error occurred: " + throwable.getMessage());
    throwable.printStackTrace();
  }

  @OnWebSocketClose
  public void onClose(int statusCode, String reason) {

  }

  public void join(Session session, String msg) throws IOException {
    JoinGameRequest joinRequest = new Gson().fromJson(msg, JoinGameRequest.class);
    String authToken =joinRequest.getAuthString();
    int gameID =joinRequest.getGameID();
    try {
      gameService.checkIfUserInGame(authToken, gameID);
      AuthData authData = userService.getUser(authToken);
      GameData gameData = gameService.getGameData(gameID);

      if ((joinRequest.getColor() == ChessGame.TeamColor.WHITE && !Objects.equals(authData.username(), gameData.whiteUsername()))
              || (joinRequest.getColor() == ChessGame.TeamColor.BLACK && !Objects.equals(authData.username(), gameData.blackUsername()))
              || joinRequest.getColor() == null) {
        ErrorResponse errorResponse = new ErrorResponse("That team slot is already taken");
        if (session.isOpen()) {
          session.getRemote().sendString(new Gson().toJson(errorResponse));
        }
        return;
      }

      connections.addUserToGame(joinRequest.getGameID(), joinRequest.getAuthString());
      LoadGameResponse loadGameResponse = new LoadGameResponse(gameData.game());
      String msgToSend = new Gson().toJson(loadGameResponse);

      if (session.isOpen()) {
        session.getRemote().sendString(msgToSend);
      }

      String broadcastMessage = String.format("%s joined the game as %s", authData.username(), joinRequest.getColor());
      connections.broadcast(broadcastMessage, authToken, gameID);

    } catch (DataAccessException e) {
      ErrorResponse errorResponse = new ErrorResponse("Error");
      session.getRemote().sendString(new Gson().toJson(errorResponse));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void observe(Session session, String msg) throws IOException {
    JoinObserverRequest joinObserverRequest = new Gson().fromJson(msg, JoinObserverRequest.class);
    try {
      AuthData authData = userService.getUser(joinObserverRequest.getAuthString());
      GameData gameData = gameService.getGameData(joinObserverRequest.getGameID());
      connections.addUserToGame(joinObserverRequest.getGameID(), joinObserverRequest.getAuthString());
      LoadGameResponse loadGameResponse=new LoadGameResponse(gameData.game());
      String msgToSend=new Gson().toJson(loadGameResponse);
      if (session.isOpen()) {
        session.getRemote().sendString(msgToSend);
      }

      String broadcastMessage = String.format("%s joined the game as an OBSERVER", authData.username());
      connections.broadcast(broadcastMessage, joinObserverRequest.getAuthString(), gameData.gameID());
    } catch (DataAccessException e) {
      ErrorResponse errorResponse = new ErrorResponse("Error");
      session.getRemote().sendString(new Gson().toJson(errorResponse));
    }
  }

  public void move(Session session, String msg) throws IOException {
    MakeMoveRequest makeMoveRequest = new Gson().fromJson(msg, MakeMoveRequest.class);
    try {
      GameData gameData = gameService.getGameData(makeMoveRequest.getGameID());
      ChessGame game = gameData.game();
      ChessMove move = makeMoveRequest.getMove();
      game.makeMove(move);
      gameService.updateGame(gameData.gameID(), game);

      connections.loadGameForAll(gameData.gameID(), game);

      AuthData authData = userService.getUser(makeMoveRequest.getAuthString());
      String msgToSend = String.format("%s made a move.", authData.username());
      connections.broadcast(msgToSend, makeMoveRequest.getAuthString(), gameData.gameID());
    } catch (DataAccessException e) {

    } catch (InvalidMoveException e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      session.getRemote().sendString(new Gson().toJson(errorResponse));
    } catch (IOException e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      session.getRemote().sendString(new Gson().toJson(errorResponse));
    }
  }

  public void leave(Session session, String msg) {
    LeaveRequest leaveRequest = new Gson().fromJson(msg, LeaveRequest.class);
    try {
      GameData gameData = gameService.getGameData(leaveRequest.getGameID());
      AuthData authData = userService.getUser(leaveRequest.getAuthString());
      String color = "";
      if (Objects.equals(gameData.whiteUsername(), authData.username())) {
        color = "WHITE";
      } else if (Objects.equals(gameData.blackUsername(), authData.username())){
        color = "BLACK";
      }
      gameService.leaveGame(new server.JoinGameRequest(color, gameData.gameID()));
      connections.deletePlayerFromGame(authData.authToken(), gameData.gameID());
      String msgToSend = String.format("%s left the game.", authData.username());
      connections.broadcast(msgToSend, authData.authToken(), gameData.gameID());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void resign(Session session, String msg) {
    ResignRequest resignRequest = new Gson().fromJson(msg, ResignRequest.class);
    try {
      GameData gameData = gameService.getGameData(resignRequest.getGameID());
      AuthData authData = userService.getUser(resignRequest.getAuthString());
      ChessGame game = gameData.game();
      game.setTeamTurn(null);
      gameService.updateGame(gameData.gameID(), game);
      String msgToSend = String.format("%s resigned from the game.", authData.username());
      connections.broadcast(msgToSend, null, gameData.gameID());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
