package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
  Collection<GameData> selectAllGames();
  GameData insertGame();
  int selectGame(int gameID) throws DataAccessException;
  void insertBlackUsername(int gameID, String username) throws DataAccessException;
  void insertWhiteUsername(int gameID, String username) throws DataAccessException;
  void updateGame(int gameID, ChessGame game) throws DataAccessException;
  void deleteAllGames();

}
