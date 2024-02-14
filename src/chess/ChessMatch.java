package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}
//metodo onde retorna uma matriz de peças de xadrez correspondente a essa partida 

	public ChessPiece[][] getPieces() {

		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		// agora vamos percorrer a matriz de peças do tabuleiro(board) e fazer um
		// downcasting para chessPiece

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);

			}
		}

		return mat;
	}
	
	//metodo para instanciarmos as peças de xadrez informando sistemas de coordenadas do xadrez não da matriz
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	

	public void initialSetup() {
		
		placeNewPiece('b',6, new Rook(board, Color.WHITE));
		placeNewPiece('e',8, new King(board, Color.BLACK));
		placeNewPiece('e',1, new King(board, Color.WHITE));
		/*
		 * board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
		 * board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
		 * board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
		 */
	}
}