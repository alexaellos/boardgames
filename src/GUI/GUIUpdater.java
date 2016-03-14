package GUI;

import java.util.ArrayList;

import boardgames.GameBoardGUI;
import boardgames.Piece;
import boardgames.gameStatus;

public interface GUIUpdater {
	public gameStatus startGUI();		
	public String loadGameBoardGUI(GameBoardGUI game);
	public void updateGameBoardGUI(String playerName, Piece[][] board);
	public String getGUIChange();
	public String getSelectedGame();
	public String getPlayerName();
	public gameStatus restartGameMenuGUI();
}
