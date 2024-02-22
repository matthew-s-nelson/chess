package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
  Collection<GameData> selectAllGames();
  GameData insertGame();
  GameData selectGame(String gameID);
  void insertBlackUsername(String username);
  void insertWhiteUsername(String username);
  void updateGame(ChessGame game);
  void deleteAllGames();

}
