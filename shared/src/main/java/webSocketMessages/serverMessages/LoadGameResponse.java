package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessPiece;

public class LoadGameResponse extends ServerMessage {
  private ChessBoard board;
  public LoadGameResponse(ChessBoard board) {
    super(ServerMessageType.LOAD_GAME);
    this.board = board;
  }

  public ChessBoard board() {
    return board;
  }
}
