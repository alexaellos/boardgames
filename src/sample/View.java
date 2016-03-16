package sample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class View {

	private JFrame frame;
	private gameStatus2 guiStatus;
	private JButton[][] board;
	

	public View() {
		frame = null;
		board = null;
		guiStatus = gameStatus2.loadingPlayer;
	}
	
	public void startGUI(){
		guiStatus = gameStatus2.gameSelected;
	}

	public void loadBoard(String title){
		// Create a new frame
		int width = 8;
		int height = 8;
		Dimension dimension = new Dimension(720, 720);
		frame = createNewFrame(title, dimension);

		// Create a new JPanel
		JPanel panel = new JPanel(new GridLayout(width, height));

		// Add the JPanel to the frame's content pane
		Container container = frame.getContentPane();
		container.add(panel, BorderLayout.CENTER);

		// Initialize a grid of buttons for the board game
		board = new JButton[width][height];

		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				JButton button = new JButton();
				button.setActionCommand(i + "," + j + ",0");
				
				// Figure out which color to set as the background of each button
				// in the grid of buttons.
				if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)){
					button.setForeground(Color.WHITE);
					button.setBackground(Color.WHITE);
				}
				else{
					button.setBackground(Color.BLACK);
					button.setForeground(Color.BLACK);
				}
				button.setOpaque(true);

				// Add a border around the buttons
				Border buttonBorder = new LineBorder(Color.DARK_GRAY, 1);
				button.setBorder(buttonBorder);

				board[i][j] = button;
				panel.add(board[i][j]);
			}
		}
		frame.setVisible(true);
		guiStatus = gameStatus2.boardLoaded;
	}
	
	public JButton[][] getBoard(){
		return board;
	}
	
	public gameStatus2 getStatus(){
		return guiStatus;
	}
	
	
	// ------------------------------------------------------------------------

	private JFrame createNewFrame(String title, Dimension d){
		JFrame frame = new JFrame(title);
		frame.setSize(d.width, d.height);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		return frame;
	}
}
