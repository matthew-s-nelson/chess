package serverfacade;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpCommunicator {
  public HttpCommunicator() {
  }

  public Object doGet(String urlString, String authToken) throws IOException {
    URL url = new URL(urlString);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setReadTimeout(5000);
    connection.setRequestMethod("GET");

    // Set HTTP request headers, if necessary
    connection.addRequestProperty("authorization", authToken);

    connection.connect();

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

      try (InputStream responseBody = connection.getInputStream()) {
        InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
        return new Gson().fromJson(inputStreamReader, Map.class);
      }

    } else {
//      InputStream responseBody = connection.getErrorStream();
    }
    return null;
  }

  public Map<String, String> doPost(String urlString, Map<String, String> body, String authToken) throws IOException {
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

      try (InputStream responseBody = connection.getInputStream()) {
        InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
        return new Gson().fromJson(inputStreamReader, Map.class);
      }
    }
    else {

      InputStream responseBody = connection.getErrorStream();

      return null;
    }
  }

  public Map<String, String> doDelete(String urlString, Map<String, String> body, String authToken) throws IOException, ResponseException {
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

      try (InputStream responseBody = connection.getInputStream()) {
        InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
        return new Gson().fromJson(inputStreamReader, Map.class);
      }
    }
    else {
.
      throw new ResponseException();
    }
  }

  public Map<String, String> doPut(String urlString, Map<String, String> body, String authToken) throws IOException {
    URL url = new URL(urlString);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setReadTimeout(5000);
    connection.setRequestMethod("PUT");
    connection.setDoOutput(true);

    if (authToken != null) {
      connection.addRequestProperty("authorization", authToken);
    }

    connection.connect();

    try (OutputStream requestBody = connection.getOutputStream()) {
      var jsonBody = new Gson().toJson(body);
      requestBody.write(jsonBody.getBytes());
    }

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

      try (InputStream responseBody = connection.getInputStream()) {
        InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
        return new Gson().fromJson(inputStreamReader, Map.class);
      }
    }
    else {

      InputStream responseBody = connection.getErrorStream();
      InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
      return new Gson().fromJson(inputStreamReader, Map.class);
    }
  }
}
