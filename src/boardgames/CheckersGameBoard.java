package boardgames;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

public class CheckersGameBoard implements GameBoard {

	static final int BOARDSIZE = 8;
	static final String BLACK = "BLACK";
	static final String RED = "RED";
	static final String REDKING = "REDKING";
	static final String BLACKKING = "BLACKKING";

	private String currentPlayerId;
	private Piece[][] board;
	
	private gameStatus gamestatus;
	ArrayList<CheckersMove> continuousJumps;
	
	private GameBoardGUI checkersGUI;

	// Initializes new Checkers Game
	public CheckersGameBoard() {
		currentPlayerId = RED;
		gamestatus = boardgames.gameStatus.inProgress;
		initializeBoard();
		initializeGUI();
	}

	//===============================================
	// Board Set Up
	//===============================================
	
	// Initializes new Checkers Game Board
	private void initializeBoard() {
		board = new Piece[BOARDSIZE][BOARDSIZE];
		for (int x = 0; x < BOARDSIZE; x++) {
			for (int y = 0; y < BOARDSIZE; y++) {
				Coordinate c = new Coordinate(x, y);
				board[x][y] = new Piece(c);
			}
		}		
		setUpPieces();
	}
	
	// Sets up initial board with correct pieces
	private void setUpPieces() {
		for (int x = 0; x < BOARDSIZE; x++) {
			for (int y = 0; y < BOARDSIZE; y++) {
				if (x % 2 == y % 2) {
					if (y < 3)
						setPieceAt(new Coordinate(x, y), BLACK, BLACK, "assets/BLACK.GIF");
					if (y > 4)
						setPieceAt(new Coordinate(x, y), RED, RED, "assets/RED.GIF");
				}
			}
		}
	}
	
	private void drawBoard() {
		for (int y = 0; y < BOARDSIZE; y++) {
			if (y == 0) {
				System.out.print("Y\\X");
				for (int x = 0; x < BOARDSIZE; x++) {
					System.out.print("{"+x+" }");
				}
				System.out.print("\n");
			}
			System.out.print("{"+y+"}");
			for (int x = 0; x < BOARDSIZE; x++) {

				if (getPieceAt(new Coordinate(x,y)).getId() == null) {
					System.out.print("[  ]");
				}
				else if (getPieceAt(new Coordinate(x,y)).getPlayerId().equals(RED)) {
					System.out.print("[R");
					if (getPieceAt(new Coordinate(x,y)).getId().equals(RED))
						System.out.print("R]");
					else if (getPieceAt(new Coordinate(x,y)).getId().equals(REDKING))
						System.out.print("K]");
				}
				else if (getPieceAt(new Coordinate(x,y)).getPlayerId().equals(BLACK)) {
					System.out.print("[B");
					if (getPieceAt(new Coordinate(x,y)).getId().equals(BLACK))
						System.out.print("B]");
					else if (getPieceAt(new Coordinate(x,y)).getId().equals(BLACKKING))
						System.out.print("K]");
				}					
			}
			System.out.print("\n");
		}
		
	}

	// Returns board
	public Piece[][] getBoard() {
		return board;
	}
	
	private void initializeGUI() {
		checkersGUI = new GameBoardGUI("Checkers", BOARDSIZE, BOARDSIZE);
		checkersGUI.setGameBoardColors(Color.RED, Color.BLACK);
	}
	
	@Override
	public int getBoardHeight() {
		return BOARDSIZE;
	}

	@Override
	public int getBoardWidth() {
		return BOARDSIZE;
	}
	
	//====================================================
	// Core Functions
	//====================================================
	
	// Checks if current player has no moves, then other player wins
	private void isGameOver() {
		ArrayList<CheckersMove> legalMoves = getLegalMoves();
		if (legalMoves.isEmpty()) {
			if (currentPlayerId.equals(RED))
				gamestatus = boardgames.gameStatus.winnerPlayer1;
			if (currentPlayerId.equals(BLACK))
				gamestatus = boardgames.gameStatus.winnerPlayer2;
			else
				gamestatus = boardgames.gameStatus.tieGame;
		}
	}
	
	/*
	 Checks if move is valid.
	 First Checks if there is a series of continuous mandatory jumps occurring.
	 If not multiple jumping, checks current command against all legal moves.
	*/
	@Override
	public boolean commandIsValid(Command c) {

		if (currentPlayerId.equals(c.getPlayerId()) && getPieceAt(c.getCoord1()).getPlayerId() != null && getPieceAt(c.getCoord1()).getPlayerId().equals(currentPlayerId)) {

			CheckersMove move = new CheckersMove(c.getCoord1(), c.getCoord2());
			ArrayList<CheckersMove> legalMoves;

			if (continuousJumps == null || continuousJumps.isEmpty())
				legalMoves = getLegalMoves();
			else
				legalMoves = continuousJumps;
			
			if (!legalMoves.isEmpty()) {
				if (legalMoves.contains(move))
					return true;
			}
		}
		return false;
	}
	
	/*
	 *  Executes command assuming the move is valid.
	 *  First makes move, calling makeMove().
	 *  Then kings piece if needed.
	 *  Then checks if move is jump, then deletes jumped piece.
	 *  If piece was just kinged, move is over. Otherwise, check for chain of jumps.
	 *  If other possible jumps are available, then store list of jumps as only set of possible next moves.
	 *  If chain of jumps, keeps current player. Else, switch player.
	 *  Checks for game-over condition. Calls checkGameOver().
	 */
	public void makeMove(Command c) {
		CheckersMove move = new CheckersMove(c.getCoord1(), c.getCoord2());
		makeMove(move);
		
		boolean kinged = kingPieceAt(move.getTo());
		
		if (move.isJump()) {
			Coordinate jumped = getJumpCoordinates(move.getFrom(), move.getTo());
			clearPieceAt(jumped);
			
			if (!kinged) {
				continuousJumps = getLegalJumpsFrom(move.getTo());
				if (continuousJumps.isEmpty())
					switchPlayer();
			}
			else
				switchPlayer();
		}
		else
			switchPlayer();
		
		isGameOver();
	}
	
	public GameBoardGUI getGameBoardGUI() {
		return checkersGUI;
	}
	
	//=======================================================
	// Piece manipulation Functions
	//=======================================================
	
	// Returns piece at coordinate
	public Piece getPieceAt (Coordinate c) {
		return board[c.getX()][c.getY()];
	}

	// Sets piece at coordinate c to id and playerId of piece p.
	// Used primarily when making moves.
	public void setPieceAt(Coordinate c, Piece p) {
		getPieceAt(c).setId(p.getId());
		getPieceAt(c).setPlayerId(p.getPlayerId());
		getPieceAt(c).setImageLocation(p.getImageLocation());
	}
	
	// Sets piece at coordinate c to id and playerId.
	// Used primarily when initializing board.
	private void setPieceAt(Coordinate c, String id, String playerId, String imageLocation) {
		getPieceAt(c).setId(id);
		getPieceAt(c).setPlayerId(playerId);
		getPieceAt(c).setImageLocation(imageLocation);
	}
	
	// Sets piece at coordinate c to empty.
	// Used primarily when moving pieces.
	private void clearPieceAt(Coordinate c) {
		getPieceAt(c).setId(null);
		getPieceAt(c).setPlayerId(null);
		getPieceAt(c).setImageLocation("");
	}
	
	// Kings piece
	private boolean kingPieceAt(Coordinate c) {
		if (c.getY() == 0 && currentPlayerId.equals(RED) && getPieceAt(c).getId().equals(RED)) {
			getPieceAt(c).setId(REDKING);
			getPieceAt(c).setImageLocation("assets/REDKING.GIF");
			return true;
		}
		else if (c.getY() == BOARDSIZE-1 && currentPlayerId.equals(BLACK) && getPieceAt(c).getId().equals(BLACK)) {
			getPieceAt(c).setId(BLACKKING);
			getPieceAt(c).setImageLocation("assets/BLACKKING.GIF");
			return true;
		}
		return false;
	}
	
	
	
	//============================================================
	// Move Checker Functions
	//============================================================
	
	/*
	 * Returns the list of all legal moves for current player.
	 * First checks if jumps are available. Calls canJump().
	 * If none, checks for normal moves. Calls canMove().
	 */
	private ArrayList<CheckersMove> getLegalMoves() {		 
		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
		
		// Checks Jumps
		for (int row = 0; row < BOARDSIZE; row++) {
			for (int col = 0; col < BOARDSIZE; col++) {
				if (getPieceAt(new Coordinate(row, col)).getPlayerId() == currentPlayerId) {
										
					if (canJump(new Coordinate(row, col), new Coordinate(row+2, col+2))) {
						moves.add(new CheckersMove(row, col, row+2, col+2)); }
					
					if (canJump(new Coordinate(row, col), new Coordinate(row-2, col+2))) {
						moves.add(new CheckersMove(row, col, row-2, col+2)); }
					
					if (canJump(new Coordinate(row, col), new Coordinate(row+2, col-2))) {
						moves.add(new CheckersMove(row, col, row+2, col-2)); }
					
					if (canJump(new Coordinate(row, col), new Coordinate(row-2, col-2))) {
						moves.add(new CheckersMove(row, col, row-2, col-2)); }
				}
			}
		}
				
		
		// If Jumps empty, checks moves
		if (moves.size() == 0) {
			for (int row = 0; row < BOARDSIZE; row++) {
				for (int col = 0; col < BOARDSIZE; col++) {
					if (getPieceAt(new Coordinate(row, col)).getPlayerId() == currentPlayerId) {
						
						if (canMove(new Coordinate(row,col), new Coordinate(row+1,col+1)))
							moves.add(new CheckersMove(row,col,row+1,col+1));
						if (canMove(new Coordinate(row,col), new Coordinate(row-1,col+1)))
							moves.add(new CheckersMove(row,col,row-1,col+1));
						if (canMove(new Coordinate(row,col), new Coordinate(row+1,col-1)))
							moves.add(new CheckersMove(row,col,row+1,col-1));
						if (canMove(new Coordinate(row,col), new Coordinate(row-1,col-1)))
							moves.add(new CheckersMove(row,col,row-1,col-1));
					}
				}
			}
		}
		
		return moves;
	}
	
	// Checks legal jumps from a coordinate.
	// Used when checking for chain of jumps.
	private ArrayList<CheckersMove> getLegalJumpsFrom (Coordinate from) {
		ArrayList<CheckersMove> jumps = new ArrayList<CheckersMove>();
		
		if (canJump(from, new Coordinate(from.getX()+2, from.getY()+2)))
			jumps.add(new CheckersMove(from, new Coordinate(from.getX()+2, from.getY()+2)));
		if (canJump(from, new Coordinate(from.getX()-2, from.getY()+2)))
			jumps.add(new CheckersMove(from, new Coordinate(from.getX()-2, from.getY()+2)));
		if (canJump(from, new Coordinate(from.getX()+2, from.getY()-2)))
			jumps.add(new CheckersMove(from, new Coordinate(from.getX()+2, from.getY()-2)));
		if (canJump(from, new Coordinate(from.getX()-2, from.getY()-2)))
			jumps.add(new CheckersMove(from, new Coordinate(from.getX()-2, from.getY()-2)));

		return jumps;
	}
	
	// Called by getLegalMoves() to check if the coordinates indicate a legal jump.
	private boolean canJump (Coordinate from, Coordinate to) {
		Coordinate jumped = getJumpCoordinates(from, to);
		
		//System.out.println(to.getX() + " " + to.getY());
				
		if (moveIsOffBoard(to))
			return false;
		else if (pieceIsAlreadyThere(to))
			return false;
		
		Piece fromPiece = getPieceAt(from);
		Piece jumpPiece = getPieceAt(jumped);
		
		if (jumpPiece.getPlayerId() == null)
			return false;
		
		if (currentPlayerId.equals(RED)) {
			
			
			if (fromPiece.getId().equals(RED) && to.getY() < from.getY() && getPieceAt(jumped).getPlayerId().equals(BLACK)) {
				return true;
			}
			if (fromPiece.getId().equals(REDKING) && jumpPiece.getPlayerId().equals(BLACK)) {
				return true;
			}
		}
		else {
			if (fromPiece.getId().equals(BLACK) && to.getY() > from.getY() && jumpPiece.getPlayerId().equals(RED))
				return true;
			if (fromPiece.getId().equals(BLACKKING) && jumpPiece.getPlayerId().equals(RED))
				return true;
		}
		return false;
	}
	
	// Called by getLegalMoves() to check if the coordinate sindicate a legal move.
	private boolean canMove (Coordinate from, Coordinate to) {
		if (moveIsOffBoard(to))
			return false;
		else if (pieceIsAlreadyThere(to))
			return false;
		else if (currentPlayerId.equals(RED)) {
			if (getPieceAt(from).getId().equals(RED) && to.getY() < from.getY()){
				return true;
			}
		}
		else {
			if (getPieceAt(from).getId().equals(BLACK) && to.getY() > from.getY()){
				return true;
			}
		}
		return false;
	}
	


	//====================================================
	// Helper Functions
	//====================================================
	
	// Checks if checked move is out of bounds.
	private boolean moveIsOffBoard (Coordinate to) {
		if (to.getY() < 0 || to.getY() >= BOARDSIZE || to.getX() < 0 || to.getX() >= BOARDSIZE)
			return true;
		return false;
	}
	
	// Checks if piece is currently in tile that is being checked.
	private boolean pieceIsAlreadyThere (Coordinate to) {
		
		return getPieceAt(to).getId() != null;
	}
	
	// Moves pieces.
	private void makeMove(CheckersMove move) {
		setPieceAt(move.getTo(), getPieceAt(move.getFrom()));
		clearPieceAt(move.getFrom());
	}
	
	// Switches current player.
	private void switchPlayer() {
		if (currentPlayerId.equals(RED))
			currentPlayerId = BLACK;
		else
			currentPlayerId = RED;
	}
	
	// Calculates jumped coordinates.
	private Coordinate getJumpCoordinates(Coordinate from, Coordinate to) {
		int x = (from.getX() + to.getX()) / 2;
		int y = (from.getY() + to.getY()) / 2;
		return new Coordinate(x, y);
	}
	
	public String getCurrentPlayer() {
		return currentPlayerId;
	}

	
	@Override
	public gameStatus getGameStatus() {
		return gamestatus;
	}
	
	
	//========================================================
	// Checkers Move - Used to execute and check possible moves
	//========================================================
	private class CheckersMove {
		private Coordinate from, to;

		CheckersMove(int fr, int fc, int tr, int tc) {
			from = new Coordinate(fr, fc);
			to = new Coordinate(tr, tc);
		}
		
		CheckersMove(Coordinate c1, Coordinate c2) {
			from = c1;
			to = c2;
		}

		public boolean isJump() {
			return Math.abs(from.getY() - to.getY()) == 2 && Math.abs(from.getX() - to.getX()) == 2;
		}

		public Coordinate getFrom() {
			return from;
		}

		public Coordinate getTo() {
			return to;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof CheckersMove))
				return false;
			if (obj == this)
				return true;
			CheckersMove move = (CheckersMove) obj;
			if (this.getFrom().equals(move.getFrom()) && this.getTo().equals(move.getTo()))
				return true;
			else
				return false;
				
		}
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		CheckersGameBoard checkers = new CheckersGameBoard();
		
		while(checkers.getGameStatus() == gameStatus.inProgress) {
			
			// Client
			checkers.drawBoard();
			System.out.println(checkers.getCurrentPlayer() + "'s Move");
			System.out.println("Enter piece to move followed by place to move to: x y x y");
			int fromx = in.nextInt();
			int fromy = in.nextInt();
			int tox = in.nextInt();
			int toy = in.nextInt();
			Command c = new Command(checkers.getCurrentPlayer(), new Coordinate(fromx, fromy), new Coordinate(tox, toy));
			
			// Server
			boolean valid = checkers.commandIsValid(c);
			while (!valid) {
				System.out.println("Invalid move. Enter move as: x y x y");
				fromx = in.nextInt();
				fromy = in.nextInt();
				tox = in.nextInt();
				toy = in.nextInt();
				
				c = new Command(checkers.getCurrentPlayer(), new Coordinate(fromx, fromy), new Coordinate(tox, toy));
				valid = checkers.commandIsValid(c);
			}
			checkers.makeMove(c);
		}
		checkers.drawBoard();
		
		if (checkers.getGameStatus() == gameStatus.winnerPlayer1)
			System.out.println("GAMEOVER! WINNER IS: " + RED);
		else if (checkers.getGameStatus() == gameStatus.winnerPlayer2)
			System.out.println("GAMEOVER! WINNER IS: " + BLACK);
		else
			System.out.println("GAMEOVER! THERE IS A DRAW");
		
		in.close();
	}	
}

