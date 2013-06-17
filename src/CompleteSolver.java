public class CompleteSolver extends Solver implements ISolver
{
  @Override
  public boolean trySolve(Board board)
  {
    try
    {
      if (!noContradiction(board))
        return false;
      solve(board);
    } catch (OutOfBoundaryException e)
    {
      throw new RuntimeException("OutOfBoundaryException");
    } catch (DuplicateException e)
    {
      throw new RuntimeException("DuplicateException");
    } catch (ReadOnlyException e)
    {
      throw new RuntimeException("ReadOnlyException");
    }
    return board.isSolved();
  }

}
