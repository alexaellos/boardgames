package boardgames;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GUI {
	
	private Image screen;
	public JFrame startFrame;
	public JFrame gameMenuFrame;
	public JFrame loadFrame;
	public JFrame mainFrame;
	
	public JPanel panel;
	private JPanel boardPanel = new JPanel();
	
	private JButton[][] boardButtons;
	private ImageIcon emptyIcon = new ImageIcon("resources/othelloEmpty.GIF");
	
	public ImageIcon icon;
	public ImageIcon iconX;
	public ImageIcon iconO;
	
	public boolean turnX;
	
	
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
		//frame.setResizable(false);
		startFrame.setVisible(true);
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

	}
	
	public void loadGameMenuFrame()
	{
		startFrame.setVisible(false);
		gameMenuFrame = new JFrame();
		gameMenuFrame.setTitle("Log In");
		gameMenuFrame.setSize(400, 200);
		gameMenuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gameMenuFrame.setLocationRelativeTo(null);
		gameMenuFrame.setResizable(false);
		gameMenuFrame.setVisible(true);
		
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
				loadTicTacToeBoardGameFrame();
			}
			
		});
		
		checkers.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				loadCheckersBoardGameFrame();
			}
			
		});
		
		othello.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				loadOthelloBoardGameFrame();
			}
			
		});
		
		gameMenuFrame.add(panel);

	}
	
	
	// Waiting screen when waiting for another client
	public void loadWaitingFrame()
	{
		gameMenuFrame.setVisible(false);
		loadFrame = new JFrame();
		loadFrame.setTitle("Loading Screen");
		loadFrame.setSize(400, 200);
		loadFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		loadFrame.setLocationRelativeTo(null);
		loadFrame.setResizable(false);
		loadFrame.setVisible(true);
		
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
		
	}
	
	// Icons need to be 240 x 240
	public void loadTicTacToeBoardGameFrame()
	{		
		gameMenuFrame.setVisible(false);
		mainFrame = new JFrame();
		mainFrame.setTitle("Tic Tac Toe");
		mainFrame.setSize(720, 720);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		
		icon = new ImageIcon(getClass().getResource("TicTacToe.GIF"));
		iconX = new ImageIcon(getClass().getResource("X.GIF"));
		iconO = new ImageIcon(getClass().getResource("O.GIF"));
		
		Container cP = mainFrame.getContentPane();
		cP.add(boardPanel, BorderLayout.CENTER);
		
		boardButtons = new JButton[3][3];
		boardPanel.setLayout(new GridLayout(3, 3));
		turnX = true;
		for (int i=0; i<3; i++)
		{
			for (int j=0; j<3; j++)
			{
				boardButtons[i][j] = new JButton();
				boardButtons[i][j].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {

						JButton pressed = (JButton) (e.getSource());

				        if (pressed.getIcon().equals(iconO) || pressed.getIcon().equals(iconX)) {
				            return;
				        }

				        if (turnX) {
				            pressed.setIcon(iconX);
				            turnX = false;
				        } else {
				            pressed.setIcon(iconO);
				            turnX = true;
				        }

					}});
				boardButtons[i][j].setIcon(icon);
				boardButtons[i][j].setActionCommand("" + i + "" + j);
				boardPanel.add(boardButtons[i][j]);
			}
		}
		
		
	}
	
	// Icons need to be 90 x 90
	public void loadCheckersBoardGameFrame()
	{
		gameMenuFrame.setVisible(false);
		mainFrame = new JFrame();
		mainFrame.setTitle("Checkers");
		mainFrame.setSize(720, 720);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		
		Container cP = mainFrame.getContentPane();
		cP.add(boardPanel, BorderLayout.CENTER);
		
		boardButtons = new JButton[8][8];
		boardPanel.setLayout(new GridLayout(8, 8));
		for (int i=0; i<8; i++)
		{
			for (int j=0; j<8; j++)
			{
				boardButtons[i][j] = new JButton("");
				boardButtons[i][j].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event) {
						updateGameFrame();
					}});
				boardButtons[i][j].setIcon(emptyIcon);
				boardButtons[i][j].setActionCommand("" + i + "" + j);
				boardPanel.add(boardButtons[i][j]);
			}
		}
	}
	
	public void loadOthelloBoardGameFrame()
	{
		gameMenuFrame.setVisible(false);
		mainFrame = new JFrame();
		mainFrame.setTitle("Othello");
		mainFrame.setSize(720, 720);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		
		Container cP = mainFrame.getContentPane();
		cP.add(boardPanel, BorderLayout.CENTER);
		
		boardButtons = new JButton[8][8];
		boardPanel.setLayout(new GridLayout(8, 8));
		for (int i=0; i<8; i++)
		{
			for (int j=0; j<8; j++)
			{
				boardButtons[i][j] = new JButton("");
				boardButtons[i][j].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event) {
						updateGameFrame();
					}});
				boardButtons[i][j].setIcon(emptyIcon);
				boardButtons[i][j].setActionCommand("" + i + "" + j);
				boardPanel.add(boardButtons[i][j]);
			}
		}
	}
	
	public void updateGameFrame()
	{
		
	}
	
	
	
}
