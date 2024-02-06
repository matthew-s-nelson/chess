package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move: possibleMoves) {
            ChessPiece replacedPiece = board.getPiece(move.getEndPosition());
            board.movePiece(move.getStartPosition(), move.getEndPosition(), piece);
            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }
            board.movePiece(move.getEndPosition(), move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), replacedPiece);
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece=board.getPiece(move.getStartPosition());
        if (piece != null && piece.getTeamColor() == turn) {
            Collection<ChessMove> valid = validMoves(move.getStartPosition());
            if (valid.contains(move)) {
                if (move.getPromotionPiece() != null) {
                    board.movePiece(move.getStartPosition(), move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
                } else {
                    board.movePiece(move.getStartPosition(), move.getEndPosition(), piece);
                }
                if (piece.getTeamColor() == TeamColor.WHITE) {
                    this.setTeamTurn(TeamColor.BLACK);
                } else {
                    this.setTeamTurn(TeamColor.WHITE);
                }
            } else {
                throw new InvalidMoveException("Move not valid.");
            }
        } else {
            throw new InvalidMoveException("Not your turn.");
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition kingPosition = board.findKing(teamColor);

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != teamColor) {
                    Collection<ChessMove> movesToCheck = board.getPiece(position).pieceMoves(board, position);
                    for (ChessMove move: movesToCheck) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == teamColor) {
                    Collection<ChessMove> movesToCheck = validMoves(position);
                    for (ChessMove move : movesToCheck) {

                        // If there's a piece where it's moving to it gets deleted, have to save that piece and add it back when you move the piece back
                        ChessPiece replacedPiece = board.getPiece(move.getEndPosition());
                        ChessPiece piece = board.getPiece(move.getStartPosition());
                        board.movePiece(move.getStartPosition(), move.getEndPosition(), piece);
                        if (!isInCheck(teamColor)) {
                            board.movePiece(move.getEndPosition(), move.getStartPosition(), piece);
                            board.addPiece(move.getEndPosition(), replacedPiece);
                            return false;
                        }
                        board.movePiece(move.getEndPosition(), move.getStartPosition(), piece);
                        board.addPiece(move.getEndPosition(), replacedPiece);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(pos).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
