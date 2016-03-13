package boardgames;

import java.awt.Color;

import javax.swing.ImageIcon;

public class GameStateGui {
	
	Color boardColor1;
	Color boardColor2;
	
	String playerIcon1;
	String playerIcon2;
	
	String gameTitle;
	
	int gameBoardWidth;
	int gameBoardHeight;
	
	public GameStateGui(Color bc1, Color bc2, String icon1, String icon2, String gt, int gbw, int gbh)
	{
		boardColor1 = bc1;
		boardColor2 = bc2;
		playerIcon1 = icon1;
		playerIcon2 = icon2;
		gameTitle = gt;
		gameBoardWidth = gbw;
		gameBoardHeight = gbh;
	}
	
	public Color getBoardColor1()
	{
		return boardColor1;
	}
	
	public Color getBoardColor2()
	{
		return boardColor2;
	}
	
	public String getPlayer1Icon()
	{
		return playerIcon1;
	}
	
	public String getPlayer2Icon()
	{
		return playerIcon2;
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
}
