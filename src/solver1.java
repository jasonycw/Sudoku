/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.util.ArrayList;


public class solver1
{
	public static boolean trySolve(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
	{
		for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
			for(int j = 0;j<Board.DIMENSION*Board.DIMENSION;j++)
				if(board.getVal(i, j)==null)
				{
					ArrayList<Character> cands = board.getCandidates(i, j);
					if(cands.size()==1)
					{// set the value to the only candidate in that element
						board.setVal(i, j, cands.get(0));
						i = 0;
						j = 0;
					}
				}
		
		return board.isSolved();
	}
}
