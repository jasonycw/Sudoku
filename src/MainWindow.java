/**
 *
 * Name: Yu Chun Wah
 * Student No: 52633870
 * 
 */

/*
 * Copyright 2007 Vagelis Giannadakis
 * 
 * This file is part of VG Sudoku.
 *
 * VG Sudoku is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * VG Sudoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with VG Sudoku; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.swing.BorderFactory;
//import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;


public class MainWindow extends JFrame implements PuzzleGrid.NumberKeyListener {

	private static final long serialVersionUID = 4819764369631103591L;
	
	private Board puzzle = new Board();
	
	private PuzzleGrid puzzleGrid = new PuzzleGrid(puzzle);
	
//	private List<PuzzleDescriptor> pDescs = null;
	
	private JFileChooser fileChooser = new JFileChooser();
	
	private FileFilter pDescFileFilter, puzzleFileFilter;
	
	private Random random = null;
	
	private JMenuItem rndPuzzleBtn, gotoPuzzleBtn, setGivensBtn, showCandsBtn, showHideCandsBtn;
	
	private JPanel numBtnPnl = new JPanel();
	private JButton [] numButtons = new JButton[9];
//	private HideCandidatesWidget hideCandsPad = new HideCandidatesWidget(puzzle, puzzleGrid);
	
//	private ChooseColorsDialog ccDialog = new ChooseColorsDialog(this, puzzleGrid, hideCandsPad);
	
	private MainWindow(Properties properties) {
		// Configure UI
		setTitle("VG Sudoku");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				saveConfiguration();
				System.exit(0);
			}
		});
		makeUI();
		pack();
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		
		// Center on screen
		DisplayMode dm = getGraphicsConfiguration().getDevice().getDisplayMode();
		Dimension size = getSize();
		//setLocation((dm.getWidth() - size.width) / 2, (dm.getHeight() - size.height) / 2);
		setLocation(200, 200);
		
		// Other configuration
		puzzleGrid.addNumberKeyListener(this);
		
		// Create custom file filters
		// Puzzle descriptors
		pDescFileFilter = new FileFilter() {
			public String getDescription() { return "Sudoku text files"; }
			public boolean accept(File f) { return f.isDirectory() || f.getName().endsWith(".txt"); }
		};
		// VG-Sudoku puzzles
		puzzleFileFilter = new FileFilter() {
			public String getDescription() { return "VG Sudoku saved puzzles"; }
			public boolean accept(File f) { return f.isDirectory() || f.getName().endsWith(".ser"); }
		};
		
		// Apply saved configuration
//		if (properties != null)
//			loadConfiguration(properties);
	}
	
	
	public static void main(String[] args) {
		// Read stored configuration from user's home
		Properties props = null;
		File cFile = new File(System.getProperty("user.home"), "VG-Sudoku.conf");
		if (cFile.exists()) {
			props = new Properties();
			InputStream in = null;
			try {
				try {
					in = new FileInputStream(cFile);
					props.load(in);
				}
				finally {
					in.close();
				}
			}
			catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
		
		MainWindow win = new MainWindow(props);
		win.setVisible(true);
		
//		Map<Object, Object> uid = UIManager.getDefaults();
//		for (Object k : uid.keySet())
//			System.out.println(k.toString() + " = " + String.valueOf(uid.get(k)));
	}
	

	public void setVisible(boolean b) {
		super.setVisible(b);
		puzzleGrid.requestFocusInWindow();
	}
	
	
	private void makeUI() {
		// == Menu bar ==
		JMenuBar mBar = new JMenuBar();
		JMenuItem mItem;
		JMenu menu;
		
		// == File menu ==
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		// New
		mItem = new JMenuItem("New");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newPuzzle();
			}
		});
		menu.add(mItem);
		// Restart
		mItem = new JMenuItem("Restart");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartPuzzle();
			}
		});
		menu.add(mItem);
		// Open...
		mItem = new JMenuItem("Open...");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openPuzzle();
			}
		});
		menu.add(mItem);
		// Save...
		mItem = new JMenuItem("Save...");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePuzzle();
			}
		});
		menu.add(mItem);
		menu.addSeparator();
//		// Load descriptor file...
//		mItem = new JMenuItem("Load descriptor file...");
//		mItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				loadDescriptorFile();
//			}
//		});
//		menu.add(mItem);
//		// Random puzzle
		mItem = new JMenuItem("Help me");
		//rndPuzzleBtn.setEnabled(false);
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        solve();
			}
		});
		menu.add(mItem);
		
		menu.addSeparator();
		rndPuzzleBtn = new JMenuItem("Random puzzle");
		//rndPuzzleBtn.setEnabled(false);
		rndPuzzleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showRandomPuzzle();
			}
		});
		menu.add(rndPuzzleBtn);
//		// Goto puzzle
//		gotoPuzzleBtn = new JMenuItem("Go to puzzle...");
//		gotoPuzzleBtn.setEnabled(false);
//		gotoPuzzleBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				showPuzzleByNumber();
//			}
//		});
//		menu.add(gotoPuzzleBtn);
		menu.addSeparator();
		// Exit
		mItem = new JMenuItem("Exit");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfiguration();
				System.exit(0);
			}
		});
		menu.add(mItem);
		mBar.add(menu);

		// == Options menu ==
		menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_O);
		
		mItem = new JMenuItem("Change character set");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCharWindow();
			}
		});
		menu.add(mItem);
		menu.addSeparator();
		// Set givens
//		setGivensBtn = new JCheckBoxMenuItem("Set givens", false);
//		menu.add(setGivensBtn);
//		menu.addSeparator();
		// Show candidates
		showCandsBtn = new JCheckBoxMenuItem("Show candidates", false);
		showCandsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzleGrid.setShowCandidates(showCandsBtn.isSelected());
			}
		});
		menu.add(showCandsBtn);
		// Show selected Row / Column / Box
		mItem = new JCheckBoxMenuItem("Show selected Row / Column / Box", false);
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzleGrid.setShowRowColumnBox(((JCheckBoxMenuItem)e.getSource()).isSelected());
			}
		});
		menu.add(mItem);
		mBar.add(menu);
		
		// == View ==
		menu = new JMenu("View");
		menu.setMnemonic(KeyEvent.VK_V);
		// Number buttons
		mItem = new JCheckBoxMenuItem("Number buttons", false);
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBtnPnl.setVisible(((JCheckBoxMenuItem)e.getSource()).isSelected());
			}
		});
		menu.add(mItem);
		// Hide candidates pad
//		showHideCandsBtn = new JCheckBoxMenuItem("Candidate hide pad", false);
//		showHideCandsBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
////				hideCandsPad.setVisible(showHideCandsBtn.isSelected());
//				pack();
//			}
//		});
//		menu.add(showHideCandsBtn);
//		menu.addSeparator();
//		// Choose colors...
//		mItem = new JMenuItem("Choose colors...");
//		mItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
////				ccDialog.setVisible(true);
//			}
//		});
//		menu.add(mItem);
		mBar.add(menu);
		
		// == Add MenuBar to window
		setJMenuBar(mBar);
		
		// == Components ==
		Container cp = getContentPane();
		
		// Top of window
		numBtnPnl.setVisible(false);
		numBtnPnl.setLayout(new BoxLayout(numBtnPnl, BoxLayout.X_AXIS));
		numBtnPnl.add(javax.swing.Box.createGlue());
		for (int i = 0; i < 9; i++) {
			final JButton btn = new JButton(""+puzzle.getAlphabet().get(i));
			//final Integer number = new Integer(i + 1);
			final Character number = puzzle.getAlphabet().get(i);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numberKeyPressed(btn.getText().charAt(0), puzzleGrid.getSelectedRow(), puzzleGrid.getSelectedColumn());
					puzzleGrid.requestFocusInWindow();
					repaint();
					if(puzzle.isSolved())
						JOptionPane.showMessageDialog(MainWindow.this, "Puzzle solved!");
				}
			});
			numBtnPnl.add(btn);
			numButtons[i] = btn;
		}
		numBtnPnl.add(javax.swing.Box.createGlue());
		numBtnPnl.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
		cp.add(numBtnPnl, BorderLayout.NORTH);
		
		// Center of window
		JPanel compPnl = new JPanel(new GridBagLayout());
		compPnl.add(puzzleGrid, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(12, 12, 12, 12), 0, 0));
//		hideCandsPad.setVisible(false);
//		compPnl.add(hideCandsPad, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//				GridBagConstraints.NONE, new Insets(12, 0, 0, 12), 0, 0));
		cp.add(compPnl, BorderLayout.CENTER);
	}
	
	
/**
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
	//TODO make the solver works
	protected void solve() {
//		ISolver solver = new TrivialSolver();
//		solver.trySolve(puzzle);
//		repaint();
		
		ISolver solver = new CompleteSolver();
		solver.trySolve(puzzle);
		repaint();
	}
/**
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */

	private void updateNumButtons()
	{
		for(int i=0; i<9; i++)
		{
			numButtons[i].setText(""+puzzle.getAlphabet().get(i));
//			numButtons[i].removeActionListener(l)
		}
	}
	protected void showCharWindow() {
		String cur = "";
		for(int i=0; i<9; i++)
			cur += puzzle.getAlphabet().get(i);
		String please = "Please provide a new character set";
		String info = please;
		while(true)
		{
			String input = JOptionPane.showInputDialog
						(info,cur);
			if(input == null) break;
			try {
				puzzle.setAlphabet(new Alphabet(input.toCharArray()));
				break;
			} catch (InvalidAlphabetException e) {
				info = please + "\nThe character set is invalid";
			}
		}
		updateNumButtons();
		repaint();
	}


	public void numberKeyPressed(Character number, int row, int column) {
		DuplicateException nc=null;
		OutOfBoundaryException obe = null;
//		if (setGivensBtn.isSelected())
//		{
//		//	nc = puzzle.setGiven(number, row, column);
//		}
//		else
		try
		{
			puzzle.setVal( row, column, number);
			
		}catch(DuplicateException e)
		{
			nc = e;
		}
		catch(OutOfBoundaryException e)
		{
			obe = e;
		} catch (ReadOnlyException e) {
			// TODO Implement this part!
			//e.printStackTrace();
		}
		if (nc != null || obe != null) {
			
			StringBuffer sb = new StringBuffer("One or more number conflicts occured:");
			if(nc == null)
				sb = new StringBuffer("");
			for(int i=0; nc != null && i<nc.getNumConflicts(); i++)
			{
				Sequence seq = nc.getSequence(i);
				Element ele = nc.getConflictElement(i);
				if(seq instanceof Row)
				{
					sb.append("\nAt Row ").append(((Row)seq).getRowId()).append(" (").append(ele.getY()).append(")");
				}
				else if(seq instanceof Column)
					sb.append("\nAt Column ").append(((Column)seq).getColId()).append(" (").append(ele.getX()).append(")");
				else if(seq instanceof Box) 
					sb.append("\nAt Box ").append(((Box)seq).getX()).append(", ").append(((Box)seq).getY()).
					append(" (").append(ele.getX()%3).append(", ").append(ele.getY()%3).append(")");
	
			}
			if(obe != null)
				sb.append("Invalid input");
//			if (nc.isRowConflict())
//				sb.append("\nAt Row ").append(nc.getRow()).append(" (").append(nc.getRowIndex()).append(")");
//			if (nc.isColumnConflict())
//				sb.append("\nAt Column ").append(nc.getColumn()).append(" (").append(nc.getColumnIndex()).append(")");
//			if (nc.isBoxConflict())
//				sb.append("\nAt Box ").append(nc.getBoxRow()).append(", ").append(nc.getBoxColumn()).
//					append(" (").append(nc.getBoxRowIndex()).append(", ").append(nc.getBoxColumnIndex()).append(")");
			JOptionPane.showMessageDialog(this, sb.toString(), "Number Conflict", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
	private void useNewPuzzle(Board newPuzzle) {
		puzzle = newPuzzle;
		puzzleGrid.setPuzzle(newPuzzle);
		//hideCandsPad.setPuzzle(newPuzzle);
	}
	
	
	private void newPuzzle() {
		useNewPuzzle(new Board());
		setTitle("VG Sudoku");
	}
	
	
	private void restartPuzzle() {
		//PuzzleDescriptor pd = puzzle.getPuzzleDescriptor();
		//Board p = new Board();
//		for (PuzzleDescriptor.Entry entry : pd)
//			p.setGiven(entry.getNumber(), entry.getRow(), entry.getColumn());
		//useNewPuzzle(p);
		puzzle.reset();
		repaint();
	}
	
	
	private void showRandomPuzzle() {
//		if (random == null)
//			random = new Random(System.currentTimeMillis());
//		
//		int rndNum = random.nextInt(pDescs.size());
////		PuzzleDescriptor pd = pDescs.get(rndNum);
////		Puzzle p = new Puzzle();
////		for (PuzzleDescriptor.Entry entry : pd)
////			p.setGiven(entry.getNumber(), entry.getRow(), entry.getColumn());
//		
//		setTitle("VG Sudoku - " + rndNum);
		//useNewPuzzle(p);
		Board newb = new Board(RandBoardGen.gen(puzzle.getAlphabet()), puzzle.getAlphabet());
		newb.setAlphabet(puzzle.getAlphabet());
		useNewPuzzle(newb);
	}
	
	
//	private void showPuzzleByNumber() {
//		String numStr = JOptionPane.showInputDialog(this, "Select a puzzle number from 1 to " + pDescs.size());
//		try {
//			int pnum = Integer.parseInt(numStr);
//			if (pnum < 1 || pnum > pDescs.size())
//				JOptionPane.showMessageDialog(this, "Please type a number from 1 to " + pDescs.size());
//			
//			PuzzleDescriptor pd = pDescs.get(pnum);
//			Board p = new Board();
////			for (PuzzleDescriptor.Entry entry : pd)
////				p.setGiven(entry.getNumber(), entry.getRow(), entry.getColumn());
//			
//			setTitle("VG Sudoku - " + pnum);
//			useNewPuzzle(p);
//		}
//		catch (NumberFormatException ex) {
//			JOptionPane.showMessageDialog(this, "Please type a number from 1 to " + pDescs.size());
//		}
//	}
	
	
//	private void loadDescriptorFile() {
//		// Configure and open file chooser
//		fileChooser.setFileFilter(pDescFileFilter);
//		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//			final File pFile = fileChooser.getSelectedFile();
//			
//			// If the file does not exist, show message and return
//			if (!pFile.exists()) {
//				JOptionPane.showMessageDialog(this, "The file " + pFile.getName() + " was not found!",
//						"File not found", JOptionPane.WARNING_MESSAGE);
//				return;
//			}
//			
//			// Display load dialog
//			final LoadDialog lDialog = new LoadDialog();
//			lDialog.setCount((int)(pFile.length() / 82));
//			lDialog.setVisible(true);
//			
//			// Display working cursor
//			setCursor(new Cursor(Cursor.WAIT_CURSOR));
//			
//			// Do work with another thread
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						if (pDescs == null)
//							pDescs = new ArrayList<PuzzleDescriptor>();
//						else
//							pDescs.clear();
//						BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pFile), "iso-8859-1"));
//						int count = 0;
//						String line;
//						while ((line = reader.readLine()) != null) {
//							if (line.length() == 81) {
//								PuzzleDescriptor pd = new PuzzleDescriptor();
//								char c;
//								for (int i = 0; i < 81; i++) {
//									c = line.charAt(i);
//									if (c != '0')
//										pd.putGiven(c - '0', i / 9, i % 9);
//								}
//								pDescs.add(pd);
//							}
//							if (++count % 100 == 0) {
//								final int c = count;
//								SwingUtilities.invokeLater(new Runnable() {
//									public void run() { lDialog.setCurrent(c); };
//								});
//							}
//						}
//						reader.close();
//					}
//					catch (Exception ex) {
//						ex.printStackTrace(System.err);
//					}
//					finally {
//						SwingUtilities.invokeLater(new Runnable() {
//							public void run() {
//								lDialog.setVisible(false);
//								rndPuzzleBtn.setEnabled(true);
//								gotoPuzzleBtn.setEnabled(true);
//								setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//							};
//						});
//					}
//				}
//			}).start();
//		}
//	}
	
	
	private void savePuzzle() {
		fileChooser.setFileFilter(puzzleFileFilter);
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File pFile = fileChooser.getSelectedFile();
			if (!pFile.getName().endsWith(".ser"))
				pFile = new File(pFile.getParentFile(), pFile.getName() + ".ser");
			
			// If file exists, ask before overwriting
			boolean goOn = true;
			if (pFile.exists())
				goOn = JOptionPane.showConfirmDialog(this,
						"The file " + pFile.getName() + " already exists, overwrite?", "File already exists",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			
			// If all is OK, go on with save
			if (goOn) {
				try {
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pFile));
					try {
						out.writeObject(puzzle);
					}
					finally {
						out.close();
					}
				}
				catch (IOException ex) {
					ex.printStackTrace(System.err);
				}
			}
		}
	}
	
	
	private void openPuzzle() {
		fileChooser.setFileFilter(puzzleFileFilter);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File pFile = fileChooser.getSelectedFile();
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(pFile));
				try {
					Board p = (Board)in.readObject();
					setTitle("VG Sudoku - " + pFile.getName());
					useNewPuzzle(p);
				}
				finally {
					in.close();
				}
			}
			catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}
	
	
	private void loadConfiguration(Properties props) {
		// Apply colors
		Properties cProps = getSubProperties(props, "colors");
		if (cProps != null) {
			Color c;
			c = Color.decode("0x" + cProps.getProperty("grid"));
			puzzleGrid.setGridColor(c);
			c = Color.decode("0x" + cProps.getProperty("givens"));
			puzzleGrid.setGivenColor(c);
			c = Color.decode("0x" + cProps.getProperty("candidates"));
			puzzleGrid.setCandColor(c);
			c = Color.decode("0x" + cProps.getProperty("numbers"));
			puzzleGrid.setNumberColor(c);
			c = Color.decode("0x" + cProps.getProperty("selected-cell"));
			puzzleGrid.setSelColor(c);
		}
		
		// Set current path on FileChooser
		String fcPath = props.getProperty("files.current-path");
		if (fcPath != null && !fcPath.equals(""))
			fileChooser.setCurrentDirectory(new File(fcPath));
	}
	
	
	private void saveConfiguration() {
		// Properties object to hold configuration
		Properties props = new Properties();
		
		// Save colors
		props.setProperty("colors.grid", Integer.toHexString(puzzleGrid.getGridColor().getRGB() & 0x00ffffff));
		props.setProperty("colors.givens", Integer.toHexString(puzzleGrid.getGivenColor().getRGB() & 0x00ffffff));
		props.setProperty("colors.candidates", Integer.toHexString(puzzleGrid.getCandColor().getRGB() & 0x00ffffff));
		props.setProperty("colors.numbers", Integer.toHexString(puzzleGrid.getNumberColor().getRGB() & 0x00ffffff));
		props.setProperty("colors.selected-cell", Integer.toHexString(puzzleGrid.getSelColor().getRGB() & 0x00ffffff));
		
		// Save FileChooser's current path
		props.setProperty("files.current-path", fileChooser.getCurrentDirectory().getAbsolutePath());
		
		// Save configuration to file
		File cFile = new File(System.getProperty("user.home"), "VG-Sudoku.conf");
		try {
			OutputStream out = null;
			try {
				out = new FileOutputStream(cFile);
				props.store(out, "VG-Sudoku configuration");
			}
			finally {
				out.close();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
	}
	
	
	private Properties getSubProperties(Properties props, String prefix) {
		if (props == null)
			return null;
		
		Properties subProps = new Properties();
		int pLen = prefix.length();
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			String key = (String)entry.getKey();
			if (key.startsWith(prefix))
				subProps.setProperty(key.substring(pLen + 1), (String)entry.getValue());
		}
		return subProps;
	}
	
	
	@SuppressWarnings("serial")
	private class LoadDialog extends JWindow {
		
		private JLabel lbl = new JLabel("Initializing...");
		
		private JProgressBar bar = new JProgressBar();
		
		private LoadDialog() {
			super(MainWindow.this);
			bar.setIndeterminate(true);
			JPanel pnl = new JPanel();
			pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
			pnl.add(lbl);
			pnl.add(bar);
			pnl.setBorder(BorderFactory.createRaisedBevelBorder());
			this.add(pnl);
			this.pack();
			
			// Center on parent
			Dimension
				pSize = MainWindow.this.getSize(),
				size = getSize();
			Point pLocation = MainWindow.this.getLocation();
			setLocation(pLocation.x + (pSize.width - size.width) / 2, pLocation.y + (pSize.height - size.height) / 2);
		}
		
		private void setCount(int count) {
			bar.setIndeterminate(false);
			bar.setMaximum(count);
			lbl.setText("Loading...");
		}
		
		private void setCurrent(int current) {
			bar.setValue(current);
			lbl.setText("Loading " + current + " of " + bar.getMaximum());
		}
		
	}
	
	
}
