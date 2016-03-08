package boardgames;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class server {
	
	private GameBoard instance;
	private String player1_id;
	private String player2_id;
	private Socket socket;
	private Player currentPlayer;
	
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
      	
//        instance = new Gameboard();
        //TODO: ASK playerx what game to play
    	/*
    	 * HAVE A WAY TO LISTEN FOR A CONNECTION
    	 * ONCE WE SEE A SOCKET CONNECTION THE SERVER REACTS
    	 * IT REACTS BY SENDING IT A METHOD WHICH WLL DETERMINE WHICH GAMEBOARD TO START
    	 */
        Player playerX = new Player(listener.accept(), 'X');
        Player playerO = new Player(listener.accept(), 'O');
        playerX.setOpponent(playerO);
        playerO.setOpponent(playerX);
        playerX.start();
        playerO.start();
        
      }
    }
    finally {
    	listener.close();
    }
    
  }
  
  class Player extends Thread {
  	private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private char mark;
    
     /**
     * Constructs a handler thread, squirreling away the socket.
     * All the interesting work is done in the run method.
     */
    public Player(Socket socket, char mark) {
        this.socket = socket;
        this.mark = mark;
    }
    
    public void run(){
    	try{
    		
    	} catch (IOException e){
      	System.out.println(e);
      	} finally{
      		
      	}
	}
  }
}
