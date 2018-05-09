package udpModule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

import org.newdawn.slick.util.Log;

import entities.Player;

public class UDPclient extends Thread{
	//variables for connecting to server
	private DatagramSocket clientSocket;
	private InetAddress ipAddress;
	private int PORT;
	public String playerName;

	//variables for handling player data	
	private boolean connection = false;
	private boolean START = false;
	private String tankColor;
	private HashMap<String, Player> players = new HashMap<>();

	//constructor
	public UDPclient(String playerName, InetAddress ipAddress, int PORT) {
		this.PORT = PORT;
		this.playerName = playerName;
		try {
			this.ipAddress = ipAddress;
			this.clientSocket = new DatagramSocket();
			this.clientSocket.setSoTimeout(5000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String text) {
		byte[] data = text.getBytes();
	    DatagramPacket sendPacket = new DatagramPacket(data, data.length, ipAddress, PORT);
	    try {
	    	clientSocket.send(sendPacket);
	    	Log.info(" UDPclient sending data...");
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public void receive() {
		byte[] receiveData = new byte[100000];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    	try {
		    clientSocket.receive(receivePacket);
		    Log.info(" UDPclient parsing data...");
		    dataParser(receivePacket);
	    } catch(Exception e) {
//	    	Log.info(e.toString());
	    }    	
	}
	
	//process all the data received from server
	public void dataParser(DatagramPacket receivePacket) {
		String data = new String(receivePacket.getData());
        
		String[] text = data.trim().split(" ");
		if(text[0].equals("ACK")) {
			connection=true;
			System.out.println("Welcome " + playerName +"!");
			//players.put(name, new Player(0, 0));
			
			//request list of player names
		}else if(text[0].equals("NAK")){
			if(text[1].equals("NNA")) {
				System.out.println("Username already exists!");
			}else {
				System.out.println("Connection not allowed! Game in progress");
			}
		}else if(data.startsWith("MOV")) {
			//new tank position
		}else if(text[0].equals("PLYR")) {
			players.put(text[1], new Player(0, 0));
			//players.get(data[1]).visible = 0;
		}else if(text[0].equals("HIT")) {
			//tank hit
			//update score
		}else if(text[0].equals("WALL")) {
			//update map
		}else if(text[0].equals("PUA")) {
			//update map and player attributes
		}else if(text[0].equals("NPUS")) {
			//update map
		}else if(text[0].equals("POS")) {
			//init tank positions
			players.get(text[1]).setXpos(Float.parseFloat(text[2]));
			players.get(text[1]).setYpos(Float.parseFloat(text[3]));
		}else if(text[0].equals("MSG")) {
			//chat
			String msg = ""; 
			for (String x: text)
				msg = msg + " " + x;
			Log.info(msg);
		}else if(text[0].equals("IMG")) {
			tankColor = text[2];
			//players.get(data[1]).setImage(tankColor);
		}else if(text[0].equals("START")) {
			START = true;
		}
	}
	
	//continuously receive from server
	@Override
	public void run() {
		while(!connection) {
			Log.info(" UDPclient establishing connection...");
			try {
	    	    send("CONNECT " + playerName);
	    	    receive();
	    	    //Thread.sleep(1000);
			} catch(Exception e) {
				e.printStackTrace();
			}			
		}

		while(true) {				
			try {
				receive();
//				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}