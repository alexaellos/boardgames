package boardgames;

public enum Games {
	 
	Checkers{
		public GameBoard getGameBoard(){
			return new CheckersGameBoard();
		}
		
		public String toString(){
			return "Checkers";
		}
	}, 
	Tic_Tac_Toe{
		public GameBoard getGameBoard(){
			return new TicTacToeGameBoard();
		}
		
		public String toString(){
			return "Tic_Tac_Toe";
		}
	},
	Othello{
		public GameBoard getGameBoard(){
			return new OthelloGameBoard();
		}
		
		public String toString(){
			return "Othello";
		}
	};
	public abstract GameBoard getGameBoard();
}
