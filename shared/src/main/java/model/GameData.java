package model;

import chess.ChessGame;

public record GameData (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
  public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    this.gameID = gameID;
    this.whiteUsername = whiteUsername;
    this.blackUsername = blackUsername;
    this.gameName = gameName;
    this.game = game;
  }
  public GameData(int gameID, String gameName) {
    this(gameID, null, null, gameName, null);
  }

  public GameData updateWhiteUsername(String newWhiteName) {
    return new GameData(gameID, newWhiteName, blackUsername, gameName, game);
  }

  public GameData updateBlackUsername(String newBlackName) {
    return new GameData(gameID, whiteUsername, newBlackName, gameName, game);
  }

  public GameData updateGame(ChessGame newGame) {
    return new GameData(gameID, whiteUsername, blackUsername, gameName, newGame);
  }
}
