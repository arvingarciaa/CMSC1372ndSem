package entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import states.GameState;

public class Bullet {
	private Vector2f pos;
	private Vector2f speed;
	private int lived = 0;
	public Rectangle bulletRectangle;

	private boolean active = true;
	private static int MAX_LIFETIME = 1000;

	public Bullet(Vector2f pos, Vector2f speed) {
		this.pos = pos;
		this.speed = speed;
		bulletRectangle = new Rectangle(pos.getX(), pos.getY(), 10, 10);
	}
	
	public Bullet() {
		active = false;
	}
	
	public void update(int delta) {
		if(active) {
			Vector2f realSpeed = speed.copy();
			realSpeed.scale(delta/1000.0f);
			bulletRectangle.setLocation(pos.getX()-10, pos.getY()-10);
			
			if(!GameState.hitsWall(bulletRectangle)){
				pos.add(realSpeed);
		    } else {
		    	active = false;
		    	//GameState.map.getCell
		    }
			
			lived += delta;
			if(lived > MAX_LIFETIME) {
				active = false;
			}
			
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if(active) {
			g.fillOval(pos.getX()-10, pos.getY()-10, 10, 10);			
		}
	}	
	
	public boolean isActive() {
		return active;
	}
}
