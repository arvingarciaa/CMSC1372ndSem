package entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import tanks.Resources;
import states.GameState;
import java.net.InetAddress;

public class Player extends Entity {

	public Player() throws SlickException {
		super();
	}

	private float speed = 0.1f;
	private float jumpSpeed = 0.3f;
	private int jump = 0;
    private String name;
    private InetAddress address;
    private int port;
    //   public Player(GameState gameState){
 //       super();
 //       this.gameState = gameState;
 //   }

	@Override
	public void init() throws SlickException {
		// x and y location of the player on render
		x = 48;
		y = 400;
		// width height of the player
		width = 32;
		height = 32;
		image = Resources.getImage("up");
		visible = 1;
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		speed = (float) 0.08;
		

		if (input.isKeyDown(Input.KEY_W)) {
			y-=( speed*delta );
			image = Resources.getImage("up");
		}
		if (input.isKeyDown(Input.KEY_S)) {
			y+=( speed*delta );

			image = Resources.getImage("down");
		}
		if (input.isKeyDown(Input.KEY_D)) {
			x+=( speed*delta );
			image = Resources.getImage("right");
		}
		if (input.isKeyDown(Input.KEY_A)) {
			x-=( speed*delta );
			image = Resources.getImage("left");
		}
		
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

    public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=x+" ";
		retval+=y;
		return retval;
	}	
}
