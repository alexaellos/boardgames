package boardgames;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
    private GameBoard instance;
    private String playerName;
    private String gameTitle;
    private ArrayList<String> listOfGames;

    /**
     * Constructs the client by connecting to a server
     */
    public client(String serverAddress) throws Exception {
        // Setup networking
     	
        socket = new Socket(serverAddress, PORT);
        outObject = new ObjectOutputStream(socket.getOutputStream());
        outObject.flush();
        inObject = new ObjectInputStream(socket.getInputStream());
        listOfGames = new ArrayList<String>();
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
        String responseString;
        Object responseObject;
        // Get list of games EX: "Tic_Tac_Toe,Checkers,Othello"
        responseString = (String) inObject.readObject();
        for (String g : responseString.split(",")){
        	listOfGames.add(g);
        }
        System.out.println("After getting the list of games");
        GUI gui = new GUI();
        gameStatus isGameSelected = gui.startGUI(listOfGames);
        System.out.println("After the .startGUI has been called");
        while(isGameSelected != gameStatus.gameSelected){
        	// Wait For user to pick something
        }
        gameTitle = gui.getSelectedGameTitle();
        // 	Send SelectedGameTitle to server
        outObject.writeObject(gameTitle);
        playerName = gui.getPlayerName();
        //	Server will response with whether there is a Connection or not
        String isThereConnection = (String) inObject.readObject();
        try {
            responseString = (String) inObject.readObject();
            if (responseString.startsWith("WELCOME")) {
                char mark = responseString.charAt(8);
            }
            while (true) {
                responseString = (String) inObject.readObject();
                if (responseString.startsWith("VALID_MOVE")) {
                } else if (responseString.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(responseString.substring(15));
                } else if (responseString.startsWith("VICTORY")) {
                    break;
                } else if (responseString.startsWith("DEFEAT")) {
                    break;
                } else if (responseString.startsWith("TIE")) {
                    break;
                } else if (responseString.startsWith("MESSAGE")) {
                }
            }
            outObject.writeObject("QUIT");
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
            System.out.println("Before creating client instance");
            client.play();
        }
    }
}