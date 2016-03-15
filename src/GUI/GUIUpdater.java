package GUI;

import boardgames.GameBoardGUI;
import boardgames.Games;
import boardgames.Piece;
import boardgames.gameStatus;

public interface GUIUpdater {
	public void startGUI();		
	
	public String loadGameBoardGUI(GameBoardGUI game);
	public void updateGameBoardGUI(String playerName, Piece[][] board);
	
	public Games getSelectedGame();
	public String getPlayerName();
	public gameStatus getStatus();
}
