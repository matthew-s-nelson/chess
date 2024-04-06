package serverfacade;

import WebSocket.WSClient;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import org.springframework.security.core.userdetails.User;
import ui.ChessBoard;
import webSocketMessages.userCommands.JoinGameRequest;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
  private String authToken;
  private final String baseURL;

  private HttpCommunicator httpCommunicator;
  private WSClient wsCommunicator;
  int port;

  public ServerFacade(int port) {
    this.port = port;
    baseURL = "http://localhost:" + port + "/";
    httpCommunicator = new HttpCommunicator();
  }



  public AuthData register(String username, String password, String email) throws IOException, ResponseException {
    Map<String, String> body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);
    body.put("email", email);

    String url = baseURL + "user";
    Map<String, String> response = httpCommunicator.doPost(url, body, authToken);
    if (response != null) {
      AuthData authData = new AuthData(response.get("username"), response.get("authToken"));
      authToken = authData.authToken();
      return authData;
    } else {
      throw new ResponseException("Username taken");
    }
  }

  public AuthData login(String username, String password) throws IOException, ResponseException {
    Map<String, String> body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);

    String url = baseURL + "session";
    Map<String, String> response = httpCommunicator.doPost(url, body, authToken);

    if (response != null) {
      AuthData authData = new AuthData(response.get("usename"), response.get("authToken"));
      authToken = authData.authToken();
      return authData;
    } else {
      throw new ResponseException("Username and Password don't match");
    }
  }

  public void logout() throws IOException, ResponseException {
    String url = baseURL + "session";
    try{
      httpCommunicator.doDelete(url, null, authToken);
      authToken = null;
    } catch (ResponseException res) {
      throw new ResponseException("Unauthorized");
    }
  }

  public GameData createGame(String gameName) throws IOException, ResponseException {
    Map<String, String> body = new HashMap<>();
    body.put("gameName", gameName);

    String url = baseURL + "game";
    Map<String, String> result = httpCommunicator.doPost(url, body, authToken);
    if (result != null) {
      var gameID = result.get("gameID");
      return new GameData(gameID, result.get("gameName"));
    } else {
      throw new ResponseException("GameName already taken");
    }
  }

  public Collection<GameData> listGames() throws IOException, ResponseException {
    String url = baseURL + "game";
    Object response = httpCommunicator.doGet(url, authToken);
    Collection<GameData> gameDataCollection = new ArrayList<>();
    if (response instanceof Map<?,?> && ((Map<?, ?>) response).get("games") instanceof ArrayList) {
      ArrayList gamesData = ((Map<?, ArrayList>) response).get("games");
      for (Object game: gamesData) {
        if (game instanceof Map<?,?>) {
          int gameID = 0;
          gameID = ((Double) ((Map<?, ?>) game).get("gameID")).intValue();
          String gameName = (String) ((Map<?, ?>) game).get("gameName");
          String whiteUsername = (String) ((Map<?, ?>) game).get("whiteUsername");
          String blackUsername = (String) ((Map<?, ?>) game).get("blackUsername");
          GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, null);
          gameDataCollection.add(gameData);
        }
      }
      return gameDataCollection;
    }
    throw new ResponseException("Unauthorized");
  }

  public void joinGame(String playerColor, String gameID, ChessBoard chessBoard) throws Exception {
    Map<String, String> body = new HashMap<>();
    body.put("playerColor", playerColor);
    body.put("gameID", gameID);

    String url = baseURL + "game";
    Map<String, String> response = httpCommunicator.doPut(url, body, authToken);

    if (response != null) {
      throw new ResponseException(response.get("message"));
    }
    wsCommunicator = new WSClient(baseURL, chessBoard);
    JoinGameRequest joinRequest = new JoinGameRequest(authToken, Integer.parseInt(gameID));
    wsCommunicator.send(new Gson().toJson(joinRequest));
  }

//  public void connect() throws Exception {
//    wsCommunicator = new WSClient(baseURL);
//  }
}
