package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Menu {
  public Menu() {}

  public void run() {
    String line = "";
    int menuNum = 1;
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    out.print(SET_TEXT_BOLD);
    out.print("WELCOME TO CHESS!\n");
    out.println(RESET_TEXT_BOLD_FAINT);



    while (!Objects.equals(line, "quit")) {
      Scanner scanner = new Scanner(System.in);
      switch (menuNum) {
        case 1:
          menuNum = startUpMenu(out, scanner);
        case 2:
          menuNum = loginScreen(out, scanner);
        case 3:
          menuNum = loggedInMenu(out, scanner);
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
        return 2;
      case 2:
        out.println("Register");
        registerScreen(out, scanner);
        return 2;
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

  public int loginScreen(PrintStream out, Scanner scanner) {
    out.println("Username:");
    String username = scanner.next();
    out.println("Password:");
    String password = scanner.next();
    out.println(username);
    out.println(password);
    return 3;
  }

  public int loggedInMenu(PrintStream out, Scanner scanner) {
    printLoggedInMenu(out);
    int response = scanner.nextInt();
    int gameID;
    switch (response) {
      case 1:
        help(out);
        break;
      case 2:
        out.println("Logging out");
        return 1;
      case 3:
        createGameScreen(out, scanner);
        break;
      case 4:
        break;
      case 5:
        gameID = joinGameScreen(out, scanner);
        break;
      case 6:
        gameID = joinGameScreen(out, scanner);
        break;
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
  }

  public void registerScreen(PrintStream out, Scanner scanner) {
    out.print("Username: ");
    String username = scanner.next();
    out.print("Password: ");
    String password = scanner.next();
    out.print("Email: ");
    String email = scanner.next();
  }

  public int joinGameScreen(PrintStream out, Scanner scanner) {
    out.println("What is the gameID of the game you would like to join?");
    int gameID = scanner.nextInt();
    return gameID;
  }

  public void help(PrintStream out) {
    out.print(SET_TEXT_ITALIC);
    out.println("Type the number of the option you would like to select and hit 'enter'");
    out.print(RESET_TEXT_ITALIC);
  }
}
