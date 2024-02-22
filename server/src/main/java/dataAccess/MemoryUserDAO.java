package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{
  Collection<UserData> userData;

  public MemoryUserDAO() {
    userData = new HashSet<>();
  }

  @Override
  public void insertUser(UserData user) throws DataAccessException {
    userData.add(user);
  }

  @Override
  public UserData selectUser(String username) throws DataAccessException {
    for (UserData user: userData) {
      if (user.username() == username) {
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
