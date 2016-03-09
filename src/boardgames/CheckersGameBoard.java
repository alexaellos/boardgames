package boardgames;

import java.util.ArrayList;

public class CheckersGameBoard implements GameBoard {

	static final int BOARDSIZE = 8;
	static final String BLACK = "BLACK";
	static final String RED = "RED";
	static final String REDKING = "REDKING";
	static final String BLACKKING = "BLACKKING";

	private String currentPlayerId;
	private ArrayList<ArrayList<Piece>> board;
	
	private boolean gameIsOver;
	private String gameWinner;
	ArrayList<CheckersMove> continuousJumps;

	// Initializes new Checkers Game
	public CheckersGameBoard() {
		board = new ArrayList<ArrayList<Piece>>();
		currentPlayerId = RED;
		gameIsOver = false;
		gameWinner = null;
		initializeBoard();
	}

	//===============================================
	// Board Set Up
	//===============================================
	
	// Initializes new Checkers Game Board
	private void initializeBoard() {
		for (int x = 0; x < BOARDSIZE; x++) {
			ArrayList<Piece> column = new ArrayList<Piece>();
			for (int y = 0; y < BOARDSIZE; y++) {
				column.add(new Piece(new Coordinate(x, y)));
			}
			board.add(column);
		}
		setUpPieces();
	}
	
	// Sets up initial board with correct pieces
	private void setUpPieces() {
		for (int x = 0; x < BOARDSIZE; x++) {
			for (int y = 0; y < BOARDSIZE; y++) {
				if (x % 2 == y % 2) {
					if (x < 3)
						setPieceAt(new Coordinate(x, y), BLACK, BLACK);
					if (x > 4)
						setPieceAt(new Coordinate(x, y), RED, RED);
				}
			}
		}
	}

	// Returns board
	public ArrayList<ArrayList<Piece>> getBoard() {
		return board;
	}
	
	//====================================================
	// Core Functions
	//====================================================
	
	// Checks if current player has no moves, then other player wins
	private void checkGameOver() {
		ArrayList<CheckersMove> legalMoves = getLegalMoves();
		if (legalMoves.isEmpty()) {
			gameIsOver = true;
			switchPlayer();
			gameWinner = currentPlayerId;
		}
	}
	
	/*
	 Checks if move is valid.
	 First Checks if there is a series of continuous mandatory jumps occurring.
	 If not multiple jumping, checks current command against all legal moves.
	*/
	@Override
	public boolean commandIsValid(Command c) {

		if (currentPlayerId.equals(c.getPlayerId()) && getPieceAt(c.getCoord1()) != null && getPieceAt(c.getCoord1()).getPlayerId().equals(currentPlayerId)) {
			
			CheckersMove move = new CheckersMove(c.getCoord1(), c.getCoord2());
			ArrayList<CheckersMove> legalMoves;
			
			if (continuousJumps.isEmpty())
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
	public void executeCommand(Command c) {
		CheckersMove move = new CheckersMove(c.getCoord1(), c.getCoord2());
		makeMove(move);
		
		boolean kinged = kingPieceAt(move.getTo());
		
		if (move.isJump()) {
			Coordinate jumped = getJumpCoordinates(move.getFrom(), move.getTo());
			clearPieceAt(jumped);
			
			if (!kinged) {
				ArrayList<CheckersMove> continuousJumps = getLegalJumpsFrom(move.getTo());
				if (continuousJumps.isEmpty())
					switchPlayer();
			}
		}
		else
			switchPlayer();
		
		checkGameOver();
	}
	
	//=======================================================
	// Piece manipulation Functions
	//=======================================================
	
	// Returns piece at coordinate
	@Override
	public Piece getPieceAt (Coordinate c) {
		return board.get(c.getX()).get(c.getY());
	}

	// Sets piece at coordinate c to id and playerId of piece p.
	// Used primarily when making moves.
	@Override
	public void setPieceAt(Coordinate c, Piece p) {
		getPieceAt(c).setId(p.getId());
		getPieceAt(c).setPlayerId(p.getPlayerId());
	}
	
	// Sets piece at coordinate c to id and playerId.
	// Used primarily when initializing board.
	private void setPieceAt(Coordinate c, String id, String playerId) {
		getPieceAt(c).setId(id);
		getPieceAt(c).setPlayerId(playerId);
	}
	
	// Sets piece at coordinate c to empty.
	// Used primarily when moving pieces.
	private void clearPieceAt(Coordinate c) {
		getPieceAt(c).setId(null);
		getPieceAt(c).setPlayerId(null);
	}
	
	// Kings piece
	private boolean kingPieceAt(Coordinate c) {
		if (c.getY() == 0 && currentPlayerId.equals(RED) && getPieceAt(c).getId().equals(RED)) {
			getPieceAt(c).setId(REDKING);
			return true;
		}
		else if (c.getY() == BOARDSIZE-1 && currentPlayerId.equals(BLACK) && getPieceAt(c).getId().equals(BLACK)) {
			getPieceAt(c).setId(BLACKKING);
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
					
					if (canJump(new Coordinate(row, col), new Coordinate(row+2, col+2)))
						moves.add(new CheckersMove(row, col, row+2, col+2));
					if (canJump(new Coordinate(row, col), new Coordinate(row-2, col+2)))
						moves.add(new CheckersMove(row, col, row-2, col+2));
					if (canJump(new Coordinate(row, col), new Coordinate(row+2, col-2)))
						moves.add(new CheckersMove(row, col, row+2, col-2));
					if (canJump(new Coordinate(row, col), new Coordinate(row-2, col-2)))
						moves.add(new CheckersMove(row, col, row-2, col-2));
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
		
		if (canJump(from, new Coordinate(from.getY()+2, from.getX()+2)))
			jumps.add(new CheckersMove(from, new Coordinate(from.getY()+2, from.getX()+2)));
		if (canJump(from, new Coordinate(from.getY()-2, from.getX()+2)))
			jumps.add(new CheckersMove(from, new Coordinate(from.getY()+2, from.getX()+2)));
		if (canJump(from, new Coordinate(from.getY()+2, from.getX()-2)))
			jumps.add(new CheckersMove(from, new Coordinate(from.getY()+2, from.getX()+2)));
		if (canJump(from, new Coordinate(from.getY()-2, from.getX()-2)))
			jumps.add(new CheckersMove(from, new Coordinate(from.getY()+2, from.getX()+2)));

		return jumps;
	}
	
	// Called by getLegalMoves() to check if the coordinates indicate a legal jump.
	private boolean canJump (Coordinate from, Coordinate to) {
		Coordinate jumped = getJumpCoordinates(from, to);
		
		Piece fromPiece = getPieceAt(from);
		Piece jumpPiece = getPieceAt(jumped);
		
		if (moveIsOffBoard(to) || pieceIsAlreadyThere(to))
			return false;
		if (currentPlayerId.equals(RED)) {
			if (fromPiece.getId().equals(RED) && to.getY() < from.getY() && jumpPiece.getPlayerId().equals(BLACK))
				return true;
			if (fromPiece.getId().equals(REDKING) && jumpPiece.getPlayerId().equals(BLACK))
				return true;
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
		if (moveIsOffBoard(to) || pieceIsAlreadyThere(to))
			return false;
		else if (currentPlayerId.equals(RED)) {
			if (getPieceAt(from).getId().equals(RED) && to.getY() > from.getY()){
				return false;
			}
		}
		else {
			if (getPieceAt(from).getId().equals(BLACK) && to.getY() < from.getY()){
				return false;
			}
		}
		return true;
	}
	


	//====================================================
	// Helper Functions
	//====================================================
	
	// Checks if checked move is out of bounds.
	private boolean moveIsOffBoard (Coordinate to) {
		if (to.getY() < 0 || to.getY() >= BOARDSIZE || to.getX() < 0 || to.getX() >= BOARDSIZE)
			return false;
		return true;
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
		return new Coordinate(y, x);
	}
	
	
	// Don't really need these...
	// Returns current player.
	@Override
	public String getCurrentPlayer() {
		return currentPlayerId;
	}

	// Sets current player
	@Override
	public void setCurrentPlayer (String s) {
		currentPlayerId = s;
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
	}

}