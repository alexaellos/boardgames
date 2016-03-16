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

	public static void main(String[] args) throws Exception {
		ServerSocket listener = null ;
		System.out.println("Server is running");
		try {
			listener = new ServerSocket(PORT);
			while (true) {
				Game game = new Game();
				Game.Player playerX = game.new Player(listener.accept(), "X");
				Game.Player playerO = game.new Player(listener.accept(), "O");

				playerX.setOpponent(playerO);
				playerO.setOpponent(playerX);
				if(game.checkGamePreference(playerX.getDesiredGameLocally(),playerO.getDesiredGameLocally())) {
					game.setGameBoard(playerX.getDesiredGameLocally());
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
		return desiredGame.equals(desiredGame2);
	}
	
	public void setGameBoard(String game) {
		if (game.equals("Tic_Tac_Toe")) {
			instance = new TicTacToeGameBoard();
		} else if (game.equals("Othello")) {
			instance = new OthelloGameBoard();
		} else if (game.equals("Checkers")) {
			instance = new CheckersGameBoard();
		}
	}

	public synchronized boolean legalMove(Command c , Player player) throws IOException {
		if (instance.commandIsValid(c)) {
			instance.makeMove(c);
//    	commandisvalid makes the move and returns a boolean
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
    private String desiredGame;
    
     /**
     * Constructs a handler thread, squirreling away the socket.
     * All the interesting work is done in the run method.
     */
    public Player(Socket socket, String mark) throws IOException {
      this.socket = socket;
      this.mark = mark;
      try {
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        getDesiredGame();
      } catch (IOException | ClassNotFoundException e) {
    	  System.out.println(e);
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
    			output.flush();
    		} else {
    			output.writeObject("You are second");
    			output.flush();

    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }

    public void getDesiredGame() throws ClassNotFoundException, IOException {
		desiredGame = (String) input.readObject();
    }
    
    public void toDisappoint() throws IOException {
    	output.writeObject("ERROR: Nobody wants to play with you. You will be disconnected.");
		output.flush();
    }

    public void setOpponent(Player opponent) {
      this.opponent = opponent;
    }

    public void sendBoard() throws IOException {
	  output.writeObject(instance.getBoard());
	  output.flush();
    }
    
    public void sendCurrentPlayer() throws IOException {
  	  output.writeObject(instance.getCurrentPlayer());
  	  output.flush();
    }
    
    public void sendGameBoardGUI() throws IOException{
		output.writeObject(instance.getGameBoardGUI());
		output.flush();
    }
    
    public String getDesiredGameLocally(){
    	return this.desiredGame;
    }
    
    public void run(){ //plays the game after being matched
    	try{
    		
        //TODO: how do player know they have to go first??
    		sendPosition();
    		updateName();
    		sendBoard();
    		sendCurrentPlayer();
    		sendGameBoardGUI();
    		while (true) {
				Command comm = (Command)input.readObject();

    			if (legalMove(comm, this) ) {
    				//talk to player who just played
    				output.writeObject(instance.getBoard());
    				output.flush();
    				output.writeObject(instance.getCurrentPlayer());
    				output.flush();
    			} else {
    				//send an error
    				output.writeObject(instance.getBoard());
    				output.flush();
    				output.writeObject(instance.getCurrentPlayer());
    				output.flush();
    			}
    			if (instance.getGameStatus()==gameStatus.gameOver ) {
    				return;
    			}
          //TODO: how will server know the players decided to quit?
    		}
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