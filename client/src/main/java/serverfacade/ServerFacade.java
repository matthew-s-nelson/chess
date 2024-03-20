package serverfacade;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

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
  int port;

  public ServerFacade(int port) {
    this.port = port;
    baseURL = "http://localhost:" + port + "/";
  }

  public Object doGet(String urlString) throws IOException {
    URL url = new URL(urlString);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setReadTimeout(5000);
    connection.setRequestMethod("GET");

    // Set HTTP request headers, if necessary
    // connection.addRequestProperty("Accept", "text/html");
    connection.addRequestProperty("authorization", authToken);

    connection.connect();

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      // Get HTTP response headers, if necessary
      // Map<String, List<String>> headers = connection.getHeaderFields();

      // OR

      //connection.getHeaderField("Content-Length");

      try (InputStream responseBody = connection.getInputStream()) {
        InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
        return new Gson().fromJson(inputStreamReader, Map.class);
      }
      // Read and process response body from InputStream ...
    } else {
      // SERVER RETURNED AN HTTP ERROR

      InputStream responseBody = connection.getErrorStream();
      // Read and process error response body from InputStream ...
    }
    return null;
  }

  public Map<String, String> doPost(String urlString, Map<String, String> body) throws IOException {
    URL url = new URL(urlString);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setReadTimeout(5000);
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);

    // Set HTTP request headers, if necessary
    if (authToken != null) {
      connection.addRequestProperty("authorization", authToken);
    }

    connection.connect();

    try (OutputStream requestBody = connection.getOutputStream()) {
      var jsonBody = new Gson().toJson(body);
      requestBody.write(jsonBody.getBytes());
    }

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      // Get HTTP response headers, if necessary
      // Map<String, List<String>> headers = connection.getHeaderFields();

      // OR

      //connection.getHeaderField("Content-Length");

//      InputStream responseBody = connection.getInputStream();
      try (InputStream responseBody = connection.getInputStream()) {
        InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
        return new Gson().fromJson(inputStreamReader, Map.class);
      }
    }
    else {
      // SERVER RETURNED AN HTTP ERROR

      InputStream responseBody = connection.getErrorStream();
      // Read and process error response body from InputStream ...
      return null;
    }
  }

  public Map<String, String> doDelete(String urlString, Map<String, String> body) throws IOException, ResponseException {
    URL url = new URL(urlString);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setReadTimeout(5000);
    connection.setRequestMethod("DELETE");
    connection.setDoOutput(true);

    // Set HTTP request headers, if necessary
    if (authToken != null) {
      connection.addRequestProperty("authorization", authToken);
    }

    connection.connect();

    try (OutputStream requestBody = connection.getOutputStream()) {
      var jsonBody = new Gson().toJson(body);
      requestBody.write(jsonBody.getBytes());
    }

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      // Get HTTP response headers, if necessary
      // Map<String, List<String>> headers = connection.getHeaderFields();

      // OR

      //connection.getHeaderField("Content-Length");

//      InputStream responseBody = connection.getInputStream();
      try (InputStream responseBody = connection.getInputStream()) {
        InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
        return new Gson().fromJson(inputStreamReader, Map.class);
      }
    }
    else {
      // SERVER RETURNED AN HTTP ERROR

      InputStream responseBody = connection.getErrorStream();
      // Read and process error response body from InputStream ...
      throw new ResponseException();
    }
  }

  public AuthData register(String username, String password, String email) throws IOException, ResponseException {
    Map<String, String> body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);
    body.put("email", email);

    String url = baseURL + "user";
    Map<String, String> response = doPost(url, body);
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
    Map<String, String> response = doPost(url, body);

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
      doDelete(url, null);
      authToken = null;
    } catch (ResponseException res) {
      throw new ResponseException("Unauthorized");
    }
  }

  public GameData createGame(String gameName) throws IOException, ResponseException {
    Map<String, String> body = new HashMap<>();
    body.put("gameName", gameName);

    String url = baseURL + "game";
    Map<String, String> result = doPost(url, body);
    if (result != null) {
      var gameID = result.get("gameID");
      return new GameData(gameID, result.get("gameName"));
    } else {
      throw new ResponseException("GameName already taken");
    }
  }

  public Collection<GameData> listGames() throws IOException, ResponseException {
    String url = baseURL + "game";
    Object response = doGet(url);
    if (response instanceof Map<?,?> && ((Map<?, ?>) response).get("games") instanceof ArrayList) {
      Object gameData = ((Map<?, ?>) response).get("games");
    }
    return null;
  }
}
