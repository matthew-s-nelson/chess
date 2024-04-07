package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
  private UserDAO userDAO;
  private AuthDAO authDAO;

  public UserService(UserDAO userDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
  }

  public AuthData register(UserData user) throws BadRequestException {
    if (user.username() == null || user.password() == null || user.email() == null) {
      throw new BadRequestException("All parameters are required");
    }
    if (userDAO.userExists(user.username())) {
      throw new RuntimeException();
    } else {
      userDAO.insertUser(user);
      return authDAO.createAuth(user.username());
    }
  }
  public AuthData login(UserData user) {
    try {
      UserData userData = userDAO.selectUser(user.username(), user.password());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    return authDAO.createAuth(user.username());
  }
  public void logout(AuthData auth) {
    try {
      authDAO.getAuth(auth.authToken());
      authDAO.deleteAuth(auth.authToken());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public AuthData getUser(String authToken) throws DataAccessException {
    return authDAO.getAuth(authToken);
  }
}
