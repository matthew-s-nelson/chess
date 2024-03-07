package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.utils.Assert;

public class daoTests {
  private AuthDAO authDAO;
  private GameDAO gameDAO;
  private UserDAO userDAO;
  @BeforeEach
  public void setUp() {
    authDAO = new SqlAuthDAO();
    gameDAO = new SqlGameDAO();
    userDAO = new SqlUserDAO();

    authDAO.deleteAllAuth();
    gameDAO.deleteAllGames();
    userDAO.deleteUsers();
  }

  @Test
  public void testInsertUserGood() {
    Assertions.assertDoesNotThrow(() -> {
      userDAO.insertUser(new UserData("TestUser", "TestPassword", "email"));
    });

  }

  @Test
  public void testInsertUserBad() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      userDAO.insertUser(new UserData("TestUser", "TestPassword", "email"));
      userDAO.insertUser(new UserData("TestUser", "TestPassword", "email"));
    });
  }

  @Test
  public void testUserExistsGood() {
    userDAO.insertUser(new UserData("TestUser", "TestPassword", "email"));
    boolean result = userDAO.userExists("TestUser");
    Assertions.assertTrue(result);
  }

  @Test
  public void testUserExistsBad() {
    boolean result = userDAO.userExists("TestUser");
    Assertions.assertFalse(result);
  }

  @Test
  public void testSelectUserGood() {
    UserData user = new UserData("TestUser", "TestPassword", "email");
    userDAO.insertUser(user);
    try {
      UserData result=userDAO.selectUser("TestUser", "TestPassword");
      Assertions.assertEquals(user, result);
    } catch (DataAccessException e) {
      Assertions.fail();
    }
  }

  @Test
  public void testSelectUserBad() {
    userDAO.insertUser(new UserData("TestUser", "TestPassword", "email"));
    Assertions.assertThrows(DataAccessException.class, () -> {
      userDAO.selectUser("TestUser", "BadPassword");
    });
  }

  @Test
  public void testDeleteUsersGood() {
    userDAO.insertUser(new UserData("TestUser", "TestPassword", "email"));
    Assertions.assertDoesNotThrow(() -> {
      userDAO.deleteUsers();
    });
  }

  @Test
  public void testCreateAuthGood() {
    AuthData result = authDAO.createAuth("TestUsername");

    Assertions.assertEquals("TestUsername", result.username());
    Assertions.assertNotNull(result.authToken());

  }

  @Test
  public void testCreateAuthBad() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      authDAO.createAuth(null);
    });
  }

  @Test
  public void testGetAuthGood() {
    AuthData expected = authDAO.createAuth("TestUsername");
    try {
      AuthData result = authDAO.getAuth(expected.authToken());
      Assertions.assertEquals(expected, result);
    } catch (DataAccessException e) {
      Assertions.fail();
    }
  }

  @Test
  public void testGetAuthBad() {
    AuthData expected = authDAO.createAuth("TestUsername");
    Assertions.assertThrows(DataAccessException.class, () -> {
      AuthData result = authDAO.getAuth("not valid auth");
    });
  }

  @Test
  public void deleteAuthGood() {
    AuthData auth = authDAO.createAuth("TestUsername");
    Assertions.assertDoesNotThrow(() -> {
      authDAO.deleteAuth(auth.authToken());
    });
  }

  @Test
  public void deleteAuthBad() {
    Assertions.assertThrows(DataAccessException.class, () -> {
      authDAO.deleteAuth("Auth that doesn't exist");
    });
  }

  @Test
  public void deleteAllAuthGood() {
    authDAO.createAuth("TestUsername");
    Assertions.assertDoesNotThrow(() -> {
      authDAO.deleteAllAuth();
    });
  }

  @Test
  public void insertGame() {

  }
}
