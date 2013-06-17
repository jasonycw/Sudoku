/**
 *
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */


public class Solver
{
	// method to copy Board object
	public static Board copyBoard(Board board)
	{
		Alphabet alphabet = board.getAlphabet();
		Character[][] val = new Character[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
			for(int j = 0;j<Board.DIMENSION*Board.DIMENSION;j++)
				val[i][j] = board.getVal(i, j);
		return new Board(val, alphabet);
	}
	
	// method to find if the board have contradiction
	public static boolean noContradiction(Board board)
	{
		for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
			for(int j = 0;j<Board.DIMENSION*Board.DIMENSION;j++)
				if(board.getVal(i, j)==null&&board.getCandidates(i, j).size()==0)
					return false;
		return true;
	}
	
	//method to call all the solvers
	public static boolean solve(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
	{
		if(solver1to4(board))
			return true;
		if(solver5.trySolve(board))
			return true;
		return false;
	}
	
	// method to call solver1 and solver2
	public static boolean solver1to2(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
	{
		if(solver1.trySolve(board))
			return true;
		if(solver2.trySolve(board))
			return true;
		return false;
	}
	
	// method to use solver 1 to 3
	public static boolean solver1to3(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
	{
		if(solver1to2(board))
			return true;
		if(solver3.trySolve(board))
			return true;
		return false;
	}
	
	// method to call solver 1 to 4
	public static boolean solver1to4(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
	{
		if(solver1to3(board))
			return true;
		if(solver4.trySolve(board))
			return true;
		return false;
	}
}
