package dataAccess;

import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlUserDAO implements UserDAO{

  public SqlUserDAO() {
    configureDatabase();
  }
  @Override
  public void insertUser(UserData user) {
    try (var conn = DatabaseManager.getConnection()) {
      var statement="INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
      try (var preparedStatement=conn.prepareStatement(statement)) {
        preparedStatement.setString(1, user.username());
        preparedStatement.setString(2, user.password());
        preparedStatement.setString(3, user.email());

        preparedStatement.executeUpdate();
      } catch (SQLException e) {
        throw new RuntimeException("SQL Exception");
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException("Problem with connection");
    }
  }

  @Override
  public boolean userExists(String username) {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT * FROM user WHERE username=?")) {
        preparedStatement.setString(1, username);
        try (var rs=preparedStatement.executeQuery()) {
          if (rs.next()) {
            var rsUsername=rs.getString("username");
            var rsPassword=rs.getString("password");
            var rsEmail=rs.getString("email");

            return true;
          } else {
            return false;
          }
        }
      } catch (SQLException sql) {
        throw new RuntimeException("SQL Exception");
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException("DataAccessException");
    }
  }

  @Override
  public UserData selectUser(String username, String password) {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT * FROM user WHERE username=? AND password=?")) {
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        try (var rs=preparedStatement.executeQuery()) {
          if (rs.next()) {
            var rsUsername=rs.getString("username");
            var rsPassword=rs.getString("password");
            var rsEmail=rs.getString("email");

            return new UserData(rsUsername, rsPassword, rsEmail);
          }
        }
      } catch (SQLException sql) {
        throw new RuntimeException(sql);
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException("DataAccessException");
    }
    return null;
  }

  @Override
  public void deleteUsers() {
    try (var conn=DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("TRUNCATE user")) {
        preparedStatement.executeUpdate();
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void executeUpdate(String statement, Object... params) {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p){
            ps.setString(i + 1, p);
          }
        }
        ps.executeUpdate();
      }
    } catch (SQLException | DataAccessException e) {

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
          CREATE TABLE IF NOT EXISTS user (
            username varchar(126) NOT NULL,
            password varchar(126) NOT NULL,
            email varchar(126) NOT NULL,
            PRIMARY KEY(username)
          )""";
      try (var preparedStatement = conn.prepareStatement(createTable)) {
        preparedStatement.executeUpdate();
      }

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
