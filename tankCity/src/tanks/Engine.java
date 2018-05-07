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
	private static int x;
	private static int y;
	private static Random rand = new Random();
	private static Player player;
	private static ArrayList<Player> players;
	
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
	
	public static Player createPlayer(String name) {
		do {
			x = rand.nextInt(20)*32;
			y = rand.nextInt(15)*32;
		}while(GameState.blocked[x/32][y/32]==true);
		player = new Player(x,y);
		//players.add(player);
		return player;
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
		this.addState(new WaitState());
	}
}
