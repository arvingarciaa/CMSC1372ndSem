package udpModule;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import entities.*;
import states.GameState;
import tanks.Constants;

public class UDPserver extends Thread{
	private int PORT = 9999; 
	private DatagramSocket serverSocket = null;
	private HashMap<String, Player> players;
	private List<String> tankColors = new ArrayList<String>();
	private int gameState = 0;
	private static int x;
	private static int y;
	private static Random rand = new Random();
	private static Player player;
	private static ArrayList<ArrayList<Integer>> mapTemplate = new ArrayList<>();
	private static int INDESTRUCTIBLE = 1;
	private static int EMPTY = 0;
	private static int DESTRUCTIBLE = 2;
	public static boolean blocked[][];
	private int mapWidth;
	private int mapHeight;
	public static int tileSize = 32;
	 
	public UDPserver(int port) {
		this.PORT = port;
	    try {
	    	serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(5000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	    players = new HashMap<String, Player>();
	    tankColors.add("GREEN");
		tankColors.add("BLUE");
		tankColors.add("PINK");
		tankColors.add("RED");
		tankColors.add("GRAY");
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
	        Player player;
	        
			if(text[0].equals("CONNECT")) {
				if(gameState == 0) {
	            	addPlayer(text[1], address, port);
	            	
	            }else {
	            	send("NAK GIP", address, port);
	            }
			}else if(text[0].equals("HIT")) {
				player = players.get(text[1]);
				int hp = player.getCurrHealth()-1;
				player.subtractHealth();
				if(hp==0) {
					removePlayer(text[2]);
				}
			}else if(text[0].equals("MOV")) {
				player = players.get(text[1]);
				player.setXpos(Float.parseFloat(text[2]));
				player.setYpos(Float.parseFloat(text[3]));
				player.setTankFace(Integer.parseInt(text[4]));
				sendToAll(data);
			}else if(text[0].equals("PUA")) {
				player = players.get(text[1]);
				int type = Integer.parseInt(text[2]);
				if(type==Constants.HEART) {
					player.addHealth();
				}else if(type==Constants.STAR) {
					//change bullet
				}else if(type==Constants.SHIELD) {
					//put shield
				}
				sendToAll(data);
			}else if(text[0].equals("NPU")) {
				//update to server's board
				sendToAll(data);
			}
			
		}//process all the data received from clients
	
	public void sendToAll(String text) {
		Object[] names = players.keySet().toArray();
		for(int i=0; i<players.size(); i++) {
			byte[] data = text.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, players.get(names[i]).ipaddress, players.get(names[i]).port);
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
				String tankColor = tankColors.get(new Random().nextInt(tankColors.size()));
				//System.out.println(name);
				player = createPlayer(name, address, port);
				player.setTankColor(tankColor);
				players.put(name, player);
				tankColors.remove(tankColor);
				send("MAP " + mapTemplate.toString(), address,port);
				Log.info(" UDPserver: " + name + " has joined the game.");
			}else {
				send("NAK NNA", address, port);
			}//checks availability of username
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removePlayer(String name) {
		Player dead = players.get(name);
		send("Game Over", dead.ipaddress, dead.port);
		System.out.println(players.keySet());
		players.remove(name);
		System.out.println(players.keySet());
		if(players.size() == 1) {
			gameState = 3;//endgame
		}
	}//remove dead tanks
	
	public void createMapTemplate(File filename){
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))){
			String currLine;
			while((currLine = br.readLine()) != null){
				if(currLine.isEmpty())continue;
				ArrayList<Integer> row = new ArrayList<>();
				String[] values = currLine.trim().split(" ");
				for(String string : values){
					if(!string.isEmpty()){
						int id = Integer.parseInt(string);
						row.add(id);
					}
				}
				mapTemplate.add(row);
			}
		}catch(IOException e){

		}
		//put destructibles
		int destructibles = 50 + (int)(Math.random() * ((100 - 50) + 1));
		for(int i = 0; i<destructibles; i++){
			do {
				x = rand.nextInt(20);
				y = rand.nextInt(15);
			}while(mapTemplate.get(y).get(x) != EMPTY);
			mapTemplate.get(y).set(x,DESTRUCTIBLE);
		}
		
	}
	
	public void run() {
        Log.info(" UDPserver started...");
        createMapTemplate(new File("res/map1.txt"));
        while(true) {        	
        	if(gameState == 0) {
        		//wait for players
        		if(players.size()<3) {
        			//wait
        			receive();
        		}else {
        			gameState = 1;
        			if (players != null) {
        				Object[] items = players.keySet().toArray();
        				for(int i=0; i<players.size();i++) {
        					player = players.get(items[i]);
        					String data1 = "PLYR " + player.getName() + " " + player.ipaddress.toString() + " " + player.port;
        					sendToAll(data1);
        					String data2 = "POS "+ player.getName() + " " + player.getXpos() + " " + player.getYpos();
        					sendToAll(data2);
        					String data3 = "IMG " + player.getName() + " " + player.tankColor;
        					sendToAll(data3);
        				}
        			}
        			receive();
        		}
        	}else if(gameState == 1) {
        		//start game ; initialize board for all clients
        		try {
					sendToAll("MSG START");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		receive();
        	}else if(gameState == 2) {
        		//game in progress
        		receive();
        	}else if(gameState == 3){
        		//end game
        	}
        }
	}//run server continuously
	
	public static Player createPlayer(String name, InetAddress address, int port) {
		do {
			x = rand.nextInt(20);
			y = rand.nextInt(15);			
		}while(mapTemplate.get(y).get(x)!=EMPTY);
		player = new Player(name,x*32+1,y*32+1, address, port);
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