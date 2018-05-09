package states;

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
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import entities.Board;
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
	public static boolean[][] destroyed;
	private TextField textFieldChatInput;
	private String playerName;
	ArrayList<String> chatMessages;
	private HashMap<String, Player> players = new HashMap<>();
	private static Player player;
	boolean boardAtStart = false;
	private static Powerup powerup;
	private Image chatbg;
	
	private ArrayList<ArrayList<Integer>> mapTemplate;
	private int mapWidth;
	private int mapHeight;
	private static int INDESTRUCTIBLE = 1;
	private static int EMPTY = 0;
	private static int DESTRUCTIBLE = 2;
	private Board board;
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		board  = new Board(); 
		font = getNewFont("Arial", 48);
		board.textFieldChatInput = new TextField(gc, gc.getDefaultFont(), 0, Constants.TOTAL_HEIGHT-25, Constants.WIDTH, 25);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		board.render(gc, s, g);
	}	

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		board.update(gc, s, delta);
	}
	
	@SuppressWarnings("unchecked")
	public UnicodeFont getNewFont(String fontName, int fontSize) {
		font = new UnicodeFont(new Font(fontName, Font.BOLD, fontSize));
		font.addGlyphs("@");
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		return (font);
	}


	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}