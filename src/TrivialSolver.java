

import java.util.ArrayList;

public class TrivialSolver implements ISolver {

	@Override
	public boolean trySolve(Board board) {
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			for(int j=0; j<Board.DIMENSION*Board.DIMENSION; j++)
			{
				if(board.getVal(i, j) == null)
				{
					ArrayList<Character> cands = board.getCandidates(i, j);
					if(cands.size() == 0)
						return false;
					try {
						board.setVal(i, j, cands.get(0));
					} catch (OutOfBoundaryException e) {
						e.printStackTrace();
						return false;
					} catch (DuplicateException e) {
						e.printStackTrace();
						return false;
					} catch (ReadOnlyException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		return true;
	}

}
