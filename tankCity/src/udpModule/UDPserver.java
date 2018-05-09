package udpModule;

import java.util.List;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.util.Log;

import entities.*;

public class UDPserver extends Thread{
	private DatagramSocket serverSocket = null;
	private int PORT = 9999; 
	
	private HashMap<String, PlayerInfo> players = null;
	private List<String> tankColors = new ArrayList<String>();
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

	   	try {
        	dataParser(receivePacket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

		public void dataParser(DatagramPacket receivePacket) throws UnknownHostException {
			String data = new String(receivePacket.getData());
	        InetAddress address = receivePacket.getAddress();
	        int port = receivePacket.getPort();
	        String[] text = data.trim().split(" ");
//	        Log.info(" UDPserver: " + text[0] + " " + text[1]);
        	
			if(text[0].equals("CONNECT")) {
//				Log.info(" UDPserver: " + gameState);
	        	if(gameState == 0) {
//	        		Log.info( text[1] );
	            	addPlayer(text[1], address, port);
//	            	System.out.println(players.keySet());
	            	//establish client connection
	            }else {
	            	send("NAK GIP", address, port);
	            }
			}else if(text[0].equals("HIT")) {
				//tank hit by another tank
				//remove destroyed tank from game
				//update score
				removePlayer(text[2]);
			}else {
				sendToAll(data);
			}
		}//process all the data received from clients
	
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
		tankColors.add("GREEN");
		tankColors.add("BLUE");
		tankColors.add("PINK");
		tankColors.add("RED");
		tankColors.add("GRAY");
		try {
			if(players.get(name)==null) {
				send("ACK", address, port);
				
				sendToAll("PLYR " + name);
				String tankColor = tankColors.get(new Random().nextInt(tankColors.size()));				
				send("IMG " + name + " " +  tankColor,address, port);
				Log.info(tankColor);
				players.put(name, new PlayerInfo(name, address, port));
				tankColors.remove(tankColor);				
				
				Log.info(" " + name + " has joined the game.");
			}else {
				send("NAK NNA", address, port);
			}//checks availability of username
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removePlayer(String name) {
		PlayerInfo dead = players.get(name);
		send("Game Over", dead.getAddress(), dead.getPort());
		System.out.println(players.keySet());
		players.remove(name);
		System.out.println(players.keySet());
		if(players.size() == 1) {
			gameState = 3;//endgame
		}
	}//remove dead tanks
	
	public void generatePowerUps() {
		
	}
	
	public void run() {
        Log.info(" Game server started.");
        while(true) {        	
        	if(gameState == 0) {
        		//wait for players
        		if(players.size()<3) {
        			//wait
        			receive();
        		}else {
        			gameState = 1;
        			receive();
        		}
        	}else if(gameState == 1) {
        		//start game ; initialize board for all clients
        		sendToAll("MSG START");
        		receive();
        	}else if(gameState == 2) {
        		//game in progress
        		receive();
        	}else if(gameState == 3){
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