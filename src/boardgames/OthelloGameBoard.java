package boardgames;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class OthelloGameBoard implements GameBoard {

	static final String player1 = "whte";
	static final String player2 = "blck";
	static final int boardSize = 8;
	static GameBoardGUI guiAssets;

	private String currentPlayerId; /* Whose turn it is */
	private gameStatus gameStatus;
	private Piece[][] board;
	private ArrayList<Coordinate> flipQueue; /*
												 * overwritten whenever
												 * piecesToFlip is called
												 */

	public OthelloGameBoard() {
		board = new Piece[8][8];
		for (int x = 0; x < boardSize; x++) {
			Piece[] tmp = new Piece[8];
			for (int y = 0; y < boardSize; y++) {
				tmp[y] = new Piece(new Coordinate(x, y));
			}
			board[x] = tmp;
		}
		setCurrentPlayer(player1);
		setGameStatus(boardgames.gameStatus.inProgress);

		getPieceAt(new Coordinate(3, 3)).setId(player1);
		getPieceAt(new Coordinate(4, 4)).setId(player1);
		getPieceAt(new Coordinate(3, 3)).setPlayerId(player1);
		getPieceAt(new Coordinate(4, 4)).setPlayerId(player1);

		getPieceAt(new Coordinate(4, 3)).setId(player2);
		getPieceAt(new Coordinate(3, 4)).setId(player2);
		getPieceAt(new Coordinate(4, 3)).setPlayerId(player2);
		getPieceAt(new Coordinate(3, 4)).setPlayerId(player2);
		
		if (guiAssets == null) {
			guiAssets = new GameBoardGUI("Othello", boardSize, boardSize);
			guiAssets.setGameBoardColors(Color.decode("#008000"), Color.decode("#008000"));
			guiAssets.setPlayerIcons("othello_white.png", "othello_black.png");
		}
		
		
	}

	public Piece[][] getBoard() {
		return board;
	}

	private boolean inBounds(Coordinate c) {
		return c.getX() < boardSize && c.getY() < boardSize;
	}

	private String opposite(String s) {
		if (player1.equals(s)) {
			return player2;
		}
		return player1;
	}

	@Override
	public boolean commandIsValid(Command c) {

		if (getPieceAt(c.getCoord1()).getId() != null) {
			/*
			 * If coordinate being clicked isn't empty. Immediately reject.
			 */
			return false;
		}
		if (piecesToFlip(c).size() > 0) {
			/*
			 * At least one enemy token can be flipped; move is valid.
			 */
			return true;
		}

		return false; /* Move is invalid */
	}

	private ArrayList<Coordinate> piecesToFlip(Command c) {
		/*
		 * GIVENS: The tile at coordinate c.coord1 is currently blank
		 * (corresponding piece id is null) currentPlayerId is the one making
		 * the move.
		 */

		flipQueue = new ArrayList<Coordinate>();
		Coordinate coord = c.getCoord1();
		ArrayList<Integer> directions = new ArrayList<Integer>();
		directions.add(-1);
		directions.add(0);
		directions.add(1);
		int startX = coord.getX();
		int startY = coord.getY();

		for (int dy : directions) {
			for (int dx : directions) {
				HashSet<Coordinate> temp = new HashSet<Coordinate>();
				Coordinate new_coord = new Coordinate(startX + dx, startY + dy);
				if (dx == 0 && dy == 0) {
					continue; /* We can't work with 0, 0 */
				}
				while (inBounds(new_coord) && opposite(currentPlayerId).equals(getPieceAt(new_coord).getId())) {
					temp.add(new Coordinate(new_coord));
					new_coord.setX(new_coord.getX() + dx);
					new_coord.setY(new_coord.getY() + dy);
				}
				if (temp.size() > 0 && inBounds(new_coord) && currentPlayerId.equals(getPieceAt(new_coord).getId())) {
					for (Coordinate t : temp) {
						flipQueue.add(t);
					}
				}
			}
		}

		return flipQueue;
	}

	private void checkGameState() {
		/*
		 * Checks for gameOver or no-move-allowed conditions and handles them
		 * appropriately
		 */
		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				if (commandIsValid(new Command(new Coordinate(x, y)))) {
					return; /* At least one possible command; we're good */
				}
			}
		}
		setCurrentPlayer(
				opposite(getCurrentPlayer())); /* Player loses his/her turn */
		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				if (commandIsValid(new Command(new Coordinate(x, y)))) {
					return; /*
							 * At least one possible command for other player;
							 * we're good
							 */
				}
			}
		}
		setGameStatus(
				boardgames.gameStatus.gameOver); /*
													 * No possible moves for either
													 * party. Game is over.
													 */
		onGameOver(); /* Specify the final outcome of the game */
	}

	public void makeMove(Command c) {
		/*
		 * GIVEN: Command `c` is already valid and is ready to be made
		 */
		piecesToFlip(c); /* populates flipQueue with correct data */
		for (Coordinate x : flipQueue) {
			System.out.println("Flipping " + getPieceAt(x) + " to " + currentPlayerId);
			getPieceAt(x).setId(currentPlayerId);
			getPieceAt(x).setPlayerId(currentPlayerId);
		}
		getPieceAt(c.getCoord1()).setId(currentPlayerId);
		getPieceAt(c.getCoord1()).setPlayerId(currentPlayerId);
		/* convertPieces(flipQueue, currentPlayerId); */
		setCurrentPlayer(opposite(getCurrentPlayer()));
		checkGameState();
	}

	public Piece getPieceAt(Coordinate c) {
		return board[c.getX()][c.getY()];
		/* return boardc.getX()).get(c.getY()); */
	}

	public void setPieceAt(Coordinate c, Piece p) {
		getPieceAt(c).setCoord(p.getCoord());
		getPieceAt(c).setId(p.getId());
		getPieceAt(c).setPlayerId(p.getPlayerId());
	}

	@Override
	public String getCurrentPlayer() {
		return currentPlayerId;
	}
	
	@Override
	public int getBoardHeight() {
		return boardSize;
	}

	@Override
	public int getBoardWidth() {
		return boardSize;
	}


	public void setCurrentPlayer(String s) {
		currentPlayerId = s;
	}

	public String toString() {
		String s = "";
		for (int y = 0; y < boardSize; y++) {
			for (int x = 0; x < boardSize; x++) {
				s += getPieceAt(new Coordinate(x, y)) + "    ";
			}
			s += "\n";
		}
		return s;
	}

	public ArrayList<Coordinate> getFlipQueue() {
		return flipQueue;
	}

	public void setFlipQueue(ArrayList<Coordinate> flipQueue) {
		this.flipQueue = flipQueue;
	}

	private int getScoreFor(String player) {
		int score = 0;
		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				if (getPieceAt(new Coordinate(x, y)).getId().equals(player)) {
					score++;
				}
			}
		}
		return score;
	}

	private void onGameOver() {
		int p1Score = getScoreFor(player1);
		int p2Score = getScoreFor(player2);
		if (p1Score > p2Score) {
			setGameStatus(boardgames.gameStatus.winnerPlayer1);
		} else if (p1Score < p2Score) {
			setGameStatus(boardgames.gameStatus.winnerPlayer2);
		} else {
			setGameStatus(boardgames.gameStatus.tieGame);
		}
	}

	public gameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(gameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public static void main(String[] args) {
		OthelloGameBoard othelloTest = new OthelloGameBoard();
		System.out.println(othelloTest);

		Command c = new Command(new Coordinate(4, 2));
		System.out.println(othelloTest.commandIsValid(c));
		System.out.println(othelloTest.getFlipQueue());
		othelloTest.makeMove(c);

		System.out.println(othelloTest);
		System.out.println(othelloTest.getCurrentPlayer());
		c.setCoord1(new Coordinate(5, 4));
		System.out.println(othelloTest.commandIsValid(c));
		System.out.println(othelloTest.getFlipQueue());
		othelloTest.makeMove(c);

		System.out.println(othelloTest);
		System.out.println(othelloTest.getCurrentPlayer());
		c.setCoord1(new Coordinate(6, 5));
		System.out.println(othelloTest.commandIsValid(c));
		System.out.println(othelloTest.getFlipQueue());
		othelloTest.makeMove(c);

		System.out.println(othelloTest);
		System.out.println(othelloTest.getCurrentPlayer());
		c.setCoord1(new Coordinate(4, 1));
		System.out.println(othelloTest.commandIsValid(c));
		System.out.println(othelloTest.getFlipQueue());
		othelloTest.makeMove(c);

		System.out.println(othelloTest);

	}

	@Override
	public GameBoardGUI getGameBoardGUI() {
		return guiAssets;
	}

}
