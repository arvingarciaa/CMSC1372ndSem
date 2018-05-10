package entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import tanks.Constants;
import tanks.Resources;

public class Powerup {

	private boolean active = true;
	private int posX, posY;
	public static Rectangle powerupRectangle;
	private Image texture;
	private int selfDelta = 0;
	private int type;
	
	public Powerup(int posX, int posY, int type) {
		this.posX = posX;
		this.posY = posY;
		this.type = type;
		this.active = true;
		texture = Resources.getImage("star1");
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
				if(this.type == Constants.HEART) {
					texture = Resources.getImage("heart1");
				} else if (this.type == Constants.SHIELD) {
					texture = Resources.getImage("shield1");
				} else if (this.type == Constants.STAR) {
					texture = Resources.getImage("star1");
				}
			}
			else if(selfDelta >= 500 && selfDelta < 1000) {
				if(this.type == Constants.HEART) {
					texture = Resources.getImage("heart2");
				} else if (this.type == Constants.SHIELD) {
					texture = Resources.getImage("shield2");
				} else if (this.type == Constants.STAR) {
					texture = Resources.getImage("star2");
				}
			} else if(selfDelta >= 1000) {
				selfDelta = 0;
			}
		}
	}
	
	public void setActiveToFalse() {
		this.active = false;
	}

	public int getType() {
		return this.type;
	}
	
	
}
