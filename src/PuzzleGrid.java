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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.Timer;


public class PuzzleGrid extends JComponent  {
	
	private static final long serialVersionUID = -37788047513964036L;
	
	private Board puzzle = null;
	
	private int selRow = 0, selColumn = 0;
	
	private Font
		numFont = Font.decode("SansSerif"),
		confNumFont = Font.decode("SansSerif");
	
	private Dimension size, oldSize = new Dimension();
	
	private Insets insets;
	
	private Set<NumberKeyListener> nkListeners = new HashSet<NumberKeyListener>();
	
	private Set<SelectionListener> selListeners = new HashSet<SelectionListener>();
	
	private boolean
		showCandidates = false,
		showRowColumnBox = false,
		showNonCandidatesMask = false;
	
	private JPopupMenu cellPopup = new JPopupMenu();
	
	private JMenuItem header = new JMenuItem("Exclude..");
	
	private float[] hsbVals = new float[3];
	
	private Color
		gridColor = new Color(0, 0, 0),
		givenColor = new Color(91, 91, 40),
		numberColor = new Color(6, 90, 184),
		candColor = new Color(6, 90, 184),
		selColor = new Color(153, 153, 153),
		sel2Color, sel3Color;
	
	private Rectangle
		tempRect = new Rectangle(),
		clip = new Rectangle();
	
	private Area tempArea = new Area();
	
	private Timer nonCandsTimer;
	

	public PuzzleGrid(Board puzzle) {
		super();

		// Keep passed Puzzle and NumberKeyListener
		if (puzzle == null)
			throw new NullPointerException("puzzle is null");
		this.puzzle = puzzle;
		
		// Popup header MenuItem is just a marker
		header.setEnabled(false);
		
		// Register as Listener with the Puzzle
		//puzzle.addPuzzleListener(this);
		
		// Add mouse listener
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				PuzzleGrid.this.mousePressed(e);
			}
			public void mouseReleased(MouseEvent e) {
				PuzzleGrid.this.mouseReleased(e);
			}
		});
		
		// Add keyboard listener
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				PuzzleGrid.this.keyPressed(e);
			}
		});
		
		// Initial Row / Column / Box color
		Color.RGBtoHSB(selColor.getRed(), selColor.getGreen(), selColor.getBlue(), hsbVals);
		sel2Color = new Color(Color.HSBtoRGB(hsbVals[0], hsbVals[1] * 0.8F, hsbVals[2] * 0.85F) & 0x80ffffff, true);
		sel3Color = new Color(Color.HSBtoRGB(hsbVals[0], hsbVals[1] * 1.5F, hsbVals[2]) & 0xaaffffff, true);
		
		// Default background color is white
		setBackground(Color.WHITE);
		
		// Configure nonCandsTimer
		nonCandsTimer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showNonCandidatesMask = true;
				repaint();
			}
		});
		nonCandsTimer.setRepeats(false);
	}
	
	
	public void addNumberKeyListener(NumberKeyListener nkl) {
		nkListeners.add(nkl);
	}
	
	
	public void removeNumberKeyListener(NumberKeyListener nkl) {
		nkListeners.remove(nkl);
	}
	
	
	public void addSelectionListener(SelectionListener ls) {
		selListeners.add(ls);
		ls.selectionChanged(selRow, selColumn);
	}
	
	
	public void removeSelectionListener(SelectionListener ls) {
		selListeners.remove(ls);
	}
	
	
	public void puzzleChanged() {
		repaint();
	}
	
	
	public void setPuzzle(Board newPuzzle) {
		// Change current puzzle
		//puzzle.removePuzzleListener(this);
		//newPuzzle.addPuzzleListener(this);
		puzzle = newPuzzle;
		repaint();
		
		// Notify selection listeners
		notifySelectionListeners();
	}
	
	
	public void setShowCandidates(boolean mode) {
		boolean oldMode = showCandidates;
		showCandidates = mode;
		if (oldMode != mode)
			repaint();
	}
	
	
	public void setShowRowColumnBox(boolean mode) {
		boolean oldMode = showRowColumnBox;
		showRowColumnBox = mode;
		if (oldMode != mode)
			repaint();
	}


	public int getSelectedRow() {
		return selRow;
	}
	
	
	public int getSelectedColumn() {
		return selColumn;
	}
	
	
	public Dimension getMinimumSize() {
		return new Dimension(360, 360);
	}
	
	
	public Dimension getPreferredSize() {
		return new Dimension(462, 462);
	}
	
	
	public boolean isOpaque() {
		return true;
	}
	
	
	public Color getCandColor() {
		return candColor;
	}


	public void setCandColor(Color candColor) {
		this.candColor = candColor;
	}


	public Color getGivenColor() {
		return givenColor;
	}


	public void setGivenColor(Color givenColor) {
		this.givenColor = givenColor;
	}


	public Color getGridColor() {
		return gridColor;
	}


	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}


	public Color getNumberColor() {
		return numberColor;
	}


	public void setNumberColor(Color numberColor) {
		this.numberColor = numberColor;
	}


	public Color getSelColor() {
		return selColor;
	}


	public void setSelColor(Color selColor) {
		this.selColor = selColor;
		Color.RGBtoHSB(selColor.getRed(), selColor.getGreen(), selColor.getBlue(), hsbVals);
		sel2Color = new Color(Color.HSBtoRGB(hsbVals[0], hsbVals[1] * 0.8F, hsbVals[2] * 0.85F) & ~(~128 << 24), true);
		sel3Color = new Color(Color.HSBtoRGB(hsbVals[0], hsbVals[1] * 1.5F, hsbVals[2]) & 0xaaffffff, true);
	}


	protected void paintComponent(Graphics g) {
		// Acquire component's size and insets
		size = getSize(size);
		insets = getInsets(insets);
		
		// Get current clip
		clip.x = clip.y = 0;
		clip.width = size.width;
		clip.height = size.height;
		g.getClipBounds(clip);
		
		// The component is always opaque, so fill area with background color
		g.setColor(getBackground());
		g.fillRect(clip.x, clip.y, clip.width, clip.height);
		
		// Transform coordinates
		g.translate(insets.left, insets.top);
		clip.translate(insets.left, insets.top);
		clip.width -= insets.left;
		clip.height -= insets.top;
		
		// Paint clip
//		g.setColor(Color.RED);
//		g.drawRect(clip.x, clip.y, clip.width, clip.height);

		// Cell dimensions
		int
			unitWidth = (size.width - insets.left - insets.right - 12) / 9,
			unitHeight = (size.height - insets.top - insets.bottom - 12) / 9;
		
		// Normalise clip's right / bottom edges
		if (clip.x + clip.width > unitWidth * 9 + 11)
			clip.width = unitWidth * 9 + 11 - clip.x + 1;
		if (clip.y + clip.height > unitHeight * 9 + 11)
			clip.height = unitHeight * 9 + 11 - clip.y + 1;
		
		// Enable anti-aliasing
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Paint grid
		paintGrid(g, unitWidth, unitHeight);
		
		// Paint selected cell
		paintSelectedCell(g, unitWidth, unitHeight);
		
		// Paint numbers
		paintNumbers(g, unitWidth, unitHeight);
		
		// If selected, paint selected Row / Column / Box
		if (showRowColumnBox)
			paintSelectedRowColumnBox(g, unitWidth, unitHeight);
		
		// If selected, mask those cells where the current is not a candidate
		if (showNonCandidatesMask)
			paintNonCandidatesMask(g, unitWidth, unitHeight);
		
		// Revert Graphics object's state
		g.translate(-insets.left, -insets.top);
	}
	
	
	private void paintGrid(Graphics g, int unitWidth, int unitHeight) {
		int x = 0, y = 0;
		
		// Draw the Rows
		g.setColor(gridColor);
		for (int r = 0; r <= 9; r++) {
			if (r > 0 && r < 9 && r % 3 == 0) {
				if (clip.contains(clip.x, y) || clip.contains(clip.x, y + 1))
					g.fillRect(clip.x, y, clip.width, 2);
				y += unitHeight + 2;
			}
			else {
				if (clip.contains(clip.x, y))
					g.drawLine(clip.x, y, clip.x + clip.width - 1, y);
				y += unitHeight + 1;
			}
		}
		
		// Draw the Columns
		for (int c = 0; c <= 9; c++) {
			if (c > 0 && c < 9 && c % 3 == 0) {
				if (clip.contains(x, clip.y) || clip.contains(x + 1, clip.y))
					g.fillRect(x, clip.y, 2, clip.height);
				x += unitWidth + 2;
			}
			else {
				if (clip.contains(x, clip.y))
					g.drawLine(x, clip.y, x, clip.y + clip.height - 1);
				x += unitWidth + 1;
			}
		}
	}
	
	
	private void paintSelectedRowColumnBox(Graphics g, int unitWidth, int unitHeight) {
		// Initial area of current box
		int bCol = selColumn / 3 * 3, bRow = selRow / 3 * 3;
		tempRect.x = bCol * unitWidth + bCol + bCol / 3 + 1;
		tempRect.y = bRow * unitHeight + bRow + bRow / 3 + 1;
		tempRect.width = 3 * unitWidth + 2;
		tempRect.height = 3 * unitHeight + 2;
		tempArea.reset();
		tempArea.add(new Area(tempRect));
		
		// Add row / column
		// Row
		tempRect.x = 1;
		tempRect.y = selRow * unitHeight + selRow + selRow / 3 + 1;
		tempRect.width = unitWidth * 9 + 10;
		tempRect.height = unitHeight;
		tempArea.add(new Area(tempRect));
		// Column
		tempRect.x = selColumn * unitWidth + selColumn + selColumn / 3 + 1;
		tempRect.y = 1;
		tempRect.width = unitWidth;
		tempRect.height = unitHeight * 9 + 10;
		tempArea.add(new Area(tempRect));
		
		// Subtract selected cell
		tempRect.x = selColumn * unitWidth + selColumn + selColumn / 3 + 1;
		tempRect.y = selRow * unitHeight + selRow + selRow / 3 + 1;
		tempRect.width = unitWidth;
		tempRect.height = unitHeight;
		tempArea.subtract(new Area(tempRect));
		
		// Intersect with clip
		tempArea.intersect(new Area(clip));
		
		// Paint
		g.setColor(sel2Color);
		((Graphics2D)g).fill(tempArea);
	}
	
	
	private void paintSelectedCell(Graphics g, int unitWidth, int unitHeight) {
		tempRect.x = selColumn * unitWidth + selColumn + selColumn / 3 + 1;
		tempRect.y = selRow * unitHeight + selRow + selRow / 3 + 1;
		tempRect.width = unitWidth;
		tempRect.height = unitHeight;
		if (clip.intersects(tempRect)) {
			tempRect = clip.intersection(tempRect);
			g.setColor(selColor);
			g.fillRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
		}
	}
	
	
	private void paintNumbers(Graphics g, int unitWidth, int unitHeight) {
		// Calculate font sizes, if component's size has changed
		if (!oldSize.equals(size)) {
			// Main font
			g.setFont(numFont);
			FontMetrics fm = g.getFontMetrics();
			numFont = numFont.deriveFont(numFont.getSize2D() * (2.0F * unitHeight / 3.0F) /
					(fm.getMaxAscent() + fm.getMaxDescent()));
			
			// Conflicting numbers font
			g.setFont(confNumFont);
			fm = g.getFontMetrics();
			confNumFont = confNumFont.deriveFont(confNumFont.getSize2D() * (unitHeight / 3.0F) /
					(fm.getMaxAscent() + fm.getMaxDescent()));
			
			oldSize.setSize(size); 
		}
		
		// Paint numbers
		FontMetrics fmMain = null, fmConf = null;
		int mainAscent = 0, mainDescent = 0, candAscent = 0, candDescent = 0;
		for (int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++) {
				// Perform draw only if the cell intersects with the clip
				tempRect.x = c * unitWidth + c + c / 3 + 1;
				tempRect.y = r * unitHeight + r + r / 3 + 1;
				tempRect.width = unitWidth;
				tempRect.height = unitHeight;
				if (clip.intersects(tempRect)) {
					Character number = puzzle.getVal(r, c);
					// Number in cell, paint number
					if (number != null) {
						String numStr = number.toString();
						g.setFont(numFont);
						if (!puzzle.canModify(r, c))
							g.setColor(givenColor);
						else
							g.setColor(numberColor);
						if (fmMain == null) {
							fmMain = g.getFontMetrics();
							mainAscent = fmMain.getAscent();
							mainDescent = fmMain.getDescent();
						}
						g.drawString(numStr, c * unitWidth + c + c / 3 + (unitWidth - fmMain.stringWidth(numStr)) / 2,
								r * unitHeight + r + r / 3 + (unitHeight + mainAscent - mainDescent) / 2);
					}
					// No number in cell, show candidates - if selected
					else if (showCandidates) {
						//Set<Integer> numsToShow = new HashSet<Integer>(puzzle.getCandidates(r, c));
						//numsToShow.removeAll(puzzle.getHiddenCandidates(r, c));
						ArrayList<Character> cands = puzzle.getCandidates(r, c);
						
						Character [][] table = new Character[3][3];
						for(int i=0; i<3; i++)
							for(int j=0; j<3; j++)
								if(cands.contains(puzzle.getAlphabet().get(i*3+j)))
									table[i][j] = puzzle.getAlphabet().get(i*3+j);
								else
									table[i][j] = null;
						//ArrayList<Character> numsToShow = puzzle.getCandidates(r, c);
						// Paint candidates
						g.setFont(confNumFont);
						g.setColor(candColor);
						if (fmConf == null) {
							fmConf = g.getFontMetrics();
							candAscent = fmConf.getAscent();
							candDescent = fmConf.getDescent();
						}
						for (int i = 0; i < 9; i++) {
							Integer num = new Integer(i + 1);
							if (table[i/3][i%3] != null) {
								String numStr = table[i/3][i%3].toString();
								g.drawString(numStr, c * unitWidth + c + c / 3 + ((i % 3) + 1) * (unitWidth / 4) - fmConf.stringWidth(numStr) / 2,
										r * unitHeight + r + r / 3 + ((i / 3) + 1) * (unitHeight / 4) + (candAscent - candDescent) / 2);
							}
						}
					}
				}
			}
	}
	
	
	private void paintNonCandidatesMask(Graphics g, int unitWidth, int unitHeight) {
//		// Number to check for
//		Character number = puzzle.getVal(selRow, selColumn);
//		
//		// Only continue if selected cell contains a number
//		if (number != null) {
//			tempArea.reset();
//			
//			// Boxes
//			tempRect.width = 3 * unitWidth + 4;
//			tempRect.height = 3 * unitHeight + 4;
//			for (int boxX = 0; boxX < 3; boxX++)
//				for (int boxY = 0; boxY < 3; boxY++) {
//				//t boxX = sel		
//				tempRect.x = boxX * 3 * unitWidth + boxX * 3 + boxX;
//					tempRect.y = boxY * 3 * unitHeight + boxY * 3 + boxY;
//					if (clip.intersects(tempRect) && puzzle.coordinatesOfNumberInBox(number, boxX, boxY) != null)
//						tempArea.add(new Area(tempRect));
//				}
//			
//			// Rows
//			tempRect.x = 0;
//			tempRect.width = 9 * unitWidth + 12;
//			tempRect.height = unitHeight + 2;
//			for (int row = 0; row < 9; row++) {
//				tempRect.y = row * unitHeight + row + row / 3;
//				if (clip.intersects(tempRect) && puzzle.indexOfNumberInRow(number, row) != -1)
//					tempArea.add(new Area(tempRect));
//			}
//			
//			// Columns
//			tempRect.y = 0;
//			tempRect.width = unitHeight + 2;
//			tempRect.height = 9 * unitWidth + 12;
//			for (int col = 0; col < 9; col++) {
//				tempRect.x = col * unitWidth + col + col / 3;
//				if (clip.intersects(tempRect) && puzzle.indexOfNumberInCol(number, col) != -1)
//					tempArea.add(new Area(tempRect));
//			}
//			
//			// Intersect with clip
//			tempArea.intersect(new Area(clip));
//			
//			// Fill resulting area
//			g.setColor(sel3Color);
//			((Graphics2D)g).fill(tempArea);
//		}
	}
	
	
	private void mousePressed(MouseEvent me) {
		if (me.isPopupTrigger())
			displayPopup(me);
		else {
			// Grid's current size and insets
			insets = getInsets(insets);
			size = getSize(size);
			
			// Only continue if mouse was pressed INSIDE the grid
			int mX = me.getX(), mY = me.getY();
			if (mX >= insets.left && mX < size.width - insets.right && mY >= insets.top && mY < size.height - insets.bottom) {
				// Deal with new selection
				size = new Dimension(size.width - insets.left - insets.right, size.height - insets.top - insets.bottom);
				changeSelection((mY - insets.top) / (size.height / 9), (mX - insets.left) / (size.width / 9));
				
				// Start nonCandsTimer
				nonCandsTimer.start();
			}
		}
	}
	
	
	private void mouseReleased(MouseEvent me) {
		if (me.isPopupTrigger())
			displayPopup(me);

		nonCandsTimer.stop();
		if (showNonCandidatesMask) {
			showNonCandidatesMask = false;
			repaint();
		}
	}
	
	
	private void changeSelection(int newSelRow, int newSelColumn) {
		// Only continue if new selection is other than the current
		if (selRow != newSelRow || selColumn != newSelColumn) {
			// Compute metrics
			insets = getInsets(insets);
			size = getSize(size);
			size.width -= insets.left + insets.right;
			size.height -= insets.top + insets.bottom;
			int unitWidth = (size.width - 12) / 9, unitHeight = (size.height - 12) / 9;
			
			// If showNonCandidatesMask is enabled, repaint the whole component
			if (showNonCandidatesMask)
				repaint();
			// Else, if showRowColumnBox is enabled, try to skip some painting
			else if (showRowColumnBox) {
				// New selection at the same Row
				if (selRow == newSelRow) {
					int curBoxCol = selColumn / 3 * 3, newBoxCol = newSelColumn / 3 * 3;
					// New selection within the same Box
					if (curBoxCol == newBoxCol)
						repaint(insets.left + curBoxCol * unitWidth + curBoxCol + (curBoxCol - 1) / 3, insets.top,
								unitWidth * 3 + 6, size.height);
					// New selection in an adjacent Box
					else if (Math.abs(curBoxCol - newBoxCol) == 3) {
						int leftBoxCol = Math.min(curBoxCol, newBoxCol);
						repaint(insets.left + leftBoxCol * unitWidth + leftBoxCol + (leftBoxCol - 1) / 3, insets.top,
								unitWidth * 6 + 10, size.height);
					}
					// New selection in a distant Box
					else
						repaint();
				}
				// New selection at the same Column
				else if (selColumn == newSelColumn) {
					int curBoxRow = selRow / 3 * 3, newBoxRow = newSelRow / 3 * 3;
					// New selection within the same Box
					if (curBoxRow == newBoxRow)
						repaint(insets.left, insets.top + curBoxRow * unitHeight + curBoxRow + (curBoxRow - 1) / 3,
								size.width, unitHeight * 3 + 6);
					// New selection in an adjacent Box
					else if (Math.abs(curBoxRow - newBoxRow) == 3) {
						int topBoxRow = Math.min(curBoxRow, newBoxRow);
						repaint(insets.left, insets.top + topBoxRow * unitHeight + topBoxRow + (topBoxRow - 1) / 3,
								size.width, unitHeight * 6 + 10);
					}
					// New selection in a distant Box
					else
						repaint();
				}
				// New selection at another Row / Column combination
				else
					repaint();
			}
			// No show option enabled, just repaint the smallest part
			else {
				int
					leftCol = Math.min(selColumn, newSelColumn), topRow = Math.min(selRow, newSelRow),
					rightCol = Math.max(selColumn, newSelColumn), bottomRow = Math.max(selRow, newSelRow),
					colSpan = rightCol - leftCol, rowSpan = bottomRow - topRow;
				repaint(insets.left + leftCol * unitWidth + leftCol + leftCol / 3 + 1,
						insets.top + topRow * unitHeight + topRow + topRow / 3 + 1,
						(colSpan + 1) * unitWidth + colSpan + rightCol / 3 - leftCol / 3,
						(rowSpan + 1) * unitHeight + rowSpan + bottomRow / 3 - topRow / 3);
			}
			
			// Update current selection and notify listeners
			selRow = newSelRow;
			selColumn = newSelColumn;
			notifySelectionListeners();
		}
	}
	
	
	private void displayPopup(MouseEvent me) {
		if (puzzle.getVal(selRow, selColumn) == null) {
			cellPopup.removeAll();
			cellPopup.add(header);
			List<Character> cands = new ArrayList<Character>(puzzle.getCandidates(selRow, selColumn));
			Collections.sort(cands);
			for (Character num : cands) {
//				JCheckBoxMenuItem mItem = new JCheckBoxMenuItem(num.toString(),
//						puzzle.getHiddenCandidates(selRow, selColumn).contains(num));
//				final Integer theNum = num;
//				mItem.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						if (((JCheckBoxMenuItem)e.getSource()).isSelected())
//							puzzle.hideCandidate(theNum, selRow, selColumn);
//						else
//							puzzle.unhideCandidate(theNum, selRow, selColumn);
//						PuzzleGrid.this.repaint();
//						notifySelectionListeners();
//					}
//				});
//				cellPopup.add(mItem);
			}
			cellPopup.show(me.getComponent(), me.getX() + 5, me.getY() + 5);
		}
	}
	
	
	private void keyPressed(KeyEvent e) {
		int newSelRow, newSelColumn;
		switch(e.getKeyCode()) {
		
		case KeyEvent.VK_HOME:
			newSelRow = newSelColumn = 0;
			changeSelection(newSelRow, newSelColumn);
			break;
		
		case KeyEvent.VK_END:
			newSelRow = newSelColumn = 8;
			changeSelection(newSelRow, newSelColumn);
			break;
		
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
			if (selRow > 0) {
				newSelRow = selRow - 1;
				newSelColumn = selColumn;
				changeSelection(newSelRow, newSelColumn);
			}
			else if (selRow == 0 && selColumn > 0) {
				newSelRow = 8;
				newSelColumn = selColumn - 1;
				changeSelection(newSelRow, newSelColumn);
			}
			break;
		
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
			if (selRow < 8) {
				newSelRow = selRow + 1;
				newSelColumn = selColumn;
				changeSelection(newSelRow, newSelColumn);
			}
			else if (selRow == 8 && selColumn < 8) {
				newSelRow = 0;
				newSelColumn = selColumn + 1;
				changeSelection(newSelRow, newSelColumn);
			}
			break;
		
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
			if (selColumn > 0) {
				newSelColumn = selColumn - 1;
				newSelRow = selRow;
				changeSelection(newSelRow, newSelColumn);
			}
			else if (selColumn == 0 && selRow > 0) {
				newSelColumn = 8;
				newSelRow = selRow - 1;
				changeSelection(newSelRow, newSelColumn);
			}
			break;
		
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
			if (selColumn < 8) {
				newSelColumn = selColumn + 1;
				newSelRow = selRow;
				changeSelection(newSelRow, newSelColumn);
			}
			else if (selColumn == 8 && selRow < 8) {
				newSelColumn = 0;
				newSelRow = selRow + 1;
				changeSelection(newSelRow, newSelColumn);
			}
			break;
		
		case KeyEvent.VK_1:
		case KeyEvent.VK_2:
		case KeyEvent.VK_3:
		case KeyEvent.VK_4:
		case KeyEvent.VK_5:
		case KeyEvent.VK_6:
		case KeyEvent.VK_7:
		case KeyEvent.VK_8:
		case KeyEvent.VK_9:
//			//Integer n1 = new Integer(e.getKeyCode() - KeyEvent.VK_0);
//			char n1=e.getKeyChar();
//			if (!nkListeners.isEmpty())
//				notifyNumberKeyListeners(n1, selRow, selColumn);
//			else
//				puzzle.move(selRow, selColumn,n1);
//			break;
			break;
		case KeyEvent.VK_NUMPAD1:
		case KeyEvent.VK_NUMPAD2:
		case KeyEvent.VK_NUMPAD3:
		case KeyEvent.VK_NUMPAD4:
		case KeyEvent.VK_NUMPAD5:
		case KeyEvent.VK_NUMPAD6:
		case KeyEvent.VK_NUMPAD7:
		case KeyEvent.VK_NUMPAD8:
		case KeyEvent.VK_NUMPAD9:
//			Integer n2 = new Integer(e.getKeyCode() - KeyEvent.VK_NUMPAD0);
//			if (!nkListeners.isEmpty())
//				notifyNumberKeyListeners(n2, selRow, selColumn);
//			else
//				puzzle.putNumber(n2, selRow, selColumn);
			break;
		
		case KeyEvent.VK_DELETE:
			try {
				puzzle.setVal(selRow, selColumn,null);
			} catch (OutOfBoundaryException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DuplicateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (ReadOnlyException e1) {
				//e1.printStackTrace();
			}
			repaint();
			break;
		}
		
		char c = e.getKeyChar();
		if(c != e.CHAR_UNDEFINED &&
				(Character.isDigit(c)||Character.isLetter(c)))
		{
			char n1=e.getKeyChar();
			if (!nkListeners.isEmpty())
				notifyNumberKeyListeners(n1, selRow, selColumn);
			else
				try {
					puzzle.setVal(selRow, selColumn,n1);
				} catch (OutOfBoundaryException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (DuplicateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}catch (ReadOnlyException e1) {
					//e1.printStackTrace();
					// TODO implement this part
					//return false;
				}

			repaint();
			if(puzzle.isSolved())
				JOptionPane.showMessageDialog(this, "Puzzle solved!");
			//break;
		}
	}
	

	private void notifyNumberKeyListeners(Character number, int row, int column) {
		for (NumberKeyListener nkl : nkListeners)
			nkl.numberKeyPressed(number, row, column);
	}
	
	
	private void notifySelectionListeners() {
		for (SelectionListener ls : selListeners)
			ls.selectionChanged(selRow, selColumn);
	}
	
	
	public interface NumberKeyListener {
		
		void numberKeyPressed(Character number, int row, int column);
		
	}
	
	
	public interface SelectionListener {
		
		void selectionChanged(int newRow, int newColumn);
		
	}


}
