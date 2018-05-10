package tanks;

import tcpModule.TCPserver;
import udpModule.UDPserver;


public class Servers {
	public Servers(int port) {
		TCPserver tcpServer = new TCPserver(port);
		tcpServer.start();
		UDPserver udpServer = new UDPserver(port);
		udpServer.start();
	}
	
	public static void main(String[] args) throws Exception{
		if (args.length > 0) {
			try {
				new Servers(Integer.parseInt(args[0]));
			} catch(Exception e) {
//				System.err.println("Argument not an integer.");
				e.printStackTrace();
			}
		} else {
			System.err.println("Please include port number.");
		}
	}
}
