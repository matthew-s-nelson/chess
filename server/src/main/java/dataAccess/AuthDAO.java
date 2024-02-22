package dataAccess;

import model.AuthData;

public interface AuthDAO {
  String createAut(String username);
  String getAuth(String authToken);
  void deleteAuth(String authToken);
  void deleteAllAuth();
}
