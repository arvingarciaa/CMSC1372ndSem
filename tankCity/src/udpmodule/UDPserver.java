package udpmodule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

import org.newdawn.slick.util.Log;

import entities.*;

public class UDPserver extends Thread{
	private int PORT = 9999; 
	private DatagramSocket serverSocket = null;
	private HashMap<String, Player> players = null;
	 
	public UDPserver(int port) {
		this.PORT = port;
	    try {
	    	serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(5000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	    players = new HashMap<String, Player>();
	 }
	 
	 public void send(String text, InetAddress address, int port) {
		 byte[] data = text.getBytes();
		 DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
		 try {
		   	serverSocket.send(sendPacket);
		 } catch(Exception e) {
		   	e.printStackTrace();
		 }
	}
	 
	public void receive() {
	    byte[] receiveData = new byte[1024];

	   	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	   	try {
	        serverSocket.receive(receivePacket); 	   		
	   	} catch(Exception e) {};

        String data = new String(receivePacket.getData());
        InetAddress address = receivePacket.getAddress();
        int port = receivePacket.getPort();
        if(data.startsWith("CONNECT")) {
        	String[] name = data.trim().split(" ");
        	//addPlayer(name[1], address, port);
        	send("ACK", address, port);
			sendToAll(name[1] + " is now connected.");
			Log.info(name[1] + " is now connected.");
        	//create establish client connection
        }else {
        	sendToAll(data);
        }
	}
	
	public void sendToAll(String text) {
		Object[] names = players.keySet().toArray();
		for(int i=0; i<players.size(); i++) {
			byte[] data = text.getBytes();
		    DatagramPacket sendPacket = new DatagramPacket(data, data.length, players.get(names[i]).getAddress(), players.get(names[i]).getPort());
		    try {
		    	serverSocket.send(sendPacket);
		    } catch(Exception e) {
		    	e.printStackTrace();
		    }			
		}
	}//broadcast to all clients
	
//	public void addPlayer(String name, InetAddress address, int port) {
//		try {
//			if(players.get(name)==null) {
//				send("ACK", address, port);
//				sendToAll(name + " is now connected.");
//				//players.put(name, new Player());
//			}else {
//				send("Username already exists!", address, port);
//			}//checks availability of username
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}//problem instantiating players
	
	public void run() {
        Log.info("Game server started.");
        while(true) {        	
        	receive();
        }
	}//run server continuously
	
	public static void main(String[] args) throws Exception{
		int port = 0;
		if(args.length < 1) {
			System.err.println("Error: Include a port number.");
			System.exit(1);
		}
		try {
			port = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException e) {
			System.err.println("Error: Argument not an integer.");
			System.exit(1);
		}
		UDPserver udpserver = new UDPserver(port);
		udpserver.start();
	}
}
/*Reference:GameOfTanks*/