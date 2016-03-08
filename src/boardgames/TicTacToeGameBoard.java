package boardgames;

import java.util.ArrayList;

public class TicTacToeGameBoard implements GameBoard{
	
	public static final int EMPTY = 0;
	public static final int CROSS = 1;
	public static final int CIRCLE = 2;
	public static final int ROWS = 3, COLS = 3;
	 

	public static final int PLAYING = 0;
	public static final int DRAW = 1;
	public static final int CROSS_WON = 2;
	public static final int CIRCLE_WON = 3;
	public static String currentPlayer;
	
	private ArrayList<ArrayList<Piece>> board;
	
	@Override
	public boolean commandIsValid(Command c) {
		return true;
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
		return currentPlayer;
	}

	public void setCurrentPlayer(String s) {
		currentPlayer = s;

	}
	
	public String toString() {
		String s = "";
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				s += getPieceAt(new Coordinate(x, y)) + "    ";
			}
			s += "\n\n";
		}
		return s;
	} 

}
