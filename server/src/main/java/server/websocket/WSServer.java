package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.gson;
import spark.Spark;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WSServer {
  private ConnectionManager connections = new ConnectionManager();
//  public static void main(String[] args) {
//    Spark.port(8080);
//    Spark.webSocket("/connect", WSServer.class);
//    Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//  }

  @OnWebSocketMessage
  public void onMessage(Session session, String msg) throws Exception {
    UserGameCommand command =new Gson().fromJson(msg, UserGameCommand.class);

    if (command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER || command.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
      connections.addConnection(command.getAuthString(), session);
    }

    Session conn = connections.getConnection(command.getAuthString());
    if (conn != null) {
      switch (command.getCommandType()) {
        case JOIN_PLAYER -> join(conn, msg);
        case JOIN_OBSERVER -> observe(conn, msg);
        case MAKE_MOVE -> move(conn, msg);
        case LEAVE -> leave(conn, msg);
        case RESIGN -> resign(conn, msg);
      }
    } else {
//      Connection.sendError(session.getRemote(), "unknown user");
    }
  }

  public void join(Session session, String msg) {

  }

  public void observe(Session session, String msg) {

  }

  public void move(Session session, String msg) {

  }

  public void leave(Session session, String msg) {

  }

  public void resign(Session session, String msg) {

  }

}
