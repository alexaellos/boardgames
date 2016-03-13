package boardgames;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GUI implements ActionListener, GUIUpdater{

	private int FRAME_WIDTH = 720;
	private int FRAME_HEIGHT = 720;

	private JFrame startFrame;
	private JFrame gameMenuFrame;
	private JFrame loadFrame;
	private JFrame mainFrame;
	
	private JFrame boardGameFrame;
	private JPanel boardGamePanel;
	
	private JPanel panel;
	
	private JLabel currentPlayer;
	private JButton pressedButton;

	private boolean isPlayer1Turn;

	private Color boardColor1;
	private Color boardColor2;

	private ImageIcon player1Icon;
	private ImageIcon player2Icon;

	private JButton[][] boardGrid;

	private String player1Id;
	private String player2Id;

	public GUI()
	{

	}

	public void loadStartFrame()
	{
		startFrame = new JFrame();
		startFrame.setTitle("Log In");
		startFrame.setSize(400, 200);
		startFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		startFrame.setLocationRelativeTo(null);
		startFrame.setResizable(false);

		panel = new JPanel(new GridBagLayout());

		JLabel labelUsername = new JLabel("Enter username: ");
		JTextField textUsername = new JTextField(20);
		JButton connect = new JButton("Connect");

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(labelUsername, constraints);

		constraints.gridx = 1;
		panel.add(textUsername, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		panel.add(connect, constraints);

		connect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				loadGameMenuFrame();
			}
		});

		startFrame.add(panel);
		startFrame.setVisible(true);
	}

	private void loadGameMenuFrame()
	{
		startFrame.setVisible(false);
		gameMenuFrame = createNewFrame("Game Menu", 400, 200);

		panel = new JPanel(new GridBagLayout());

		JLabel game = new JLabel("Please select a game: ");
		JButton ticTacToe = new JButton("Tic Tac Toe");
		JButton checkers = new JButton("Checkers");
		JButton othello = new JButton("Othello");

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(game, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(ticTacToe, constraints);

		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		panel.add(checkers, constraints);

		constraints.gridx = 2;
		panel.add(othello, constraints);

		ticTacToe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				setBoardColors(Color.WHITE, Color.WHITE);
				loadPlayerIcons("X.GIF", "O.GIF");		// Icons need to be 240 x 240
				JFrame boardGame = loadBoardGameFrame("Tic Tac Toe", 3, 3);
				boardGame.setVisible(true);
			}
		});

		checkers.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				setBoardColors(Color.BLACK, Color.RED);
				loadPlayerIcons("X.GIF", "O.GIF");		// Icons need to be 90 x 90
				JFrame boardGame = loadBoardGameFrame("Checkers", 8, 8);
				boardGame.setVisible(true);
			}

		});

		othello.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				setBoardColors(Color.BLACK, Color.WHITE);
				loadPlayerIcons("X.GIF", "O.GIF");
				JFrame boardGame = loadBoardGameFrame("Othello", 8, 8);
				boardGame.setVisible(true);
			}

		});
		gameMenuFrame.add(panel);
		gameMenuFrame.setVisible(true);
	}


	// Waiting screen when waiting for another client
	private void loadWaitingFrame()
	{
		gameMenuFrame.setVisible(false);

		loadFrame = createNewFrame("Loading Screen", 400, 200);
		panel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);


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
				loadGameMenuFrame();
			}

		});

		loadFrame.add(panel);
		loadFrame.setVisible(true);	
	}

	private JFrame loadBoardGameFrame(String gameTitle, int gridWidth, int gridHeight){
		// Initialize player 1's turn
		isPlayer1Turn = true;

		// Create a new frame
		boardGameFrame = createNewFrame(gameTitle, FRAME_WIDTH, FRAME_HEIGHT);

		// Create a new JPanel
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
					gridButton.setForeground(boardColor1);
					gridButton.setBackground(boardColor1);
				}
				else{
					gridButton.setBackground(boardColor2);
					gridButton.setForeground(boardColor2);
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

	private JFrame createNewFrame(String title, int width, int height){
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		return frame;
	}

	private void setBoardColors(Color color1, Color color2){
		boardColor1 = color1;
		boardColor2 = color2;
	}

	private void loadPlayerIcons(String player1IconImgLocation, String player2IconImgLocation){
		player1Icon = new ImageIcon(getClass().getResource(player1IconImgLocation));
		player2Icon = new ImageIcon(getClass().getResource(player2IconImgLocation));
	}

	public void updateGameBoardGUI(String playerName, ArrayList<ArrayList<Piece>> board){
		currentPlayer = new JLabel("Currently " + playerName + "'s Turn");

		Container container = boardGameFrame.getContentPane();
		container.remove(boardGamePanel);

		for(int i = 0; i < board.size(); i++){
			for(int j = 0; j < board.get(i).size(); j++){
				Piece currentPiece = board.get(i).get(j);
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
	public void actionPerformed(ActionEvent e) {
		pressedButton = (JButton) e.getSource();

		// TIC TAC TOE GUI LOGIC
		if (pressedButton.getIcon() == null && isPlayer1Turn){
			pressedButton.setIcon(player1Icon);
			isPlayer1Turn = false;
		} 
		else if(pressedButton.getIcon() == null && !isPlayer1Turn){
			pressedButton.setIcon(player2Icon);
			isPlayer1Turn = true;
		}
	}

	@Override
	public String getGUIChange() {
		return "";
	}

	@Override
	public String loadGameBoardGUI(GameStateGui game) {
		
	/* Game[Board]GUI class needs to store the following:
	 * - Tile background 1 color
	 * - Tile background 2 color
	 * - Player 1 icon
	 * - Player 2 icon
	 * - Game title
	 * - Game board width
	 * - Game board height
	 * */
		setBoardColors(game.getBoardColor1(), game.getBoardColor2());
		loadPlayerIcons(game.getPlayer1Icon(), game.getPlayer2Icon());
		JFrame boardGame = loadBoardGameFrame(game.getTitle(), game.getBoardWidth(), game.getBoardHeight());
		boardGame.setVisible(true);
		return "";
	}

	@Override
	public String startGUI(ArrayList<String> gameList) {
		return "";
	}

}
