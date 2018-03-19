import java.io.*;
import java.net.*;
import java.lang.*;

public class Server {

  private static ServerSocket serverSocket = null;
  private static Socket clientSocket = null;

  private static final int maxClients = 2;
  private static final ClientThread[] threads = new ClientThread[maxClients];

  public static void main(String args[]) {
    int portNumber = 0;

    if (args.length != 1) {
      // returns the usage if there are less than one arguments
      System.out.println("Usage: java ProjectServer <port number>");
      System.exit(0);
    } else {
      // get the first param as port number
      portNumber = Integer.parseInt(args[0]);
    }

    try {
      // create a socket for server
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }

    boolean connected = true;
    while (connected) {
      try {
        // wait or accepts connection from client
        clientSocket = serverSocket.accept();
        int i = 0;

        for (i = 0; i < maxClients; i++) {
          if (threads[i] == null) {
            // create a thread for each clients
            threads[i] = new ClientThread(clientSocket, threads);
            Thread t = new Thread(threads[i]);
            t.start();
            break;
          }
        }

        if (i == maxClients) {
          // send data to the client
          OutputStream outputToClient = clientSocket.getOutputStream();
          PrintStream output = new PrintStream(outputToClient);
          output.println("All players are complete. Please try again later.");

          // closing the socket for this client
          output.close();
          clientSocket.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

