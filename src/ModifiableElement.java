/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a grid in the board that can be modified
 * 
 */
public class ModifiableElement extends Element
{
	/**
	 * For serialization
	 */
	private static final long	serialVersionUID	= 8025282256821727988L;
	private int					val;
	private Box					box;
	private Row					row;
	private Column				col;
	
	
	/**
	 * Constructs a new element
	 * 
	 * @param i
	 *            x coordinate (row number) of the element
	 * @param j
	 *            y coordinate (column number) of the element
	 * @param seqs
	 *            the sequences that the element relates to
	 */
	public ModifiableElement(int i, int j, Box box, Row row, Column col, Alphabet alphabet)
	{
		super(i, j, null, box, row, col, alphabet);
		val = -1;
		this.box = box;
		this.row = row;
		this.col = col;
	}
	
	
	/**
	 * Set value to the element. The value will not be set if any of the
	 * following exceptions happen
	 * 
	 * Sets a value to the element.
	 * 
	 * If val = null, it removes the existing value in the element.
	 * 
	 * Throws OutOfBoundaryException
	 * if the input value is invalid (outside the valid alphabet range).
	 * 
	 * Throws DuplicateException
	 * if the new value conflicts with one or more of the sequences that the element is associated with.
	 * 
	 * @param val
	 *            the value to be set. Null if you want to empty the
	 *            element.
	 * @throws OutOfBoundaryException
	 *             The entered value is out-of-boundary
	 * @throws DuplicateException
	 *             The entered value conflicts with some of
	 *             the related sequences. The information of the conflicts is included
	 *             in the DuplicateException object.
	 */
	public void setVal(Character val) throws OutOfBoundaryException, DuplicateException
	{
		if(val==null)
			this.val = -1;
		else
		{
			if(!super.getAlphabet().isValidChar(val))
				throw new OutOfBoundaryException();
			// TODO DuplicateException
			ArrayList<Character> notExistedVal = getCandidates();
			if(!notExistedVal.contains(val))
			{
				List<Sequence> seq = new ArrayList<Sequence>();
				List<Element> conflictEleList = new ArrayList<Element>();
				
				if(box.findElement(val)!=null)
				{
					seq.add(box);
					conflictEleList.add(box.findElement(val));
				}
				if(row.findElement(val)!=null)
				{
					seq.add(row);
					conflictEleList.add(row.findElement(val));
				}
				if(col.findElement(val)!=null)
				{
					seq.add(col);
					conflictEleList.add(col.findElement(val));
				}
				throw new DuplicateException(seq, conflictEleList);
			}
			
			// for(int i=0;i<existedVal.size();i++)
			// if(val == existedVal.get(i))
			// throw new DuplicateException(null,null);
			this.val = getAlphabet().getPosition(val);
		}
	}
	
	
	/**
	 * Gets a list of candidate values for the element that can be used as input.
	 * 
	 * The candidates are judged by the values entered in element¡¦s associated sequences
	 * by calling their getEnteredVals() method.
	 * 
	 * @return
	 */
	public ArrayList<Character> getCandidates()
	{
		ArrayList<Character> result = new ArrayList<Character>();
		
		for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
			result.add(getAlphabet().get(i));
		
		result.removeAll(row.getEnteredVals());
		result.removeAll(col.getEnteredVals());
		result.removeAll(box.getEnteredVals());
		
		return result;
	}
	
	
	/**
	 * Gets the value of the element.
	 * The value will later be translated by the Board¡¦s alphabet attribute.
	 * Returns null if no value has been set for the element yet.
	 */
	public Character getVal()
	{
		if(val==-1)
			return null;
		return getAlphabet().get(val);
	}
	
}
