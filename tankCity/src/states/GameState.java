package states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import entities.Bullet;
import entities.Powerup;
import entities.Player;
import entities.PlayerInfo;
import tanks.Constants;
import tanks.Engine;
import tanks.Resources;
import tcpModule.TCPclient;
import udpModule.UDPclient;

public class GameState extends BasicGameState{
	public static TiledMap map;
	public static int solidsLayer;
	public static int objectLayer;
	public static boolean blocked[][];
	public static boolean dest[][];
	private static ArrayList<Rectangle> blocks;
	public static int tileSize = 32;
	public static int collX, collY;
	private static float alpha = 0;
	private UnicodeFont font;
	private float textWidth;
	private String text;
	private static int pause = 0;
	private UDPclient udpclient;
	private TCPclient tcpclient;
	private int x,y;
	private Random rand = new Random();
	public static boolean[][] destroyed = new boolean[20][15];
	private TextField textFieldChatInput;
	private String playerName;
	ArrayList<String> chatMessages;
	private HashMap<String, Player> players = new HashMap<>();
	private static Player player;
	boolean boardAtStart = false;
	private static Powerup powerup;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		map = new TiledMap("res/map.tmx","res");
		powerup = new Powerup();
		solidsLayer = map.getLayerIndex("solids");
		blocked = new boolean[Constants.WIDTH][Constants.HEIGHT];  // This will create an Array with all the Tiles in your map. When set to true, it means that Tile is blocked.
		dest = new boolean[Constants.WIDTH][Constants.HEIGHT];
		blocks = new ArrayList<Rectangle>();
		
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

		font = getNewFont("Arial", 48);
		textFieldChatInput = new TextField(gc, gc.getDefaultFont(), 0, Constants.TOTAL_HEIGHT-25, 640, 25);
		
		boardAtStart = true;
		
		//randomize x and y position of tank then check if blocked
//		do {
//			x = rand.nextInt(20)*32;
//			y = rand.nextInt(15)*32;
//		}while(blocked[x/32][y/32]==true);
//		player = new Player(x,y);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		map.render(0,0,0,0,640,480);
		for(int x=0; x < map.getWidth(); x++) {
				for(int y=0; y < map.getHeight(); y++) {
					if(destroyed[x][y] == true) {
						Image texture = Resources.getImage("GREEN_grass");
			            g.drawImage(texture, x*32, y*32, null);
			       }
				}
			}
//		players = udpclient.players;
//		Object[] currPlayers = players.keySet().toArray();
//		for(int i=0; i<players.size(); i++) {
//			players.get(currPlayers[i]).render(gc,g);
//			for(Bullet b: players.get(currPlayers[i]).bullets) {	//pre-renders the bullets
//				b.render(gc,g);
//			}
//		}
		player.render(gc,g);	//renders the tank
		for(Bullet b: player.bullets) {	//pre-renders the bullets
			b.render(gc,g);
		}
		if (pause%2==1)		// to see scores
		{
		    Rectangle rect = new Rectangle (0, 0, 640, 480);
		    g.setColor(new Color (0.2f, 0.2f, 0.2f, alpha));
		    g.fill(rect);   
		    font.loadGlyphs();
		    udpclient = Engine.udpclient;
		    playerName = udpclient.getPlayerName();
			text = playerName + ": " + Player.score;
	        textWidth = font.getWidth(text);
			font.drawString(Constants.WIDTH/2f - textWidth/2f, 100, text);
 
			text = playerName + ": " + Player.score;
			textWidth = font.getWidth(text);
			font.drawString(Constants.WIDTH/2f - textWidth/2f, 170, text);
			 
			text = playerName + ": " + Player.score;
			textWidth = font.getWidth(text);
			font.drawString(Constants.WIDTH/2f - textWidth/2f, 240, text);
			 
			text = playerName + ": " + Player.score;
			textWidth = font.getWidth(text);
			font.drawString(Constants.WIDTH/2f - textWidth/2f, 310, text);
			
			if (alpha < 0.5f)
		        alpha += 0.01f;
		}
		else
		{
		    if (alpha > 0)
		        alpha -= 0.01f;
		    g.setColor(new Color(1.0f,1.0f,1.0f,1.0f));
		}
		
		textFieldChatInput.render(gc, g);
		powerup.render(gc, g);
		
//		render chat messages
		int x_position = 15;
		int y_position = Constants.TOTAL_HEIGHT-50;
		for(int i=chatMessages.size()-1; i>=0; i--) {
			if(chatMessages.isEmpty()) break;
			if (y_position > Constants.HEIGHT)
				g.drawString(chatMessages.get(i), x_position, y_position);
			y_position-=13;
		}
		
	}	

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		tcpclient = Engine.tcpclient;
		udpclient = Engine.udpclient;
		players = udpclient.players;
		//get the chat messages
		chatMessages = tcpclient.getMessages();
		
				
		if (gc.getInput().isKeyPressed(Input.KEY_TAB))	//to see score
			pause++;
		
		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();
		
		if (!(mouseX>=0 && mouseX<=Constants.WIDTH && mouseY>=Constants.HEIGHT)) {
			player.update(gc, delta);
		}
		powerup.update(delta);
		
		//getting chat input
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			String data = textFieldChatInput.getText();
			if (data.length()>0) {
				data = data + "\n";
				playerName = udpclient.getPlayerName();
				Engine.tcpclient.sendToServer(playerName + ": " + data);
				textFieldChatInput.setText("");
			}
		}		
	}

	@Override
	public int getID() {
		return States.GAME;
	}

	public static boolean intersects(Rectangle rec1) { //for tank to wall
	    for(int i=0; i<blocks.size(); i++){
	        if(rec1.intersects(blocks.get(i))){
	            return true;
	        }
	    }
	    return false;
	}
	
	public static boolean collidesWith(Rectangle rec1, Rectangle rec2) {
		if(rec1.intersects(rec2)) {
			return true;
		}
		return false;
	}
	
	public static boolean hitsWall(Rectangle rec1) { //for bullet
		 for(int i=0; i<blocks.size(); i++){
		        if(rec1.intersects(blocks.get(i))){
		        		collX = (int) (blocks.get(i).getX()/32);
		        		collY = (int) (blocks.get(i).getY()/32);
		        		if(dest[collX][collY] == true)	{
		        			blocks.remove(i);
		        			Player.score++;
		        			Player.subtractHealth();
		        			destroyed[collX][collY] = true;
		        		}
		            return true;
		        }
		    }
		    return false;
		}
	
	@SuppressWarnings("unchecked")
	public UnicodeFont getNewFont(String fontName, int fontSize) {
		font = new UnicodeFont(new Font(fontName, Font.BOLD, fontSize));
		font.addGlyphs("@");
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		return (font);
	}
}


