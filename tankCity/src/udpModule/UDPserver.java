package udpModule;

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
	private HashMap<String, PlayerInfo> players = null;
	private int gameState = 0;
	 
	public UDPserver(int port) {
		this.PORT = port;
	    try {
	    	serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(5000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	    players = new HashMap<String, PlayerInfo>();
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
        	if(gameState == 0) {
            	String[] name = data.trim().split(" ");
            	addPlayer(name[1], address, port);
            	System.out.println(players.keySet());
            	//establish client connection
            }else {
            	send("Game in progress. You cannot connect at the moment.", address, port);
            }
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
	
	public void addPlayer(String name, InetAddress address, int port) {
		try {
			if(players.get(name)==null) {
				send("ACK", address, port);
				sendToAll(name + " is now connected.");
				players.put(name, new PlayerInfo(name, address, port));
				Log.info(name + " has joined the game");
			}else {
				send("NAK", address, port);
			}//checks availability of username
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//problem instantiating players
	
	public void removePlayer(String name) {
		players.remove(name);
	}//remove dead tanks
	
	public void run() {
        Log.info("Game server started.");
        while(true) {        	
        	if(gameState == 0) {
        		//wait for players
        		if(players.size()<3) {
        			//wait
        			receive();
        		}else
        			gameState = 1;
        	}else if(gameState == 1) {
        		//start game ; initialize board for all clients
        		sendToAll("Game Start");
        		receive();
        	}else if(gameState == 2) {
        		//game in progress
        	}else {
        		//end game
        	}
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
/*Reference:Janoras, et.al, (2017) GameOfTanks*/