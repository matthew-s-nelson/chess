package serverfacade;

import com.google.gson.Gson;
import model.AuthData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

  public void doGet(String urlString) throws IOException {
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

      InputStream responseBody = connection.getInputStream();
      // Read and process response body from InputStream ...
    } else {
      // SERVER RETURNED AN HTTP ERROR

      InputStream responseBody = connection.getErrorStream();
      // Read and process error response body from InputStream ...
    }
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

  public Map<String, String> doDelete(String urlString, Map<String, String> body) throws IOException {
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

  public AuthData register(String username, String password, String email) throws IOException, ResponseException {
    Map<String, String> body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);
    body.put("email", email);

    String url = baseURL + "user";
    Map<String, String> response = doPost(url, body);
    if (response != null) {
      AuthData authData = new AuthData(response.get("username"), response.get("authToken"));
      return authData;
    } else {
      throw new ResponseException("Username taken");
    }
  }
}
