
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class generates a random traditional Sudoku board using backtracking. It
 * also solves the generated Sudoku board using a similar backtracking
 * algorithm.
 * 
 * Note: Some styling conventions have been ignored to conserve space
 *
 * @author Chris Benton
 */
public class Sudoku {

	private int[][] board;
	private int[][] solved;

	public Sudoku() {
		board = new int[9][9];
		solved = new int[9][9];
		generateBoard(0, 0);
		removeValues();
	}

	/**
	 * Generates Sudoku board using backtracking recursion
	 * 
	 * @param row
	 *            Row of current step
	 * @param col
	 *            Column of current step
	 * @return Returns true if step is valid, false if backtracking is required
	 */
	private boolean generateBoard(int row, int col) {
		if (col > 8) {
			col = 0;
			row++;
		}

		if (row == 9) { return true; }

		int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		shuffleValues(values);

		for (int val : values) {
			if (isValid(board, row, col, val)) {
				board[row][col] = val;
				if (generateBoard(row, col + 1)) { return true; }
			}
		}

		board[row][col] = 0;
		return false;
	}


	/**
	 * Copies original board to solved board and calls solver
	 */
	void solve() {
		for (int row = 0; row < 9; row++) {
			solved[row] = Arrays.copyOf(board[row], 9);
		}
		solver(0, 0);
	}

	/**
	 * Solves the generated Sudoku board using backtracking recursion
	 * 
	 * @param row
	 *            Row of current step
	 * @param col
	 *            Column of current step
	 * @return Returns true if step is valid, false if backtracking is required
	 */
	private boolean solver(int row, int col) {
		if (col > 8) {
			col = 0;
			row++;
		}
		if (row == 9) { return true; }
		if (board[row][col] != 0) { return solver(row, col + 1); }

		List<Integer> possibles = getPossibles(row, col);

		for (int possible : possibles) {
			solved[row][col] = possible;
			if (solver(row, col + 1)) { return true; }
		}
		solved[row][col] = 0;
		return false;
	}

	/**
	 * Compiles list of possible values for given cell
	 * 
	 * @param row
	 *            Row of cell to be evaluated
	 * @param col
	 *            Column of cell to be evaluated
	 * @return List of all possible values
	 */
	private List<Integer> getPossibles(int row, int col) {
		List<Integer> possibles = new ArrayList<Integer>();

		for (int val = 1; val <= 9; val++) {
			if (isValid(solved, row, col, val)) { possibles.add(val); }
		}
		return possibles;
	}


	/**
	 * Shuffles values of given array
	 * 
	 * @param values
	 *            int array to be shuffled
	 */
	void shuffleValues(int[] values) {
		Random rand = ThreadLocalRandom.current();
		for (int i = 0; i < 9; i++) {
			int index = rand.nextInt(i + 1);

			int temp = values[index];
			values[index] = values[i];
			values[i] = temp;
		}
	}

	/**
	 * Randomly remove values from original board for game play.
	 */
	private void removeValues() {
		Random rand = ThreadLocalRandom.current();
		for (int count = 0; count < 75; count++) {
			int row = rand.nextInt(9);
			int col = rand.nextInt(9);
			board[row][col] = 0;
		}
	}

	/**
	 * Evaluates validity of given value at given cell
	 * 
	 * @param board 	Board to be evaluated
	 * @param row 	Row of cell to be evaluated
	 * @param col 	Column of cell to be evaluated
	 * @param val 	Value to be evaluated
	 * @return 		True if value is valid for given cell, false otherwise
	 */
	private boolean isValid(int[][] board, int row, int col, int val) {
		// Check associated rows and columns
		for (int index = 0; index < 9; index++) {
			if (board[row][index] == val || board[index][col] == val) { return false; }
		}
		// Check associated 3 x 3 “sector”
		int sector = ((row / 3) * 3) + (col / 3);
		int rowAdjust = (sector / 3) * 3;
		int colAdjust = (sector - rowAdjust) * 3;
		for (int r = rowAdjust; r < rowAdjust + 3; r++) {
			for (int c = colAdjust; c < colAdjust + 3; c++) {
				if (board[r][c] == val) { return false; }
			}
		}
		return true;
	}

	
/**
	 * Creates string representation of ASCII based board
	 * 
	 * @param board
	 *            String representation of board
	 */
	public void printBoard(int[][] board) {
		String horizBar = "+-------+-------+-------+";
		System.out.println(horizBar);

		for (int row = 0; row < 9; row++) {
			System.out.print("| ");

			for (int col = 0; col < 9; col++) {
				if (board[row][col] == 0) { System.out.print("  "); } 
else { System.out.print(board[row][col] + " "); }
				if (col == 2 || col == 5 || col == 8) { System.out.print("| "); }
			}

			if (row == 2 || row == 5 || row == 8) { System.out.print("\n" + horizBar); }
			System.out.println();
		}

	}

	public static void main(String[] args) {
		Sudoku s = new Sudoku();
		System.out.println("ORIGINAL BOARD");
		s.printBoard(s.board);

		System.out.println();
		System.out.println("SOLVED BOARD");
		s.solve();
		s.printBoard(s.solved);
	}
}

