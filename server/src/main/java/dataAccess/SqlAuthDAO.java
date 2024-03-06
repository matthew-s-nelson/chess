package dataAccess;

import model.AuthData;
import model.UserData;

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
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT * FROM auth WHERE authToken=?")) {
        preparedStatement.setString(1, authToken);
        try (var rs=preparedStatement.executeQuery()) {
          if (rs.next()) {
            var rsUsername=rs.getString("username");
            var rsAuth=rs.getString("authToken");

            return new AuthData(rsUsername, rsAuth);
          }
          throw new DataAccessException("AuthToken doesn't exist");
        }
      } catch (SQLException sql) {
        throw new RuntimeException(sql);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    try (var conn=DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken=?")) {
        preparedStatement.setString(1, authToken);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected == 0) {
          throw new DataAccessException("AuthToken doesn't exist");
        }
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAllAuth() {
    try (var conn=DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("TRUNCATE auth")) {
        preparedStatement.executeUpdate();
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException(e);
    }
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
                PRIMARY KEY(authToken)
              )""";
      try (var preparedStatement = conn.prepareStatement(createTable)) {
        preparedStatement.executeUpdate();
      }

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
