package boardgames;

import java.util.ArrayList;
import java.util.HashSet;

public class OthelloGameBoard implements GameBoard {

	static final String player1 = "whte";
	static final String player2 = "blck";
	static final int boardSize = 8;

	private String currentPlayerId; /* Whose turn it is */
	private ArrayList<ArrayList<Piece>> board;
	private ArrayList<Coordinate> flipQueue; 	/* overwritten whenever
												 * piecesToFlip is called
												 */
	public OthelloGameBoard() {
		board = new ArrayList<ArrayList<Piece>>();
		for (int x = 0; x < boardSize; x++) {
			ArrayList<Piece> tmp = new ArrayList<Piece>();
			for (int y = 0; y < boardSize; y++) {
				tmp.add(new Piece(new Coordinate(x, y)));
			}
			board.add(tmp);
		}
		
		/* Initialize center tiles */
		getPieceAt(new Coordinate(3, 3)).setId(player1);
		getPieceAt(new Coordinate(4, 4)).setId(player1);
		getPieceAt(new Coordinate(3, 3)).setPlayerId(player1);
		getPieceAt(new Coordinate(4, 4)).setPlayerId(player1);

		getPieceAt(new Coordinate(4, 3)).setId(player2);
		getPieceAt(new Coordinate(3, 4)).setId(player2);
		getPieceAt(new Coordinate(4, 3)).setPlayerId(player2);
		getPieceAt(new Coordinate(3, 4)).setPlayerId(player2);
	}


	public ArrayList<ArrayList<Piece>> getBoard() {
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

		if (getPieceAt(c.getCoord1()).getId() != null || !c.getPlayerId().equals(currentPlayerId)) {
			/*
			 * Player ID doesn't match the ID of the player who needs to make a
			 * move or the coordinate being clicked isn't empty. Immediately
			 * reject.
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
		String pId = c.getPlayerId();
		String id = pId;
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
				while (inBounds(new_coord) && opposite(id).equals(getPieceAt(new_coord).getId())) {
					temp.add(new Coordinate(new_coord));
					System.out.println(temp + " " + new_coord + " " + pId + " " + getPieceAt(new_coord).getId());
					new_coord.setX(new_coord.getX() + dx);
					new_coord.setY(new_coord.getY() + dy);
					System.out.println(temp + " " + new_coord);
				}
				if (temp.size() > 0 && inBounds(new_coord) && id.equals(getPieceAt(new_coord).getId())) {
					for (Coordinate t : temp) {
						flipQueue.add(t);
					}
				}
			}
		}



		return flipQueue;
	}

	private void convertPieces(ArrayList<Piece> pieces, String id) {
		/* Change the ID of each piece in `pieces` to `id` */
		for (Piece p : pieces) {
			p.setId(id);
		}
	}

	@Override
	public Piece getPieceAt(Coordinate c) {
		return board.get(c.getX()).get(c.getY());
	}

	@Override
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
	
	
	
	
	
	public static void main(String[] args) {
		OthelloGameBoard othelloTest = new OthelloGameBoard();
		System.out.println(othelloTest);
		othelloTest.setCurrentPlayer("whte");
		Command c = new Command("whte", new Coordinate(4, 2));
		System.out.println(c);
		System.out.println(othelloTest.commandIsValid(c));
		System.out.println(othelloTest.getFlipQueue());
	}

}
