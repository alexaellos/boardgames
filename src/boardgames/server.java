package boardgames;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
  //don't need boardfilledup

  public boolean checkGamePreference(String desiredGame, String desiredGame2) {
    if (desiredGame==(desiredGame2)) {
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

   
    if (instance.commandIsValid(c) && currentPlayer == player) {
      Coordinate c1 = c.getCoord1();
      Coordinate c2 = c.getCoord2();
      Piece p1 = new Piece(c1);
      instance.setPieceAt(c2, p1);
      currentPlayer = currentPlayer.opponent;
      currentPlayer.otherPlayerMoved();
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
        sendGameList();
        getDesiredGame();

      } catch (IOException e) {
        System.out.println("Player could not connect or he died");
      }
    } 
    
    public void updateName() {
    	try {
    		mark = input.readUTF();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    public void sendPosition() {
    	try {
    		if (mark.equals("X")) {
    			output.writeChars("You are first");
    		} else {
    			output.writeChars("You are second");
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    }
    public void sendGameList() {
    	StringBuffer sb = new StringBuffer();
    	for (Games g: Games.values()) {
    		sb.append(g+", ");
    	}
    	try {
    		output.writeChars(sb.toString());
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    public void getDesiredGame() {
    	try {
        desiredGame = input.readUTF();
      } catch (IOException e) {
    		// TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    public void toDisappoint() {
      try {
        output.writeChars("Nobody wants to play with you. You will be disconnected.");
      } catch (IOException e) {
		// TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    }

    public void setOpponent(Player opponent) {
      this.opponent = opponent;
    }

    public void otherPlayerMoved() {
    	try {
    		output.writeObject(instance.getBoard());
			output.writeChars(instance.getCurrentPlayer());
      } catch (IOException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		  }
    }
    
    public void run(){ //plays the game after being matched
    	try{
    		
        //TODO: how do player know they have to go first??
    		sendPosition();
    		updateName();
    		
    		while (true) {

    			Command comm = (Command)input.readObject();

    			if (legalMove(comm, this) ) {
    				//talk to player who just played
    				output.writeObject(instance.getBoard());
    				output.writeChars(instance.getCurrentPlayer());
    				//how do set instance current player?
    				
    			} else {
    				//send an error
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