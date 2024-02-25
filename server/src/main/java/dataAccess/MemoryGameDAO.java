package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
  private static Collection<GameData> gameData;

  public MemoryGameDAO() {
    gameData = new HashSet<>();
  }
  @Override
  public Collection<GameData> selectAllGames() {
    return gameData;
  }

  @Override
  public GameData insertGame(String gameName) {
    int gameID = gameData.size() + 1;
    GameData newGame = new GameData(gameID, gameName);
    gameData.add(newGame);
    return newGame;
  }

  @Override
  public int selectGame(int gameID) throws DataAccessException{
    for (GameData game: gameData) {
      if (game.gameID() == gameID) {
        return gameID;
      }
    }
    throw new DataAccessException("No game exists with this gameID.");
  }

  @Override
  public void insertBlackUsername(int gameID, String username) throws DataAccessException {
    for (GameData game: gameData) {
      if (game.gameID() == gameID) {
        gameData.remove(game);
        GameData updatedGame = game.updateWhiteUsername(username);
        gameData.add(updatedGame);
        return;
      }
    }
    throw new DataAccessException("No game exists with this gameID");
  }

  @Override
  public void insertWhiteUsername(int gameID, String username) throws DataAccessException {
    for (GameData game: gameData) {
      if (game.gameID() == gameID) {
        gameData.remove(game);
        GameData updatedGame = game.updateBlackUsername(username);
        gameData.add(updatedGame);
        return;
      }
    }
    throw new DataAccessException("No game exists with this gameID");
  }

  @Override
  public void updateGame(int gameID, ChessGame game) throws DataAccessException{
    for (GameData chessGame: gameData) {
      if (chessGame.gameID() == gameID) {
        gameData.remove(chessGame);
        GameData updatedGame = chessGame.updateGame(game);
        gameData.add(updatedGame);
        return;
      }
    }
    throw new DataAccessException("No game exists with this gameID");
  }

  @Override
  public void deleteAllGames() {
    gameData.clear();
  }
}
