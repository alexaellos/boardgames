package boardgames;

public interface GameBoard {

	/* private ArrayList<ArrayList<Piece>> board; */
	
	public boolean commandIsValid (Command c);
	public Piece getPieceAt(Coordinate c);
	public void setPieceAt(Coordinate c);
	public String getCurrentPlayer();
	public void setCurrentPlayer(String s);	
}
