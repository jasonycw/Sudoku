/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.util.ArrayList;


public class solver4 extends Solver
{
	// method to find if the specific value in the candidate list will lead to a possible solution
	private static boolean noContradiction(Board board, int row, int col, Character val)
	{
		Board cloneBoard = copyBoard(board);
		try
		{
			cloneBoard.setVal(row, col, val);
			solver1to3(cloneBoard);
			return noContradiction(cloneBoard);
		}
		catch(OutOfBoundaryException e)
		{
			return false;
		}
		catch(DuplicateException e)
		{
			return false;
		}
		catch(ReadOnlyException e)
		{
			return false;
		}
	}
	
	
	// method to eliminate the bad guess and return the possible alphabet in an array list
	private static ArrayList<Character> eliminateBadGuess(Board board, int row, int col)
	{
		ArrayList<Character> possibleResults = new ArrayList<Character>();
		ArrayList<Character> currentCandidate = board.getCandidates(row, col);
		
		for(int i = 0;i<currentCandidate.size();i++)
			if(noContradiction(board, row, col, currentCandidate.get(i)))
				possibleResults.add(currentCandidate.get(i));
		
		return possibleResults;
	}
	
	//solve procedure loop
	public static boolean trySolve(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
	{
		for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
			for(int j = 0;j<Board.DIMENSION*Board.DIMENSION;j++)
				if(board.getVal(i, j)==null)
				{
					ArrayList<Character> candidateListWithoutBadGuess = eliminateBadGuess(board, i, j);
					if(candidateListWithoutBadGuess.size()==1)
					{
						board.setVal(i, j, candidateListWithoutBadGuess.get(0));
						solver1to3(board);
					}
				}
		
		return board.isSolved();
	}
	
}
