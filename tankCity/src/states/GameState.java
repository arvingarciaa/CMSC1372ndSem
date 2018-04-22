package states;


import java.util.ArrayList;

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

public class GameState extends BasicGameState{
	public static TiledMap map;
	public static int solidsLayer;
	public static int objectLayer;
	private static Player tank;
	public boolean blocked[][];
	private static ArrayList<Rectangle> blocks;
	public static int tileSize = 32;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		map = new TiledMap("res/map.tmx","res");
		tank = new Player();
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

		for(int i = 0; i < map.getWidth(); i++) {
		    for(int j = 0; j < map.getHeight(); j++) {
		        System.out.println("blocked[" + i + "][" + j + "] = " + blocked[i][j]);
		    }
		}
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		map.render(0,0,0,0,640,480);
		tank.render(gc,g);	//renders the tank
		//g.drawRect(tank.x,tank.y,32,32);
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

	public static boolean isUpSafe() {
		int top = (map.getTileId((int)(tank.x/32), (int)(tank.y/32), solidsLayer));
		if (top == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isDownSafe() {
		int bot = (map.getTileId((int)(tank.x/32), (int)(tank.y/32)+1, solidsLayer));
		if (bot == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isRightSafe() {
		int right = (map.getTileId((int)(tank.x/32)+1, (int)(tank.y/32)+1, solidsLayer));
		if (right == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isLeftSafe() {
		int left = (map.getTileId((int)(tank.x/32), (int)(tank.y/32)+1, solidsLayer));
		if (left == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean intersects(Rectangle player) {
	    for(int i=0; i<blocks.size(); i++){
	        if(player.intersects(blocks.get(i))){
	            return true;
	        }
	    }
	    return false;
	}


	

	

}
