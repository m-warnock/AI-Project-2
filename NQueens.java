package nqueens;

public class NQueens {
	
	public static void main(String args[]) {
		ChessBoard game_board = new ChessBoard(4);
		game_board.print_board();
		System.out.println("\n\n\n-----------------------------------------");
		//game_board.next_state_board_heuristic();
		game_board.solve();
		//game_board.print_board();
		//Tuple next_move;
		//next_move = game_board.next_move();
		//System.out.print("\n" + next_move.a + " " + next_move.b);
		//game_board.print_queen_arr();

	}
}
