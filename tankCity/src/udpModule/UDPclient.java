package udpModule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.newdawn.slick.util.Log;

import entities.PlayerInfo;


public class UDPclient extends Thread{
	DatagramSocket socket = null;

	public String name;
	private static boolean CONNECTION = false;
	
	InetAddress address = null;
	int PORT;
	
	private HashMap<String, PlayerInfo> players = null;

	public UDPclient(String name, InetAddress ipAddress, int PORT) {
		this.PORT = PORT;
		this.setPlayerName(name);;
		try {
			address = ipAddress;
			socket = new DatagramSocket();
			socket.setSoTimeout(5000);
		} catch(Exception e) {
			e.printStackTrace();;
		}
		players = new HashMap<String, PlayerInfo>();
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
	    	e.printStackTrace();
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
		if(data[0] == "ACK" ) {
			CONNECTION=true;
			System.out.println("Welcome " + name +"!");
		}else if(data[0] == "NAK"){
			if(data[1] == "NNA") {
				System.out.println("Username already exists!");
			}else {
				System.out.println("Connection not allowed! Game in progress");
			}
		}else if(text.startsWith("MOV")) {
			//new tank position
		}else if(data[0]=="PLYR") {			
			try {
				PlayerInfo newPlayer = new PlayerInfo(data[1],InetAddress.getByName(data[2]),Integer.parseInt(data[3]));
				players.put(data[1], newPlayer);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(data[0] == "HIT") {
			//tank hit
			//update score
		}else if(data[0] == "WALL") {
			//update map
		}else if(data[0] == "PUA") {
			//update map and player attributes
		}else if(data[0] == "NPUS") {
			//update map
		}else if(data[0] == "POS") {
			//init tank positions
		}else if(data[0] == "MSG") {
			//chat
		}
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