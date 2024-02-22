package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
  Collection<AuthData> authData;
  public MemoryAuthDAO() {
    authData = new HashSet<>();
  }
  @Override
  public String createAuth(String username) {
    String authToken = UUID.randomUUID().toString();
    authData.add(new AuthData(authToken, username));
    return authToken;
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
      }
    }
    throw new DataAccessException("AuthToken does not exist.");
  }

  @Override
  public void deleteAllAuth() {
    authData.clear();
  }
}
