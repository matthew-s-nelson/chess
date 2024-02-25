package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
  private UserDAO userDAO;
  private AuthDAO authDAO;
  private GameDAO gameDAO;

  public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
    this.gameDAO = gameDAO;
  }

  public Collection<GameData> listGames(AuthData authData) {
    try {
      authDAO.getAuth(authData.authToken());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    return gameDAO.selectAllGames();
  }

  public GameData createGame(AuthData authData, String gameName) {
    try {
      authDAO.getAuth(authData.authToken());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    return gameDAO.insertGame(gameName);
  }

//  public void joinGame(AuthData auth, )
}
