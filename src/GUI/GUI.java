package GUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import boardgames.GameBoardGUI;
import boardgames.Games;
import boardgames.Piece;
import boardgames.gameStatus;

public class GUI implements ActionListener, GUIUpdater{

	private JFrame currentFrame;
	private int GAMEBOARD_FRAME_WIDTH = 720;
	private int GAMEBOARD_FRAME_HEIGHT = 720;

	private InputDialog dialog;
	

	private Games[] gamesList;
	private Games selectedGame;
	private gameStatus status;

	private String playerName;
	
	
	private JFrame boardGameFrame;
	private JPanel boardGamePanel;

	private JButton pressedButton;
	private JButton[][] boardGrid;

	private String player1Id;
	private String player2Id;
	
	private boolean isPlayer1Turn;

	public GUI(){
		currentFrame = createNewFrame("Game Client", new Dimension(0, 0));
		dialog = new InputDialog(currentFrame);
		gamesList = Games.values();
		playerName = "";
		selectedGame = null;
		status = gameStatus.loadingPlayer;
	}

	@Override
	public void startGUI(){
		currentFrame.setVisible(false);
		playerName = dialog.createTextInputDialog("Log In", "Enter your name:");
		
		if(playerName != null){
			if(playerName.length() > 0){
				loadGameMenu();
			}
			else{
				startGUI();
			}
		}
		else{
			status = gameStatus.exit;
		}
	}

	private void loadGameMenu(){
		currentFrame.setVisible(false);
		selectedGame = dialog.createGamesDropDownDialog("Game Menu", "Please select a game:", gamesList);
		
		if(selectedGame != null){
			status = gameStatus.gameSelected;
			loadWaitingFrame();
		}
		else{
			status = gameStatus.loadingPlayer;
			startGUI();
		}
	}

	// Waiting screen when waiting for another client
	private void loadWaitingFrame(){
		currentFrame.setVisible(false);
		
		Dimension dimension = new Dimension(400, 200);
		JFrame waitFrame = createNewFrame("Loading Screen", dimension);
		
		// Create panel
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		// Set panel sizes
		panel.setMaximumSize(dimension);
		panel.setPreferredSize(dimension);
		panel.setMinimumSize(dimension);
		
		// Frame message
		JLabel waitMsg = new JLabel("Please wait while we search for other players...");
		waitMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Exit button
		JButton exitBtn = new JButton("Exit");
		exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		exitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				status = gameStatus.loadingPlayer;
				loadGameMenu();
			}
		});
		
		// Add components to the panel
		panel.add(waitMsg);
		panel.add(Box.createVerticalGlue());
		panel.add(exitBtn);

		// Add the panel to the frame
		waitFrame.add(panel);
		
		// Set current frame to wait frame
		currentFrame = waitFrame;
		currentFrame.setVisible(true);
	}

	private JFrame loadGameBoardFrame(GameBoardGUI gameGUI){
		// Initialize player 1's turn
		isPlayer1Turn = true;

		// Create a new frame
		Dimension dimension = new Dimension(GAMEBOARD_FRAME_WIDTH, GAMEBOARD_FRAME_HEIGHT);
		JFrame boardGameFrame = createNewFrame(gameGUI.getTitle(), dimension);

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

	private JFrame createNewFrame(String title, Dimension d){
		JFrame frame = new JFrame(title);
		frame.setSize(d.width, d.height);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		return frame;
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public Games getSelectedGame() {
		return selectedGame;
	}
	
	public gameStatus getStatus(){
		return status;
	}
}
