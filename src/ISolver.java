/**
 * The interface for solving Sudoku boards.
 *
 */

public interface ISolver {
	/**
	 * Try to solve a board by manipulating the passed board
	 *  object. Returns true if the board is solved. Returns 
	 *  false if the solve fails to solve the board.
	 * @param board the board to be solved.
	 * @return Returns true if the board is solved. Returns 
	 *  false if the solve fails to solve the board.
	 */
	public boolean trySolve(Board board);
}
