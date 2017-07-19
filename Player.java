import java.util.ArrayList;
import java.util.List;

public class Player {
	private int playerNum; // 1 is top player, -1 is bottom player
	private List<Piece> pieces;
	
	public Player(int playerNum) {
		this.playerNum = playerNum;
		pieces = new ArrayList<Piece>();
	}
	
	// check if this player has no pieces/lost the game
	public boolean hasNoPieces() {
		if(pieces.size() == 0) {
			return true;
		}
		
		return false;
	}
	
	// check if this player has no legal moves/jumps to make (results in loss)
	public boolean hasNoMove() {
		boolean hasNoMove = true;
		for(Piece p : pieces) {
			if(p.getPossibleMoveSquares().size() > 0 || p.getPossibleJumpSquares().size() > 0) {
				hasNoMove = false;
			}
		}
		
		return hasNoMove;
	}
	
	// adds a piece to this player
	public void addPiece(Piece p) {
		pieces.add(p);
	}
	
	// removes a piece from this player
	public void removePiece(Piece p) {
		pieces.remove(p);
	}
	
	// updates pieces for this player that can be selected after each move
	public void updateSelectablePieces() {
		boolean hasToJump = false;
		
		for(Piece p : pieces) {
			// make all pieces unselectable
			p.makeUnselectable();
			
			// if there are any possible jump squares for any piece, player has to jump, make those pieces selectable
			if(p.getPossibleJumpSquares().size() > 0) {
				hasToJump = true;
				p.makeSelectable();
			}
		}
		
		// if player does not have to jump, make all pieces with possible move squares selectable
		if(!hasToJump) {
			for(Piece p : pieces) {
				if(p.getPossibleMoveSquares().size() > 0) {
					p.makeSelectable();
				}
			}
		}
	}
	
	// returns this player's number (1 for top player, -1 for bottom player)
	public int getPlayerNum() {
		return playerNum;
	}
	
	// returns the piece that is currently selected by this player, otherwise returns null
	public Piece getSelectedPiece() {
		for(Piece p : pieces) {
			if(p.isSelected()) return p;
		}
		
		return null;
	}
}
