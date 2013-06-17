/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.io.Serializable;


public class Element implements Serializable
{
	
	private static final long	serialVersionUID	= 5575699296262255060L;
	/**
	 * Coordinate of the point
	 */
	private int					x;
	private int					y;
	private int					val;
	private Alphabet			alphabet;
	
	
	// get the coordinate and the position of the value in the alphabet set
	// then link this element to it's corresponding sequence
	public Element(int x, int y, Character val,
				Box box, Row row, Column col, Alphabet alphabet)
	{
		this.x = x;
		this.y = y;
		this.val = (val==null)?-1:alphabet.getPosition(val);
		box.associate(this);
		row.associate(this);
		col.associate(this);
		this.alphabet = alphabet;
	}
	
	
	/**
	 * Get the row number of the element. (0-based)
	 * 
	 * @return the row number of the element. (0-based)
	 */
	public int getX()
	{
		return x;
	}
	
	
	/**
	 * Get the column number of the element. (0-based)
	 * 
	 * @return the column number of the element. (0-based)
	 */
	public int getY()
	{
		return y;
	}
	
	
	/**
	 * Get the value entered in the element. Null if the element is empty.
	 * 
	 * @return the value entered in the element
	 */
	public Character getVal()
	{
		if(val==-1)
			return null;
		return alphabet.get(val); //
	}
	
	
	// get the alphabet set that this element use
	protected Alphabet getAlphabet()
	{
		return alphabet;
	}
	
	
	
}
