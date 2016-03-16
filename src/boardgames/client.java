package boardgames;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import GUI.GUI;

/**
 *
 *  Client -> Server           Server -> Client
 *  ----------------           ----------------
 *  MOVE <n>  (0 <= n <= x)    WELCOME <char> 
 *  QUIT                       VALID_MOVE
 *                             OTHER_PLAYER_MOVED <n>
 *                             VICTORY
 *                             DEFEAT
 *                             TIE
 *                             MESSAGE <text>
 *
 */
public class client {

    private static int PORT = 8901;
    private Socket socket;
    private ObjectInputStream inObject;
    private ObjectOutputStream outObject;
    private String playerName;
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
    

    /**
     * The main thread of the client will listen for messages
     * from the server.  The first message will be a "WELCOME"
     * message in which we receive our mark.  Then we go into a
     * loop listening for "VALID_MOVE", "OPPONENT_MOVED", "VICTORY",
     * "DEFEAT", "TIE", "OPPONENT_QUIT or "MESSAGE" messages,
     * and handling each message appropriately.  The "VICTORY",
     * "DEFEAT" and "TIE" ask the user whether or not to play
     * another game.  If the answer is no, the loop is exited and
     * the server is sent a "QUIT" message.  If an OPPONENT_QUIT
     * message is received then the loop will exit and the server
     * will be sent a "QUIT" message also.
     */
    public void play() throws Exception {
        GUI gui = new GUI();
        gui.startGUI();
        gameTitle = gui.getSelectedGame();
        // 	Send SelectedGameTitle to server
        outObject.writeObject(gameTitle.toString());
        outObject.flush();
        try {
        	// If gotten here there is a connection with another player
        	// Receive whether I am player1 or player2
            String whichPlayer = (String) inObject.readObject();
            playerName = gui.getPlayerName();
            //	Send Sever my name
            outObject.writeObject(playerName);
            outObject.flush();
            System.out.println(playerName);
            // Get the 2D Array from Server
            Piece[][] board = (Piece[][]) inObject.readObject();
            // Get Current Player from Server as String
            String currentPlayer = (String) inObject.readObject();
            GameBoardGUI gameBoardGUI = (GameBoardGUI) inObject.readObject();
            gui.loadGameBoardGUI(gameBoardGUI);
            outObject.writeObject("aString");
            outObject.flush();
            while (true) {
            	
            }
        }
        finally {
            socket.close();
        }
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