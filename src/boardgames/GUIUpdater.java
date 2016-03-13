package boardgames;

import java.util.ArrayList;

public interface GUIUpdater {
	public gameStatus startGUI(ArrayList<String> gameList);		// Replace with Game[Board]GUI later
	public String loadGameBoardGUI(GameBoardGUI game);
	public void updateGameBoardGUI(String playerName, Piece[][] board);
	public String getGUIChange();
	public String loadWaitingGUI();
}
