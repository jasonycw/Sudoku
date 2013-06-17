/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.util.ArrayList;

public class solver2
{
  private static final int COLUMN = 0;
  private static final int ROW = 1;
  private static final int BOX = 2;

  // method to find the singletons in a specific sequence(row or column or box)
  private static boolean checkSingletons(Board board, int row_column_box, int x) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
  {
    boolean changed = false;

    // initialize a table for all alphabet and set the number of that alphabet to 0
    int[] columnCandidateDuplication = new int[Board.DIMENSION * Board.DIMENSION];
    for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++)
      columnCandidateDuplication[i] = 0;

    // get candidates in each element in that column and add the corresponding box to increase by 1
    for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++)
    {
      int row = (row_column_box == ROW) ? x : ((row_column_box == BOX) ? (x / Board.DIMENSION * Board.DIMENSION + i / Board.DIMENSION) : i);
      int col = (row_column_box == COLUMN) ? x : ((row_column_box == BOX) ? (x % Board.DIMENSION * Board.DIMENSION + i % Board.DIMENSION) : i);
      if (board.getVal(row, col) == null)
      {
        ArrayList<Character> cands = board.getCandidates(row, col);
        // Get the position of that candidate for matching
        for (int j = 0; j < cands.size(); j++)
          columnCandidateDuplication[board.getAlphabet().getPosition(cands.get(j))]++;
      }
    }

    // find the one with only one occurrence in candidate list and set it to that value
    for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++)
      if (columnCandidateDuplication[i] == 1)
        for (int j = 0; j < Board.DIMENSION * Board.DIMENSION; j++)
        {
          int row = (row_column_box == ROW) ? x : ((row_column_box == BOX) ? (x / Board.DIMENSION * Board.DIMENSION + j / Board.DIMENSION) : j);
          int col = (row_column_box == COLUMN) ? x : ((row_column_box == BOX) ? (x % Board.DIMENSION * Board.DIMENSION + j % Board.DIMENSION) : j);
          if (board.getVal(row, col) == null && board.getCandidates(row, col).contains(board.getAlphabet().get(i)))
            board.setVal(row, col, board.getAlphabet().get(i));
          solver1.trySolve(board);
          changed = true;
        }

    return changed;
  }

  // main solver loop
  public static boolean trySolve(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
  {
    boolean changed;
    do
    {
      changed = false;
      for (int i = 0; i < 3; i++)
        for (int j = 0; j < Board.DIMENSION * Board.DIMENSION; j++)
          if (checkSingletons(board, i, j))
          {
            j = -1;// if there is any change, reset the for loop and
            changed = true;// repeat the whole loop again next time
          }
    } while (changed);

    return board.isSolved();
  }

}
