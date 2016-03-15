package GUI;

import boardgames.GameBoard;
import boardgames.Games;
import boardgames.gameStatus;

public class GUIController {

	public static void main(String[] args)
	{	
		GUI gui = new GUI();
		
		gui.startGUI();
		
		while(true){
			// The player decided to exit out of the name input dialog,
			// which means they decided to exit the program.
			gameStatus status = gui.getStatus();
			if(status == gameStatus.exit){
				break;
			}
			// The program is still waiting for the user to:
			// (1) Enter their name
			// (2) Select a game
			else if(status == gameStatus.loadingPlayer){
				System.out.println("loading");
			}
			// The player has selected a game and has reached the loading screen.
			// They are now waiting to be matched to another player.
			else if(status == gameStatus.gameSelected){
				Games game = gui.getSelectedGame();
				GameBoard gameboard = game.getGameBoard();
				
				// This doesn't work for Tic Tac Toe but it works for Othello & Checkers.
				// It won't work until Tic Tac Toe instantiates itself and matches the structure
				// of the other game plug-ins.
				System.out.println("Selected game: " + gameboard.getGameBoardGUI().getTitle());
			}
			else{
				System.out.println(status);
			}
		}
		System.out.println("Terminated program.");
	}
}

// To-do List:
/* - Make sure to check if the Piece.id is null before grabbing the Icon image
 * - Make sure GUI works with the client (and the server)
 * 
 * */