package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessExcepition;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();

		while (true) {
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch);
				System.out.println();
				System.out.print("Source: ");
				// ler posição de origem
				ChessPosition source = UI.readChessPosition(sc);
	
				//imprimir na tela os movimentos pssiveis atras do metodo possibleMoves da classe chessMatch
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				//inserir uma sobrecarga no printBoard para que imprima em colorido os possiveis movimentos 
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				
				
				// ler posição de destino
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
	
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			}
			catch(ChessExcepition e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}

	}

}
