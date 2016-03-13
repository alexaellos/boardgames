package boardgames;

public interface GameBoard {

	/* private ArrayList<ArrayList<Piece>> board; */
	
	public boolean commandIsValid (Command c);
	public String getCurrentPlayer();
	public gameStatus getGameStatus();
	public GameBoardGUI getGameBoardGUI();
}
