package boardgames;

import java.util.ArrayList;

public interface GUIUpdater {
	public String startGUI(ArrayList<GameBoard> gameList);
	public String loadGameBoardGUI(GameBoard game);
	public void updateGameBoardGUI(String playerName, ArrayList<ArrayList<Piece>> board);
	public String getGUIChange();
}
