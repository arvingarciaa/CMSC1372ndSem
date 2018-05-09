package udpModule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import entities.*;

public class UDPserver extends Thread{
	private static Random rand;
	//variables for connecting to clients
	private DatagramSocket serverSocket = null;
	private int PORT; 
	
	//lists for monitoring the game
	private ArrayList<String> tankColors = new ArrayList<String>();
	private HashMap<String, Player> players = new HashMap<>();
	
	private int WAIT = 0;
	private int START = 1;
	private int INPROGRESS = 2;
	private int END = 3;

	private int gameState = WAIT;
	private Board board;
	private Player player;
	
	//constructor
	public UDPserver(int port) {
		this.PORT = 9999;
	    try {
	    	this.serverSocket = new DatagramSocket();
			this.serverSocket.setSoTimeout(5000);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	 }
	 
	 public void send(String text, InetAddress address) {
		 byte[] data = text.getBytes();
		 DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, this.PORT);
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
//			Log.info(receivePacket.toString());
			serverSocket.receive(receivePacket); 		        
			Log.info(" UDPserver parsing data...");

			String data = new String(receivePacket.getData());
	        InetAddress address = receivePacket.getAddress();
	        int port = receivePacket.getPort();
	        dataParser(data, address, port);
	   	} catch(Exception e) {
//	   		Log.info(e.toString());
	   	};	   	
	}

	//broadcast to clients
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
	}
	
	//process all the data received from clients	
	public void dataParser(String data, InetAddress address, int port) {
	   		String[] text = data.trim().split(" ");
			if(text[0].equals("CONNECT")) {
	        	if(gameState == WAIT) {
	            	addPlayer(text[1], address, port);
	            	//establish client connection
	            }else {
	            	send("NAK GIP", address);
	            }
			}else if(text[0].equals("HIT")) {
				//tank hit by another tank
				//remove destroyed tank from game
				//update score
				removePlayer(text[2]);
			}else if(text[0].equals("POS")){
				players.get(text[1]).setXpos(Float.parseFloat(text[2]));
				players.get(text[1]).setYpos(Float.parseFloat(text[3]));
			}else{
				sendToAll(data);
			}
		}
		
	public void addPlayer(String name, InetAddress address, int port) {
		tankColors.add("GREEN");
		tankColors.add("BLUE");
		tankColors.add("PINK");
		tankColors.add("RED");
		tankColors.add("GRAY");
		try {
			if(players.get(name)==null) {
				Log.info(" UDPserver establishing connection." );
				send("ACK", address);				
				sendToAll("PLYR " + name);
				String tankColor = tankColors.get(new Random().nextInt(tankColors.size()));	
				player = createPlayer(name);
				player.setImage(tankColor);			
				players.put(name, player);		
				tankColors.remove(tankColor);				
				
				Log.info(" " + name + " has joined the game.");
			}else {
				send("NAK NNA", address);
			}//checks availability of username
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removePlayer(String name) {
		Player dead = players.get(name);
		send("Game Over", dead.getAddress());
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
        Log.info(" UDPserver started...");
        while(true) {        	
        	if(gameState == WAIT) {
        		//wait for players
        		if(players.size()==3) {
        			gameState = START;
        		}
                Log.info(" UDPserver receiving data...");        		
        		receive();
        	}else if(gameState == START) {
        		//start game ; initialize board for all clients
        		try {
					sendToAll("MSG START");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        		try {
					board = new Board();
				} catch (SlickException e) {
					e.printStackTrace();
				}
        		board.initializeBoard(players);
        		receive();
        	}else if(gameState == INPROGRESS) {
        		//game in progress
        		receive();
        	}else if(gameState == END){
        		//end game
        	}
        }
	}//run server continuously
	
	public static Player createPlayer(String name) {
		int initial_x;
		int initial_y;
		do {
			initial_x = rand.nextInt(20)*32;
			initial_y = rand.nextInt(15)*32;
		}while(Board.blocked[initial_x/32][initial_y/32]==true);
		Player player = new Player(initial_x,initial_y);
		Board.blocked[initial_x][initial_y]=true;
		return player;
	}
	
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