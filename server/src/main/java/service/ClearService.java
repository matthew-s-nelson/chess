package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ClearService {
  private AuthDAO authDAO;
  private UserDAO userDAO;
  private GameDAO gameDAO;
  public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
    this.authDAO = authDAO;
    this.gameDAO = gameDAO;
    this.userDAO = userDAO;
  }
  public void clear() {
    authDAO.deleteAllAuth();
    userDAO.deleteUsers();
    gameDAO.deleteAllGames();
  }
}
