package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
import org.newdawn.slick.util.Log;

import entities.Bullet;
import entities.Powerup;
import states.States;
import entities.Player;
import tanks.Constants;
import tanks.Engine;
import tanks.Resources;
import tcpModule.TCPclient;
import udpModule.UDPclient;

public class Board{
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
	private UDPclient udpclient = Engine.udpclient;
	private TCPclient tcpclient;
	private int x,y;
	private Random rand = new Random();
	public static boolean[][] destroyed;
	public TextField textFieldChatInput;
	private String playerName;
	ArrayList<String> chatMessages;
	private HashMap<String, Player> players;
	private static Player player;
	private static Powerup powerup;
	private Image chatbg;
	//map attributes
	private ArrayList<ArrayList<Integer>> mapTemplate=null;
	private int mapWidth;
	private int mapHeight;
	private static int INDESTRUCTIBLE = 1;
	private static int EMPTY = 0;
	private static int DESTRUCTIBLE = 2;
	private Image asphalt, metal, brick;
	
	
	public Board() {
		powerup = new Powerup();
		blocked = new boolean[Constants.WIDTH][Constants.HEIGHT];  // This will create an Array with all the Tiles in your map. When set to true, it means that Tile is blocked.
		blocks = new ArrayList<Rectangle>();
		
	}
	

	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {

		for(int i=0; i<mapHeight; i++){
			for(int j=0; j<mapWidth; j++){
				Integer cell = mapTemplate.get(i).get(j);
				if(cell==EMPTY) {
					g.drawImage(asphalt, j*tileSize, i*tileSize, null);
				}else if(cell==INDESTRUCTIBLE) {
					g.drawImage(metal, j*tileSize, i*tileSize, null);
				}else if(cell==DESTRUCTIBLE) {
					g.drawImage(brick, j*tileSize, i*tileSize, null);
				}
			}
		}
		
		for(int x=0; x < mapWidth; x++) {
			for(int y=0; y < mapHeight; y++) {
				//Log.info(x+ " "+y);
				if(destroyed[x][y] == true) {
		            g.drawImage(asphalt, x*tileSize, y*tileSize, null);
		       }
			}
		}
//		player.render(gc,g);	//renders the tank
//		for(Bullet b: player.bullets) {	//pre-renders the bullets
//			b.render(gc,g);
//		}
		Object[] tanks = players.keySet().toArray();
		for(int n=0; n<players.size();n++) {
			Player tank = players.get(tanks[n]);
			tank.setImage(tank.tankColor);
			tank.render(gc, g);
			for(Bullet b: player.bullets) {	//pre-renders the bullets
				b.render(gc,g);
			}
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
		
		chatbg.draw(0,Constants.HEIGHT);
		
		textFieldChatInput.render(gc, g);
		powerup.render(gc, g);
		
//		render chat messages
		int x_position = 25;
		int y_position = Constants.TOTAL_HEIGHT-50;
		for(int i=chatMessages.size()-1; i>=0; i--) {
			if(chatMessages.isEmpty()) break;
			if (y_position > Constants.HEIGHT)
				g.drawString(chatMessages.get(i), x_position, y_position);
			y_position-=13;
		}
		
	}	

	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		tcpclient = Engine.tcpclient;
		udpclient = Engine.udpclient;
		players =  Engine.udpclient.getPlayers();
		
		if(mapTemplate==null) {
			mapTemplate = udpclient.mapTemplate;
			mapWidth = mapTemplate.get(0).size();
			mapHeight = mapTemplate.size();
			destroyed = new boolean[mapWidth][mapHeight];
			dest = new boolean[mapWidth][mapHeight];
			for(int i=0; i<mapHeight; i++){
				for(int j=0; j<mapWidth; j++){
					int cell = mapTemplate.get(i).get(j);
					if(cell!=EMPTY) {
						for(int n = 0; n<tileSize; n++) {
							for(int m = 0; m<tileSize; m++) {
								blocked[j*tileSize+m][i*tileSize+n] = true;
								if(cell==DESTRUCTIBLE) {
									dest[j][i] = true;	
					            }
							}
						}//populate per pixel
						blocks.add(new Rectangle(j*tileSize,i*tileSize,tileSize,tileSize));
					}
				}
			}
			if (players != null) {
				Object[] items = players.keySet().toArray();
				for(int i=0; i<players.size();i++) {
					if(items[i].toString().equals(udpclient.getPlayerName())) {
						player = players.get(items[i].toString());
					}
					Log.info(players.get(items[i].toString()).toString());
				}
			}
		}
		
		Object[] tanks = players.keySet().toArray();
		for(int n=0; n<players.size();n++) {
			Player tank = players.get(tanks[n]);
			tank.update(gc, delta);
		}
		
//		Log.info(mapTemplate.get(0).size()+ " "+mapTemplate.size());
		//get the chat messages
		chatMessages = tcpclient.getMessages();
		chatbg = Resources.getImage("chatbg");
		asphalt = Resources.getImage("asphalt");
		asphalt = asphalt.getScaledCopy(tileSize,tileSize);
		metal = Resources.getImage("metal");
		metal = metal.getScaledCopy(tileSize,tileSize);
		brick = Resources.getImage("brick");
		brick = brick.getScaledCopy(tileSize,tileSize);
				
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
		        		collX = (int) (blocks.get(i).getX()/tileSize);
		        		collY = (int) (blocks.get(i).getY()/tileSize);
		        		if(dest[collX][collY] == true)	{
		        			blocks.remove(i);
		        			Player.score++;
		        			//Player.subtractHealth();
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
