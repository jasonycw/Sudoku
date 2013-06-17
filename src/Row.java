/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */


/**
 * The row type of sequence
 * 
 */
public class Row extends Sequence
{
	
	
	/**
	 * For serialization
	 */
	private static final long	serialVersionUID	= 8046294874695168279L;
	private int					rowId;
	
	
	/**
	 * The ID of the row
	 */
	public Row(int i)
	{
		rowId = i;
	}
	
	
	/*
	 * To link the element into this sequence
	 * 
	 * (non-Javadoc)
	 */
	@ Override
	public void associate(Element element)
	{
		setElement(element.getY(), element);
	}
	
	
	/**
	 * Get the ID of the row
	 * 
	 * @return the ID of the row
	 */
	public int getRowId()
	{
		return rowId;
	}
	
	
	// get the detail of this sequence
	@ Override
	public String toString()
	{
		return "row: "+rowId+", value: "+super.getEnteredVals().toString();
	}
	
}
