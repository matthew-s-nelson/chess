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
    var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
    executeUpdate(statement, user.username(), user.password(), user.email());
  }

  @Override
  public boolean userExists(String username) {
    return false;
  }

  @Override
  public UserData selectUser(String username, String password) throws DataAccessException {
    return null;
  }

  @Override
  public void deleteUsers() {

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
    } catch (SQLException e) {

    }
  }

  private final String[] createStatements = {
          """
          CREATE TABLE IF NOT EXISTS user (
            'username' varchar(126) NOT NULL,
            'password' varchar(126) NOT NULL,
            'email' varchar(126) NOT NULL,
            PRIMARY KEY('username')
          )
          """
  };

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
        var rs = preparedStatement.executeQuery();
        rs.next();
        System.out.println(rs.getInt((1)));
      }
    }
  }
}
