// TicTacToe Board
/*
1.) need to initialize board
2.) before each turn, check if selection is valid, if so place x or o else, error message
3.) check board for 3 in a row if none after 9 turns, game stalemate
*/
package board;

public class TicTacToeBoard{

    char[][] board = new char[3][3];

    public boolean placeXClient(String input){
        // get coord
        String[] part = input.split(" ");

        int row = Integer.parseInt(part[0]);
        int col = Integer.parseInt(part[1]);

        // check if spot is valid
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            System.out.println("invalid move");
            return false;
        }

        if(board[row][col] != '\0'){
            System.out.println("invalid move");
            return false;
        }

        board[row][col] = 'X';
        return true;
    }

    public boolean placeOServer(String input){
        // get coord
        String[] part = input.split(" ");

        int row = Integer.parseInt(part[0]);
        int col = Integer.parseInt(part[1]);

        // check if spot is valid
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            System.out.println("invalid move");
            return false;
        }

        if(board[row][col] != '\0'){
            System.out.println("invalid move");
            return false;
        }
        board[row][col] = 'O';
        return true;
    }

    public String boardToString() {
        return board[0][0] + " | " + board[0][1] + " | " + board[0][2] + "\n" +
                "---------\n" +
                board[1][0] + " | " + board[1][1] + " | " + board[1][2] + "\n" +
                "---------\n" +
                board[2][0] + " | " + board[2][1] + " | " + board[2][2];
    }

    public void displayBoard() {
        System.out.println(boardToString());
    }

    public boolean checkWin(){
        //check row
        int row = 3;
        int col = 3;

        for(int i = 0; i < row; i++){
            if(board[i][0] == 'X' && board[i][1] == 'X' && board[i][2] == 'X') {
                System.out.print("Client wins");
                return true;
            }else if(board[i][0] == 'O' && board[i][1] == 'O' && board[i][2] == 'O') {
                System.out.print("Server wins");
                return true;
            }
        }
        //check col
        for(int i = 0; i < col; i++){
            if(board[0][i] == 'X' && board[1][i] == 'X' && board[2][i] == 'X') {
                System.out.print("Client wins");
                return true;
            }else if(board[0][i] == 'O' && board[1][i] == 'O' && board[2][i] == 'O') {
                System.out.print("Server wins");
                return true;
            }
        }

        //check diags
        if((board[0][0] == 'X' && board[1][1] == 'X' && board[2][2] == 'X' ) || (board[0][2] == 'X' && board[1][1] == 'X' && board[2][0] == 'X' )  ){
            System.out.println("Client wins");
            return true;
        }else if((board[0][0] == 'O' && board[1][1] == 'O' && board[2][2] == 'O') || (board[0][2] == 'O' && board[1][1] == 'O' && board[2][0] == 'O')  ) {
            System.out.print("Server wins");
            return true;
        }
        return false;
    }

}
