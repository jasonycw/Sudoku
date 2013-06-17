

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Tests {
	public boolean testCorrect1()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(0, i, (char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			} catch (ReadOnlyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	public boolean testCorrect2()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(i, 0, (char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			} catch (ReadOnlyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	public boolean testCorrect3()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(i/Board.DIMENSION, i%Board.DIMENSION, (char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			} catch (ReadOnlyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	public boolean testCorrect4()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(i, i,(char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			} catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		return true;
	}
	public boolean testCorrect5()
	{
		 int [][] grid = 
			 {
				 {5,3,4,6,7,8,9,1,2},
				 {6,7,2,1,9,5,3,4,8},
				 {1,9,8,3,4,2,5,6,7},
				 {8,5,9,7,6,1,4,2,3},
				 {4,2,6,8,5,3,7,9,1},
				 {7,1,3,9,2,4,8,5,6},
				 {9,6,1,5,3,7,2,8,4},
				 {2,8,7,4,1,9,6,3,5},
				 {3,4,5,2,8,6,1,7,9}
			 };
		 Board b = new Board();
		 for(int i=0; i<grid.length; i++)
			 for(int j=0; j<grid[i].length; j++)
				try {
					b.setVal(i, j, (char) ('0'+grid[i][j]));
				} catch (OutOfBoundaryException e) {
					e.printStackTrace();
					return false;
				} catch (DuplicateException e) {
					e.printStackTrace();
					return false;
					
				}catch (ReadOnlyException e) {
					e.printStackTrace();
					return false;
				}
		 System.out.println(b.toString());
		 return b.isSolved();
	}
	

	public boolean testIncorrect1()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(0, i,'1');
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				//e.printStackTrace();
				return true;
			}catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		return false;
	}
	public boolean testIncorrect2()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(0, i, '0');
			} catch (OutOfBoundaryException e) {
				//e.printStackTrace();
				return true;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			}catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		return false;
	}
	public boolean testIncorrect3()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(0, i, 
						(char) ('1'+Board.DIMENSION*Board.DIMENSION+1));
			} catch (OutOfBoundaryException e) {
				//e.printStackTrace();
				return true;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			}catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		return false;
	}
	public boolean testIncorrect4()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(0, i, (char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			}catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		try {
			b.setVal(0, 0, (char) ('0'+Board.DIMENSION*Board.DIMENSION));
		} catch (OutOfBoundaryException e) {
			e.printStackTrace();
			return false;
		} catch (DuplicateException e) {
			return (e.getSequence(0) instanceof Row);
		}catch (ReadOnlyException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public boolean testIncorrect5()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(i, 0, (char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			}catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		try {
			b.setVal(0, 0, (char) ('0'+Board.DIMENSION*Board.DIMENSION));
		} catch (OutOfBoundaryException e) {
			e.printStackTrace();
			return false;
		} catch (DuplicateException e) {
			return (e.getSequence(0) instanceof Column);
		}catch (ReadOnlyException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public boolean testIncorrect6()
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(i/Board.DIMENSION, i%Board.DIMENSION, (char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			}catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		try {
			b.setVal(0, 0, (char) ('0'+Board.DIMENSION*Board.DIMENSION));
		} catch (OutOfBoundaryException e) {
			e.printStackTrace();
			return false;
		} catch (DuplicateException e) {
			return (e.getSequence(0) instanceof Box);
		}catch (ReadOnlyException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public boolean testIncorrect7() 
	{
		Board b = new Board();
		try {
			//move.invoke(b,0, 0, 1);
			b.setVal(0, 0, '1');
			b.setVal(4, 1, '1');
			//move.invoke(b,4, 1, 1);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			b.setVal(4, 0, '1');
		}  catch (DuplicateException e) {
			
			int num = e.getNumConflicts();
			Set<Class> s = new HashSet<Class>();
			for(int i=0; i<num; i++)
			{
				Sequence sobj = e.getSequence(i);
				s.add(sobj.getClass());
			}
			return s.size() == 3;
			
		
		} catch (OutOfBoundaryException e) {
			e.printStackTrace();
			return false;
		}catch (ReadOnlyException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	public boolean testIO() throws IOException
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(i, 0,(char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			}catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		String path = "temp.o";
		
        
        FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(b);
		oos.close(); 
		fos.close();
		
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			Board newb = (Board) ois.readObject();
			for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
				if(newb.getVal(i, 0) != '0'+i+1)
					return false;
			
			return true;
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			return false;
		}
		finally{
			ois.close();
			fis.close();
//			new  File(path).delete();
		}
	}
	public boolean testManualIO() throws IOException
	{
		Board b = new Board();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			try {
				b.setVal(i, 0,(char) ('0'+i+1));
			} catch (OutOfBoundaryException e) {
				e.printStackTrace();
				return false;
			} catch (DuplicateException e) {
				e.printStackTrace();
				return false;
			}catch (ReadOnlyException e) {
				e.printStackTrace();
				return false;
			}
		String path = "temp.o";
		
        
        b.save(path);
		try {
			Board newb = new Board(path);
			for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
				if(newb.getVal(i, 0) != '0'+i+1)
					return false;
			
			return true;
		} catch (CorruptedSaveFileException e) {
			
			e.printStackTrace();
			return false;
		}finally
		{
//			new File(path).delete();
		}
		
	}
	
	public boolean testCandidate()
	{
		Board b = new Board();
		try {
			b.setVal(0, 1, '1');
			b.setVal(3, 2, '2');
			b.setVal(4, 3, '3');
			ArrayList<Character> cands = b.getCandidates(4, 1);
			if(cands.size() != 6)
			{
				System.out.println("cands.size() != 6   now=" + cands.size());
				return false;
			}
			ArrayList<Character> correct = new ArrayList<Character>();
			for(char i = '4'; i<='9'; i++)
				correct.add(i);
			
			//           i<6
			for(int i=0; i<correct.size(); i++)
				if(!cands.contains(correct.get(i)))
				{
					System.out.println("not contain: "+correct.get(i));
					return false;
				}
			return true;
		} catch (OutOfBoundaryException e) {

			e.printStackTrace();
			return false;
		} catch (DuplicateException e) {

			e.printStackTrace();
			return false;
		}catch (ReadOnlyException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean testChangeAlpha()
	{
		Board b = new Board();
		try {
			b.setVal(0, 1, '1');
			b.setVal(3, 2, '2');
			b.setVal(4, 3, '3');
			String newAlpha = "abcdefghi";
			
			b.setAlphabet(new Alphabet(newAlpha.toCharArray()));
			if(b.getVal(0, 1) != 'a')
			{
				System.out.println("getVal="+b.getVal(0,1));
				return false;
			}
			if(b.getVal(3, 2) != 'b')
			{
				System.out.println("getVal="+b.getVal(3,2));
				return false;
			}
			if(b.getVal(4, 3) != 'c')
			{
				System.out.println("getVal="+b.getVal(4,3));
				return false;
			}
			b.setVal(0, 2, 'd');

			try
			{
				b.setVal(0, 3, '5');
				System.out.println("should be out of boundary");
				return false;
			}catch(OutOfBoundaryException e)
			{
				return true;
			}
			
		} catch (OutOfBoundaryException e) {
			e.printStackTrace();
			System.out.println("out of boundary");
			return false;
		} catch (DuplicateException e) {
			e.printStackTrace();
			System.out.println("duplicated");
			return false;
		}catch (InvalidAlphabetException e) {
			e.printStackTrace();
			System.out.println("invalid");
			return false;
		}catch (ReadOnlyException e) {
			e.printStackTrace();
			System.out.println("read only");
			return false;
		}
	}
	
	public boolean testChangeAlphaIO() throws IOException
	{
		Board b = new Board();
		try {
			b.setVal(0, 1, '1');
			b.setVal(3, 2, '2');
			b.setVal(4, 3, '3');
			String newAlpha = "abcdefghi";
			
			b.setAlphabet(new Alphabet(newAlpha.toCharArray()));
			if(b.getVal(0, 1) != 'a')
				return false;
			if(b.getVal(3, 2) != 'b')
				return false;
			if(b.getVal(4, 3) != 'c')
				return false;
			b.setVal(0, 2, 'd');
				
			try
			{
				b.setVal(0, 3, '5');
				return false;
			}catch(OutOfBoundaryException e)
			{
				
			}
			
		} catch (OutOfBoundaryException e) {

			e.printStackTrace();
			return false;
		} catch (DuplicateException e) {

			e.printStackTrace();
			return false;
		}catch (InvalidAlphabetException e) {
			e.printStackTrace();
			return false;
		}catch (ReadOnlyException e) {
			e.printStackTrace();
			return false;
		}
		String path = "temp.o";
		
        
        FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(b);
		oos.close(); 
		fos.close();
		
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			Board newb = (Board) ois.readObject();
			if(newb.getVal(0, 1) != 'a')
				return false;
			if(newb.getVal(3, 2) != 'b')
				return false;
			if(newb.getVal(4, 3) != 'c')
				return false;
			if(newb.getVal(0, 2) != 'd')
				return false;
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		finally
		{
			ois.close();
			//new  File(path).delete();
		}
	}
	public boolean testChangeAlphaManualIO() throws IOException
	{
		Board b = new Board();
		try {
			b.setVal(0, 1, '1');
			b.setVal(3, 2, '2');
			b.setVal(4, 3, '3');
			String newAlpha = "abcdefghi";
			
			b.setAlphabet(new Alphabet(newAlpha.toCharArray()));
			if(b.getVal(0, 1) != 'a')
				return false;
			if(b.getVal(3, 2) != 'b')
				return false;
			if(b.getVal(4, 3) != 'c')
				return false;
			b.setVal(0, 2, 'd');
				
			try
			{
				b.setVal(0, 3, '5');
				return false;
			}catch(OutOfBoundaryException e)
			{
				
			}
			
		} catch (OutOfBoundaryException e) {

			e.printStackTrace();
			return false;
		} catch (DuplicateException e) {

			e.printStackTrace();
			return false;
		}catch (InvalidAlphabetException e) {
			e.printStackTrace();
			return false;
		}catch (ReadOnlyException e) {
			e.printStackTrace();
			return false;
		}
		String path = "temp.o";
		b.save(path);
		try {
			Board newb = new Board(path);
			if(newb.getVal(0, 1) != 'a')
				return false;
			if(newb.getVal(3, 2) != 'b')
				return false;
			if(newb.getVal(4, 3) != 'c')
				return false;
			if(newb.getVal(0, 2) != 'd')
				return false;
			return true;
		} catch (CorruptedSaveFileException e) {
			e.printStackTrace();
			return false;
		}
		finally
		{
//			ois.close();
//			new  File(path).delete();
		}
	}
	public boolean testTrivialSolver()
	{
		 int [][] grid = 
			 {
				 {5,3,4,6,7,8,9,1,2},
				 {6,7,2,1,9,5,3,4,8},
				 {1,9,8,3,4,2,5,6,7},
				 {8,5,9,7,6,1,4,2,3},
				 {4,2,6,8,5,3,7,9,1},
				 {7,1,3,9,2,4,8,5,6},
				 {9,6,1,5,3,7,2,8,4},
				 {2,8,7,4,1,9,6,3,5},
				 {3,4,5,2,8,6,1,7,9}
			 };
		 Board b = new Board();
		 for(int i=0; i<grid.length; i++)
			 for(int j=0; j<grid[i].length; j++)
				try {
					b.setVal(i, j, (char) ('0'+grid[i][j]));
				} catch (OutOfBoundaryException e) {
					e.printStackTrace();
					return false;
				} catch (DuplicateException e) {
					e.printStackTrace();
					return false;
					
				}catch (ReadOnlyException e) {
					e.printStackTrace();
					return false;
				}
		 for(int i=0; i<b.DIMENSION*b.DIMENSION; i++)
			 for(int j=0; j<b.DIMENSION*b.DIMENSION; j++)
				 if(b.getVal(i, j) == '1')
					try {
						b.setVal(i, j, null);
					} catch (OutOfBoundaryException e) {
						System.out.println("out of boundart");
						e.printStackTrace();
						return false;
					} catch (DuplicateException e) {
						System.out.println("duplicate");
						e.printStackTrace();
						return false;
					}catch (ReadOnlyException e) {
						System.out.println("read only");
						e.printStackTrace();
						return false;
					}
		 TrivialSolver ts = new TrivialSolver();
		 ts.trySolve(b);
		 
		 System.out.println(b.toString());
		 return b.isSolved();
	}
	
	public boolean testReadOnly()
	{
		int [][] grid = 
			 {
				 {5,3,4,6,7,8,9,1,2},
				 {6,7,2,1,9,5,3,4,8},
				 {1,9,8,3,4,2,5,6,7},
				 {8,5,9,7,6,1,4,2,3},
				 {4,2,6,8,5,3,7,9,1},
				 {7,1,3,9,2,4,8,5,6},
				 {9,6,1,5,3,7,2,8,4},
				 {2,8,7,4,1,9,6,3,5},
				 {3,4,5,2,8,6,1,7,9}
			 };
		Alphabet alp = new Alphabet();
		Character [][] table = new Character[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					table[i][j] = null;
				else 
					table[i][j] = alp.get(grid[i][j]-1);
			}
		Board b = new Board(table, alp);
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					try {
						b.setVal(i, j, b.getAlphabet().get(0));
					} catch (OutOfBoundaryException e) {
						e.printStackTrace();
						return false;
					} catch (DuplicateException e) {
						e.printStackTrace();
						return false;
					} catch (ReadOnlyException e) {
						e.printStackTrace();
						return false;
					}
				
			}
		
		return b.isSolved();
	}
	public boolean testReadOnly2()
	{
		int [][] grid = 
			 {
				 {5,3,4,6,7,8,9,1,2},
				 {6,7,2,1,9,5,3,4,8},
				 {1,9,8,3,4,2,5,6,7},
				 {8,5,9,7,6,1,4,2,3},
				 {4,2,6,8,5,3,7,9,1},
				 {7,1,3,9,2,4,8,5,6},
				 {9,6,1,5,3,7,2,8,4},
				 {2,8,7,4,1,9,6,3,5},
				 {3,4,5,2,8,6,1,7,9}
			 };
		Alphabet alp = new Alphabet();
		Character [][] table = new Character[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					table[i][j] = null;
				else 
					table[i][j] = alp.get(grid[i][j]-1);
			}
		Board b = new Board(table,alp);
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] != 1)
					try {
						if(b.canModify(i, j))
							return false;
						b.setVal(i, j, b.getAlphabet().get(0));
						return false;
					} catch (OutOfBoundaryException e) {
						e.printStackTrace();
						return false;
					} catch (DuplicateException e) {
						e.printStackTrace();
						return false;
					} catch (ReadOnlyException e) {
						//e.printStackTrace();
						//return false;
					}
				
			}
		return true;
	}
	public boolean testReadOnlyIO() throws IOException, ClassNotFoundException
	{
		int [][] grid = 
			 {
				 {5,3,4,6,7,8,9,1,2},
				 {6,7,2,1,9,5,3,4,8},
				 {1,9,8,3,4,2,5,6,7},
				 {8,5,9,7,6,1,4,2,3},
				 {4,2,6,8,5,3,7,9,1},
				 {7,1,3,9,2,4,8,5,6},
				 {9,6,1,5,3,7,2,8,4},
				 {2,8,7,4,1,9,6,3,5},
				 {3,4,5,2,8,6,1,7,9}
			 };
		Alphabet alp = new Alphabet();
		Character [][] table = new Character[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					table[i][j] = null;
				else 
					table[i][j] = alp.get(grid[i][j]-1);
			}
		Board b = new Board(table,alp);
		String path = "temp.o";
		
        
        FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(b);
		oos.close(); 
		fos.close();
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		b = (Board) ois.readObject();
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					try {
						b.setVal(i, j, b.getAlphabet().get(0));
						//return false;
					} catch (OutOfBoundaryException e) {
						e.printStackTrace();
						return false;
					} catch (DuplicateException e) {
						e.printStackTrace();
						return false;
					} catch (ReadOnlyException e) {
						//e.printStackTrace();
						return false;
					}
				
			}
//		new File(pathte();
		ois.close();
		return true;
	}
	public boolean testReadOnlyManualIO() throws IOException, ClassNotFoundException, CorruptedSaveFileException
	{
		int [][] grid = 
			 {
				 {5,3,4,6,7,8,9,1,2},
				 {6,7,2,1,9,5,3,4,8},
				 {1,9,8,3,4,2,5,6,7},
				 {8,5,9,7,6,1,4,2,3},
				 {4,2,6,8,5,3,7,9,1},
				 {7,1,3,9,2,4,8,5,6},
				 {9,6,1,5,3,7,2,8,4},
				 {2,8,7,4,1,9,6,3,5},
				 {3,4,5,2,8,6,1,7,9}
			 };
		Alphabet alp = new Alphabet();
		Character [][] table = new Character[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					table[i][j] = null;
				else 
					table[i][j] = alp.get(grid[i][j]-1);
			}
		Board b = new Board(table,alp);
		String path = "temp.o";
		b.save(path);
        
		b = new Board(path);
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					try {
						b.setVal(i, j, b.getAlphabet().get(0));
						//return false;
					} catch (OutOfBoundaryException e) {
						e.printStackTrace();
						return false;
					} catch (DuplicateException e) {
						e.printStackTrace();
						return false;
					} catch (ReadOnlyException e) {
						//e.printStackTrace();
						return false;
					}
				
			}
//		new File(pathte();
		return true;
	}
	
	public boolean testReset()
	{
		int [][] grid = 
			 {
				 {5,3,4,6,7,8,9,1,2},
				 {6,7,2,1,9,5,3,4,8},
				 {1,9,8,3,4,2,5,6,7},
				 {8,5,9,7,6,1,4,2,3},
				 {4,2,6,8,5,3,7,9,1},
				 {7,1,3,9,2,4,8,5,6},
				 {9,6,1,5,3,7,2,8,4},
				 {2,8,7,4,1,9,6,3,5},
				 {3,4,5,2,8,6,1,7,9}
			 };
		Alphabet alp = new Alphabet();
		Character [][] table = new Character[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					table[i][j] = null;
				else 
					table[i][j] = alp.get(grid[i][j]-1);
			}
		Board b = new Board(table,alp);
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
					try {
						b.setVal(i, j, b.getAlphabet().get(0));
					} catch (OutOfBoundaryException e) {
						e.printStackTrace();
						return false;
					} catch (DuplicateException e) {
						e.printStackTrace();
						return false;
					} catch (ReadOnlyException e) {
						e.printStackTrace();
						return false;
					}
				
			}
		System.out.println("Start reset");
		b.reset();
		for(int i=0; i<grid.length;i++)
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j] == 1)
				{
					if(b.getVal(i, j) != null)
					{
						System.out.println(i+" "+j+" val="+b.getVal(i, j));
						return false;
					}
					if(b.getCandidates(i, j).size() != 1)
					{
						System.out.println(i+" "+j+" size="+b.getCandidates(i, j).size());
						return false;
					}
				}	
				
			}
		return true;
	}
	
	public boolean testAlphabet1()
	{
		Alphabet a = new Alphabet();
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			if(!a.get(i).equals((char)('1'+i)))
					return false;
		return true;
	}
	public boolean testAlphabet2() throws InvalidAlphabetException
	{
		String input = "abcdefghi";
		Alphabet a = new Alphabet(input.toCharArray());
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
			if(!a.get(i).equals((char)('a'+i)))
			{
				System.out.println(a.get(i));
				return false;
			}
		return true;
	}
	
	public boolean testAlphabet3()
	{
		String input = "a";
		try {
			Alphabet a = new Alphabet(input.toCharArray());
			return false;
		} catch (InvalidAlphabetException e) {
			return true;
		}
		
	}
	public boolean testAlphabet4()
	{
		String input = "abcdefgha";
		try {
			Alphabet a = new Alphabet(input.toCharArray());
			return false;
		} catch (InvalidAlphabetException e) {
			return true;
		}
		
	}
	public boolean testAlphabet5() throws InvalidAlphabetException
	{
		String input = "abcdefghi";
		Alphabet a = new Alphabet(input.toCharArray());
		if(!a.isValidChar('e'))
			return false;
		if(a.isValidChar('1'))
			return false;
		return a.getPosition('d') == 3;
	}
	public boolean testBox()
	{
		Box box = new Box(0,1);
		if(box.getX() != 0)
			return false;
		if(box.getY() != 1)
			return false;
		return true;
	}
	
	public boolean testColumn()
	{
		Column col = new Column(1);
		return col.getColId() == 1;
	}
	public boolean testRow()
	{
		Row col = new Row(1);
		return col.getRowId() == 1;
	}
	
	public boolean testElement()
	{
		Alphabet alp = new Alphabet();
		Box box = new Box(0,1);
		Row row = new Row(0);
		Column col = new Column(3);
		Element ele = new Element(0,3,'1',box, row, col, alp);
		if(ele.getVal() != '1')
			return false;
		if(ele.getX() != 0)
			return false;
		if(ele.getY() != 3)
			return false;
		return true;
	}
	
	public boolean testModifiableElement1() throws OutOfBoundaryException, DuplicateException
	{
		Alphabet alp = new Alphabet();
		Box [][] boxes = new Box[Board.DIMENSION][Board.DIMENSION];
		Row [] rows = new Row[Board.DIMENSION*Board.DIMENSION];
		Column [] cols = new Column[Board.DIMENSION*Board.DIMENSION];
		Element[][] elements = new Element[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
		{
			boxes[i/3][i%3]= new Box(i/3, i%3);
			rows[i] = new Row(i);
			cols[i] = new Column(i);
			
		}
		
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
		{
			for(int j=0; j<Board.DIMENSION*Board.DIMENSION; j++)
			{
				elements[i][j] = new ModifiableElement(i, j, boxes[i/3][j/3], rows[i], cols[j],alp);
			}
			
		}
		
		((ModifiableElement) elements[0][0]).setVal('1');
		return elements[0][0].getVal() == '1';
	}
	public boolean testModifiableElement2() 	
	{
		Alphabet alp = new Alphabet();
		Box [][] boxes = new Box[Board.DIMENSION][Board.DIMENSION];
		Row [] rows = new Row[Board.DIMENSION*Board.DIMENSION];
		Column [] cols = new Column[Board.DIMENSION*Board.DIMENSION];
		Element[][] elements = new Element[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
		{
			boxes[i/3][i%3]= new Box(i/3, i%3);
			rows[i] = new Row(i);
			cols[i] = new Column(i);
			
		}
		
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
		{
			for(int j=0; j<Board.DIMENSION*Board.DIMENSION; j++)
			{
				elements[i][j] = new ModifiableElement(i, j, boxes[i/3][j/3], rows[i], cols[j], alp);
			}
			
		}
		try {
			((ModifiableElement) elements[0][0]).setVal((char) ('0'+Board.DIMENSION*Board.DIMENSION+1));
			return false;
		} catch (OutOfBoundaryException e) {
			
			return true;
		} catch (DuplicateException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean testModifiableElement3() throws OutOfBoundaryException, DuplicateException 	{
		Alphabet alp = new Alphabet();
		Box [][] boxes = new Box[Board.DIMENSION][Board.DIMENSION];
		Row [] rows = new Row[Board.DIMENSION*Board.DIMENSION];
		Column [] cols = new Column[Board.DIMENSION*Board.DIMENSION];
		Element[][] elements = new Element[Board.DIMENSION*Board.DIMENSION][Board.DIMENSION*Board.DIMENSION];
		
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
		{
			boxes[i/3][i%3]= new Box(i/3, i%3);
			rows[i] = new Row(i);
			cols[i] = new Column(i);
			
		}
		
		for(int i=0; i<Board.DIMENSION*Board.DIMENSION; i++)
		{
			for(int j=0; j<Board.DIMENSION*Board.DIMENSION; j++)
			{
				elements[i][j] = new ModifiableElement(i, j, boxes[i/3][j/3], rows[i], cols[j],alp);
			}
			
		}
		((ModifiableElement)elements[2][5]).setVal('1');
		((ModifiableElement)elements[4][3]).setVal('2');
		((ModifiableElement)elements[0][0]).setVal('1');
		try {
			
			((ModifiableElement)elements[0][3]).setVal('1');
			return false;
		} catch (OutOfBoundaryException e) {
			
			return false;
		} catch (DuplicateException e) {
			if(e.getNumConflicts() != 2)
				return false;
			for(int i=0; i<2; i++)
			{
				if(e.getSequence(i) instanceof Box)
					if(!(e.getConflictElement(i).getX() == 2) ||
					!(e.getConflictElement(i).getY() == 5))
						return false;
				if(e.getSequence(i) instanceof Column)
					if(!(e.getConflictElement(i).getX() == 4) ||
					!(e.getConflictElement(i).getY() == 3))
						return false;
			}
		}
		
		return true;
	}
}
