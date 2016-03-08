package boardgames;

import java.util.ArrayList;

public class OthelloGameBoard implements GameBoard {

	static final int boardSize = 8;
	private String currentPlayerId;
	private ArrayList<ArrayList<Piece>> board;
	
	
	public OthelloGameBoard() {
		board = new ArrayList<ArrayList<Piece>>();
		for (int x = 0; x < boardSize; x++) {
			ArrayList<Piece> tmp = new ArrayList<Piece>();
			for (int y = 0; y < boardSize; y++) {
				tmp.add(new Piece(new Coordinate(x, y)));
			}
			board.add(tmp);
		}
	}
	
	public ArrayList<ArrayList<Piece>> getBoard() {
		return board;
	}

	@Override
	public boolean commandIsValid(Command c) {
		
		if (getPieceAt(c.getCoord1()) != null || !c.getPlayerId().equals(currentPlayerId)) {
			/* Player ID doesn't match the ID of the player who needs to make a move
			 * or the coordinate being clicked isn't empty. Immediately reject.
			 */
			return false;
		}
		if (piecesToFlip(c).size() > 0) {
			/* At least one enemy token can be flipped; move is valid. 
			 */
			return true;
		}
		
		
		
		
		
		
		return false; /* Move is invalid */
	}
	
	private ArrayList<Piece> piecesToFlip(Command c) {
		
		
		
		return null;
	}
	
	private void convertPieces(ArrayList<Piece> pieces, String id) {
		/* Change the ID of each piece in `pieces` to `id`*/
		for (Piece p : pieces) {
			p.setId(id);
		}
	}
	
	

	@Override
	public Piece getPieceAt(Coordinate c) {
		return board.get(c.getX()).get(c.getY());
	}

	@Override
	public void setPieceAt(Coordinate c, Piece p) {
		getPieceAt(c).setCoord(p.getCoord());
		getPieceAt(c).setId(p.getId());
		getPieceAt(c).setPlayerId(p.getPlayerId());
	}

	@Override
	public String getCurrentPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentPlayer(String s) {
		// TODO Auto-generated method stub

	}
	
	
	public String toString() {
		String s = "";
		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				s += getPieceAt(new Coordinate(x, y)) + "    ";
			}
			s += "\n\n";
		}
		return s;
	} 
	
	
	public static void main(String[] args) {
		OthelloGameBoard othelloTest = new OthelloGameBoard();
		System.out.println(othelloTest);
		
		
	}
	

}
