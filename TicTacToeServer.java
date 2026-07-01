//server
import board.TicTacToeBoard;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TicTacToeServer {
    public static void main(String[] args) {

        /*
        1.) establish connection via socket
        2.) create I/O stream (establish the pipeline from client to server outgoing)
        3.) write/flush to client

        wait for the message ping and send pong back to client
         */
        final int port = 6769;
        Scanner scnr = new Scanner(System.in);
        boolean win = false;


        try(ServerSocket ss = new ServerSocket(port)){
            System.out.println("Server listening on port: " + port);


            // create board
            TicTacToeBoard board = new TicTacToeBoard();

            //keeping track of turn;
            int turn = 0;

            Socket s = ss.accept();
            OutputStream outputStream = s.getOutputStream();
            InputStream inputStream = s.getInputStream();

            DataOutputStream dos = new DataOutputStream(outputStream);
            DataInputStream dis = new DataInputStream(inputStream);


            while(turn < 9 && !win) {
                //response from client
                String msg = dis.readUTF(); // should be something like "1 2"

                if (board.placeXClient(msg)) {
                    turn++;
                } else {
                    dos.writeUTF("Invalid move");
                    dos.flush();
                    continue; // goes back to if statement
                }

                if(turn >= 5 && board.checkWin()){
                    dos.writeUTF("Client wins\n" + board.boardToString());
                    dos.flush();
                    win = true;
                    break;
                }

                // after client move make sure not 9
                if(turn == 9){
                    break;
                }


                board.displayBoard();

                // server move
                System.out.println("Your move, server (enter as 'row col')");
                String serverMove = scnr.nextLine();
                while(!board.placeOServer(serverMove)) {
                    System.out.println("invalid move buddy");
                    serverMove = scnr.nextLine();
                }
                turn++;

                //check at 5 and above moves if a win has happened
                if(turn >= 5 && board.checkWin()){
                    dos.writeUTF("Server wins\n" + board.boardToString());
                    dos.flush();
                    win = true;
                    break;
                }

                dos.writeUTF(serverMove);
                dos.flush();
            }

            if(turn == 9 && !win){
                dos.writeUTF("Game stalemate\n" + board.boardToString());
                dos.flush();
                System.out.println("Game stalemate");
            }


            dos.close();
            dis.close();
            s.close();

        } catch(Exception ex) {
            System.out.println("Something went wrong:" + ex);
        }
    }
}
