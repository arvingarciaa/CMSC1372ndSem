package udpModule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import org.newdawn.slick.util.Log;


public class UDPclient extends Thread{
	DatagramSocket socket = null;

	private String name;
	private static boolean CONNECTION = false;
	
	InetAddress address = null;
	int PORT;

	public UDPclient(String name, InetAddress ipAddress, int PORT) {
		this.PORT = PORT;
		this.name = name;
		try {
			address = ipAddress;
			socket = new DatagramSocket();
			socket.setSoTimeout(5000);
		} catch(Exception e) {
			e.printStackTrace();;
		}
	}
	
	public void receive() {
	    try {
		    byte[] data = new byte[100000];
			DatagramPacket packet = new DatagramPacket(data, data.length);
	    	socket.receive(packet);
			String text = new String(packet.getData());
			if(text.startsWith("ACK") ) {
				CONNECTION=true;
				System.out.println(CONNECTION);
			}else if(text.startsWith("NAK")){
				System.out.println("Username already exists!");
			}
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//continuously receive from server
	}
	
//	public static void main(String[] args){
//		int port = 0;
//		String name = "";
//		String ipAddress = "";
//		
//		
//		if(args.length < 3) {
//			System.err.println("Error: Include a player name, ipAddress and port number.");
//			System.exit(1);
//		}
//		try {
//			name = args[0];
//			System.out.println(args[0]);
//			ipAddress = args[1];
//			System.out.println(args[1]);
//			port = Integer.parseInt(args[2]);
//			System.out.println(args[2]);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		UDPclient udpclient= new UDPclient(name, ipAddress, port);
//		udpclient.start();
//	}
}