import java.util.ArrayList;
import java.util.Random;

public class RandBoardGen
{
	private static int TRIALS = 23;

	public static Character[][] gen(Alphabet alp)
	{
		Character[][] res = new Character[Board.DIMENSION * Board.DIMENSION][Board.DIMENSION * Board.DIMENSION];
		boolean isCanSolvePuzzle = false;
		Random rand = new Random();
		TRIALS -= rand.nextInt()%4;
		
		do
		{
			Board b = new Board();
			int randNum = rand.nextInt();
			for (int i = 0; i < TRIALS; i++)
			{
				int x, y;
				do
				{
					x = rand.nextInt(Board.DIMENSION * Board.DIMENSION);
					y = rand.nextInt(Board.DIMENSION * Board.DIMENSION);
				} while (b.getVal(x, y) != null);
				if (b.getVal(x, y) == null)
				{
					ArrayList<Character> cands = b.getCandidates(x, y);
					if (cands.size() > 0)
						try
						{
							b.setVal(x, y, cands.get(rand.nextInt(cands.size())));
						} catch (OutOfBoundaryException e)
						{
							// should not happen
							e.printStackTrace();
						} catch (DuplicateException e)
						{
							// should not happen
							e.printStackTrace();
						} catch (ReadOnlyException e)
						{
							// should not happen
							e.printStackTrace();
						}
				}
			}
			for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++)
				for (int j = 0; j < Board.DIMENSION * Board.DIMENSION; j++)
					res[i][j] = b.getVal(i, j);
			try
			{
				Board board = new Board(res, b.getAlphabet());
				isCanSolvePuzzle = Solver.solve(board);
				rand.setSeed(randNum*randNum);
			} catch (OutOfBoundaryException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DuplicateException e)
			{
				// TODO Auto-generated catch block
				System.out.println(b.toString());
				e.printStackTrace();
			} catch (ReadOnlyException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				rand.setSeed(randNum*randNum);
			}
		} while (!isCanSolvePuzzle);

		return res;
	}

}
