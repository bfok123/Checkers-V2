import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

public class Piece extends JButton implements ActionListener {
	private int row, col;
	private Player player;
	private boolean isKing, selectable, selected, jumped;
	private Color bgColor;
	private List<Square> possibleMoveSquares;
	private List<Square> possibleJumpSquares;
	private Checkers game;
	
	public Piece(int row, int col, Player player, Checkers game) {
		this.row = row;
		this.col = col;
		this.player = player;
		this.game = game;
		isKing = false;
		selected = false;
		jumped = false;
		bgColor = getPlayerNum() == 1 ? Color.GRAY : Color.RED;
		possibleJumpSquares = new ArrayList<Square>();
		possibleMoveSquares = new ArrayList<Square>();
		updatePossibleJumpSquares();
		updatePossibleMoveSquares();
		
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setPreferredSize(new Dimension(50, 50));
		this.addActionListener(this);
		
		// if piece belongs to bottom player and piece has move (because bottom player goes first) then this piece is selectable
		selectable = getPlayerNum() == -1 && possibleMoveSquares.size() > 0 ? true : false;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(!selected) {
			g.setColor(bgColor);
		}
		else if(selected) {
			g.setColor(Color.YELLOW);
		}
		
		g.fillOval(4, 2, 40, 40);
		
		if(isKing) {
			g.setColor(Color.BLACK);
			g.drawLine(15, 7, 15, 37);
			g.drawLine(15, 22, 33, 7);
			g.drawLine(15, 22, 33, 37);
		}
	}
	
	// moves/jumps this piece to a specified square
	public void move(Square newSquare) {
		hideGhostPieces();
		deselect();
		
		game.getSquareAt(row, col).removePiece();
		newSquare.addPiece(this);
		
		if(newSquare.getCol() - 2 == col || newSquare.getCol() + 2 == col) {
			int opponentNum = getPlayerNum() == 1 ? -1 : 1;
			Square jumpedSquare = game.getSquareAt((row + newSquare.getRow()) / 2, (col + newSquare.getCol()) / 2);
			game.playerRemovePiece(opponentNum, jumpedSquare.getPiece());
			jumpedSquare.removePiece();
			jumped = true;
		}
		
		row = newSquare.getRow();
		col = newSquare.getCol();

		checkForKing();
		
		game.onMove();
		
		// if this piece just jumped and has another jump available, do not switch the player turn
		if(jumped && possibleJumpSquares.size() > 0);
		// otherwise switch the player turn
		else {
			game.switchPlayerTurn();
		}
		
		jumped = false;
	}
	
	// checks to see if this piece can become a king
	public void checkForKing() {
		if(getPlayerNum() == 1 && row == 7) {
			isKing = true;
		}
		else if(getPlayerNum() == -1 && row == 0) {
			isKing = true;
		}
	}
	
	// returns the player number of the player that this piece belongs to
	public int getPlayerNum() {
		return player.getPlayerNum();
	}
	
	// updates then returns the list of possible jump squares for this piece
	public List<Square> getPossibleJumpSquares() {
		updatePossibleJumpSquares();
		return possibleJumpSquares;
	}
	
	// updates then returns the list of possible move squares for this piece
	public List<Square> getPossibleMoveSquares() {
		updatePossibleMoveSquares();
		return possibleMoveSquares;
	}
	
	// makes this piece selectable
	public void makeSelectable() {
		selectable = true;
	}
	
	// makes this piece unselectable
	public void makeUnselectable() {
		selectable = false;
	}
	
	// returns true if this piece is currently selected, otherwise false
	public boolean isSelected() {
		return selected;
	}
	
	// returns true if this piece just jumped, otherwise false
	public boolean jumped() {
		return jumped;
	}
	
	// deselects this piece
	public void deselect() {
		selected = false;
		hideGhostPieces();
		repaint();
	}
	
	// updates list of possible squares this piece can jump to
	public void updatePossibleJumpSquares() {
		possibleJumpSquares.clear();
		
		if(row + getPlayerNum() <= 6 && row + getPlayerNum() >= 1) {
			if(col >= 2 && game.getSquareAt(row + getPlayerNum(), col - 1).getPiece() != null) {
				// if a diagonal piece belongs to opponent and the next square diagonal to that piece is empty
				if(game.getSquareAt(row + getPlayerNum(), col - 1).getPiece().getPlayerNum() != this.getPlayerNum()
						&& game.getSquareAt(row + (getPlayerNum() * 2), col - 2).getPiece() == null) {
					possibleJumpSquares.add(game.getSquareAt(row + (getPlayerNum() * 2), col - 2));
				}
			}
			
			if(col <= 5 && game.getSquareAt(row + getPlayerNum(), col + 1).getPiece() != null) {
				if(game.getSquareAt(row + getPlayerNum(), col + 1).getPiece().getPlayerNum() != this.getPlayerNum() 
						&& game.getSquareAt(row + (getPlayerNum() * 2), col + 2).getPiece() == null) {
					possibleJumpSquares.add(game.getSquareAt(row + (getPlayerNum() * 2), col + 2));
				}
			}
		}
		
		if(isKing && row - getPlayerNum() <= 6 && row - getPlayerNum() >= 1) {
			if(col >= 2 && game.getSquareAt(row - getPlayerNum(), col - 1).getPiece() != null) {
				if(game.getSquareAt(row - getPlayerNum(), col - 1).getPiece().getPlayerNum() != this.getPlayerNum()
						&& game.getSquareAt(row - (getPlayerNum() * 2), col - 2).getPiece() == null) {
					possibleJumpSquares.add(game.getSquareAt(row - (getPlayerNum() * 2), col - 2));
				}
			}
			
			if(col <= 5 && game.getSquareAt(row - getPlayerNum(), col + 1).getPiece() != null) {
				if(game.getSquareAt(row - getPlayerNum(), col + 1).getPiece().getPlayerNum() != this.getPlayerNum()
						&& game.getSquareAt(row - (getPlayerNum() * 2), col + 2).getPiece() == null) {
					possibleJumpSquares.add(game.getSquareAt(row - (getPlayerNum() * 2), col + 2));
				}
			}
		}
	}
	
	// updates list of possible squares this piece can move to
	public void updatePossibleMoveSquares() {		
		possibleMoveSquares.clear();
		
		if(row + getPlayerNum() >= 0 && row + getPlayerNum() <= 7) {
			if(col != 0) {
				if(game.getSquareAt(row + getPlayerNum(), col - 1).getPiece() == null) {
					possibleMoveSquares.add(game.getSquareAt(row + getPlayerNum(), col - 1));
				}
			}
			
			if(col != 7) {
				if(game.getSquareAt(row + getPlayerNum(), col + 1).getPiece() == null) {
					possibleMoveSquares.add(game.getSquareAt(row + getPlayerNum(), col + 1));
				}
			}
		}
		
		if(row - getPlayerNum() >= 0 && row - getPlayerNum() <= 7) {
			if(isKing) {
				if(col != 0) {
					if(game.getSquareAt(row - getPlayerNum(), col - 1).getPiece() == null) {
						possibleMoveSquares.add(game.getSquareAt(row - getPlayerNum(), col - 1));
					}
				}
				
				if(col != 7) {
					if(game.getSquareAt(row - getPlayerNum(), col + 1).getPiece() == null) {
						possibleMoveSquares.add(game.getSquareAt(row - getPlayerNum(), col + 1));
					}
				}
			}
		}
	}
	
	// when this piece is selected, show possible places this piece can jump/move to
	public void showGhostPieces() {		
		if(possibleJumpSquares.size() > 0) {
			for(Square square : possibleJumpSquares) {
				GhostPiece ghostPiece = new GhostPiece(square, game);
				square.addGhostPiece(ghostPiece);
			}
		}
		else if(possibleMoveSquares.size() > 0) {
			for(Square square : possibleMoveSquares) {
				GhostPiece ghostPiece = new GhostPiece(square, game);
				square.addGhostPiece(ghostPiece);
			}
		}
	}
	
	// when this piece is deselected or moved, remove the ghost pieces
	public void hideGhostPieces() {
		if(possibleJumpSquares.size() > 0) {
			for(Square square : possibleJumpSquares) {
				square.removeGhostPiece();
			}
		}
		else if(possibleMoveSquares.size() > 0) {
			for(Square square : possibleMoveSquares) {
				square.removeGhostPiece();
			}
		}
	}

	// when this piece is clicked on
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// if selectable and is player's turn
		if(selectable && game.getPlayerTurn() == getPlayerNum()) {
			Piece selectedPiece = player.getSelectedPiece();
			
			if(selectedPiece != null) {
				selectedPiece.deselect();
			}
			
			selected = true;
			showGhostPieces();
			
			repaint();
		}
	}
}
