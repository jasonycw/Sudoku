/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */


/**
 * The column type of sequence
 * 
 */
public class Column extends Sequence
{
	
	/**
	 * For serialization
	 */
	private static final long	serialVersionUID	= -9172599321818777827L;
	private int					colId;
	
	
	/**
	 * The column ID
	 */
	public Column(int i)
	{
		colId = i;
	}
	
	
	/*
	 * To link the element into this sequence
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.cityu.zhilinyin2.Sequence#offer(edu.cityu.zhilinyin2.Element)
	 */
	@ Override
	public void associate(Element element)
	{
		setElement(element.getX(), element);
	}
	
	
	/**
	 * Get the column ID
	 * 
	 * @return the column ID
	 */
	public int getColId()
	{
		return colId; //
	}
	
	
	// get the detail of the sequence
	@ Override
	public String toString()
	{
		return "column: "+colId+", value: "+super.getEnteredVals().toString();
	}
	
}
