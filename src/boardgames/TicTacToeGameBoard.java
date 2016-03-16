package boardgames;

import java.util.Scanner;

public class TicTacToeGameBoard implements GameBoard{
	
	public static final int ROWS = 3, COLS = 3;
	
	public static boolean gameOver;
	public static String currentPlayer;
	public static int currentRow, currentCol;
	public static int currentState;
	public static Command command;
	public static gameStatus gameStatus;
	
	public static Piece[][] board = new Piece[ROWS][COLS];
	public static Scanner in = new Scanner(System.in);
	
	private static GameBoardGUI ticTacToeGUI;
	
	public TicTacToeGameBoard() {
		ticTacToeGUI = new GameBoardGUI("Tic Tac Toe", ROWS, COLS);
	    for (int row = 0; row < ROWS; ++row) {
	       for (int col = 0; col < COLS; ++col) {
	          board[row][col] = new Piece(new Coordinate(row, col), "", null, "");
	       }
	    }
	    gameStatus = boardgames.gameStatus.inProgress;
	    currentPlayer = "X";
	}
	
	public static void main(String[] args) {
		playGame();
	}
	public static void playGame() {

	      //initGame();
	      printBoard();
	      do {
	         makeMove(currentPlayer);
	         updateStatus(currentPlayer, currentRow, currentCol);
	         printBoard();
	         if (gameStatus == boardgames.gameStatus.winnerPlayer1) {
	            System.out.println("'X' wins!");
	         } else if (gameStatus == boardgames.gameStatus.winnerPlayer2) {
	            System.out.println("'O' wins!");
	         } else if (gameStatus == boardgames.gameStatus.tieGame) {
	            System.out.println("It's a Draw!");
	         }
	         currentPlayer = (currentPlayer == "X") ? "O" : "X";
	      } while (gameStatus == boardgames.gameStatus.inProgress);
	}
	
	
	public static void makeMove(String currentTurn){
		do {
	         if (currentTurn == "X") {
	            System.out.print("Player 'X', enter your move (row[1-3] column[1-3]): ");
	         } else {
	            System.out.print("Player 'O', enter your move (row[1-3] column[1-3]): ");
	         }
	         int row = in.nextInt() - 1;
	         int col = in.nextInt() - 1;
	         Coordinate move = new Coordinate(row, col);
	         command = new Command(currentTurn, move);
		}
	    while(!commandValid(command));
		currentRow = command.getCoord1().getX();
        currentCol = command.getCoord1().getY();
        board[currentRow][currentCol].setId(command.getPlayerId());
        board[currentRow][currentCol].setImageLocation((currentPlayer == "X") ? "assets/X.GIF" : "assets/Y.GIF"); 
	}
	
	public static boolean commandValid(Command c) {
		if (c.getCoord1().getX() >= 0 && c.getCoord1().getX() < ROWS && 
				c.getCoord1().getY() >= 0 && c.getCoord1().getY() < COLS && 
				board[c.getCoord1().getX()][c.getCoord1().getY()].getId() == "") { 
            return true;
         } else {
        	 System.out.println("Invalid Move!");
            return false;
         }
	}
	
	public static void updateStatus(String currentTurn, int currentRow, int currentCol) {
	      if (hasWon(currentTurn, currentRow, currentCol)) {
	         gameStatus = (currentTurn == "X") ? boardgames.gameStatus.winnerPlayer1 : boardgames.gameStatus.winnerPlayer2;
	      } else if (isDraw()) {
	    	  gameStatus = boardgames.gameStatus.tieGame;
	      }
	   }
	
	public static boolean isDraw() {
	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < COLS; ++col) {
	            if (board[row][col].getId() == "") {
	               return false;
	            }
	         }
	      }
	      return true;
	}
	
	public static boolean hasWon(String currentTurn, int currentRow, int currentCol) {
	      return (board[currentRow][0].getId() == currentTurn
	                   && board[currentRow][1].getId() == currentTurn
	                   && board[currentRow][2].getId() == currentTurn
	              || board[0][currentCol].getId() == currentTurn
	                   && board[1][currentCol].getId() == currentTurn
	                   && board[2][currentCol].getId() == currentTurn
	              || currentRow == currentCol
	                   && board[0][0].getId() == currentTurn
	                   && board[1][1].getId() == currentTurn
	                   && board[2][2].getId() == currentTurn
	              || currentRow + currentCol == 2
	                   && board[0][2].getId() == currentTurn
	                   && board[1][1].getId() == currentTurn
	                   && board[2][0].getId() == currentTurn);
	   }
	
	
	public static void printBoard() {
	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < COLS; ++col) {
	            printCell(board[row][col].getId());
	            if (col != COLS - 1) {
	               System.out.print("|");
	            }
	         }
	         System.out.println();
	         if (row != ROWS - 1) {
	            System.out.println("-----------");
	         }
	      }
	      System.out.println();
	   }
	 
	   public static void printCell(String content) {
	      switch (content) {
	         case "":  System.out.print("   "); break;
	         case "O": System.out.print(" O "); break;
	         case "X":  System.out.print(" X "); break;
	      }
	}

	@Override
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	
	@Override
	public boolean commandIsValid(Command c) {
		return (commandValid(c)) ? true : false;
		
	}
	
	@Override
	public gameStatus getGameStatus(){
		return gameStatus;
	}
	
	@Override
	public Piece[][] getBoard(){
		return board;
	}
	
	@Override
	public int getBoardHeight(){
		return ROWS;
	}
	
	@Override
	public int getBoardWidth(){
		return COLS;
	}
	
	@Override
	public GameBoardGUI getGameBoardGUI(){
		return ticTacToeGUI;
	}
}
