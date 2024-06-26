package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.websocket.WSServer;
import service.*;
import spark.*;

import java.util.Collection;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final WSServer webSocketHandler;

    public Server() {
        AuthDAO authDAO = new SqlAuthDAO();
        GameDAO gameDAO = new SqlGameDAO();
        UserDAO userDAO = new SqlUserDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(userDAO, authDAO, gameDAO);
        clearService = new ClearService(authDAO, userDAO, gameDAO);

        webSocketHandler = new WSServer();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", WSServer.class);

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
        } catch (JsonSyntaxException | BadRequestException j) {
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
            return new Gson().toJson(null);
        } catch (RuntimeException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
        }
    }

    private Object createGame(Request req, Response res) {
        try {
            var authToken=req.headers("authorization");
            AuthData authData=new AuthData("", authToken);
            var gameRequest=new Gson().fromJson(req.body(), CreateGameRequest.class);
            GameData gameData=gameService.createGame(authData, gameRequest.gameName());
            CreateGameResponse game = new CreateGameResponse(String.valueOf(gameData.gameID()), gameData.gameName());
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
            return new Gson().toJson(new ListGamesResponse(games));
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
            return new Gson().toJson(null);
        } catch (JsonSyntaxException | DataAccessException j) {
            res.status(400);
            return new Gson().toJson(new ErrorResponse("Error: gameID does not exist"));
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
        return new Gson().toJson(null);
    }

    public ClearService getClearService() {
        return clearService;
    }

    public static void main(String[] args) {
        new Server().run(8080);
    }
}
