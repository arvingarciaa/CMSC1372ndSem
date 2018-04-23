package tcpModule;

import java.io.*;
import java.net.*;
import java.util.*;

import org.newdawn.slick.util.Log;

public class TCPserver extends Thread {
	private ServerSocket serverSocket = null;
	private HashMap<String,OutputStream> clientOutputStreams = new HashMap<>();
		
	//constructor
	public TCPserver(int port) {
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(99999999);
        } catch (IOException e) {
        	Log.info(e.toString());
			System.exit(MAX_PRIORITY);
		}
	}
	
	//thread for accepting connection from clients
	@Override
    public void run() {
        while (serverSocket != null) {
            Log.info("Waiting for more players...");
			try {
				Socket clientSocket = serverSocket.accept();
	            // add new client to the collection of clients
	            InputStream clientInputStream = clientSocket.getInputStream();
	            BufferedReader clientBufferedReader = new BufferedReader(new InputStreamReader(clientInputStream));
	            OutputStream clientOutputStream = clientSocket.getOutputStream();
	            
	            String data = clientBufferedReader.readLine();
	            String[] words = data.split(" ");
	            if (words[0].equals("CONNECT")) {
	            	String clientName = words[1];
	            	data = clientName + " joined the game.";
	            	Log.info(data);
	            	sendToClients(data+"\n");
	            	this.clientOutputStreams.put(clientName, clientOutputStream);
	            	clientOutputStream.write("You joined the game.\n".getBytes());
	            }
	            
	            //create new thread for reading each new client's inputs
	            new InputReader(clientBufferedReader, this).start();
            	Thread.sleep(1000);
			} catch (Exception e) {
	        	Log.info(e.toString());
				System.exit(MAX_PRIORITY);
			}        
        }
        try {
			serverSocket.close();
		} catch (IOException e) {
			Log.info(e.toString());
			System.exit(MAX_PRIORITY);
		}
    }
	
	public void sendToClients(String data) {
		Object[] clientNames = clientOutputStreams.keySet().toArray();
		for(int i=0; i<clientOutputStreams.size(); i++) {
			if(clientOutputStreams.get(clientNames[i]) != null) {
				try {
					clientOutputStreams.get(clientNames[i]).write(data.getBytes());
				} catch (IOException e) {
					Log.info(e.toString());
					System.exit(MAX_PRIORITY);
				}
			}
		}
	}
	
	public static void main(String args[]) {
		TCPserver mTCPserver = new TCPserver(Integer.parseInt(args[0]));
		mTCPserver.start();
	}
}

class InputReader extends Thread {
	private TCPserver serverSocket;
	private BufferedReader clientBufferedReader;
	
	//constructor
	public InputReader(BufferedReader clientBufferedReader, TCPserver serverSocket) {
		this.clientBufferedReader = clientBufferedReader;
		this.serverSocket = serverSocket;       
	}
	
	//continues reading of client's inputs
	@Override
	public void run() {		
		while(this.clientBufferedReader != null) {
			try {					
	            String data;
	            do {
	            	data = this.clientBufferedReader.readLine();
	            	if(data != null && data.length() > 0) 
	            		this.serverSocket.sendToClients(data);
	            } while (data != null);
			}
	        catch(Exception e) {
	        	Log.info(e.toString());
				System.exit(MAX_PRIORITY);
	        }
		}
	}
}

