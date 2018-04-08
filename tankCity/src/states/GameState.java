package states;

import java.awt.Rectangle;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import tanks.Resources;
import entities.Player;

public class GameState extends BasicGameState{
	public static TiledMap map;
	public static int solidsLayer;
	public static int objectLayer;
	private Player tank;
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		map = new TiledMap("res/map.tmx","res");
		tank = new Player();
		//solidsLayer = map.getLayerIndex("solids");
		//objectLayer = map.getLayerIndex("objects");
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		// TODO Auto-generated method stub 
		map.render(0,0,0,0,640,480);
		tank.render(gc,g);
		g.drawRect(tank.x,tank.y,32,32);
	}	

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
		//for up
		//System.out.println("Layer: " + map.getTileId((int)(tank.x/32), (int)(tank.y/32), solidsLayer) + " x: " + (int)(tank.x/32) + " y: " + ((int)(tank.y/32)));
		
		//for down
		System.out.println("Layer: " + map.getTileId((int)(tank.x/32), (int)(tank.y/32)+1, solidsLayer) + " x: " + (int)(tank.x/32) + " y: " + ((int)(tank.y/32)));
	
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

/*
	public static boolean isRightSafe(float y) {
		int topHalf = map.getTileId((int) ((Math.round(x) / 32) + 2), Math.round(y / 32), solidsLayer);
		int bottomHalf = map.getTileId((int) ((Math.round(x) / 32) + 2), Math.round(y / 32) + 1, solidsLayer);
		if (topHalf == 12 && bottomHalf == 12) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isUpSafe(float y) {
		int top = (map.getTileId((int) ((Math.abs(xStart) / 32) + 1), (int) (y / 32), solidsLayer));
		if (top == 12) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isDownSafe(float y) {
		int bot = (map.getTileId((int) ((Math.abs(xStart) / 32) + 1), (int) (y / 32) + 2, solidsLayer));
		if (bot == 12) {
			return true;
		} else {
			return false;
		}
	}
	*/
}
