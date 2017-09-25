// Michael Warnock
// CS 461
// Project 2 - N Queens
// September 24, 2017

package nqueens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


public class ChessBoard {
	
	private int[][] board;
	private int[] q_location;
	private int num_queens;
	private int current_board_state_h;
	private int plateau_counter;
	
	ChessBoard(int n){
		this.board = new int[n][n];
		this.q_location = new int[n];
		this.num_queens = n;
		this.randomly_place_queens();
		this.current_board_state_h = single_config_heuristic(this.q_location);
		this.plateau_counter = 0;
	}
	
	
	public void randomly_place_queens() {
		//clear the board
		for(int i = 0; i < this.num_queens; i++) {
			for(int j = 0; j < this.num_queens; j++) {
				this.board[i][j] = 0;
			}
		}
		int randint;
		// Iterate through each column and randomly place a queen (-1) in that column 
		for(int i = 0; i < this.num_queens; i++) {
			randint = ThreadLocalRandom.current().nextInt(0, this.num_queens);
			board[randint][i] = -1;
			
			//keep track of where the queens are, i is (column), # at i is (row)
			this.q_location[i] = randint; 	
		}
		
	}
	
	
	public void solve() {
		next_state_board_heuristic();
		Tuple position_to_move_to = next_move();
		
		while(this.current_board_state_h != 0)  {   
			
			//If Failure. reset board and try again
			//Output that there was a restart
			if(this.plateau_counter == 200 || position_to_move_to.equals(new Tuple(-1,-1))) {
				if(this.plateau_counter == 200) {
					System.out.println("Stuck on plateau. Restart.");
					this.plateau_counter = 0;
				}
				
				else
					System.out.println("No decreasing move or side step available. Restart.");
				
				randomly_place_queens();
				next_state_board_heuristic();
				position_to_move_to = next_move();
				continue;
			}
			//Move the queen. Sorry this is hard to understand
			//take the queen off the board at its old location (queen from the same column as the new position)
			this.board[this.q_location[position_to_move_to.b]][position_to_move_to.b] = 0;
			
			//add its new location on the q_location array
			this.q_location[position_to_move_to.b] = position_to_move_to.a;
			
			//put the queen in its new location on the actual board
			this.board[this.q_location[position_to_move_to.b]][position_to_move_to.b] = -1;
			
			//run heuristic
			next_state_board_heuristic();
			//get next move
			position_to_move_to = next_move();
		}
		
		//move the queen the last time
		this.board[this.q_location[position_to_move_to.b]][position_to_move_to.b] = 0;
		this.q_location[position_to_move_to.b] = position_to_move_to.a;
		this.board[this.q_location[position_to_move_to.b]][position_to_move_to.b] = -1;
		//output results
		print_board();
		
	}
	
	
	private Tuple next_move() {
		
		Tuple nextmove = new Tuple(-1,-1);
		ArrayList<Tuple> possible_moves = new ArrayList<Tuple>();
		int min_conflicts = this.current_board_state_h;
		boolean side_step_available = false;
		
		//find what the smallest next step is
		for(int i = 0; i < this.num_queens; i++) {
			for(int j = 0; j < this.num_queens; j++) {
				if(this.board[i][j] != -1 && this.board[i][j] < min_conflicts) {
					min_conflicts = this.board[i][j];
				}
				if(this.board[i][j] == this.current_board_state_h) {
					side_step_available = true;
				}
			}
		}
		//There is no next state of lesser conflict and there is no next state with equal conflict
		//Therefore declare failure by returning (-1, -1) coordinate
		if(min_conflicts == this.current_board_state_h && side_step_available == false) {
			return nextmove;
		}
		
		//The case of a side step needs to be counted
		else if(min_conflicts == this.current_board_state_h) {
			this.plateau_counter++;
			
		}
		//else there will be an improved next state, and there is no plateau so reset counter
		else
			this.plateau_counter = 0;
		
		//gather all of the coordinates with the minimum number of next state conflicts (it may be a side step)
		for(int i = 0; i < this.num_queens; i++) {
			for(int j = 0; j < this.num_queens; j++) {
				if(this.board[i][j] == min_conflicts) {
					possible_moves.add(new Tuple(i,j));
				}
			}
		}
		//set the current state as the next state
		this.current_board_state_h = min_conflicts;

		//randomly pick from possible moves
		int randint = ThreadLocalRandom.current().nextInt(0, possible_moves.size());
		nextmove = possible_moves.get(randint);
		return nextmove;
	}
	
	
	//gives the number of conflicts for every next state 
	private void next_state_board_heuristic() {
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
		
		return h/2;
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
				if(this.board[i][j] == -1)
					System.out.print("Q   ");
				else
					System.out.print("X   ");
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

