package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sample.gameStatus2;

public class GUIActionListener implements ActionListener{

	private Object o;
	private gameStatus2 status;
	
	public GUIActionListener(){
		o = null;
		status = null;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		o = e.getSource();
		status = gameStatus2.buttonClicked;
	}	
	
	public gameStatus2 getStatus(){
		return status;
	}
	
	public Object getObject(){
		return o;
	}
}
