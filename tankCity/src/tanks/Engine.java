package tanks;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import entities.Player;
import states.*;
import tcpModule.TCPclient;
import udpModule.UDPclient;

public class Engine extends StateBasedGame{
	public static UDPclient udpclient;
	public static TCPclient tcpclient;
	public static int score = 0;
	
	public Engine() {
		super("Tank City");
	}

	public static void main(String[] args) {
		try {
			AppGameContainer game = new AppGameContainer(new Engine());
			game.setDisplayMode(Constants.WIDTH, Constants.HEIGHT+Constants.CHAT_HEIGHT, false);
			game.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static void createConnection (String name, InetAddress clientAddress, int clientport) {
		udpclient = new UDPclient(name, clientAddress, clientport);
		udpclient.start();
		tcpclient = new TCPclient(name, clientAddress, clientport);
		tcpclient.start();
	}	

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		gc.setAlwaysRender(true);
		gc.setShowFPS(true);
		gc.setVSync(true);
	
		new Resources();
		this.addState(new MenuState());
		this.addState(new GameState());
		this.addState(new ManualState());
		this.addState(new EndState());
	}
}
