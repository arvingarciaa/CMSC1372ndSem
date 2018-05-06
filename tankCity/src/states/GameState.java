package states;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import entities.Bullet;
import entities.Player;
import tanks.Constants;
import tanks.Resources;
import udpModule.UDPclient;

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
	private static float alpha = 0;
	private static Font font;
	private static TrueTypeFont ttf;
	private static int pause = 0;
	private UDPclient udpclient;
	private int x,y;
	private Random rand = new Random();
	public static boolean[][] destroyed = new boolean[20][15];
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		udpclient = MenuState.udpclient;
		map = new TiledMap("res/map.tmx","res");
		
		solidsLayer = map.getLayerIndex("solids");
		blocked = new boolean[Constants.WIDTH][Constants.HEIGHT];  // This will create an Array with all the Tiles in your map. When set to true, it means that Tile is blocked.
		dest = new boolean[Constants.WIDTH][Constants.HEIGHT];
		blocks = new ArrayList<Rectangle>();
		
		System.out.println("getWidth: " + map.getWidth());
		System.out.println("getHeight: " + map.getHeight());
		for(int i = 0; i < map.getWidth(); i++) {
		    for(int j = 0; j < map.getHeight(); j++) {
		        // Read a Tile
		        int tileID = map.getTileId(i, j, solidsLayer);
		        
		        String value = map.getTileProperty(tileID, "blocked", "false");
		        String destroyable = map.getTileProperty(tileID, "destroyable", "false");

		        if(value.equals("true")) {
		            blocks.add(new Rectangle(i * tileSize, j * tileSize, tileSize, tileSize));
		            blocked[i][j] = true;
		            if(destroyable.equals("true")) {
		            	dest[i][j] = true;	
		            }
		        }
		    }
		}
		
		font = new Font("Verdana", Font.BOLD, 20);
	    ttf = new TrueTypeFont(font, true);
		
		//randomize x and y pos of tank then check if blocked
		do {
			x = rand.nextInt(20)*32;
			y = rand.nextInt(15)*32;
			System.out.println("position: " + x + " " + y);
		}while(blocked[x/32][y/32]==true);
		tank = new Player(x,y);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		map.render(0,0,0,0,640,480);
		for(int x=0; x < map.getWidth(); x++) {
				for(int y=0; y < map.getHeight(); y++) {
					if(destroyed[x][y] == true) {
						Image texture = Resources.getImage("grass");
			            g.drawImage(texture, x*32, y*32, null);
			       }
				}
			}
		tank.render(gc,g);	//renders the tank
		for(Bullet b: tank.bullets) {	//pre-renders the bullets
			b.render(gc,g);
		}
		if (pause%2==1)		// to see scores
		{
		    Rectangle rect = new Rectangle (0, 0, 640, 480);
		    g.setColor(new Color (0.2f, 0.2f, 0.2f, alpha));
		    g.fill(rect);   
		    ttf.drawString(320,240,Player.name + ": " + Player.score);
		    if (alpha < 0.5f)
		        alpha += 0.01f;
		}
		else
		{
		    if (alpha > 0)
		        alpha -= 0.01f;
		    g.setColor(new Color(1.0f,1.0f,1.0f,1.0f));
		}
	}	

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		tank.update(gc, delta);
		if (gc.getInput().isKeyPressed(Input.KEY_TAB))	//to see score
			pause++;
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
		        		if(dest[collX][collY] == true)	{
		        			blocks.remove(i);
		        			Player.score++;
		        			destroyed[collX][collY] = true;
		        		}
		            return true;
		        }
		    }
		    return false;
		}
}


