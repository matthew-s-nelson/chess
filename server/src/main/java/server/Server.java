package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
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
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
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

    private Object login(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = userService.login(user);
        return new Gson().toJson(auth);
    }

    //Not working
    private Object logout(Request req, Response res) {
        var authToken = req.headers("authorization");
        AuthData authData = new AuthData("", authToken);
        userService.logout(authData);
        res.status(200);
        return "Logout successful";
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
