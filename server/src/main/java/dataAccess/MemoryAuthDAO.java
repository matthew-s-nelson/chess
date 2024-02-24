package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
  private static Collection<AuthData> authData;
  public MemoryAuthDAO() {
    authData = new HashSet<>();
  }
  @Override
  public AuthData createAuth(String username) {
    String authToken = UUID.randomUUID().toString();
    AuthData newAuth = new AuthData(username, authToken);
    authData.add(newAuth);
    return newAuth;
  }

  @Override
  public String getAuth(String authToken) throws DataAccessException{
    for (AuthData auth: authData) {
      if (Objects.equals(auth.authToken(), authToken)) {
        return authToken;
      }
    }
    throw new DataAccessException("AuthToken does not exist.");
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    for (AuthData auth: authData) {
      if (Objects.equals(auth.authToken(), authToken)) {
        authData.remove(auth);
        return;
      }
    }
    throw new DataAccessException("AuthToken doesn't exist.");
  }

  @Override
  public void deleteAllAuth() {
    authData.clear();
  }
}
