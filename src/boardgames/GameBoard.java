package boardgames;

public interface GameBoard {	
	public boolean commandIsValid (Command c);
	public Piece getPieceAt(Coordinate c);
	public void setPieceAt(Coordinate c, Piece p);
	public String getCurrentPlayer();
	public void setCurrentPlayer(String s);
	public Piece[][] getBoard();
	public int getBoardHeight();
	public int getBoardWidth();
}
