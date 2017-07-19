import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class GhostPiece extends JButton implements ActionListener {
	private Square currSquare;
	private Checkers game;
	
	public GhostPiece(Square currSquare, Checkers game) {
		this.currSquare = currSquare;
		this.game = game;
		
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setPreferredSize(new Dimension(50, 50));
		this.addActionListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		g.drawOval(4, 2, 40, 40);
	}
	
	// when this ghost piece is clicked on, move the current selected piece to the square that this ghost piece is in
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Piece selectedPiece = game.getSelectedPieceFromCurrPlayer();
		
		if(selectedPiece != null) {
			selectedPiece.move(currSquare);
		}
	}
}
