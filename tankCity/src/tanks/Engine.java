package tanks;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import states.*;
import udpModule.UDPclient;

public class Engine extends StateBasedGame{
	public static UDPclient udpclient;
	public static int score = 0;
	
	public static void createUdp (String name, InetAddress clientAddress, int clientport) {
			udpclient = new UDPclient(name, clientAddress, clientport);
			udpclient.start();
	}

	public Engine() {
		super("Tank City");
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			AppGameContainer game = new AppGameContainer(new Engine());
			game.setDisplayMode(Constants.WIDTH, Constants.HEIGHT, false);
			game.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
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
