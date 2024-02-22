package dataAccess;

import model.UserData;

public interface UserDAO {
  void insertUser(UserData user) throws DataAccessException;
  UserData selectUser(String username) throws DataAccessException;
  void deleteUsers();
}
