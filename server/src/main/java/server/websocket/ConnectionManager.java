package server.websocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlUserDAO;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import service.UserService;
import webSocketMessages.serverMessages.NotificationResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConnectionManager {
  public static Map<String, Session> connections;
  public static Map<Integer, Set<String>> gameMap;

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
    Set<String> users = gameMap.get(gameID);
    if (users == null) {
      users = new HashSet<>();
      gameMap.put(gameID, users);
    }
    users.add(authToken);
  }

  public Map<Integer, Set<String>> getGameMap() {
    return gameMap;
  }

  public Set<String> getGameParticipants(int gameID) {
    return gameMap.get(gameID);
  }

  public void broadcast(String message, String authToken, int gameID) throws DataAccessException, IOException {
    Set<String> gameParticipants = this.getGameParticipants(gameID);
    NotificationResponse notificationResponse = new NotificationResponse(message);
    String gsonMessage = new Gson().toJson(notificationResponse);
    for (String participant: gameParticipants) {
      if (participant != authToken) {
        Session participantSession = this.getSession(participant);
        participantSession.getRemote().sendString(gsonMessage);
      }
    }
  }

  public void remove(String authToken) {
    connections.remove(authToken);
  }
}
