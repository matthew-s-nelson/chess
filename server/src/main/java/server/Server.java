package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Collection;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(userDAO, authDAO, gameDAO);
        clearService = new ClearService(authDAO, userDAO, gameDAO);

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.delete("/db", this::clearDB);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);

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

    // Add an exception here.
    private Object logout(Request req, Response res) {
        var authToken = req.headers("authorization");
        AuthData authData = new AuthData("", authToken);
        userService.logout(authData);
        res.status(200);
        return new Gson().toJson("");
    }

    // If gameName already exists, throw an error
    private Object createGame(Request req, Response res) {
        var authToken = req.headers("authorization");
        AuthData authData = new AuthData("", authToken);
        var gameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        GameData game = gameService.createGame(authData, gameRequest.gamename());
        return new Gson().toJson(game);
    }

    private Object listGames(Request req, Response res) {
        var authToken = req.headers("authorization");
        AuthData authData = new AuthData("", authToken);
        Collection<GameData> games = gameService.listGames(authData);
        return new Gson().toJson(games);
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
