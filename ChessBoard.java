package nqueens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


public class ChessBoard {
	
	private int[][] board;
	private int[] q_location;
	private int num_queens;
	private int current_board_state_h;
	private ArrayList<Tuple> next_move;
	
	ChessBoard(int n){
		this.board = new int[n][n];
		this.q_location = new int[n];
		this.num_queens = n;
		this.randomly_place_queens();
		this.current_board_state_h = single_config_heuristic(this.q_location);
	}
	
	
	public void randomly_place_queens() {
		int randint;
		// Iterate through each column and randomly place a queen (-1) in that column 
		for(int i = 0; i < this.num_queens; i++) {
			randint = ThreadLocalRandom.current().nextInt(0, this.num_queens);
			board[randint][i] = -1;
			
			//keep track of where the queens are, i is (column), # at i is (row)
			q_location[i] = randint; 	
		}
		
	}
	
	//gives the number of conflicts for every next state 
	public void next_state_board_heuristic() {
		int[] temp_qloc = this.q_location.clone();
		//Deep copy board
		int[][] temp_board = Arrays.stream(this.board).map(int[]::clone).toArray(int[][]::new);

		
		for(int i = 0; i < this.num_queens; i++) {
			for(int j = 0; j < this.num_queens; j++) {
				if(temp_board[j][i] != -1) {
				
					this.board[temp_qloc[i]][i] = 0;
					temp_qloc[i] = j;
					this.board[j][i] = -1;
					temp_board[j][i] = single_config_heuristic(temp_qloc);
				}
			}
			//put queen back where it started
			this.board[temp_qloc[i]][i] = 0;
			temp_qloc[i] = this.q_location[i];
			this.board[temp_qloc[i]][i] = -1;
		}
		this.board = temp_board;
	}
	
	
	//returns the number of conflicts for given board configuration
	private int single_config_heuristic(int[] qloc) {
		int h = 0;
		//sum the number of attacks from each queen
		for(int i = 0; i < this.num_queens; i++) {
			h += num_attacks_heuristic(qloc[i], i);
		}
		
		return h;
	}
	
	
	//finds the number of conflicts from a given tile on the board (as if a queen was located at that tile)
	//only counts conflicts of one queen
	private int num_attacks_heuristic(int row, int col) {
		int h = 0; //The number of conflicts
		//The following constants are used to calculate the tiles on the queens attack path
		int tl_to_br_const = row - col; //top-left to bottom-right diagonal
		int tr_to_bl_const = row + col; //top-right to bottom-left diagonal
		
		for(int i = 0; i < this.num_queens; i++) {
			for(int j = 0; j < this.num_queens; j++) {
				//ignore moves in the same column since it is known there are no queens there
				//but include all other attack zone tiles
				if(j != col && (i-j == tl_to_br_const || i+j == tr_to_bl_const || i == row)) {
					if(this.board[i][j] == -1) {
						h++;
					}
				}
			}
		}
		
		return h;
	}
	
			
	public void print_board() {
		for(int i = 0; i < this.num_queens; i++) {
			for(int j = 0; j < this.num_queens; j++) {
				System.out.print(this.board[i][j] + " ");
			}
			System.out.println("\n");
		}
	}
	
	
	public void print_queen_arr() {
		for(int i = 0; i < this.num_queens; i++) {
			System.out.println("(" + this.q_location[i] + ", " + i + ")  " + board[this.q_location[i]][i]);
		}
		
		
	}
	
}

