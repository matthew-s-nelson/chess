package serverfacade;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
  private String authToken;
  private final String baseURL = "http://localhost:8080/";
  int port;

  public ServerFacade(int port) {
    this.port = port;
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

  public void doPost(String urlString, Map<String, String> body) throws IOException {
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
        System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
      }
    }
    else {
      // SERVER RETURNED AN HTTP ERROR

      InputStream responseBody = connection.getErrorStream();
      // Read and process error response body from InputStream ...
    }
  }

  public Object register(String username, String password, String email) throws IOException {
    Map<String, String> body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);
    body.put("email", email);

    String url = baseURL + "user";

    doPost(url, body);

  }
}
