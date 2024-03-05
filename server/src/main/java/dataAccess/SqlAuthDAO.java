package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class SqlAuthDAO implements AuthDAO {
  public SqlAuthDAO() {
    configureDatabase();
  }

  @Override
  public AuthData createAuth(String username) {
    String authToken = UUID.randomUUID().toString();
    try (var conn = DatabaseManager.getConnection()) {
      var statement="INSERT INTO auth (username, authToken) VALUES (?, ?)";
      try (var preparedStatement=conn.prepareStatement(statement)) {
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, authToken);

        preparedStatement.executeUpdate();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException("Problem with connection");
    }
    return new AuthData(username, authToken);
  }

  @Override
  public AuthData getAuth(String authToken) throws DataAccessException {
    return null;
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {

  }

  @Override
  public void deleteAllAuth() {

  }

  private void configureDatabase() {
    try {
      DatabaseManager.createDatabase();
    } catch (DataAccessException e) {
      throw new RuntimeException("Problem starting the server");
    }
    try (var conn = DatabaseManager.getConnection()) {
      var createTable = """
              CREATE TABLE IF NOT EXISTS auth (
                username varchar(126) NOT NULL,
                authToken varchar(126) NOT NULL,
                PRIMARY KEY(username),
                foreign key(username) references user(username)
              )""";
      try (var preparedStatement = conn.prepareStatement(createTable)) {
        preparedStatement.executeUpdate();
      }

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
