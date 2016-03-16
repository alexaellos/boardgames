package boardgames;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class server {
	
	private String player1_id;
	private String player2_id;
	private Socket socket;
	
	static int PORT = 8901;
	
	private void update(){
		
	}
	
	private void run(){
		
	}
	
	private void initSocket(){
		
	}
	
	private void send(){
		
	}

	public static void main(String[] args) throws Exception {
		ServerSocket listener = null ;
		System.out.println("Server is running");
		try {
			while (true) {
				listener = new ServerSocket(PORT);
				
				Game game = new Game();
				Game.Player playerX = game.new Player(listener.accept(), "X");
				Game.Player playerO = game.new Player(listener.accept(), "O");

				playerX.setOpponent(playerO);
				playerO.setOpponent(playerX);
				if(game.checkGamePreference(playerX.desiredGame,playerO.desiredGame)) {
					game.setGameBoard(playerX.desiredGame);
				}
				else {
					playerX.toDisappoint();
					playerO.toDisappoint();
					listener.close();
					continue;
				}
				game.currentPlayer = playerX;
				playerX.start();
				playerO.start();
			}
		}
		finally {
			listener.close();
		}
    
	}


}
class Game {
	private GameBoard instance;
	Player currentPlayer;
	//don't need board
	//don't need board will have hasWinner();
	//don't need board filled up

	public boolean checkGamePreference(String desiredGame, String desiredGame2) {
		if (desiredGame.equals(desiredGame2)){
			return true;
		}
		else {
			return false;
		}
	}
	public void setGameBoard(String player1) {
		if (player1.equals("Tic_Tac_Toe")) {
			instance = new TicTacToeGameBoard();
		} else if (player1.equals("Othello")) {
			instance = new OthelloGameBoard();
		} else if (player1.equals("Checkers")) {
			instance = new CheckersGameBoard();
		}
	}

	public synchronized boolean legalMove(Command c , Player player) {
		if (instance.commandIsValid(c)) {
    	//commandisvalid makes the move too if its true
//      Coordinate c1 = c.getCoord1();
//      Coordinate c2 = c.getCoord2();
//      Piece p1 = new Piece(c1);
//      instance.setPieceAt(c2, p1);

			currentPlayer = currentPlayer.opponent;
			currentPlayer.sendBoard();
			currentPlayer.sendCurrentPlayer();
			return true;
		}
		return false;
	}


  class Player extends Thread {
  	private String name;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String mark;
    private Player opponent;
    String desiredGame;
    
     /**
     * Constructs a handler thread, squirreling away the socket.
     * All the interesting work is done in the run method.
     */
    public Player(Socket socket, String mark) {
      this.socket = socket;
      this.mark = mark;
      try {
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        getDesiredGame();
      } catch (IOException e) {
        System.out.println("DISCONNECT: Player could not connect or he died");
      }
    } 
    
    public void updateName() {
    	try {
    		mark = (String) input.readObject();
    	} catch (IOException | ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    }
    public void sendPosition() {
    	try {
    		if (mark.equals("X")) {
    			output.writeObject("You are first");
    		} else {
    			output.writeObject("You are second");
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    }

    public void getDesiredGame() {
    	try {
    		desiredGame = (String) input.readObject();
    	} catch (ClassNotFoundException | IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    public void toDisappoint() {
      try {
        output.writeObject("Nobody wants to play with you. You will be disconnected.");
      } catch (IOException e) {
		// TODO Auto-generated catch block
        e.printStackTrace();
      } 
    }

    public void setOpponent(Player opponent) {
      this.opponent = opponent;
    }

    public void sendBoard() {
      try {
    	  output.writeObject(instance.getBoard());
    	  output.flush();
      } catch (IOException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		  }
    }
    
    public void sendCurrentPlayer() {
        try {
      	  output.writeObject(instance.getCurrentPlayer());
      	  output.flush();
        } catch (IOException e) {
  			// TODO Auto-generated catch block
  			 e.printStackTrace();
        }
    }
    
    public void sendGameBoardGUI(){
    	try{
    		output.writeObject(instance.getGameBoardGUI());
    		output.flush();
    	} catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    public void run(){ //plays the game after being matched
    	try{
    		
        //TODO: how do player know they have to go first??
    		sendPosition();
    		updateName();
    		sendBoard();
    		sendCurrentPlayer();
    		sendGameBoardGUI();
//    		while (true) {
    			if(this.mark.equals(this.name)){
    				Command comm = (Command)input.readObject();
    			}

//    			if (legalMove(comm, this) ) {
//    				//talk to player who just played
//    				output.writeObject(instance.getBoard());
//    				output.flush();
//    				output.writeObject(instance.getCurrentPlayer());
//    				output.flush();
//    				//how do set instance current player?
//    				
//    			} else {
//    				//send an error
//    				output.writeObject(instance.getBoard());
//    				output.flush();
//    				output.writeObject(instance.getCurrentPlayer());
//    				output.flush();
//    			}
//    			if (instance.getGameStatus()==gameStatus.gameOver ) {
//    				return;
//    			}

          
          //TODO: how will server know the players decided to quit?
//    		}
    	} catch (IOException | ClassNotFoundException e){
    		e.printStackTrace();
    		
    	} finally{
    		try {
    			socket.close();

    		} catch (IOException e) {
    			System.out.println("a WTF moment here.");
    		}
    	}
    }
  }
}