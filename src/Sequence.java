/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Represents a group of elements whose number cannot be the same
 * 
 */
public abstract class Sequence implements Serializable
{
	/**
	 * for serialization
	 */
	private static final long	serialVersionUID	= 4911806490982002779L;
	/**
	 * The elements owned by the sequence
	 */
	private Element[]			elements;
	
	
	/**
	 * Constructor
	 */
	public Sequence()
	{
		elements = new Element[Board.DIMENSION*Board.DIMENSION];
		for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
			elements[i] = null;
	}
	
	
	
	/**
	 * Add an element to the sequence (for initialization only)
	 * 
	 * @param element
	 *            the element to be added
	 */
	public abstract void associate(Element element);
	
	
	
	/**
	 * Retrieve a list of all entered numbers in the sequence
	 * 
	 * @return a list of all entered numbers in the sequence
	 */
	public ArrayList<Character> getEnteredVals()
	{
		ArrayList<Character> result = new ArrayList<Character>();
		for(int i = 0;i<Board.DIMENSION*Board.DIMENSION;i++)
			if(elements[i].getVal()!=null)
				result.add(elements[i].getVal());
		return result;
	}
	
	
	/**
	 * Get an element with value = val
	 * 
	 * @param val
	 *            the value to be searched
	 * @return The element with value = val. Null if no such element exists
	 */
	public Element findElement(Character val)
	{
		for(int i = 0;i<elements.length;i++)
			if(elements[i].getVal()==val)
				return elements[i];
		return null;
	}
	
	
	/**
	 * Set an element to the sequence
	 * 
	 * @param pos
	 *            the position of the element (0 ~ DIMENSION^2-1)
	 * @param element
	 *            the element to be set
	 */
	protected void setElement(int pos, Element element)
	{
		elements[pos] = element;
	}
	
	
	
}
