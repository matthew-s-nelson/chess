package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

public class LoadGameResponse extends ServerMessage {
  private ChessGame game;
  public LoadGameResponse(ChessGame game) {
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

  public ChessGame game() {
    return game;
  }
}
