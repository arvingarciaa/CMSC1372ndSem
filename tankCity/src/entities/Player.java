package entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

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
    public Bullet[] bullets;
    private int current = 0;
    private static int FIRE_RATE = 500;
    private int bullet_interval;
    private int tank_face = 0;  // 0 for up, 1 for left, 2 for down, 3 for right
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
		bullets = new Bullet[2];	//increase to add more bullets
		for(int i = 0; i < bullets.length; i++) {
			bullets[i] =  new Bullet();
		}
		
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		speed = (float) 0.08;
		bullet_interval += delta;
		
		if (input.isKeyDown(Input.KEY_W) && GameState.isUpSafe()==true) {
			y-=( speed*delta );
			image = Resources.getImage("up");
			tank_face = 0;
		}
		if (input.isKeyDown(Input.KEY_S) && GameState.isDownSafe()==true) {
			y+=( speed*delta );
			image = Resources.getImage("down");
			tank_face = 2;
		}
		if (input.isKeyDown(Input.KEY_D) && GameState.isRightSafe()==true) {
			x+=( speed*delta );
			image = Resources.getImage("right");
			tank_face = 3;
		}
		if (input.isKeyDown(Input.KEY_A) && GameState.isLeftSafe()==true) {
			x-=( speed*delta );
			image = Resources.getImage("left");
			tank_face = 1;
		}
		
		if(bullet_interval > FIRE_RATE && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			if(tank_face == 0) bullets[current] = new Bullet(new Vector2f(x+22,y), new Vector2f(0,-200));
			if(tank_face == 1) bullets[current] = new Bullet(new Vector2f(x,y+22), new Vector2f(-200, 0));
			if(tank_face == 2) bullets[current] = new Bullet(new Vector2f(x+22,y+32), new Vector2f(0, 200));
			if(tank_face == 3) bullets[current] = new Bullet(new Vector2f(x+32,y+22), new Vector2f(200, 0));
			current++;
			if(current >= bullets.length) {
				current = 0;
			}
			bullet_interval = 0;
		}
		for(Bullet b : bullets) {
			b.update(delta);
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
