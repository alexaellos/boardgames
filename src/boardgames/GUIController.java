package boardgames;

import java.util.ArrayList;

public class GUIController {
	
	public static void main(String[] args)
	{	
		GUI gui = new GUI();
		ArrayList<String> gameList = new ArrayList<String>();
		gameList.add("Tic Tac Toe");
		gameList.add("Checkers");
		gameList.add("Chess");
		gui.startGUI(gameList);
	}

}
