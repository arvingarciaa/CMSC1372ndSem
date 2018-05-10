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
import tanks.Engine;

public class UDPclient extends Thread{
	DatagramSocket socket = null;

	public String name;
	public boolean CONNECTION = false;
	public InetAddress address;
	public int PORT;
	private HashMap<String, Player> players = new HashMap<>();
	private static Player player;
	private String tankColor;
	public ArrayList<ArrayList<Integer>> mapTemplate;

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
			Engine.tcpclient.sendToServer("CONNECT " + name + "\n");
			//request list of player names
		}else if(data[0].equals("NAK")){
			if(data[1].equals("NNA")) {
				System.out.println("Username already exists!");
			}else {
				System.out.println("Connection not allowed! Game in progress");
			}
		}
		if(CONNECTION) {
			if(data[0].equals("PLYR")) {
				players.put(data[1], new Player(data[1], 0, 0, InetAddress.getByName(data[2].substring(1, data[2].length())), Integer.parseInt(data[3])));
			}else if(data[0].equals("POS")) {
				player = getPlayers().get(data[1]);
				player.setXpos(Float.parseFloat(data[2]));
				player.setYpos(Float.parseFloat(data[3]));
			}else if(data[0].equals("IMG")) {
				player = getPlayers().get(data[1]);
				player.setTankColor(data[2]);
			}else if(data[0].equals("MOV")) {
				player = getPlayers().get(data[1]);
				player.setXpos(Float.parseFloat(data[2]));
				player.setYpos(Float.parseFloat(data[3]));
				player.setTankFace(Integer.parseInt(data[4]));
			}else if(data[0].equals("HIT")) {
				player = getPlayers().get(data[1]);
				int hp = player.getCurrHealth()-1;
				player.subtractHealth();
				if(hp==0) {
					getPlayers().remove(data[2]);
				}
			} else if(data[0].equals("MAP")) {
				mapTemplate = new ArrayList<>();
				String map = text.substring(data[0].length()+2, text.lastIndexOf("]]"));
				String[] rows = map.trim().split("], ");
				for (String val: rows) {
					val = val.substring(1,val.length());
					String[] x = val.trim().split(", ");
					ArrayList<Integer> row = new ArrayList<>();
					for (String y: x) {
						row.add(Integer.parseInt(y));
					}
					mapTemplate.add(row);
				}
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
	    	    send("CONNECT " + name);
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

	public HashMap<String, Player> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<String, Player> players) {
		this.players = players;
	}
	
	public boolean checkGameStart() {
//		Log.info(players.toString());
		if (players.size()==3) return true;
		return false;
	}
}