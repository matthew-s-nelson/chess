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
          startUpMenu(out, scanner);
      }
//      System.out.printf("Type your numbers%n>>> ");

      line = scanner.nextLine();
      System.out.println(line);
    }
    System.out.println("Goodbye!");
  }

  public void startUpMenu(PrintStream out, Scanner scanner) {
    printStartUpMenu(out);
    int response = scanner.nextInt();
    switch (response) {
      case 1:
        out.println("Login");
        break;
      case 2:
        out.println("Register");
        break;
      case 3:
        out.print(SET_TEXT_ITALIC);
        out.println("Type the number of the option you would like to select and hit 'enter'");
        out.print(RESET_TEXT_ITALIC);
        break;
      case 4:
        out.print("Goodbye!");
        System.exit(200);
        break;
    }
  }

  public void printStartUpMenu(PrintStream out) {
      out.println("What would you like to do?");
      out.println("  1. Login");
      out.println("  2. Register");
      out.println("  3. Help");
      out.println("  4. Quit");
  }
}
