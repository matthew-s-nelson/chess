package dataAccess;

import model.AuthData;

public interface AuthDAO {
  AuthData createAuth(String username);
  String getAuth(String authToken) throws DataAccessException;
  void deleteAuth(String authToken) throws DataAccessException;
  void deleteAllAuth();
}
