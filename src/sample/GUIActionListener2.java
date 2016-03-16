package sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;

public class GUIActionListener2 implements ActionListener{

	private ObjectOutputStream output;

	public GUIActionListener2(){
	}

	public GUIActionListener2(ObjectOutputStream output){
		this.output = output;
	}

	@Override
	public void actionPerformed(ActionEvent e){

		JButton pressedButton = (JButton) e.getSource();
		String[] pressedButtonData = parseButtonData(pressedButton);	
		printButtonData(pressedButtonData);

		//		try{
		//			Object object = e.getSource();
		//			output.writeObject(object);
		//			output.flush();
		//		} 
		//		catch (IOException e1) {
		//			e1.printStackTrace();
		//		}
	}	

	private String[] parseButtonData(JButton button){
		String buttonData = button.getActionCommand();
		String[] parsedButtonData = buttonData.split(",");
		return parsedButtonData;
	}

	private void printButtonData(String[] data){
		if(data.length == 3){
			System.out.println("buttonClicked player " + data[2] + " @ " + data[0] + "," + data[1]);
		}
	}
}
