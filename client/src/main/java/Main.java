import chess.*;
import serverfacade.ServerFacade;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade serverFacade = new ServerFacade(8080);
        Menu menu = new Menu(serverFacade);
        menu.run();
    }
}