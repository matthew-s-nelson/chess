package ui;

import model.GameData;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Menu {
  private ServerFacade serverFacade;
  ChessBoard chessBoard;
  int currentGameID;
  public Menu(ServerFacade serverFacade) {
    this.serverFacade = serverFacade;
  }

  public void run() {
    String line = "";
    int menuNum = 1;
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    out.print(SET_TEXT_BOLD);
    out.print("WELCOME TO CHESS!\n");
    out.println(RESET_TEXT_BOLD_FAINT);



    while (!Objects.equals(line, "quit")) {
      out.print(SET_TEXT_COLOR_WHITE);
      Scanner scanner = new Scanner(System.in);
      switch (menuNum) {
        case 1:
          menuNum = startUpMenu(out, scanner);
          break;
        case 2:
          menuNum = loggedInMenu(out, scanner);
          break;
      }
//      System.out.printf("Type your numbers%n>>> ");

//      line = scanner.nextLine();
//      System.out.println(line);
    }
    System.out.println("Goodbye!");
  }

  public int startUpMenu(PrintStream out, Scanner scanner) {
    printStartUpMenu(out);
    int response = scanner.nextInt();
    switch (response) {
      case 1:
        out.println("Login");
        boolean loggedIn = loginScreen(out, scanner);
        if (loggedIn) {
          return 2;
        }
        return 1;
      case 2:
        out.println("Register");
        boolean registered = registerScreen(out, scanner);
        if (registered) {
          return 2;
        }
        return 1;
      case 3:
        help(out);
        break;
      case 4:
        out.print("Goodbye!");
        System.exit(200);
        break;
    }
    return 1;
  }

  public void printStartUpMenu(PrintStream out) {
      out.println("What would you like to do?");
      out.println("  1. Login");
      out.println("  2. Register");
      out.println("  3. Help");
      out.println("  4. Quit");
  }

  public boolean loginScreen(PrintStream out, Scanner scanner) {
    out.println("Username:");
    String username = scanner.next();
    out.println("Password:");
    String password = scanner.next();
    out.println(username);
    out.println(password);
    try {
      serverFacade.login(username, password);
      return true;
    } catch (ResponseException | IOException res) {
      printError(out, res);
    }
    return false;
  }

  public int loggedInMenu(PrintStream out, Scanner scanner) {
    printLoggedInMenu(out);
    int response = scanner.nextInt();

    switch (response) {
      case 1:
        help(out);
        return 2;
      case 2:
        out.println("Logging out");
        try{
          serverFacade.logout();
        } catch (ResponseException | IOException res) {
          printError(out, res);
        }
        return 1;
      case 3:
        createGameScreen(out, scanner);
        return 2;
      case 4:
        listGames(out, scanner);
        return 2;
      case 5:
        joinGameScreen(out, scanner);
        return 2;
      case 6:
        joinAsObserverScreen(out, scanner);
        return 2;
    }
    return 3;
  }

  public void printLoggedInMenu(PrintStream out) {
    out.println("What would you like to do?");
    out.println("  1. Help");
    out.println("  2. Logout");
    out.println("  3. Create Game");
    out.println("  4. List Games");
    out.println("  5. Join Game");
    out.println("  6. Join Game As Observer");
  }

  public void createGameScreen(PrintStream out, Scanner scanner) {
    out.println("Game name: ");
    String gameName = scanner.next();
    try {
      GameData gameData = serverFacade.createGame(gameName);
      out.print("Game created with gameID ");
      out.println(gameData.gameID());
    } catch (IOException | ResponseException res) {
      printError(out, res);
    }
  }

  public boolean registerScreen(PrintStream out, Scanner scanner) {
    boolean loggedIn = false;
    out.print("Username: ");
    String username = scanner.next();
    out.print("Password: ");
    String password = scanner.next();
    out.print("Email: ");
    String email = scanner.next();
    try {
      serverFacade.register(username, password, email);
      return true;
    } catch (IOException e) {
      printError(out, e);
    } catch (ResponseException res) {
      printError(out, res);
    }
    return loggedIn;
  }

  public void joinGameScreen(PrintStream out, Scanner scanner) {
    out.println("What is the gameID of the game you would like to join?");
    String gameID = scanner.next();
    currentGameID = Integer.valueOf(gameID);
    out.println("Which color do you want to join as?");
    out.println("1. White");
    out.println("2. Black");
    int playerColor = scanner.nextInt();
    try {
      chessBoard = new ChessBoard(playerColor);
      serverFacade.joinGame(selectColor(playerColor), gameID, chessBoard);
      out.println("IT WORKED");
      inGameScreen(out, scanner);
    } catch (ResponseException | IOException e) {
      printError(out, e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public int inGameScreen(PrintStream out, Scanner scanner) throws Exception {
    inGameOptions(out);
    int optionSelected = scanner.nextInt();

    switch (optionSelected) {
      case 1:
        help(out);
        break;
      case 2:
        serverFacade.redraw();
        break;
      case 3:
        serverFacade.leave(currentGameID);
        return 0;
      case 4:
        makeMoveScreen(out, scanner);
        break;
      case 5:
        serverFacade.resign(currentGameID);
        break;
      case 6:
//        chessBoard.highlightLegalMoves(playerColor);
        break;
    }
    inGameScreen(out, scanner);
    return 0;
  }

  public void inGameOptions(PrintStream out) {
    out.println("1. Help");
    out.println("2. Redraw Chess Board");
    out.println("3. Leave");
    out.println("4. Make Move");
    out.println("5. Resign");
    out.println("6. Highlight Legal moves");
  }

  public void joinAsObserverScreen(PrintStream out, Scanner scanner) {
    out.println("What is the gameID of the game you would like to join?");
    String gameID = scanner.next();
    try {
      chessBoard = new ChessBoard(1);
      serverFacade.joinObserver(gameID, chessBoard);
    } catch (ResponseException | IOException e) {
      printError(out, e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String selectColor(int colorNum) {
    if (colorNum == 1) {
      return "WHITE";
    } else if (colorNum == 2) {
      return "BLACK";
    }
    return null;
  }

  public void listGames(PrintStream out, Scanner scanner) {
    out.println("List of Games:");
    out.println();
    try{
      Collection<GameData> games = serverFacade.listGames();
      for (GameData game: games) {
        printListGame(out, scanner, game);
      }
    } catch (ResponseException | IOException e) {
      printError(out, e);
    }
  }

  public void printListGame(PrintStream out, Scanner scanner, GameData game) {
    out.print("Game ID: ");
    out.println(game.gameID());
    out.print("Game Name: ");
    out.println(game.gameName());
    out.print("White Team: ");
    out.println(game.whiteUsername());
    out.print("Black Team: ");
    out.println(game.blackUsername());
    out.println();
  }

  public void help(PrintStream out) {
    out.print(SET_TEXT_ITALIC);
    out.println("Type the number of the option you would like to select and hit 'enter'");
    out.print(RESET_TEXT_ITALIC);
  }

  public void printError(PrintStream out, Exception e) {
    out.print(SET_TEXT_BOLD);
    out.print(SET_TEXT_COLOR_RED);
    out.println(e.getMessage());
    out.print(RESET_TEXT_BOLD_FAINT);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  public void makeMoveScreen(PrintStream out, Scanner scanner) {
    out.println("What is the position of the piece you would like to move?");
    String startPos = scanner.next();
    out.println("What is the position you would like to move it to?");
    String endPos = scanner.next();
    try {
      serverFacade.makeMove(startPos, endPos, currentGameID);
    } catch (Exception e) {
      out.print(e);
    }
  }
}
