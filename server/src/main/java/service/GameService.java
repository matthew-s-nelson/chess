package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.springframework.security.core.userdetails.User;
import server.JoinGameRequest;

import java.util.Collection;
import java.util.Objects;

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

  public void joinGame(AuthData authData, JoinGameRequest joinGameRequest) throws GameJoinException {
    try {
      AuthData auth = authDAO.getAuth(authData.authToken());
      GameData game = gameDAO.selectGame(joinGameRequest.gameID());
      if (Objects.equals(joinGameRequest.playerColor(), "WHITE") && game.whiteUsername() == null) {
        gameDAO.insertWhiteUsername(joinGameRequest.gameID(), auth.username());
        return;
      } else if (Objects.equals(joinGameRequest.playerColor(), "BLACK") && game.blackUsername() == null) {
        gameDAO.insertBlackUsername(joinGameRequest.gameID(), auth.username());
        return;
      }
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    throw new GameJoinException("Gane already taken");
  }
}
