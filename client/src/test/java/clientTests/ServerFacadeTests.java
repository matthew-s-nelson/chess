package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

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

}
