/**
 * 
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * The controller of the sudoku board.
 * 
 */
public class Board implements Serializable, Cloneable
{
	/**
	 * For serialization
	 */
	private static final long	serialVersionUID	= 6136897931786320404L;
	
	
	public static final int		DIMENSION			= 3;
	// 2D array to store all the element
	private Element[][]			elements;
	// 2D array to store all seqence
	// sequence[0] is box, sequence[1] is row, sequence[2] is column
	private Sequence[][]		sequences;
	// the alphabet set that used in this game
	private Alphabet			alphabet;
	
	
	/**
	 * Creates a new game
	 * Everything set to default
	 */
	public Board()
	{
		initSequences();
		alphabet = new Alphabet();
		elements = new Element[DIMENSION*DIMENSION][DIMENSION*DIMENSION];
		for(int i = 0;i<DIMENSION*DIMENSION;i++)
			for(int j = 0;j<DIMENSION*DIMENSION;j++)
				elements[i][j] = new ModifiableElement(i, j, (Box)sequences[0][(i/DIMENSION)*DIMENSION+j/DIMENSION], (Row)sequences[1][i], (Column)sequences[2][j], alphabet);
	}
	
	
	/**
	 * Constructs a game with given characters specified in the ¡¥initBoard¡¦ parameter
	 * together with an initial alphabet.
	 * 
	 * initBoard[i][j] provides a given character on grid on i-th row and j-th column.
	 * If initBoard[i][j] = null, there is no given value on that grid.
	 * 
	 * @param initBoard
	 * @param alphabet
	 */
	public Board(Character[][] initBoard, Alphabet alphabet)
	{
		initSequences();
		this.alphabet = alphabet;
		elements = new Element[DIMENSION*DIMENSION][DIMENSION*DIMENSION];
		for(int i = 0;i<initBoard.length;i++)
			for(int j = 0;j<initBoard[i].length;j++)
				if(initBoard[i][j]!=null)
					elements[i][j] = new Element(i, j, initBoard[i][j], (Box)sequences[0][(i/DIMENSION)*DIMENSION+j/DIMENSION], (Row)sequences[1][i], (Column)sequences[2][j], alphabet);
				else
					elements[i][j] = new ModifiableElement(i, j, (Box)sequences[0][(i/DIMENSION)*DIMENSION+j/DIMENSION], (Row)sequences[1][i], (Column)sequences[2][j], alphabet);
	}
	
	
	/**
	 * Read a game from a saved file
	 * 
	 * @param path
	 *            the path of the game
	 * @throws CorruptedSaveFileException
	 *             the format of the file is invalid
	 * @throws InvalidAlphabetException
	 * @throws IOException
	 */
	public Board(String path) throws FileNotFoundException, CorruptedSaveFileException
	{
		open(path);
	}
	
	
	/**
	 * Initialize all sequences
	 * sequence[type][i]
	 * 
	 * type: 0=box, 1=row, 2=column
	 * i: which row/column/box
	 */
	private void initSequences()
	{
		sequences = new Sequence[3][];
		
		sequences[0] = new Box[DIMENSION*DIMENSION];
		sequences[1] = new Row[DIMENSION*DIMENSION];
		sequences[2] = new Column[DIMENSION*DIMENSION];
		
		for(int i = 0;i<DIMENSION*DIMENSION;i++)
		{
			sequences[1][i] = new Row(i);
			sequences[2][i] = new Column(i);
		}
		for(int i = 0;i<DIMENSION;i++)
			for(int j = 0;j<DIMENSION;j++)
				sequences[0][i*3+j] = new Box(i, j);
	}
	
	
	/**
	 * Sets a value on one grid of the board.
	 * 
	 * The grid¡¦s row number is x and column number is y.
	 * 
	 * The value to be set is val.
	 * If val = null, it will empty the grid.
	 * 
	 * Throws OutOfBoundaryException if the input val is invalid (not in the alphabet).
	 * 
	 * Throws DuplicateException when the new value causes a conflict with one or more
	 * sequences that it belongs to.
	 * 
	 * Throws ReadOnlyException if the grid cannot be written (i.e a given value).
	 * 
	 * @param x
	 *            = number
	 * @param y
	 * @param val
	 * @throws OutOfBoundaryException
	 * @throws DuplicateException
	 * @throws ReadOnlyException
	 */
	public void setVal(int x, int y, Character val) throws OutOfBoundaryException, DuplicateException, ReadOnlyException
	{
		if(!(elements[x][y] instanceof ModifiableElement))
			throw new ReadOnlyException();
		
		if(x>=this.elements.length||y>=this.elements[x].length)
			throw new OutOfBoundaryException();
		if(val!=null)
		{
			if(!alphabet.isValidChar(val))
				throw new OutOfBoundaryException();
			
			ArrayList<Character> notExistedVal = ((ModifiableElement)elements[x][y]).getCandidates();
			if(!notExistedVal.contains(val))
			{
				List<Sequence> seq = new ArrayList<Sequence>();
				List<Element> conflictEleList = new ArrayList<Element>();
				
				if(sequences[0][((x/DIMENSION)*DIMENSION)+y/DIMENSION].findElement(val)!=null)
				{
					seq.add(sequences[0][((x/DIMENSION)*DIMENSION)+y/DIMENSION]);
					conflictEleList.add(sequences[0][((x/DIMENSION)*DIMENSION)+y/DIMENSION].findElement(val));
				}
				if(sequences[1][x].findElement(val)!=null)
				{
					seq.add(sequences[1][x]);
					conflictEleList.add(sequences[1][x].findElement(val));
				}
				if(sequences[2][y].findElement(val)!=null)
				{
					seq.add(sequences[2][y]);
					conflictEleList.add(sequences[2][y].findElement(val));
				}
				
				throw new DuplicateException(seq, conflictEleList);
			}
		}
		((ModifiableElement)elements[x][y]).setVal(val);
	}
	
	
	/**
	 * Get the element at position (i, j)
	 * 
	 * @param i
	 *            the row number of the element
	 * @param j
	 *            the column number of the element
	 * @return the element at position (i, j)
	 */
	public Character getVal(int i, int j)
	{
		return elements[i][j].getVal();
	}
	
	
	/**
	 * Get a list of candidate input at a certain position
	 * 
	 * @param x
	 *            the row number
	 * @param y
	 *            the column number
	 * @return the list of all candidates for the position
	 */
	public ArrayList<Character> getCandidates(int x, int y)
	{
		return ((ModifiableElement)elements[x][y]).getCandidates();
	}
	
	
	/**
	 * Get the character set for the game
	 * 
	 * @return the character set for the game
	 */
	public Alphabet getAlphabet()
	{
		return alphabet;
	}
	
	
	/**
	 * Set the character set for the game
	 * 
	 * @param alphabet2
	 *            the character set for the game
	 */
	public void setAlphabet(Alphabet alphabet2)
	{
		this.alphabet.copy(alphabet2);
	}
	
	
	/**
	 * Save the game in text format
	 * 
	 * @param path
	 *            the path in dist
	 * @throws IOException
	 *             if the path is invalid
	 */
	public void save(String path) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		for(int i = 0;i<DIMENSION*DIMENSION;i++)
			oos.writeChar(this.getAlphabet().get(i));
		
		oos.writeChars("|");
		
		for(int i = 0;i<DIMENSION*DIMENSION;i++)
		{
			for(int j = 0;j<DIMENSION*DIMENSION;j++)
				if(elements[i][j].getVal()==null)
					oos.writeChars("-");
				else
					oos.writeChars(elements[i][j].getVal().toString());
			oos.writeChars("|");
		}
		
		oos.writeChar('|');
		oos.close();
		fos.close();
	}
	
	
	// method use to open the saved file
	private void open(String path) throws FileNotFoundException, CorruptedSaveFileException
	{
		try
		{
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			char[] inputAlphabet = new char[DIMENSION*DIMENSION];
			Character[][] initBoard = new Character[DIMENSION*DIMENSION][DIMENSION*DIMENSION];
			char seperator;
			
			for(int i = 0;i<DIMENSION*DIMENSION;i++)
				inputAlphabet[i] = ois.readChar();
			seperator = ois.readChar();
			if(seperator!='|')
				throw new CorruptedSaveFileException();
			
			this.alphabet = new Alphabet(inputAlphabet);
			
			for(int i = 0;i<DIMENSION*DIMENSION;i++)
				for(int j = 0;j<DIMENSION*DIMENSION;j++)
				{
					initBoard[i][j] = ois.readChar();
					if(initBoard[i][j]!='|'&&initBoard[i][j]!='-'&&!alphabet.isValidChar(initBoard[i][j]))
						throw new CorruptedSaveFileException();
					if(initBoard[i][j]=='|')
						j--;
				}
			
			initSequences();
			this.elements = new Element[DIMENSION*DIMENSION][DIMENSION*DIMENSION];
			for(int i = 0;i<initBoard.length;i++)
				for(int j = 0;j<initBoard[i].length;j++)
					if(initBoard[i][j]!='-')
						elements[i][j] = new Element(i, j, initBoard[i][j], (Box)sequences[0][(i/DIMENSION)*DIMENSION+j/DIMENSION], (Row)sequences[1][i], (Column)sequences[2][j], alphabet);
					else
						elements[i][j] = new ModifiableElement(i, j, (Box)sequences[0][(i/DIMENSION)*DIMENSION+j/DIMENSION], (Row)sequences[1][i], (Column)sequences[2][j], alphabet);
			
			ois.close();
			fis.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(InvalidAlphabetException e)
		{
			e.printStackTrace();
			throw new CorruptedSaveFileException();
		}
	}
	
	
	/*
	 * method to convert the whole board in to one String
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@ Override
	public String toString()
	{
		String result = new String();
		
		for(int i = 0;i<DIMENSION*DIMENSION;i++)
		{
			for(int j = 0;j<DIMENSION*DIMENSION;j++)
				if(elements[i][j].getVal()==null)
					result += " - ";
				else
					result += " "+elements[i][j].getVal().toString()+" ";
			result += "\n";
		}
		return result;
	}
	
	
	/**
	 * Checks if the game has finished
	 * 
	 * @return true if the game has finished
	 */
	public boolean isSolved()
	{
		for(int i = 0;i<DIMENSION*DIMENSION;i++)
			for(int j = 0;j<DIMENSION*DIMENSION;j++)
				if(elements[i][j].getVal()==null)
					return false;
		return true;
	}
	
	
	/**
	 * Checks if a grid can be modified
	 * 
	 * @param r
	 *            the row number
	 * @param c
	 *            the column number
	 * @return true if it can be modified
	 */
	public boolean canModify(int r, int c)
	{
		if(elements[r][c] instanceof ModifiableElement)
			return true;
		return false;
	}
	
	
	/**
	 * Reset the game to its original state
	 * 
	 * Removes all user input on the board.
	 * The alphabet should not be affected, as well as the given values.
	 */
	public void reset()
	{
		for(int i = 0;i<DIMENSION*DIMENSION;i++)
			for(int j = 0;j<DIMENSION*DIMENSION;j++)
				if(elements[i][j] instanceof ModifiableElement)
					try
					{
						((ModifiableElement)elements[i][j]).setVal(null);
					}
					catch(OutOfBoundaryException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(DuplicateException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	}
}
