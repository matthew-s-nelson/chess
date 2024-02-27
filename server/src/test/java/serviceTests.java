import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BadRequestException;
import service.UserService;

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
    try {
      AuthData auth = userService.register(new UserData("matt", "nel", "email"));
      userService.logout(auth);
      AuthData result = userService.login(new UserData("matt", "nel", "email"));
      AuthData expected = new AuthData("matt", "");
    } catch (BadRequestException e) {
      Assertions.fail();
    } catch (RuntimeException f) {
      Assertions.fail();
    }

    Assertions.assertEquals(true, true);
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
  public void testGameServiceGood() {
    this.userD
  }
}
