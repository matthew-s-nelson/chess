package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.UserData;
import service.ClearService;
import service.UserService;
import spark.*;

public class Server {
    private final UserService userService;
    private final ClearService clearService;

    public Server(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(authDAO, userDAO, gameDAO);

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", this::register);
        Spark.delete("/db", this::clearDB);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = userService.register(user);
        return new Gson().toJson(auth);
    }

    private Object clearDB(Request req, Response res) {
        clearService.clear();
        res.status(200);
        return "";
    }

    public static void main(String[] args) {
        new Server(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO()).run(8080);
    }
}
