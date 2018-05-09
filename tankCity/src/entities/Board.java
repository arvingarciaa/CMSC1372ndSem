package entities;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import tanks.Constants;
import tanks.Engine;
import tanks.Resources;
import tcpModule.TCPclient;
import udpModule.UDPclient;

public class Board {
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
	public static boolean[][] destroyed = new boolean[20][15];
	private TextField textFieldChatInput;
	private String playerName;
	ArrayList<String> chatMessages;
	private HashMap<String, Player> players = new HashMap<>();
	private static Player player;
	
	public Board() throws SlickException{
		//initialize map
		map = new TiledMap("res/map.tmx","res");
		
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
	}
	
	public void initializeBoard(HashMap<String, Player> players) {
		this.players = players;
		//put players on the board
	}
	
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
		//render players and bullets
		Object[] currPlayers = players.keySet().toArray();
		for(int i=0; i<players.size(); i++) {
			player = players.get(currPlayers[i]);
			player.image.draw(x,y,player.playerWidth,player.playerHeight,player.color);
			for(Bullet b: players.get(currPlayers[i]).bullets) {	//pre-renders the bullets
				b.render(gc,g);
			}
		}
//		player.render(gc,g);	//renders the tank
//		for(Bullet b: player.bullets) {	//pre-renders the bullets
//			b.render(gc,g);
//		}
		if (pause%2==1)		// to see scores
		{
		    Rectangle rect = new Rectangle (0, 0, 640, 480);
		    g.setColor(new Color (0.2f, 0.2f, 0.2f, alpha));
		    g.fill(rect);   
		    font.loadGlyphs();
		    udpclient = Engine.udpclient;
		    playerName = udpclient.playerName;
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
	}	

	//@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		tcpclient = Engine.tcpclient;
		udpclient = Engine.udpclient;
//		players = udpclient.players;
		//get the chat messages
		chatMessages = tcpclient.getMessages();	
				
		if (gc.getInput().isKeyPressed(Input.KEY_TAB))	//to see score
			pause++;
		
		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();
		
		if (!(mouseX>=0 && mouseX<=Constants.WIDTH && mouseY>=Constants.HEIGHT)) {
			player.update(gc, delta);
		}
		
		//getting chat input
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			String data = textFieldChatInput.getText();
			if (data.length()>0) {
				data = data + "\n";
				playerName = udpclient.playerName;
				Engine.tcpclient.sendToServer(playerName + ": " + data);
				textFieldChatInput.setText("");
			}
		}		
	}

}
