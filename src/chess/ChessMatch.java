package chess;

import java.security.DrbgParameters.NextBytes;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		this.turn = 1;
		check = false;
		this.currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
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

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();

	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {

		// Convertendo essas posições para posições da matriz
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();

		// operação para validar se tem uma peça
		validateSourcePosition(source);

		// validar posição de destino
		validateTargetPosition(source, target);

		Piece capturedPiece = makeMove(source, target);

		// testando se o movimento deiou o jogador em check

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessExcepition("You can't put yourself in check");

		}
		// testar pra ver se oponente ficou em check
		// expressao condicional ternaria
		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			// trocar o turno(jogador)
			nextTurn();
		}

		// fazer um Dowcasting
		return (ChessPiece) capturedPiece;
	}

	// logica ara realizar o movimento
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		// agora vamos capturar a peça que esta no local de destino
		Piece capturedPiece = board.removePiece(target);

		// e colocar a outra que foi removida no"source" na posição de destino
		board.placePiece(p, target);

		// agora vamos testar se houve peça capturada, vomos remover da lista de peças
		// no tabuleiro (piecesOnTheBoard) e add na lista de peças capturadas
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		// retornar a peça capturada
		return capturedPiece;
	}

	// Desfazer o movimento
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		// devolvendo a peça na possição de origem
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}

	// metodo para validar se tem peça na osição inicial
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessExcepition("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessExcepition("The chosen piece is not yours");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessExcepition("There is no possible moves for the chosen piece");
		}
	}

	// metodo para validar posição de destino
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessExcepition("The chosen piece can't move to target position");
		}
	}

	// Mudando o turno(Jogador)

	private void nextTurn() {
		turn++;
		// Para mudarmos de cor faremos uma expressão condicional ternaria
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		// chamar esse metodo depois que terminar uma jogada
	}

	// metodo para instanciarmos as peças de xadrez informando sistemas de
	// coordenadas do xadrez não da matriz

	// metodo dando uma cor ele me retorna o oponente dessa cor
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		// testando pra cada peça p na minha lista list
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There iss no " + color + " King on the board");
	}

	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
		// verificar agora se algum movimento posssivel dessa peça p que leva para a
		// posição do rei
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;

	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		// colocar peças dentro da lista de peças no tabuleiro
		piecesOnTheBoard.add(piece);

	}

	public void initialSetup() {

		placeNewPiece('h', 7, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));

//		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
//		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
//		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('b', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 8, new King(board, Color.BLACK));

//		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
//		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
//		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
//		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}