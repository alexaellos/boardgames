package boardgames;

public interface GameBoard {

	/* private ArrayList<ArrayList<Piece>> board; */
	
	public boolean commandIsValid (Command c);
	public Piece getPieceAt(Coordinate c);
	public void setPieceAt(Coordinate c, Piece p);
	public String getCurrentPlayer();
	public void setCurrentPlayer(String s);
	public boolean isGameOver();
	public void setGameOver(boolean gameOver);
}
