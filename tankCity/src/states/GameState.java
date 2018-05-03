package states;


import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
	public static boolean dest[][];
	private static ArrayList<Rectangle> blocks;
	private static ArrayList<Rectangle> destroy;
	public static int tileSize = 32;
	public static int collX, collY;


	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		map = new TiledMap("res/map.tmx","res");
		tank = new Player();
		solidsLayer = map.getLayerIndex("solids");
		blocked = new boolean[Constants.WIDTH][Constants.HEIGHT];  // This will create an Array with all the Tiles in your map. When set to true, it means that Tile is blocked.
		dest = new boolean[Constants.WIDTH][Constants.HEIGHT];
		blocks = new ArrayList<Rectangle>();
		destroy = new ArrayList<Rectangle>();
		
		for(int i = 0; i < map.getWidth(); i++) {
		    for(int j = 0; j < map.getHeight(); j++) {
		    	
		        // Read a Tile
		        int tileID = map.getTileId(i, j, solidsLayer);
		        
		        // Get the value of the Property named "blocked"
		        String value = map.getTileProperty(tileID, "blocked", "false");
		        String destroyable = map.getTileProperty(tileID, "destroyable", "false");

		        // If the value of the Property is "true"...
		        if(value.equals("true")) {
		            // We set that index of the TileMap as blocked
		            // blocked[i][j] = true;		            
		            // And create the collision Rectangle
		            blocks.add(new Rectangle(i * tileSize, j * tileSize, tileSize, tileSize));
		            if(destroyable.equals("true")) {
		            	dest[i][j] = true;	
		            }
		        }
		    }
		}

		for(int i = 0; i < map.getWidth(); i++) {
		    for(int j = 0; j < map.getHeight(); j++) {
		        System.out.println("dest[" + i + "][" + j + "] = " + dest[i][j]);
		    }
		}
		
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
		        		collX = (int) (blocks.get(i).getX()/32);
		        		collY = (int) (blocks.get(i).getY()/32);
		        		System.out.println("X: " + collX + "    Y: " + collY + "   dest[collX][collY]: " + dest[collX][collY]);	
		        		if(dest[collX][collY] == true)	blocks.remove(i);
		            return true;
		        }
		    }
		    return false;
		}
}


	

	


