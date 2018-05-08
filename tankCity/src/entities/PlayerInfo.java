package entities;

import java.net.InetAddress;

import tanks.Resources;

public class PlayerInfo {
	private String name;
	private InetAddress address;
	private int port;
	private float xpos;
	private float ypos;
	private String tankColor;
	public org.newdawn.slick.Image image;
	
	public PlayerInfo(String name, InetAddress address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	/**
	 * Returns the address
	 * @return
	 */
	public InetAddress getAddress(){
		return address;
	}
	
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * Returns the port number
	 * @return
	 */
	public int getPort(){
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Returns the name of the player
	 * @return
	 */
	public String getName(){
		return name;
    }
	
	public void setName(String name) {
		this.name = name;
	}
	
	public float getXpos() {
		return this.xpos;
	}
	
	public float getYpos() {
		return this.ypos;
	}
	
	public void setXpos(float xpos) {
		this.xpos = xpos;
	}
	
	public void setYpos(float ypos) {
		this.ypos = ypos;
	}
	
	public void setImage(String tankColor) {
		image = Resources.getImage(tankColor,"up");
		//this.tankColor = tankColor;
	}
}
