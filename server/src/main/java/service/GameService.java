package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
  private UserDAO userDAO;
  private AuthDAO authDAO;
  private GameDAO gameDAO;

  public GameService() {
    userDAO = new MemoryUserDAO();
    authDAO = new MemoryAuthDAO();
    gameDAO = new MemoryGameDAO();
  }

  public Collection<GameData> listGames(AuthData authData) {
    try {
      authDAO.getAuth(authData.authToken());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    return gameDAO.selectAllGames();
  }

  public GameData createGame(AuthData authData) {
    try {
      authDAO.getAuth(authData.authToken());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    return gameDAO.insertGame();
  }

//  public void joinGame(AuthData auth, )
}
