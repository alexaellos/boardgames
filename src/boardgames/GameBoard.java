package boardgames;

public interface GameBoard {	
	public boolean commandIsValid (Command c);
	public String getCurrentPlayer();
	public Piece[][] getBoard();
	public int getBoardHeight();
	public int getBoardWidth();
	public gameStatus getGameStatus();
	public GameBoardGUI getGameBoardGUI();
	public void makeMove(Command c);
}
