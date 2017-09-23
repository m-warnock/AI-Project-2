package nqueens;

public class NQueens {
	
	public static void main(String args[]) {
		ChessBoard game_board = new ChessBoard(4);
		game_board.print_board();
		System.out.println("\n\n\n-----------------------------------------");
		game_board.next_state_board_heuristic();
		game_board.print_board();
		//game_board.print_queen_arr();

	}
}
