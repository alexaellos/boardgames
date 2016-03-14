package boardgames;

import java.awt.Color;

public class GameBoardGUI {

	private String gameTitle;

	private int gameBoardWidth;
	private int gameBoardHeight;
	
	private Color boardColor1;
	private Color boardColor2;

	public GameBoardGUI(String gameTitle, int gameBoardWidth, int gameBoardHeight)
	{
		this.gameTitle = gameTitle;
		this.gameBoardWidth = gameBoardWidth;
		this.gameBoardHeight = gameBoardHeight;
		
		setGameBoardColors(Color.WHITE, Color.WHITE);
	}

	public void setGameBoardColors(Color boardColor1, Color boardColor2){
		this.boardColor1 = boardColor1;
		this.boardColor2 = boardColor2;
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
}
