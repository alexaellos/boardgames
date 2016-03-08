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
  	ServerSocket listener = new ServerSocket(PORT);
    System.out.println("Server is running");
    try {
    	while (true) {
        Game game = new Game();
        Game.Player playerX = game.new Player(listener.accept(), 'X');
        Game.Player playerO = game.new Player(listener.accept(), 'O');

        playerX.setOpponent(playerO);
        playerO.setOpponent(playerX);
        if(game.checkGamePreference(playerX.desiredGame,playerO.desiredGame) {
          setGameBoard(playerX.desiredGame);
        }
        else {
          break;
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

  private Player currentPlayer;
  private int gameSelectedByPlayerOne;
  private int gameSelectedByPlayerTwo;
  //don't need board
  //don't need board will have hasWinner();
  //don't need boardfilledup

  public boolean checkGamePreference(int player1, int player2) {
    if (player1.equals(player2)) {
      return true;
    }
    else {
      return false;
    }
  }
  public void setGameBoard(int player1) {
    if (player1.equals(0)) {
      instance = new TicTacToeGameBoard();
    } else if (player1.equals(1)) {
      instance = new OthelloGameBoard();
    } else if (player1.equals(2)) {
      instance = new CheckersGameBoard();
    }

     
  }

  public synchronized boolean legalMove(Coordinate c , Player player) {
    if (commandIsValid(c)) {
      Piece p = new Piece(c);

      instance.setPieceAt(c, p);
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
    private char mark;
    private Player opponent;
    private int desiredGame;
    
     /**
     * Constructs a handler thread, squirreling away the socket.
     * All the interesting work is done in the run method.
     */
    public Player(Socket socket, char mark) {
      this.socket = socket;
      this.mark = mark;
      try {
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        desiredGame = input.readInt();
      } catch (IOException e) {
        System.out.println("Player could not connect or he died");
      }
    } 

    public void setOpponent(Player opponent) {
      this.opponent = opponent;
    }

    public void otherPlayerMoved() {
        output.writeObject(instance);
    }
    
    public void run(){
    	try{
    		
        //TODO: how do player know they have to go first??

        while (true) {
          Command comm = (Command)input.readObject();

          if !(legalMove(comm, this) ) {
            output.writeObject(instance);
          }

          
          //TODO: how will server know the players decided to quit?
        }
    	} catch (IOException e){
        System.out.println(e);
      } 
      finally{
        try {
          socket.close();

        } catch (IOException e) {
          System.out.println("a WTF moment here.");
        }
      }

    }
  }
}