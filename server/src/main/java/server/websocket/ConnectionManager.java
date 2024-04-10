package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlUserDAO;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import service.UserService;
import webSocketMessages.serverMessages.LoadGameResponse;
import webSocketMessages.serverMessages.NotificationResponse;

import java.io.IOException;
import java.util.*;

public class ConnectionManager {
  public static Map<String, Session> connections;
  public static Map<Integer, Collection<String>> gameMap;

  public ConnectionManager() {
    connections = new HashMap<>();
    gameMap = new HashMap<>();
  }

  public void addConnection(String authToken, Session session) {
    connections.put(authToken, session);
  }

  public Session getSession(String authToken) {
    return connections.get(authToken);
  }

  public void addUserToGame(int gameID, String authToken) {
    Collection<String> users = gameMap.get(gameID);
    if (users == null) {
      users = new ArrayList<>();
      gameMap.put(gameID, users);
    }
    users.add(authToken);
  }

  public Collection<String> getGameParticipants(int gameID) {
    return gameMap.get(gameID);
  }

  public void broadcast(String message, String authToken, int gameID) throws DataAccessException, IOException {
    Collection<String> gameParticipants = this.getGameParticipants(gameID);
    NotificationResponse notificationResponse = new NotificationResponse(message);
    String gsonMessage = new Gson().toJson(notificationResponse);
    for (String participant: gameParticipants) {
      if (!Objects.equals(participant, authToken)) {
        Session participantSession = this.getSession(participant);
        if (participantSession.isOpen()) {
          participantSession.getRemote().sendString(gsonMessage);
        }
      }
    }
  }

  public void loadGameForAll(int gameID, ChessGame game) throws IOException {
    LoadGameResponse loadGameResponse = new LoadGameResponse(game);
    String gsonMessage = new Gson().toJson(loadGameResponse);
    Collection<String> gameParticipants = getGameParticipants(gameID);
    for (String participant: gameParticipants) {
      Session participantSession = getSession(participant);
      if (participantSession.isOpen()) {
        participantSession.getRemote().sendString(gsonMessage);
      }
    }
  }

  public void deletePlayerFromGame(String authToken, int gameID) {
    Collection<String> users = gameMap.get(gameID);
    users.remove(authToken);
    connections.remove(authToken);
  }
}
