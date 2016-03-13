package boardgames;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
    private BufferedReader in;
    private PrintWriter out;
    private GameBoard instance;
    private String playerName;
    private ArrayList<String> listOfGames;

    /**
     * Constructs the client by connecting to a server
     */
    public client(String serverAddress) throws Exception {
        // Setup networking
     	
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
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
        responseString = in.readLine();
        for (String g : responseString.split(",")){
        	listOfGames.add(g);
        }
        GUI gui = new GUI();
        gameStatus isLoading = gui.startGUI(listOfGames);
        gui.getSelectedGameTitle(); 
        // 	Send SelectedGameTitle to server
        //	Server will response with whether there is a Connection or not
        gui.getPlayerName();
        try {
            responseString = in.readLine();
            if (responseString.startsWith("WELCOME")) {
                char mark = responseString.charAt(8);
            }
            while (true) {
                responseString = in.readLine();
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
            out.println("QUIT");
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