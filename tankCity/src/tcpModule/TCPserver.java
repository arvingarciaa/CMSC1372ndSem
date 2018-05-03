package tcpModule;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPserver extends Thread {
	private ServerSocket serverSocket = null;
	private HashMap<String,OutputStream> clientOutputStreams = new HashMap<>();
		
	//constructor
	public TCPserver(int port) {
		try {
			this.serverSocket = new ServerSocket(port);
			this.serverSocket.setSoTimeout(99999999);
        } catch (IOException e) {
        	System.out.println("Error 1: "+e.toString());
			System.exit(MAX_PRIORITY);
		}
	}
	
	//thread for accepting connection from clients
	@Override
    public void run() {
        while (this.serverSocket != null) {
            System.out.println("Waiting for players...");
			try {
				Socket clientSocket = this.serverSocket.accept();
	            // add new client to the collection of clients
	            InputStream clientInputStream = clientSocket.getInputStream();
	            BufferedReader clientBufferedReader = new BufferedReader(new InputStreamReader(clientInputStream));
	            OutputStream clientOutputStream = clientSocket.getOutputStream();
	            
	            String data = clientBufferedReader.readLine();
	            String[] words = data.split(" ");
	            if (words[0].equals("CONNECT")) {
	            	String clientName = words[1];
	            	String msg = "";
//	            	check if the user name is already used
	            	if (this.clientOutputStreams.get(clientName) == null) {
	            		data = clientName + " joined the game.";
		            	System.out.println(data);
		            	this.sendToClients(data+"\n");
		            	this.clientOutputStreams.put(clientName, clientOutputStream);
		            	msg = "You successfully joined the game " + clientName + ".\n";
		            	clientOutputStream.write(msg.getBytes());

			            //create new thread for reading each new client's inputs
			            new InputReader(clientName, clientBufferedReader, this).start();
		            	Thread.currentThread().sleep(1000);
	            	} else {
	            		msg = clientName + " is already in use.\n";
	            		System.out.print(msg);
	            		clientOutputStream.write(("USED " + msg).getBytes());
	            	}
	            }
			} catch (Exception e) {
	        	System.out.println("Error 2: "+e.toString());
				System.exit(MAX_PRIORITY);
			}        
        }
        try {
        	this.serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error 3: "+e.toString());
			System.exit(MAX_PRIORITY);
		}
    }
	
	public void sendToClients(String data) {
		Object[] clientNames = this.clientOutputStreams.keySet().toArray();
		for(int i=0; i<this.clientOutputStreams.size(); i++) {
			if(this.clientOutputStreams.get(clientNames[i]) != null) {
				try {
					this.clientOutputStreams.get(clientNames[i]).write(data.getBytes());
				} catch (IOException e) {
					System.out.println("Error 4: "+e.toString());
//					System.exit(MAX_PRIORITY);
				}
			}
		}
	}
	
	public void deleteClient(String clientName) {
		this.clientOutputStreams.remove(clientName);
	}
	
	public static void main(String args[]) {
		TCPserver mTCPserver = new TCPserver(Integer.parseInt(args[0]));
		mTCPserver.start();
	}
}

class InputReader extends Thread {
	private TCPserver serverSocket;
	private BufferedReader clientBufferedReader;
	private String clientName;
	
	//constructor
	public InputReader(String clientName, BufferedReader clientBufferedReader, TCPserver serverSocket) {
		this.clientBufferedReader = clientBufferedReader;
		this.serverSocket = serverSocket;  
		this.clientName = clientName;
	}
	
	//continues reading of client's inputs
	@SuppressWarnings("deprecation")
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
	        	String data = clientName+" disconnected.\n";
	        	System.out.print(data + "Waiting for players...\n");
	        	this.serverSocket.deleteClient(clientName);
	        	this.serverSocket.sendToClients(data);
//				System.exit(MAX_PRIORITY);
	        	Thread.currentThread().stop();
	        }
		}
	}
}

