package tcpModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class TCPclient extends Thread {
	private Socket clientSocket;
	private int port;
	private String name;
	private InetAddress ipAddress;
	private InputStream clientInputStream;
	private OutputStream clientOutputStream;
	private BufferedReader clientBufferedReader;
	private ArrayList<String> chatMessages = new ArrayList<>();
		
	//constructor
	public TCPclient(String name, InetAddress clientAddress, int port) {
		this.name = name;
		this.port = port;
		this.ipAddress = clientAddress;
		
		try {
			this.clientSocket = new Socket(this.ipAddress, this.port);
			this.clientInputStream = this.clientSocket.getInputStream();
			this.clientOutputStream = this.clientSocket.getOutputStream();
			this.clientBufferedReader = new BufferedReader(new InputStreamReader(this.clientInputStream));
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(MAX_PRIORITY);
		}
	}
	
	public void sendToServer(String data) {
		try {
			this.clientOutputStream.write(data.getBytes());
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(MAX_PRIORITY);
		}
	}
	
	//continues reading of the server's messages
//	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (true) {
			while(this.clientBufferedReader != null) {
				try {
		            String data;
		            do {
		            	data = this.clientBufferedReader.readLine();
		            	if(data != null && data.length() > 0)
		            		this.processData(data);		            		
		            } while (data != null);
				}
		        catch(Exception e) {
		        	System.out.println("error 1: " + e.toString());
//		        	Thread.currentThread().stop();
		        	System.exit(MAX_PRIORITY);
		        }
			}
		}
		
	}
	
	private void processData(String data) {
		String[] words = data.split(" ");
		if (words[0].equals("USED")) {
			try {
				this.clientSocket.close();
			} catch (IOException e) {
				System.out.println("error: " + e.toString());;
			}
		} else {
			System.out.println(data);
			chatMessages.add(data);			
		}
	}
	
	public ArrayList<String> getMessages() {
		return chatMessages;
	}
}