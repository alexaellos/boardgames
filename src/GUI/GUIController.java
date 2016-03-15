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
			gameStatus status = gui.getStatus();
			if(status == gameStatus.exit){
				break;
			}
			else if(status == gameStatus.loadingPlayer){
				System.out.println("loading");
			}
			else if(status == gameStatus.gameSelected){
				Games game = gui.getSelectedGame();
				GameBoard gameboard = game.getGameBoard();
				
				// This doesn't work for Tic Tac Toe but it works for Othello & Checkers.
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