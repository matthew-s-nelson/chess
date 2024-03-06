package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class SqlGameDAO implements GameDAO{

  public SqlGameDAO() {
    configureDatabase();
  }

  @Override
  public Collection<GameData> selectAllGames() {
    return null;
  }

  @Override
  public GameData insertGame(String gameName) {
    ChessGame chessGame = new ChessGame();
    int id;

    try (var conn = DatabaseManager.getConnection()) {
      var statement="INSERT INTO game (gameName, whiteUsername, blackUsername, board) VALUES (?, ?, ?, ?)";
      try (PreparedStatement preparedStatement=conn.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, gameName);
        preparedStatement.setString(2, null);
        preparedStatement.setString(3, null);
        preparedStatement.setObject(4, new Gson().toJson(chessGame));

        preparedStatement.executeUpdate();
        try (var rs = preparedStatement.getGeneratedKeys()) {
          if (rs.next()) {
            // Retrieve the auto-increment key
            id = rs.getInt(1);
          } else {
            throw new SQLException("No auto-generated keys were returned.");
          }
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException("Problem with connection");
    }
    return new GameData(id, gameName);
  }

  @Override
  public GameData selectGame(int gameID) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT * FROM game WHERE gameID=?")) {
        preparedStatement.setInt(1, gameID);
        try (var rs=preparedStatement.executeQuery()) {
          if (rs.next()) {
            var rsGameID=rs.getInt("gameID");
            var rsGameName=rs.getString("gameName");
            var rsWhiteUsername = rs.getString("whiteUsername");
            var rsBlackUsername = rs.getString("blackUsername");
            var rsBoard = rs.getString("board");

            return new GameData(rsGameID, rsGameName, rsWhiteUsername, rsBlackUsername, new Gson().fromJson(rsBoard, ChessGame.class));
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
  public void insertBlackUsername(int gameID, String username) {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("UPDATE game SET blackUsername=? WHERE gameID=?")) {
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, gameID);

        preparedStatement.executeUpdate();
      } catch (SQLException sql) {
        throw new RuntimeException(sql);
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException("DataAccessException");
    }
  }

  @Override
  public void insertWhiteUsername(int gameID, String username) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("UPDATE game SET whiteUsername=? WHERE gameID=?")) {
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, gameID);

        preparedStatement.executeUpdate();
      } catch (SQLException sql) {
        throw new RuntimeException(sql);
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException("DataAccessException");
    }
  }

  @Override
  public void updateGame(int gameID, ChessGame game) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("UPDATE game SET board=? WHERE gameID=?")) {
        preparedStatement.setObject(1, new Gson().toJson(game));
        preparedStatement.setInt(2, gameID);

        preparedStatement.executeUpdate();
      } catch (SQLException sql) {
        throw new RuntimeException(sql);
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException("DataAccessException");
    }
  }

  @Override
  public void deleteAllGames() {
    try (var conn=DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("TRUNCATE game")) {
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
              CREATE TABLE IF NOT EXISTS game (
                gameID int NOT NULL AUTO_INCREMENT,
                gameName varchar(128) NOT NULL,
                whiteUsername varchar(128),
                blackUsername varchar(128),
                board JSON NOT NULL,
                PRIMARY KEY(gameID)
              )""";
      try (var preparedStatement = conn.prepareStatement(createTable)) {
        preparedStatement.executeUpdate();
      }

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
