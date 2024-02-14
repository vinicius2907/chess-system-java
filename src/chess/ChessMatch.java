package chess;

import boardgame.Board;
import boardgame.Piece;
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

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		// Convertendo essas posições para posições da matriz
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		// operação para validar se tem uma peça
		validateSourcePosition(source);

		Piece capturedPiece = makeMove(source, target);
		// fazer um Dowcasting
		return (ChessPiece) capturedPiece;
	}

		// logica ara realizar o movimento
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		
		// agora vamos capturar a peça que esta no local de destino
		Piece capturedPiece = board.removePiece(target);
		
		//e colocar a outra que foi removida no"source" na posição de destino
		board.placePiece(p, target);
		
		//retornar a peça capturada
		return capturedPiece;
	}

	// metodo para validar se tem peça na osição inicial
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessExcepition("There is no piece on source position");
		}
	}

	// metodo para instanciarmos as peças de xadrez informando sistemas de
	// coordenadas do xadrez não da matriz

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	public void initialSetup() {

		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}