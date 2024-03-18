import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
  private String authToken;

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

  public void doPost(String urlString) throws IOException {
    URL url = new URL(urlString);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setReadTimeout(5000);
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);

    // Set HTTP request headers, if necessary
    if (authToken != null) {
      connection.addRequestProperty("authorization", authToken);
    }

    var body = Map.of("name", "joe", "type", "cat");
    try (var outputStream = connection.getOutputStream()) {
      var jsonBody = new Gson().toJson(body);
      outputStream.write(jsonBody.getBytes());
    }

    connection.connect();

    try(OutputStream requestBody = connection.getOutputStream();) {
      // Write request body to OutputStream ...
    }

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      // Get HTTP response headers, if necessary
      // Map<String, List<String>> headers = connection.getHeaderFields();

      // OR

      //connection.getHeaderField("Content-Length");

      InputStream responseBody = connection.getInputStream();
      // Read response body from InputStream ...
    }
    else {
      // SERVER RETURNED AN HTTP ERROR

      InputStream responseBody = connection.getErrorStream();
      // Read and process error response body from InputStream ...
    }
  }
}
