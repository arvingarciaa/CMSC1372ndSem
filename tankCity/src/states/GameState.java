package states;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import entities.Bullet;
import entities.Player;
import tanks.Constants;
import udpModule.UDPclient;

public class GameState extends BasicGameState{
	public static TiledMap map;
	public static int solidsLayer;
	public static int objectLayer;
	private static Player tank;
	public boolean blocked[][];
	private static ArrayList<Rectangle> blocks;
	public static int tileSize = 32;
	private UDPclient udpclient;
	private int x,y;
	private Random rand = new Random();

	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		udpclient = MenuState.udpclient;
		map = new TiledMap("res/map.tmx","res");
		
		solidsLayer = map.getLayerIndex("solids");
		blocked = new boolean[Constants.WIDTH][Constants.HEIGHT];  // This will create an Array with all the Tiles in your map. When set to true, it means that Tile is blocked.
		blocks = new ArrayList<Rectangle>();
		
		for(int i = 0; i < map.getWidth(); i++) {
		    for(int j = 0; j < map.getHeight(); j++) {

		        // Read a Tile
		        int tileID = map.getTileId(i, j, solidsLayer);
		        
		        // Get the value of the Property named "blocked"
		        String value = map.getTileProperty(tileID, "blocked", "false");

		        // If the value of the Property is "true"...
		        if(value.equals("true")) {

		            // We set that index of the TileMap as blocked
		            blocked[i][j] = true;

		            // And create the collision Rectangle
		            blocks.add(new Rectangle(i * tileSize, j * tileSize, tileSize, tileSize));
		        }
		    }
		}
		//randomize x and y pos of tank and check if blocked
		do {
			x = rand.nextInt(20)*32;
			y = rand.nextInt(15)*32;
			System.out.println("position: " + x + " " + y);
		}while(blocked[x/32][y/32]==true);
		tank = new Player(x,y);
		//send init pos to server

//		for(int i = 0; i < map.getWidth(); i++) {
//		    for(int j = 0; j < map.getHeight(); j++) {
//		        System.out.println("blocked[" + i + "][" + j + "] = " + blocked[i][j]);
//		    }
//		}
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		map.render(0,0,0,0,640,480);
		tank.render(gc,g);	//renders the tank
		for(Bullet b: tank.bullets) {	//pre-renders the bullets
			b.render(gc,g);
		}
	}	

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		/*
		//TESTERS
		//for up
		System.out.print("Up: " + map.getTileId((int)(tank.x/32), (int)(tank.y/32), solidsLayer) );
		//for down
		System.out.print(" Down: " + map.getTileId((int)(tank.x/32), (int)(tank.y/32)+1, solidsLayer));	
		//for right
		System.out.print(" Right: " + map.getTileId((int)(tank.x/32)+1, (int)(tank.y/32)+1, solidsLayer));	
		//for left
		System.out.println(" Left: " + map.getTileId((int)(tank.x/32), (int)(tank.y/32)+1, solidsLayer) + " x: " + ((int)(tank.x/32)) + " y: " + ((int)(tank.y/32)));
		 */
		
		tank.update(gc, delta);
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			s.enterState(States.MENU);
		} 
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.GAME;
	}

	public static boolean intersects(Rectangle rec1) {
	    for(int i=0; i<blocks.size(); i++){
	        if(rec1.intersects(blocks.get(i))){
	            return true;
	        }
	    }
	    return false;
	}
	
	public static boolean hitsWall(Rectangle rec1) {
		 for(int i=0; i<blocks.size(); i++){
		        if(rec1.intersects(blocks.get(i))){
		        	blocks.remove(i);
		            return true;
		        }
		    }
		    return false;
		}
}


