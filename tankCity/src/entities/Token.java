package entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import states.GameState;
import tanks.Resources;

public class Token {

	private static boolean active = true;
	private int posX, posY;
	public static Rectangle tokenRectangle;
	private Image texture = Resources.getImage("star1");
	private int selfDelta = 0;
	
	public Token() {
		this.posX = 10*32;
		this.posY = 8*32;
		tokenRectangle = new Rectangle(posX, posY, 32, 32);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		if(active) {
            g.drawImage(texture, posX, posY, null);			
		}
	}	
	
	public void update(int delta) {
		if(active) {
			selfDelta += delta;
			if(selfDelta > 0 && selfDelta < 500) {
				texture = Resources.getImage("star1");
			}
			else if(selfDelta >= 500 && selfDelta < 1000) {
				texture = Resources.getImage("star2");
			} else if(selfDelta >= 1000) {
				selfDelta = 0;
			}
		} else {
			tokenRectangle = new Rectangle(0,0,0,0);
		}
	}
	
	public static void setActiveToFalse() {
		active = false;
	}
}