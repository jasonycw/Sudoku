/**
 *
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.util.ArrayList;

public class solver5 extends Solver
{
	//method to guess the board from row,col
	private static boolean guess(Board board, int row, int col)
	{
		//get the possible candidate at row,col
		ArrayList<Character> candidateList = board.getCandidates(row, col);
		
		//there must be more than one candidate if this method is used
		while(candidateList.size()>0)
		{
			//copy the board to prevent corrupting the original board
			Board tempBoard = copyBoard(board);
			try
			{
				//guess the value at row,col to be the first candidate
				tempBoard.setVal(row, col, candidateList.get(0));
				
				//call solver 5 to "try to solve the board"
				if(solver5.trySolve(tempBoard))
				{
					//if the tempBoard can lead to a good ending
					//tempBoard will be the solution
					//so copy the tempBoard back to the original board
					for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
						for(int j = 0;j<Board.DIMENSION*Board.DIMENSION;j++)
							if(board.getVal(i, j) == null)
								board.setVal(i, j, tempBoard.getVal(i, j));
					return true;
				}
				//else if it is a bad ending
				//the first candidate will be eliminated
				candidateList.remove(0);
			}
			catch(OutOfBoundaryException e)
			{
				e.printStackTrace();
			}
			catch(DuplicateException e)
			{
				e.printStackTrace();
			}
			catch(ReadOnlyException e)
			{
				e.printStackTrace();
			}
		}
		
		//after the loop the board is sure to be a bad end
		return false;
	}
	
	//try to solve the board
	public static boolean trySolve(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
	{
		//check if the board is bugged
		if(!noContradiction(board))
			return false;
		
		//use solver 1 to 4 to solve the board
		if(solver1to4(board))
			return true;
		
		//check if the half-way solved board is bugged
		if(!noContradiction(board))
			return false;
		
		//find the first empty element to guess
		for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
			for(int j = 0;j<Board.DIMENSION*Board.DIMENSION;j++)
				if(board.getVal(i, j)==null)
					return guess(board, i, j);
		
		return board.isSolved();
	}
	
}
