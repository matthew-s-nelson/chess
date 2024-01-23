package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case BISHOP:
                BishopMove bishopMoving = new BishopMove(pieceColor, board, myPosition);
                return bishopMoving.bishopMoves();
            case ROOK:
                RookMove rookMoving = new RookMove(pieceColor, board, myPosition);
                return rookMoving.rookMoves();
            case QUEEN:
                QueenMove queenMoving = new QueenMove(pieceColor, board, myPosition);
                return queenMoving.queenMoves();
            case KING:
                KingMove kingMoving = new KingMove(pieceColor, board, myPosition);
                return kingMoving.kingMoves();
            case KNIGHT:
                KnightMove knightMoving = new KnightMove(pieceColor, board, myPosition);
                return knightMoving.knightMoves();
            case PAWN:
                PawnMove pawnMoving = new PawnMove(pieceColor, board, myPosition);
                return pawnMoving.pawnMoves();


        }
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessPiece that=(ChessPiece) o;

        if (pieceColor != that.pieceColor) return false;
      return type == that.type;
    }

    @Override
    public int hashCode() {
        int result=pieceColor.hashCode();
        result=31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
