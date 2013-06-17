/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.io.Serializable;


public class Alphabet implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6906385698060963797L;
	// String the store all the alphabet as a string
	private String				alphabet;
	
	
	/**
	 * Initialize the alphabet with the default values
	 */
	public Alphabet()
	{
		alphabet = "123456789";
	}
	
	
	// constructor to convert the char array into the sting
	public Alphabet(char[] input) throws InvalidAlphabetException
	{
		if(input.length!=Board.DIMENSION*Board.DIMENSION)
			throw new InvalidAlphabetException();
		for(int i = 0;i<input.length;i++)
			for(int j = i+1;j<input.length;j++)
				if(input[j]==input[i])
					throw new InvalidAlphabetException();
		
		alphabet = new String(input);
	}
	
	
	// method to get the position of a character in alphabet, return 0 if it is not in the string
	public int getPosition(char c)
	{
		return alphabet.indexOf(c);
	}
	
	
	// check if the alphabet contain the inputed character
	public boolean isValidChar(char c)
	{
		return alphabet.contains(""+c);
	}
	
	
	// get the character that store in a position
	public Character get(int pos)
	{
		return alphabet.charAt(pos);
	}
	
	
	// copy the new alphabet string to this alphabet string
	public void copy(Alphabet alphabet2)
	{
		alphabet = alphabet2.alphabet;
	}
	
}
