package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import tanks.*;
import udpModule.UDPclient;
import states.GameState;

import java.net.InetAddress;
import java.util.Random;

public class Player {

	public org.newdawn.slick.Image image;
	public org.newdawn.slick.Color color;
	
	public float x;
	public float y;
	public float playerHeight;
	public float playerWidth;
	public static int lives;
	public static int visible;
	public static int position;
	
	private float speed = 0.1f;
    private String name;
    public Bullet[] bullets;
    private int current = 0;
    private static int FIRE_RATE = 500;
    private int bullet_interval;
    private int tank_face = 0;  // 0 for up, 1 for left, 2 for down, 3 for right
    public static int score;
    public String tankColor;
    private int playerHealth;
    private UDPclient udpclient;
    public InetAddress ipaddress;
    public int port;
    
    public Player(String name, float x, float y, InetAddress ipaddress, int port) {
    	this.x = x;
    	this.y = y;
    	this.name = name;
    	this.ipaddress = ipaddress;
    	this.port = port;
		// width height of the player
		playerWidth = 29;
		playerHeight = 29;
		visible = 1;
		score = 0;
		bullets = new Bullet[2];	//increase to add more bullets
		for(int i = 0; i < bullets.length; i++) {
			bullets[i] =  new Bullet();
		}
		playerHealth = 4;
		//image = Resources.getImage(tankColor,"up");
    }
    
    public void render(GameContainer gc, Graphics g) {
    	if (image != null) {
    		image.draw(x,y,playerWidth,playerHeight);
    	}
    	g.setColor(Color.black);
    	g.drawRect(x, y-7, playerWidth, 5);
    	g.setColor(Color.green);
    	g.fillRect(x, y-7,Math.round(playerWidth * (playerHealth/4)), 5);
	};
	
	public void update(GameContainer gc, int delta) throws SlickException {
		udpclient = Engine.udpclient;
		ipaddress = udpclient.address;
		port = udpclient.PORT;
		Input input = gc.getInput();
		speed = (float) 0.08;
		bullet_interval += delta;
		Rectangle playerBox = new Rectangle(x,y,playerWidth,playerHeight);
		
		float deltaX = x;
		float deltaY = y;
		
		
		if (input.isKeyDown(Input.KEY_W)||input.isKeyDown(Input.KEY_UP)) {
	        deltaY -= delta * speed;
			image = Resources.getImage(tankColor, "up");
			tank_face = Constants.UP;
			udpclient.send("MOV " + name + " " + deltaX + " " + deltaY + " " + Constants.UP);
		}
		if (input.isKeyDown(Input.KEY_S)||input.isKeyDown(Input.KEY_DOWN)) {
			deltaY += delta * speed;
			image = Resources.getImage(tankColor, "down");
			tank_face = Constants.DOWN;
			udpclient.send("MOV " + name + " " + deltaX + " " + deltaY + " " + Constants.DOWN);
		}
		if (input.isKeyDown(Input.KEY_D)||input.isKeyDown(Input.KEY_RIGHT)) {
			deltaX += speed*delta;
			image = Resources.getImage(tankColor, "right");
			tank_face = Constants.RIGHT;
			udpclient.send("MOV " + name + " " + deltaX + " " + deltaY + " " + Constants.RIGHT);
		}
		if (input.isKeyDown(Input.KEY_A)||input.isKeyDown(Input.KEY_LEFT)) {
			deltaX -= speed*delta;
			image = Resources.getImage(tankColor, "left");
			tank_face = Constants.LEFT;
			udpclient.send("MOV " + name + " " + deltaX + " " + deltaY + " " + Constants.LEFT);
		}
		
		float clipX = x;
		
		//checker for wall Y collision
		playerBox.setLocation(deltaX,y);
	    if(!Board.intersects(playerBox)){
	        x = deltaX;
	        udpclient.send("WALL " + name);
	    }
	    playerBox.setLocation(clipX,deltaY);  //checker for wall X collision
	    if(!Board.intersects(playerBox)){
	        y = deltaY;
	        udpclient.send("WALL " + name);
	    }
	    
	    if(Board.collidesWith(playerBox, Powerup.powerupRectangle)) {
	    	this.addHealth();
	    	Powerup.setActiveToFalse();
	    	udpclient.send("PUA "+ name + " " + Constants.HEART);
	    }

		
		if(bullet_interval > FIRE_RATE && input.isKeyDown(Input.KEY_SPACE)) {
			
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
	
	public int getCurrHealth() {
		return playerHealth;
	}
	
	public void subtractHealth() {
		if(playerHealth > 0) playerHealth--;
	}
	
	public void addHealth() {
		playerHealth++;
	}
	
	public void setTankColor(String tankColor) {
		//image = Resources.getImage(tankColor,"up");
		this.tankColor = tankColor;
	}
	
	public void setImage(String tankColor) {
		image = Resources.getImage(tankColor,"up");
		//this.tankColor = tankColor;
	}

	public String getName(){
		return name;
    }
	
	public void setName(String name) {
		this.name = name;
	}
	
	public float getXpos() {
		return this.x;
	}
	
	public float getYpos() {
		return this.y;
	}
	
	public void setXpos(float xpos) {
		this.x = xpos;
	}
	
	public void setYpos(float ypos) {
		this.y = ypos;
	}
	
	public void setTankFace(int face) {
		this.tank_face = face;
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
