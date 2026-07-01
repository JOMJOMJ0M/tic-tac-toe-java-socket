// Client
import board.TicTacToeBoard;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


public class TicTacToeClient {

    public static void main(String[] args) {
        final String host = "localhost";
        final int port = 6769;
        boolean gameStatus = false;

        try{

            // recieving Client input
            Scanner scnr = new Scanner(System.in);

            // creating socket to establish connection to server
            Socket s = new Socket(host, port);

            // creating I/O streams and DIS/DOS wrappers to convert data into bytes and vice versa
            OutputStream outputStream = s.getOutputStream();
            InputStream inputStream = s.getInputStream();

            DataInputStream dis = new DataInputStream(inputStream);
            DataOutputStream dos = new DataOutputStream(outputStream);

            TicTacToeBoard board = new TicTacToeBoard();

        while(!gameStatus) {
            // get user input here
            System.out.println("Your move, client (enter as 'row col')");
            String input = scnr.nextLine();

            while (!board.placeXClient(input)) {
                System.out.println("Invalid move. Try again:");
                input = scnr.nextLine();
            }

            board.displayBoard();

            dos.writeUTF(input);
            dos.flush();


            //from server
            String inmsg = dis.readUTF();
            System.out.println("Received message from server:" + inmsg);

            if (inmsg.equals("Invalid move")) {
                System.out.println("Server rejected the move. Try again.");
            }else if (inmsg.startsWith("Client wins") || inmsg.startsWith("Server wins") || inmsg.startsWith("Game stalemate")) {
                System.out.println(inmsg);
                gameStatus = true;
            } else {
                board.placeOServer(inmsg);
                board.displayBoard();
            }

        }
            //close pipe
            dos.close();
            dis.close();
            s.close();

        } catch(Exception ex){
            System.out.println("Something went wrong: " + ex);
        }
    }
}
