package boardgames;

//This is a test

public class Command {

	private Coordinate coord1, coord2;
	private String playerId;
	private boolean gameOver;
	private Piece[][] board;

	public Command(String playerId, Coordinate c1, Coordinate c2) {
		setPlayerId(playerId);
		setCoord1(c1);
		setCoord2(c2);
	}

	public Command(String playerId, Coordinate c1) {
		setPlayerId(playerId);
		setCoord1(c1);
		setCoord2(null);
	}

	public Command(Coordinate c1) {
		setPlayerId(null);
		setCoord1(c1);
		setCoord2(null);
	}
	
	public Command(Coordinate c1, Coordinate c2) {
		setPlayerId(null);
		setCoord1(c1);
		setCoord2(c2);
	}
	
	public Command(Piece[][] board, boolean gameOver) {
		setGameOver(gameOver);
		setBoard(board);	
	}

	public Coordinate getCoord1() {
		return coord1;
	}

	public void setCoord1(Coordinate coord1) {
		this.coord1 = coord1;
	}

	public Coordinate getCoord2() {
		return coord2;
	}

	public void setCoord2(Coordinate coord2) {
		this.coord2 = coord2;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public Piece[][] getBoard() {
		return board;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}

}
