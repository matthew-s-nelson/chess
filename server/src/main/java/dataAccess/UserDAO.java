package dataAccess;

import model.UserData;

public interface UserDAO {
  void insertUser(UserData user);
  boolean userExists(String username);
  UserData selectUser(String username, String password) throws DataAccessException;
  void deleteUsers();
}
