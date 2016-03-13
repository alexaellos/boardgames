package boardgames;

import java.awt.Color;

import javax.swing.ImageIcon;

public class GameStateGUI {

	private String gameTitle;

	private int gameBoardWidth;
	private int gameBoardHeight;
	
	private Color boardColor1;
	private Color boardColor2;

	private ImageIcon player1Icon;
	private ImageIcon player2Icon;

	public GameStateGUI(String gameTitle, int gameBoardWidth, int gameBoardHeight)
	{
		this.gameTitle = gameTitle;
		this.gameBoardWidth = gameBoardWidth;
		this.gameBoardHeight = gameBoardHeight;
		
		setGameBoardColors(Color.WHITE, Color.WHITE);
		setPlayerIcons("","");
	}

	public void setGameBoardColors(Color boardColor1, Color boardColor2){
		this.boardColor1 = boardColor1;
		this.boardColor2 = boardColor2;
	}

	public void setPlayerIcons(String player1IconLocation, String player2IconLocation){
		player1Icon = new ImageIcon(getClass().getResource(player1IconLocation));
		player2Icon = new ImageIcon(getClass().getResource(player2IconLocation));
	}
	
	public String getTitle()
	{
		return gameTitle;
	}

	public int getBoardWidth()
	{
		return gameBoardWidth;
	}

	public int getBoardHeight()
	{
		return gameBoardHeight;
	}

	public Color getBoardColor1()
	{
		return boardColor1;
	}

	public Color getBoardColor2()
	{
		return boardColor2;
	}

	public ImageIcon getPlayer1Icon()
	{
		return player1Icon;
	}

	public ImageIcon getPlayer2Icon()
	{
		return player2Icon;
	}
}
