package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
  private static Collection<UserData> userData;

  public MemoryUserDAO() {
    userData = new HashSet<>();
  }

  @Override
  public void insertUser(UserData user) {
    userData.add(user);
  }

  @Override
  public boolean userExists(String username) {
    for (UserData user: userData) {
      if (Objects.equals(user.username(), username)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public UserData selectUser(String username, String password) throws DataAccessException {
    for (UserData user: userData) {
      if (Objects.equals(user.username(), username) && Objects.equals(user.password(), password)) {
        return user;
      }
    }
    throw new DataAccessException("No user with this username.");
  }

  @Override
  public void deleteUsers() {
    userData.clear();
  }
}
