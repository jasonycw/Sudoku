/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.util.List;


/**
 * Represents a conflict of entering a new value to the grid
 * 
 */
public class DuplicateException extends Exception
{
	
	/**
	 * For serialization
	 */
	private static final long	serialVersionUID	= -2517791178834377773L;
	private List<Sequence>		seq;
	private List<Element>		conflictEleList;
	
	
	/**
	 * The list of sequences that the exception happen
	 */
	public DuplicateException(List<Sequence> seq, List<Element> conflictEleList)
	{
		this.seq = seq;
		this.conflictEleList = conflictEleList;
	}
	
	
	/**
	 * Get one sequence that has the exception
	 * 
	 * @param pos
	 *            the position of the exception (0-based)
	 * @return the sequence that has the exception in position pos.
	 */
	public Sequence getSequence(int pos)
	{
		return seq.get(pos);
	}
	
	
	/**
	 * Get one element that that the new value conflicts
	 * with in the sequence getSequence(pos)
	 * 
	 * @param pos
	 *            the position of the element (0-based)
	 * @return the element that has the exception in position pos.
	 */
	public Element getConflictElement(int pos)
	{
		return conflictEleList.get(pos);
	}
	
	
	/**
	 * Get the total number of conflicts happen to the value
	 * 
	 * @return the total number of conflicts happen to the value
	 */
	public int getNumConflicts()
	{
		return conflictEleList.size();
	}
	
}
