package tcpModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPclient extends Thread {
	private Socket clientSocket;
	private int port;
	private String name;
	private InetAddress ipAddress;
	private InputStream clientInputStream;
	private OutputStream clientOutputStream;
	private BufferedReader clientBufferedReader;
		
	//constructor
	public TCPclient(String name, String address, int port) {
		this.name = name;
		this.port = port;
		
		try {
			this.ipAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
        	System.exit(MAX_PRIORITY);
		}
		
		try {
			this.clientSocket = new Socket(this.ipAddress, this.port);
			this.clientInputStream = this.clientSocket.getInputStream();
			this.clientOutputStream = this.clientSocket.getOutputStream();
			this.clientBufferedReader = new BufferedReader(new InputStreamReader(this.clientInputStream));
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(MAX_PRIORITY);
		}
		
		this.sendToServer("CONNECT " + this.name + "\n");
	}
	
	private void sendToServer(String data) {
		try {
			this.clientOutputStream.write(data.getBytes());
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(MAX_PRIORITY);
		}
	}
	
	//continues reading of the server's messages
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
//		        	System.exit(MAX_PRIORITY);
		        }
			}
		}
		
	}
	
	public void processData(String data) {
		String[] words = data.split(" ");
		if (words[0].equals("USED")) {
			try {
				this.clientSocket.close();
			} catch (IOException e) {
				System.out.println("error: " + e.toString());;
			}
		} else {
			System.out.println(data);
		}
	}
	
	public static void main(String args[]) {
		TCPclient mTCPclient = new TCPclient(args[0],args[1],Integer.parseInt(args[2]));
		mTCPclient.start();
	}
}