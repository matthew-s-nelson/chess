package dataAccess;

import model.UserData;

public interface UserDAO {
  void insertUser(UserData u) throws DataAccessException;
  UserData selectUser(String username) throws DataAccessException;
  void deleteUsers();
}
