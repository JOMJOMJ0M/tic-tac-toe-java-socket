# Tic-Tac-Toe Sockets: Break It and Explain It Report

## Protocol Messages

For this Tic-Tac-Toe socket program, I used a simple text-based protocol between the client and server. Normal moves are sent as two numbers in the format:

~~text
row col
~~

For example:

~~text
1 2
~~

This means the player wants to place their symbol at row `1`, column `2`. The client always plays as `X`, and the server always plays as `O`, so the message does not need to include the symbol. Each side already knows whose turn it is.

The server can also send special messages back to the client:

~~text
Invalid move
Client wins
Server wins
Game stalemate
~~

These messages are used so the client knows whether to continue playing, retry a move, or end the game. For win and stalemate messages, the server also sends the final board using `boardToString()` so the client can see the finished game state.

## What Happens If the Client Sends a Move When It Is Not Supposed To?

Because the game is turn-based and does not use threads, the client and server take turns waiting on socket input. The client sends a move, then waits for the server’s response. The server receives the client move, updates its board, then asks the server player for a move.

If the client sends a move when it is not supposed to, the protocol can become inconsistent. The server expects exactly one client move per round. If extra data is sent at the wrong time, the server may read that extra message as the next turn’s move. This can cause the server board and client board to become different because each program may think the game is in a different turn state.

## Broken Protocol Test

For my break test, I temporarily sent an invalid move message from the client. Instead of sending a normal move like:

~~text
1 2
~~

I tested a bad message such as:

~~text
hello
~~

or a move outside the board like:

~~text
5 5
~~

This breaks the expected `row col` protocol. The board logic expects two integer values that represent a valid row and column between `0` and `2`.

With validation code added, the program does not crash for this normal invalid input. The board rejects the move and prints an invalid move message. The server can send back:

~~text
Invalid move
~~

The client then asks the user to try again instead of continuing with a bad board state.

## What Breaks and Why?

Before adding input validation, invalid input could crash the program. For example, if the client sent:

~~text
hello
~~

then the board code would try to parse `"hello"` as an integer using `Integer.parseInt()`. That causes a `NumberFormatException`.

If the client sent:

~~text
5 5
~~

then the code could try to access `board[5][5]`, which is outside the 3x3 board and causes an `ArrayIndexOutOfBoundsException`.

The reason this happens is that the socket only sends raw text. The server does not automatically know whether the message is valid. Both programs must follow the same protocol, and the receiver must check that the message matches the expected format before using it.

Another possible break is skipping a move or closing the socket unexpectedly. If the client closes the socket while the server is waiting on:

~~java
dis.readUTF();
~~

then the server cannot receive the expected move. The game stops because one side disconnected. If one side simply waits and never sends a move, the other side may wait forever because `readUTF()` is blocking. This happens because the program is single-threaded and turn-based, so each side waits for input when it is not its turn.

Overall, this experiment shows why a clear protocol matters. The client and server must agree on message formats, whose turn it is, and what messages mean. If one side sends unexpected data, sends data at the wrong time, or disconnects, the other side may reject the move, block while waiting, or end with an error depending on how much validation is included.
