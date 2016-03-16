package boardgames;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;

import GUI.GUI;

public class client {

    private static int PORT = 8901;
    private Socket socket;
    private ObjectInputStream inObject;
    private ObjectOutputStream outObject;
    private String playerName;
    private String playerId;
    private Games gameTitle;

    /**
     * Constructs the client by connecting to a server
     */
    public client(String serverAddress) throws Exception {
        // Setup networking
     	
        socket = new Socket(serverAddress, PORT);
        outObject = new ObjectOutputStream(socket.getOutputStream());
        outObject.flush();
        inObject = new ObjectInputStream(socket.getInputStream());
    }
    
    public void play() throws Exception {
        GUI gui = new GUI();
        gui.startGUI();
        gameTitle = gui.getSelectedGame();
        try {
        	sendGameSelection();
        	// Receive whether I am player1 or player2
            String whichPlayer = (String) inObject.readObject();
            if(whichPlayer.contains("ERROR:")){
            	// No other player selected the same game
            	System.out.println(whichPlayer);
            	System.exit(0);
            }
            playerName = gui.getPlayerName();
            sendPlayerName();
            // Get the initial 2D Array from Server
            Piece[][] board = getBoard();
            // Get Current Player from Server as String
            String currentPlayer = getCurrentPlayer();
            GameBoardGUI gameBoardGUI = (GameBoardGUI) inObject.readObject();
            gui.loadGameBoardGUI(gameBoardGUI);
            gui.updateGameBoardGUI(currentPlayer, board);
            while (true) {
//            	sendCommand();
            	board = getBoard();
            	currentPlayer = getCurrentPlayer();
            	gui.updateGameBoardGUI(currentPlayer, board);
            }
        }
        finally {
            socket.close();
        }
    }
    
    public void sendGameSelection(){
    	try{
    		outObject.writeObject(gameTitle.toString());
            outObject.flush();
    	} catch(IOException e){
    		System.out.println("ERROR: Unable to send Game Selection");
    		e.printStackTrace();
    	}
    }
    
    public void sendPlayerName() throws IOException{
        outObject.writeObject(playerName);
        outObject.flush();
    }
    
    public void sendCommand(Coordinate c1, Coordinate c2) throws IOException{
		Command command =  new Command(this.playerName, c1, c2);
		outObject.writeObject(command);
        outObject.flush();
    }
    
    public Piece[][] getBoard() throws ClassNotFoundException, IOException{
		return (Piece[][]) inObject.readObject();
    }
    
    public String getCurrentPlayer() throws ClassNotFoundException, IOException{
    	return (String) inObject.readObject();
    }

    /**
     * Runs the client as an application.
     */
    public static void main(String[] args) throws Exception {
        while (true) {
            String serverAddress = (args.length == 0) ? "localhost" : args[1];
            client client = new client(serverAddress);
            client.play();
        }
    }
}