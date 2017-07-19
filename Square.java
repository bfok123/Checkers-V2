import java.awt.Color;

import javax.swing.JPanel;

public class Square extends JPanel {
	private int row, col;
	private Piece piece;
	private GhostPiece ghostPiece;
	
	public Square(int row, int col) {
		this.row = row;
		this.col = col;
		piece = null;
		ghostPiece = null;
		
		determineColor();
	}
	
	// updates this square (when components are added or removed from it)
	public void updateGraphics() {
		revalidate();
		repaint();
	}
	
	// returns the piece that is in this square (null if there is no piece)
	public Piece getPiece() {
		return piece;
	}
	
	// returns row that this square is in
	public int getRow() {
		return row;
	}
	
	// returns column that this square is in
	public int getCol() {
		return col;
	}
	
	// determines the color of this square
	public void determineColor() {
		if(row % 2 == 0 && col % 2 == 0) {
			this.setBackground(Color.WHITE);
		}
		else if(row % 2 == 1 && col % 2 == 1) {
			this.setBackground(Color.WHITE);
		}
		else if(row % 2 == 1 && col % 2 == 0) {
			this.setBackground(Color.BLACK);
		}
		else if(row % 2 == 0 && col % 2 == 1) {
			this.setBackground(Color.BLACK);
		}
	}
	
	// adds a piece to this square if there is not already a piece in it
	public void addPiece(Piece p) {
		// if there is not already a piece in this square
		if(piece == null) {
			this.add(p);
			piece = p;
			updateGraphics();
		}
	}
	
	// removes the piece from this square if there is one
	public void removePiece() {
		// if there is a piece in this square
		if(piece != null) {
			this.remove(piece);
			piece = null;
			updateGraphics();
		}
	}
	
	// adds a ghost piece to this square if there is not already a ghost piece in it
	// (used to show where a player can move a selected piece)
	public void addGhostPiece(GhostPiece p) {
		if(ghostPiece == null) {
			this.add(p);
			ghostPiece = p;
			updateGraphics();
		}
	}
	
	// removes the ghost piece from this square if there is one
	public void removeGhostPiece() {
		if(ghostPiece != null) {
			this.remove(ghostPiece);
			ghostPiece = null;
			updateGraphics();
		}
	}
}
