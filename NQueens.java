// Michael Warnock
// CS 461
// Project 2 - N Queens
// September 24, 2017

package nqueens;

import java.util.Scanner;

public class NQueens {
	
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		
		//get input
		System.out.println("Enter the size of the board you want to solve for: ");
		String input = scanner.nextLine();
		int N = Integer.parseInt(input);
		scanner.close();
		
		//print initial state
		System.out.println("\nGenerated " + N + " x " + N + " starting board: ");
		ChessBoard game_board = new ChessBoard(N);
		game_board.print_board();
		
		//print solved 
		System.out.println("\n\nSolved Board:\n");
		game_board.solve();
		
	}
}
