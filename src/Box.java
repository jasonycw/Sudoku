/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.util.ArrayList;


/**
 * The box type of sequence
 * 
 */
public class Box extends Sequence
{
	
	/**
	 * For serialization
	 */
	private static final long	serialVersionUID	= 3381374557853843834L;
	private int					x;
	private int					y;
	
	
	/**
	 * The position of the box
	 */
	public Box(int i, int j)
	{
		x = i;
		y = j;
	}
	
	
	/*
	 * To link the element into this sequence
	 * 
	 * (non-Javadoc)
	 */
	@ Override
	public void associate(Element element)
	{
		setElement(element.getX()%Board.DIMENSION*Board.DIMENSION+element.getY()%Board.DIMENSION, element);
	}
	
	
	/**
	 * Get the row number of the box
	 * 
	 * @return the row number of the box
	 */
	public int getX()
	{
		return x;
	}
	
	
	/**
	 * Get the column number of the box
	 * 
	 * @return the column number of the box
	 */
	public int getY()
	{
		return y;
	}
	
	
	// get the detail of this sequence
	@ Override
	public String toString()
	{
		return "x: "+x+", y: "+y+", value: "+super.getEnteredVals().toString();
	}
	
	
}
