import java.io.*;
import java.net.*;
import java.lang.*;

public class ClientThread extends Thread {

  private String clientName;
  private DataInputStream input;
  private PrintStream output;

  private Socket clientSocket;

  private int maxClients;
  private final ClientThread[] threads;

  public ClientThread(Socket clientSocket, ClientThread[] threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    maxClients = threads.length;

  }

  public void run() {
    int maxClients = this.maxClients;
    ClientThread[] threads = this.threads;

    try {
    
      // create input and output streams
      InputStream inputStream = clientSocket.getInputStream();
      input = new DataInputStream(inputStream);

      OutputStream outputStream = clientSocket.getOutputStream();
      output = new PrintStream(outputStream);

      // get the client's name
      String name;
      output.println("Enter your name: ");
      name = input.readLine().trim();



      /* SYNCHRONIZATION OF */

      output.println("Welcome " + name + ".\nTo leave type 'bye'.\n");
      synchronized (this) {  //used to be able to manipulate a thread without affecting other threads
        // loop for giving the name of the client
        for (int i = 0; i < maxClients; i++) {
          if (threads[i] != null && threads[i] == this) {
            clientName = name;
            break;
          }
        }
        
        // loop for notifications in each clients
        for (int i = 0; i < maxClients; i++) {
          if (threads[i] != null && threads[i] != this) {
            threads[i].output.println(name + " joined the chat.");
          }
        }
      }
      
      // conversation of clients
      while (true) {
        String line = input.readLine();

        // message will be seen by all clients
        synchronized (this) {
          for (int i = 0; i < maxClients; i++) {
            if (threads[i] != null && threads[i].clientName != null && threads[i] != this) {
              // prints the conversation
              threads[i].output.println("<" + name + "> " + line);
            }
          }
        }

        // check if the response is "bye"
        if ((line.toLowerCase()).equals("bye")) break;        
      }

      synchronized (this) {
        for (int i = 0; i < maxClients; i++) {
          if (threads[i] != null && threads[i].clientName != null && threads[i] != this) {
            // prints the response left
            threads[i].output.println(name + " left the conversation");
          }
        }
      }

      output.println("Bye " + name + "!");

      // sets the value of thread in null
      synchronized (this) {
        for (int i = 0; i < maxClients; i++) {
          if (threads[i] == this) threads[i] = null;
        }
      }

      // closing the socket and data streams
      input.close();
      output.close();
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}