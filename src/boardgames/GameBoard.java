package boardgames;

public interface GameBoard {

	/* private ArrayList<ArrayList<Piece>> board; */
	
	public boolean commandIsValid (Command c);
	public void setCurrentPlayer(String s);
	public void setGameOver(boolean gameOver);
	public String getCurrentPlayer();
	public boolean isGameOver();
	public gameStatus getGameStatus();
	public GameBoardGUI getGameBoardGUI();
}
