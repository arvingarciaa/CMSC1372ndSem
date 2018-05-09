package udpModule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.util.Log;

import entities.Player;
import entities.PlayerInfo;

public class UDPclient extends Thread{
	DatagramSocket socket = null;

	public String name;
	public boolean CONNECTION = false;
	public InetAddress address;
	public int PORT;
	private HashMap<String, Player> players;
	private static Player player;
	private String tankColor;

	public UDPclient(String name, InetAddress ipAddress, int PORT) {
		this.PORT = PORT;
		this.setPlayerName(name);
		try {
			address = ipAddress;
			socket = new DatagramSocket();
			socket.setSoTimeout(5000);
		} catch(Exception e) {
			e.printStackTrace();;
		}
	}
	
	public String getPlayerName() {
		return this.name;
	}
	
	public void setPlayerName(String name) {
		this.name = new String(name);
	}
	
	public void receive() {
	    try {
		    byte[] data = new byte[100000];
			DatagramPacket packet = new DatagramPacket(data, data.length);
	    	socket.receive(packet);
			String text = new String(packet.getData());
			dataParser(text);
	    } catch(SocketTimeoutException e) {
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public void send(String text) {
		text = text + " " + this.name;
	    byte[] data = new byte[1024];
	    data = text.getBytes();
	    DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, PORT);
	    try { 
	    	socket.send(sendPacket);
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	//process all the data received from server
	public void dataParser(String text) throws UnknownHostException {
		String[] data = text.trim().split(" ");
		if(data[0].equals("ACK" )) {
			CONNECTION=true;
			System.out.println("Welcome " + name +"!");
			players.put(name, new Player(name, 0, 0));
			
			//request list of player names
		}else if(data[0].equals("NAK")){
			if(data[1].equals("NNA")) {
				System.out.println("Username already exists!");
			}else {
				System.out.println("Connection not allowed! Game in progress");
			}
		}else if(data[0].equals("POS")) {
			player = players.get(data[1]);
			player.setXpos(Integer.parseInt(data[2]));
			player.setYpos(Integer.parseInt(data[3]));
		}else if(data[0].equals("IMG")) {
			player = players.get(data[1]);
			player.setImage(data[2]);
		}else if(data[0].equals("MOV")) {
			player = players.get(data[1]);
			player.setXpos(Integer.parseInt(data[2]));
			player.setYpos(Integer.parseInt(data[3]));
			player.setTankFace(Integer.parseInt(data[4]));
		}else if(data[0].equals("HIT")) {
			player = players.get(data[1]);
			int hp = player.getCurrHealth()-1;
			player.subtractHealth();
			if(hp==0) {
				players.remove(data[2]);
			}
		}
	}
	
	public String getTankColor() {
		return tankColor;
	}
	
	@Override
	public void run() {
		Log.info("Game Client Started.");
		
		while(CONNECTION==false) {
			try {
	    	    send("CONNECT");
	    	    receive();
	    	    //Thread.sleep(1000);
			} catch(Exception e) {}			
		}

		while(true) {				
			try {
				receive();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}//continuously receive from server
	}
}