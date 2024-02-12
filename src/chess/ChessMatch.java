package chess;

import boardgame.Board;

public class ChessMatch {
	
	private Board board;
	
	
	public ChessMatch() {
		board = new Board(8, 8);
		
	}
//metodo onde retorna uma matriz de peças de xadrez correspondente a essa partida 
	
	public ChessPiece[][] getPieces(){
		
		ChessPiece[][] mat= new ChessPiece[board.getRows()][board.getColumns()];
		//agora vamos percorrer a matriz de peças do tabuleiro(board) e fazer um downcast para chessPiece
		
		for(int i=0; i < board.getRows();i++) {
			for(int j=0; j < board.getColumns();j++) {
			mat[i][j] = (ChessPiece) board.piece(i, j);	
				
			}
		}	
		
		return mat;
	}

}