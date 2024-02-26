package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameJoinException;
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
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            var auth = userService.register(user);
            res.status(200);
            return new Gson().toJson(auth);
        } catch (JsonSyntaxException j) {
            res.status(400);
            return new Gson().toJson(new ErrorResponse("Error: bad request"));
        } catch (RuntimeException e) {
            res.status(403);
            return new Gson().toJson(new ErrorResponse("Error: already taken"));
        }

    }

    private Object login(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        try {
            var auth = userService.login(user);
            res.status(200);
            return new Gson().toJson(auth);
        } catch(RuntimeException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
        }
    }

    // Add an exception here.
    private Object logout(Request req, Response res) {
        try {
            var authToken=req.headers("authorization");
            AuthData authData=new AuthData("", authToken);
            userService.logout(authData);
            res.status(200);
            return new Gson().toJson("");
        } catch (RuntimeException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
        }
    }

    // If gameName already exists, throw an error
    // List only gameID?
    // Put empty string for other params of GameData instead of null?
    private Object createGame(Request req, Response res) {
        try {
            var authToken=req.headers("authorization");
            AuthData authData=new AuthData("", authToken);
            var gameRequest=new Gson().fromJson(req.body(), CreateGameRequest.class);
            GameData game=gameService.createGame(authData, gameRequest.gamename());
            return new Gson().toJson(game);
        } catch (JsonSyntaxException j) {
        res.status(400);
        return new Gson().toJson(new ErrorResponse("Error: bad request"));
        } catch (RuntimeException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
        }
    }

    private Object listGames(Request req, Response res) {
        try {
            var authToken=req.headers("authorization");
            AuthData authData=new AuthData("", authToken);
            Collection<GameData> games=gameService.listGames(authData);
            return new Gson().toJson(games);
        } catch (RuntimeException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
        }
    }

    private Object joinGame(Request req, Response res) {
        try {
            var authToken=req.headers("authorization");
            AuthData authData=new AuthData("", authToken);
            var joinGameRequest=new Gson().fromJson(req.body(), JoinGameRequest.class);
            gameService.joinGame(authData, joinGameRequest);
            res.status(200);
            return new Gson().toJson("");
        } catch (JsonSyntaxException j) {
            res.status(400);
            return new Gson().toJson(new ErrorResponse("Error: bad request"));
        } catch (RuntimeException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
        } catch (GameJoinException g) {
            res.status(403);
            return new Gson().toJson(new ErrorResponse("Error: already taken"));
        }
    }

    private Object clearDB(Request req, Response res) {
        clearService.clear();
        res.status(200);
        return new Gson().toJson("");
    }

    public static void main(String[] args) {
        new Server(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO()).run(8080);
    }
}
