package dataAccess;

import model.AuthData;

public interface AuthDAO {
  String createAuth(String username);
  String getAuth(String authToken) throws DataAccessException;
  void deleteAuth(String authToken) throws DataAccessException;
  void deleteAllAuth();
}
