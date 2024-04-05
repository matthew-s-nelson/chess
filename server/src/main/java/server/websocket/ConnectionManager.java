package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

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

  public Session getConnection(String authToken) {
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

  public void remove(String authToken) {
    connections.remove(authToken);
  }
}
