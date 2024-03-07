package dataAccessTests;

import dataAccess.*;
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
  public void testCreateAuth()
}
