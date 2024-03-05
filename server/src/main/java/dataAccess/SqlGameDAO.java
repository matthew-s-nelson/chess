package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

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
    ChessBoard board = new ChessBoard();
    board.resetBoard();
    try (var conn = DatabaseManager.getConnection()) {
      var statement="INSERT INTO game (gameID, password, email) VALUES (?, ?, ?)";
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
  public GameData selectGame(int gameID) throws DataAccessException {
    return null;
  }

  @Override
  public void insertBlackUsername(int gameID, String username) throws DataAccessException {

  }

  @Override
  public void insertWhiteUsername(int gameID, String username) throws DataAccessException {

  }

  @Override
  public void updateGame(int gameID, ChessGame game) throws DataAccessException {

  }

  @Override
  public void deleteAllGames() {

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
                whiteUsername varchar(128),
                blackUsername varchar(128),
                board json,
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
