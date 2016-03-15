package GUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import boardgames.Games;

public class InputDialog {

	private JFrame frame;

	public InputDialog(JFrame frame) {
		this.frame = frame;
	}
	
	public String createTextInputDialog(String frameTitle, String inputMsg){
		//TODO: Add string verification so that user input isn't empty
		//JOptionPane optionPane = new JOptionPane();
		String textInput = (String) JOptionPane.showInputDialog(frame, inputMsg, frameTitle, 
				JOptionPane.PLAIN_MESSAGE, null, null, "");
		return textInput;
	}

	public Games createGamesDropDownDialog(String frameTitle, String selectionMsg, Games[] selectionList){
		String firstListItem = "";
		
		if(selectionList.length > 0){
			firstListItem = selectionList[0].toString();
		}
		
		return (Games) JOptionPane.showInputDialog(frame, selectionMsg, frameTitle, 
				JOptionPane.PLAIN_MESSAGE, null, selectionList, firstListItem);
	}
	
	public int createModalDialog(String frameTitle, String message, String buttonText){
		Object[] buttons = {buttonText};
		return (int) JOptionPane.showOptionDialog(frame, message, frameTitle, 
				JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
	}
}
