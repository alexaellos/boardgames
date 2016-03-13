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

// To-do List:
/* - Fix the action listeners
 * - Make sure to check if the Piece.id is null before grabbing the Icon image
 * - Make sure GUI works with the client (and the server)
 * 
 * */

/* Changes List:
 * - startGUI() will return a gameStatus.gameSelected enum
 * -- occurs after the player enters their name and selects a game
 * - GUIUpdater
 * -- now has get methods for player name and game title
 *
 * 
 */