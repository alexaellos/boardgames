package GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import boardgames.GameBoardGUI;
import boardgames.Games;
import boardgames.Piece;
import boardgames.gameStatus;

public class GUI implements ActionListener, GUIUpdater{

	private int FRAME_WIDTH = 720;
	private int FRAME_HEIGHT = 720;

	private JFrame currentFrame;
	private JFrame loadFrame;

	private JFrame boardGameFrame;
	private JPanel boardGamePanel;

	private JPanel panel;

	private JButton pressedButton;

	private boolean isPlayer1Turn;

	private JButton[][] boardGrid;

	private String player1Id;
	private String player2Id;

	public String playerName;
	private Games[] gamesList;

	public GUI()
	{
		gamesList = Games.values();
		
	}

	@Override
	public gameStatus startGUI() {

		//loadStartFrame();
		String loadingYet = "";
		while(!loadingYet.equals("done")){
			// do something

			if(loadingYet.equals("done")){
				break;
			}
		}
		return gameStatus.gameSelected;
	}

	public void loadFrame(GUIFrames GUI){
		JFrame previousFrame = currentFrame;
		
		switch(GUI){
		case loadStart:
			currentFrame = loadStartFrame(GUIFrames.loadGameMenu);
			break;
		case loadGameMenu:
			currentFrame = loadGameMenuFrame(previousFrame, GUIFrames.loadWaiting);
			break;
		case loadWaiting:
			loadWaitingFrame(previousFrame);
			break;
			//		case loadGameBoard:
			//			break;
		default:
			loadStartFrame(GUIFrames.loadGameMenu);
			break;
		}
	}

	private JFrame loadStartFrame(final GUIFrames nextFrame)
	{
		JFrame startFrame = createNewFrame("Log In", 400, 200);

		panel = new JPanel(new GridBagLayout());

		JLabel nameLabel = new JLabel("Enter name: ");
		JTextField nameTextField = new JTextField(20);
		playerName = nameTextField.getText();
		JButton connect = new JButton("Connect");

		GridBagConstraints constraints = setGridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(nameLabel, constraints);

		constraints.gridx = 1;
		panel.add(nameTextField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		panel.add(connect, constraints);

		connect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				//loadGameMenuFrame(startFrame);
				loadFrame(nextFrame);
			}
		});

		startFrame.add(panel);
		startFrame.setVisible(true);

		return startFrame;
	}

	private JFrame loadGameMenuFrame(JFrame previousFrame, final GUIFrames nextFrame)
	{
		previousFrame.setVisible(false);
		JFrame gameMenuFrame = createNewFrame("Game Menu", 400, 200);

		panel = new JPanel(new GridBagLayout());

		JLabel gameMenuLabel = new JLabel("Please select a game: ");
		
		GridBagConstraints constraints = setGridBagConstraints();
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(gameMenuLabel, constraints);
		
		for(int i = 0; i < gamesList.length; i++){
			String gameTitle = gamesList[i].getGameBoard().getGameBoardGUI().getTitle();
			JButton gameSelectionBtn = new JButton(gameTitle);
			
			constraints.gridx = i;
			constraints.gridy = 1;
			
			// TODO: Check this because rn it centers the Checkers button
			// but is there a better way to do it?
			if(i == 1){
				constraints.anchor = GridBagConstraints.CENTER;
			}
			
			gameSelectionBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					// Icons need to be 240 x 240 for a 3x3; 90 x 90 for an 8x8
					JFrame boardGame = loadGameBoardFrame(new GameBoardGUI("Tic Tac Toe", 3, 3));
					boardGame.setVisible(true);
					loadFrame(nextFrame);
				}
			});
			
			panel.add(gameSelectionBtn, constraints);
		}

		gameMenuFrame.add(panel);
		gameMenuFrame.setVisible(true);
		
		return gameMenuFrame;
	}

	// Waiting screen when waiting for another client
	private void loadWaitingFrame(JFrame previousFrame)
	{
		previousFrame.setVisible(false);

		loadFrame = createNewFrame("Loading Screen", 400, 200);
		panel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = setGridBagConstraints();

		JLabel waitMsg = new JLabel("Please wait while we search for other players...");
		JButton exit = new JButton("Exit");

		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(waitMsg, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		panel.add(exit, constraints);

		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				loadFrame.setVisible(false);
				loadFrame(GUIFrames.loadGameMenu);
			}

		});

		loadFrame.add(panel);
		loadFrame.setVisible(true);	
	}

	private JFrame loadGameBoardFrame(GameBoardGUI gameGUI){
		// Initialize player 1's turn
		isPlayer1Turn = true;

		// Create a new frame
		JFrame boardGameFrame = createNewFrame(gameGUI.getTitle(), FRAME_WIDTH, FRAME_HEIGHT);

		// Create a new JPanel
		int gridWidth = gameGUI.getBoardWidth();
		int gridHeight = gameGUI.getBoardHeight();
		JPanel panel = new JPanel(new GridLayout(gridWidth, gridHeight));

		// Add the JPanel to the frame's content pane
		Container container = boardGameFrame.getContentPane();
		container.add(panel, BorderLayout.CENTER);

		// Initialize a grid of buttons for the board game
		boardGrid = new JButton[gridWidth][gridHeight];

		// Add JButton objects to the grid of buttons
		for (int i = 0; i < gridWidth; i++){
			for (int j = 0; j < gridHeight; j++){
				JButton gridButton = new JButton();
				gridButton.addActionListener(this);
				gridButton.setActionCommand("" + i + "," + j + "," + "0");

				// Figure out which color to set as the background of each button
				// in the grid of buttons.
				if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)){
					gridButton.setForeground(gameGUI.getBoardColor1());
					gridButton.setBackground(gameGUI.getBoardColor1());
				}
				else{
					gridButton.setBackground(gameGUI.getBoardColor2());
					gridButton.setForeground(gameGUI.getBoardColor2());
				}
				gridButton.setOpaque(true);

				// Add a border around the buttons
				Border buttonBorder = new LineBorder(Color.BLACK, 1);
				gridButton.setBorder(buttonBorder);

				boardGrid[i][j] = gridButton;
				panel.add(boardGrid[i][j]);
			}
		}
		return boardGameFrame;
	}

	public void updateGameBoardGUI(String playerName, Piece[][] board){
		JLabel currentPlayer = new JLabel("Currently " + playerName + "'s Turn");

		Container container = boardGameFrame.getContentPane();
		container.remove(boardGamePanel);

		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				Piece currentPiece = board[i][j];
				JButton gridButton = boardGrid[i][j];

				String player = currentPiece.getPlayerId();
				String actionCommandStr = "" + i + "," + j + ",";

				if(player.equals(player1Id)){
					actionCommandStr += "1";
				}
				else if(player.equals(player2Id)){
					actionCommandStr += "2";
				}
				gridButton.setActionCommand(actionCommandStr);
				boardGrid[i][j] = gridButton;
				boardGamePanel.add(boardGrid[i][j]);
			}
		}
		container.add(boardGamePanel, BorderLayout.CENTER);
	}

	@Override
	public String loadGameBoardGUI(GameBoardGUI gameGUI) {
		JFrame boardGame = loadGameBoardFrame(gameGUI);
		boardGame.setVisible(true);
		return "";
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		pressedButton = (JButton) e.getSource();

		// TIC TAC TOE GUI LOGIC
		pressedButton.getText();
	}
	
	private JFrame createNewFrame(String title, int width, int height){
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		return frame;
	}
	
	private GridBagConstraints setGridBagConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 10, 10);
		return gbc;
	}
	
	@Override
	public gameStatus restartGameMenuGUI() {
		// TODO Auto-generated method stub
		return gameStatus.gameSelected;
	}

	@Override
	public String getSelectedGameTitle() {
		return "";
	}

	@Override
	public String getPlayerName() {
		return "";
	}
	
	@Override
	public String getGUIChange() {
		return "";
	}
}
