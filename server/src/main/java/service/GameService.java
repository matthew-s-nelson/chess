package service;

import chess.ChessGame;
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

  public void joinGame(AuthData authData, JoinGameRequest joinGameRequest) throws GameJoinException, DataAccessException {
    GameData game = gameDAO.selectGame(joinGameRequest.gameID());
    try {
      AuthData auth = authDAO.getAuth(authData.authToken());
      if (joinGameRequest.playerColor() == null) {
        return;
      } else if (Objects.equals(joinGameRequest.playerColor(), "WHITE") && game.whiteUsername() == null) {
        gameDAO.insertWhiteUsername(joinGameRequest.gameID(), auth.username());
        return;
      } else if (Objects.equals(joinGameRequest.playerColor(), "BLACK") && game.blackUsername() == null) {
        gameDAO.insertBlackUsername(joinGameRequest.gameID(), auth.username());
        return;
      }
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    throw new GameJoinException("Game already taken");
  }

  public void leaveGame(JoinGameRequest joinGameRequest) throws DataAccessException {
    if (joinGameRequest.playerColor() == "BLACK") {
      gameDAO.insertBlackUsername(joinGameRequest.gameID(), null);
    } else if (joinGameRequest.playerColor() == "WHITE") {
      gameDAO.insertWhiteUsername(joinGameRequest.gameID(), null);
    }
  }

  public boolean checkIfUserInGame(String authToken, int gameID) throws DataAccessException {
    GameData game = gameDAO.selectGame(gameID);
    AuthData auth = authDAO.getAuth(authToken);
    // Check if the user is in the
    if (Objects.equals(game.blackUsername(), auth.username()) || Objects.equals(game.whiteUsername(), auth.username())){
      return true;
    } else {
      throw new DataAccessException("User not in game.");
    }
  }
  public GameData getGameData(int gameID) throws DataAccessException {
    return gameDAO.selectGame(gameID);
  }

  public void updateGame(int gameID, ChessGame game) throws DataAccessException {
    gameDAO.updateGame(gameID, game);
  }
}
