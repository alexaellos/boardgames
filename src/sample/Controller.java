package sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import GUI.GUIActionListener;

public class Controller {

	// private static ObjectOutputStream output;

	public static gameStatus2 status;

	private static View gui;
	public static String[] pressedButtonData;

	public static void main(String[] args) {
		status = null;	
		gui = new View();
		pressedButtonData = null;

		System.out.println("waiting for user input");
		waitForUserInfoInput();

//		 METHOD 1: PASSING INFORMATION BACK AND FORTH
//		
//		 Attempting to grab information from the listener which doesn't work
//		 because the board contains multiple new instances of the button in
//		 the board which means a 3 x 3 board has 9 action listeners to monitor.
//		
//		GUIActionListener guiListener = new GUIActionListener();
//		
//		if(status == gameStatus2.boardLoaded){
//			System.out.println("adding actions");
//			addActionToBoardButtons(guiListener);
//		}
//
//		System.out.println("listening for actions");
//		while(true){
//			gameStatus2 listenerStatus = guiListener.getStatus();
//			
//			if(listenerStatus == gameStatus2.buttonClicked){
//				System.out.println("player clicked on a button");
//				pressedButtonData = parseButtonData((JButton) guiListener.getObject());
//				printButtonData(pressedButtonData);
//				break;
//			}
//		}
		
// 		METHOD 2: PRINTING BUTTON ACTIONS		
		GUIActionListener2 guiListener2 = new GUIActionListener2();
		
		if(status == gameStatus2.boardLoaded){
			System.out.println("adding actions");
			addActionToBoardButtons(guiListener2);
		}
		
		System.out.println("listening for actions");
	}

	public static void waitForUserInfoInput(){

		gui.startGUI();
		System.out.println("loading player");

		while(true){

			status = gui.getStatus();

			if(status == gameStatus2.gameSelected){
				System.out.println("loading checkers");
				gui.loadBoard("Checkers");
				status = gameStatus2.boardLoaded;
				break;
			}
		}
	}

	// addActionToBoardButtons() for METHOD 1
	public static void addActionToBoardButtons(GUIActionListener actionListener){
		
		JButton[][] guiBoard = gui.getBoard();

		for(int i = 0; i < guiBoard.length; i++){
			for(int j = 0; j < guiBoard[i].length; j++){

				final JButton button = guiBoard[i][j];

				button.addActionListener(actionListener);
			}
		}
	}
	
	// addActionToBoardButtons() for METHOD 2
	public static void addActionToBoardButtons(GUIActionListener2 actionListener){
		
		JButton[][] guiBoard = gui.getBoard();

		for(int i = 0; i < guiBoard.length; i++){
			for(int j = 0; j < guiBoard[i].length; j++){

				final JButton button = guiBoard[i][j];

				button.addActionListener(actionListener);
			}
		}
	}

	public static String[] parseButtonData(JButton button){
		String buttonData = button.getActionCommand();
		String[] parsedButtonData = buttonData.split(",");
		return parsedButtonData;
	}
	
	public static void printButtonData(String[] data){
		for(String s: data){
			System.out.println(s);
		}
	}
}
