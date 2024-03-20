package clientTests;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;
import spark.utils.Assert;

import java.io.IOException;


public class ServerFacadeTests {
    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clear() {
        server.getClearService().clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerGood() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerBad() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.register("player1", "password", "p1@email.com");
        });
    }

    @Test
    void logoutGood() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertDoesNotThrow(() -> {
            facade.logout();
        });
    }

    @Test
    void logoutBad() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.logout();
        });
    }

    @Test
    void loginGood() throws Exception {
        var authData = facade.register("player1", "password", "hi");
        facade.logout();
        var login = facade.login("player1", "password");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginBad() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login("players", "password");
        });
    }

    @Test
    void createGameGood() throws ResponseException, IOException {
        facade.register("player1", "password", "hi");
        GameData expected = new GameData(1, "test");
        GameData result = facade.createGame("test");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void createGameBad() throws Exception {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.createGame("test");
        });
    }
}
