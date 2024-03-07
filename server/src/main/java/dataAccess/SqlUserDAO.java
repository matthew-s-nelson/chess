package dataAccess;

import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import static dataAccess.DatabaseManager.configureDatabase;

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
        preparedStatement.setString(2, PasswordHasher.hashPassword(user.password()));
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
  public UserData selectUser(String username, String password) throws DataAccessException {
    String hashedPassword = PasswordHasher.hashPassword(password);

    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT * FROM user WHERE username=?")) {
        preparedStatement.setString(1, username);
//        preparedStatement.setString(2, password);
        try (var rs=preparedStatement.executeQuery()) {
          if (rs.next()) {
            var rsUsername=rs.getString("username");
            var rsPassword=rs.getString("password");
            var rsEmail=rs.getString("email");

            if (PasswordHasher.comparePassword(password, rsPassword)) {
              return new UserData(rsUsername, password, rsEmail);
            } else {
              throw new DataAccessException("Incorrect Password");
            }
          } else {
            throw new DataAccessException("No user with this username.");
          }
        }
      } catch (SQLException sql) {
        throw new RuntimeException(sql);
      }
    } catch (SQLException e) {
      throw new RuntimeException("DataAccessException");
    }
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
}
