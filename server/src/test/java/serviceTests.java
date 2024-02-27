import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.io.RuntimeIOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.JoinGameRequest;
import service.BadRequestException;
import service.GameJoinException;
import service.GameService;
import service.UserService;
import spark.utils.Assert;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashSet;

public class serviceTests {
  @Test
  public void testRegisterGood() {
    UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    try {
      AuthData result = userService.register(new UserData("matt", "nel", "email"));
      AuthData expected = new AuthData("matt", "");
      Assertions.assertEquals(expected.username(), result.username());
    } catch (BadRequestException e) {
      Assertions.fail("A BadRequestException was thrown");
    }
  }

  @Test
  public void testRegisterBad() {
    UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    Assertions.assertThrows(BadRequestException.class, () -> {
      userService.register(new UserData("matt", null, "email"));
    });
  }

  @Test
  public void testLoginGood() {
    UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    try {
      AuthData auth = userService.register(new UserData("matt", "nel", "email"));
      userService.logout(auth);
      AuthData result = userService.login(new UserData("matt", "nel", "email"));
      AuthData expected = new AuthData("matt", "");
      Assertions.assertEquals(expected.username(), result.username());
    } catch (BadRequestException e) {
      Assertions.fail();
    }
  }

  @Test
  public void testLoginBad() {
    UserService userService=new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    try {
      AuthData auth=userService.register(new UserData("matt", "nel", "email"));
      userService.logout(auth);
    } catch (BadRequestException e) {
      Assertions.fail();
    }

    Assertions.assertThrows(RuntimeException.class, () -> {
      userService.login(new UserData("matt", "ne", "email"));
    });
  }

  @Test
  public void testLogoutGood() {
    UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    AuthData auth = null;
    try {
      auth = userService.register(new UserData("matt", "nel", "email"));
    } catch (BadRequestException e) {
      Assertions.fail();
    }
    AuthData finalAuth=auth;
    Assertions.assertDoesNotThrow(() -> {
      userService.logout(finalAuth);
    });
  }

  @Test
  public void testLogoutBad() {
    UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    try {
      AuthData auth = userService.register(new UserData("matt", "nel", "email"));
    } catch (BadRequestException e) {
      Assertions.fail();
    }
    AuthData badAuth = new AuthData("matt", "");
    Assertions.assertThrows(RuntimeException.class, () -> {
      userService.logout(badAuth);
    });
  }

  @Test
  public void testCreateGameGood() {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    UserService userService = new UserService(userDAO, authDAO);
    GameService gameService = new GameService(userDAO, authDAO, gameDAO);
    AuthData auth=null;
    try {
      auth = userService.register(new UserData("matt", "nel", "email"));
    } catch (BadRequestException e) {
      Assertions.fail();
    }
    try {
      int result = gameService.createGame(auth, "test").gameID();
      int expected = 1;
      Assertions.assertEquals(expected, result);
    } catch (RuntimeException e) {
      Assertions.fail();
    }
  }

  @Test
  public void testCreateGameBad() {
    UserDAO userDAO=new MemoryUserDAO();
    AuthDAO authDAO=new MemoryAuthDAO();
    GameDAO gameDAO=new MemoryGameDAO();
    UserService userService=new UserService(userDAO, authDAO);
    GameService gameService=new GameService(userDAO, authDAO, gameDAO);
    AuthData auth=null;
    try {
      auth=userService.register(new UserData("matt", "nel", "email"));
    } catch (BadRequestException e) {
      Assertions.fail();
    }
    Assertions.assertThrows(RuntimeException.class, () -> {
      gameService.createGame(new AuthData("matt", ""), "test");
    });
  }

  @Test
  public void testListGamesGood() {
    UserDAO userDAO=new MemoryUserDAO();
    AuthDAO authDAO=new MemoryAuthDAO();
    GameDAO gameDAO=new MemoryGameDAO();
    UserService userService=new UserService(userDAO, authDAO);
    GameService gameService=new GameService(userDAO, authDAO, gameDAO);
    AuthData auth=null;
    try {
      auth=userService.register(new UserData("matt", "nel", "email"));
    } catch (BadRequestException e) {
      Assertions.fail();
    }
    Collection<GameData> expected = new HashSet<>();
    expected.add(gameService.createGame(auth, "test"));
    expected.add(gameService.createGame(auth, "test2"));
    Collection<GameData> result = gameService.listGames(auth);
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testListGamesBad() {
    UserDAO userDAO=new MemoryUserDAO();
    AuthDAO authDAO=new MemoryAuthDAO();
    GameDAO gameDAO=new MemoryGameDAO();
    UserService userService=new UserService(userDAO, authDAO);
    GameService gameService=new GameService(userDAO, authDAO, gameDAO);
    AuthData auth=null;
    try {
      auth=userService.register(new UserData("matt", "nel", "email"));
    } catch (BadRequestException e) {
      Assertions.fail();
    }
    gameService.createGame(auth, "test");
    gameService.createGame(auth, "test2");
    Assertions.assertThrows(RuntimeException.class, () ->{
      gameService.listGames(new AuthData("matt",""));
    });
  }

  @Test
  public void testJoinGameGood() {
    UserDAO userDAO=new MemoryUserDAO();
    AuthDAO authDAO=new MemoryAuthDAO();
    GameDAO gameDAO=new MemoryGameDAO();
    UserService userService=new UserService(userDAO, authDAO);
    GameService gameService=new GameService(userDAO, authDAO, gameDAO);
    AuthData auth=null;
    try {
      auth=userService.register(new UserData("matt", "nel", "email"));
    } catch (BadRequestException e) {
      Assertions.fail();
    }
    gameService.createGame(auth, "test");
    AuthData finalAuth=auth;
    Assertions.assertDoesNotThrow(() -> {
      gameService.joinGame(finalAuth, new JoinGameRequest("BLACK", 1));
    });
  }

  @Test
  public void testJoinGameBad() {
    UserDAO userDAO=new MemoryUserDAO();
    AuthDAO authDAO=new MemoryAuthDAO();
    GameDAO gameDAO=new MemoryGameDAO();
    UserService userService=new UserService(userDAO, authDAO);
    GameService gameService=new GameService(userDAO, authDAO, gameDAO);
    AuthData auth=null;
    try {
      auth=userService.register(new UserData("matt", "nel", "email"));
    } catch (BadRequestException e) {
      Assertions.fail();
    }
    AuthData finalAuth=auth;
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameService.joinGame(finalAuth, new JoinGameRequest("BLACK", 1));
    });
  }
}
