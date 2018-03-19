import java.io.*;
import java.net.*;
import java.lang.*;

public class Client implements Runnable {

  private static boolean connection = false;
  private static Socket clientSocket = null;
  private static PrintStream output = null;
  private static DataInputStream input = null;
  private static BufferedReader message = null;

  public void run() {
    String line;
 
    try {
      // reads from the server socket
      while ((line = input.readLine()) != null) {
        System.out.println(line);
        if (line.indexOf("Bye") != -1) System.exit(0);
      }
      connection = true;

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String serverName = "";
    int port = 0;

    if (args.length != 2) {
      // returns the usage if there are less than two arguments
      System.out.println("Usage: java Client <server ip> <port number>");
      System.exit(0);
    } else {
      // get the first and second parameters
      // server ip address and port number for first and second param respectively
      serverName = args[0]; 
      port = Integer.parseInt(args[1]);
      System.out.println("Connecting to " + serverName + " on port " + port +"\n\n");
    }

    try {
      // create a socket for client
      clientSocket = new Socket(serverName, port);

      // InputStreamReader used to obtain a stream of bytes from the keyboard
      // then converts them to characters
      InputStreamReader inputLine = new InputStreamReader(System.in);
      message = new BufferedReader(inputLine);

      // sending data to the server
      OutputStream outputToServer = clientSocket.getOutputStream();
      output = new PrintStream(outputToServer);

      // receiving data from the server
      InputStream inputFromServer = clientSocket.getInputStream();
      input = new DataInputStream(inputFromServer);

      /* Create a thread to read from the server. */
      Thread t = new Thread(new Client());
      t.start();

      while (!connection) {
        output.println(message.readLine().trim());
      }

      // closing the socket of the client and data streams
      output.close();
      input.close();
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Cannot find (or disconnected from) Server");
    }
  }
}