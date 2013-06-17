/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.util.ArrayList;

public class solver3 extends Solver
{
  private static final int COLUMN = 0;
  private static final int ROW = 1;
  private static final int BOX = 2;

  // array list contain all the candidate array list of all element in the board
  private static ArrayList<ArrayList<Character>> findAllCandidateList(Board board, int row_column_box, int x)
  {
    ArrayList<ArrayList<Character>> allElementsCandidates = new ArrayList<ArrayList<Character>>();

    for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++)
    {
      int row = (row_column_box == ROW) ? x : ((row_column_box == BOX) ? (x / Board.DIMENSION * Board.DIMENSION + i / Board.DIMENSION) : i);
      int col = (row_column_box == COLUMN) ? x : ((row_column_box == BOX) ? (x % Board.DIMENSION * Board.DIMENSION + i % Board.DIMENSION) : i);
      if (board.getVal(row, col) == null)
        allElementsCandidates.add(board.getCandidates(row, col));
      else
        allElementsCandidates.add(new ArrayList<Character>());
    }

    return allElementsCandidates;
  }

  // find pairs that meet the pair condition, both only have 2 candidate and the same
  @SuppressWarnings("unchecked")
  private static ArrayList<ArrayList<Character>> findPairs(Board board, ArrayList<ArrayList<Character>> allElementsCandidates)
  {
    ArrayList<ArrayList<Character>> resultPairs = new ArrayList<ArrayList<Character>>();

    // get the array list of candidate that there is only 2 candidate in it
    ArrayList<ArrayList<Character>> pairElementsCandidates = new ArrayList<ArrayList<Character>>();
    for (int i = 0, j = 0; i < allElementsCandidates.size(); i++)
      if (allElementsCandidates.get(i).size() == 2)
        pairElementsCandidates.add(j++, (ArrayList<Character>) allElementsCandidates.get(i).clone());

    // Approach 1
    // //initial counter for each pairs
    // int[] pairElementsRepeatTime = new int[pairElementsCandidates.size()];
    // for(int i=0;i<pairElementsCandidates.size();i++)
    // pairElementsRepeatTime[i] = 1;
    //
    // //count the number of time each the pair candidates repeat
    // for(int i=0;i<pairElementsCandidates.size();i++)
    // for(int j=0;j<pairElementsCandidates.size();j++)
    // if(i!=j && pairElementsCandidates.get(j).containsAll(pairElementsCandidates.get(i)))
    // pairElementsRepeatTime[i]++;
    //
    // //add the pair that only repeat 2 times
    // for(int i=0;i<pairElementsCandidates.size();i++)
    // if(pairElementsRepeatTime[i] == 2 && !resultPairs.contains(pairElementsCandidates.get(i)))
    // resultPairs.add(pairElementsCandidates.get(i));

    // Approach 2
    // add the pair to the result if it occur more than 1 times
    for (int i = 0; i < pairElementsCandidates.size(); i++)
      for (int j = i + 1; j < pairElementsCandidates.size(); j++)
        if (!resultPairs.contains(pairElementsCandidates.get(i)) && pairElementsCandidates.get(i).containsAll(pairElementsCandidates.get(j)))
        {
          resultPairs.add(pairElementsCandidates.get(i));
          break;
        }

    return resultPairs;
  }

  // edit a sequence of element with the pairs
  private static boolean editElement(Board board, int row_column_box, int x) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
  {
    boolean changed = false;
    ArrayList<ArrayList<Character>> allElementsCandidates = findAllCandidateList(board, row_column_box, x);
    ArrayList<ArrayList<Character>> pairs = findPairs(board, allElementsCandidates);

    // if the element's candidates contain one alphabet in pairs, remove those 2 alphabet in that clone candidate list
    for (int i = 0; i < pairs.size(); i++)
      for (int j = 0; j < allElementsCandidates.size(); j++)
        if (!(allElementsCandidates.get(j).contains(pairs.get(i).get(0)) && allElementsCandidates.get(j).contains(pairs.get(i).get(1))))
        {
          allElementsCandidates.get(j).remove(pairs.get(i).get(0));
          allElementsCandidates.get(j).remove(pairs.get(i).get(1));
        }

    // specific solver to solve the inputed sequence(row or column)
    for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++)
    {
      int row = (row_column_box == ROW) ? x : ((row_column_box == BOX) ? (x / Board.DIMENSION * Board.DIMENSION + i / Board.DIMENSION) : i);
      int col = (row_column_box == COLUMN) ? x : ((row_column_box == BOX) ? (x % Board.DIMENSION * Board.DIMENSION + i % Board.DIMENSION) : i);
      if (board.getVal(row, col) == null && allElementsCandidates.get(i).size() == 1)
      {
        board.setVal(row, col, allElementsCandidates.get(i).get(0));
        changed = true;
      }
    }

    return changed;
  }

  // solve procedure loop
  public static boolean trySolve(Board board) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
  {
    solver1to2(board);
    boolean changed;
    do
    {
      changed = false;
      for (int i = 0; i < 3; i++)
        for (int j = 0; j < Board.DIMENSION * Board.DIMENSION; j++)
          if (editElement(board, i, j))
          {
            solver1to2(board);
            j = -1;
            changed = true;
          }
    } while (changed);

    return board.isSolved();
  }

}
