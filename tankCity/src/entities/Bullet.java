package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Bullet {
	private Vector2f pos;
	private Vector2f speed;
	private int lived = 0;

	private boolean active = true;
	private static int MAX_LIFETIME = 1000;

	public Bullet(Vector2f pos, Vector2f speed) {
		this.pos = pos;
		this.speed = speed;
	}
	
	public Bullet() {
		active = false;
	}
	
	public void update(int delta) {
		if(active) {
			Vector2f realSpeed = speed.copy();
			realSpeed.scale(delta/1000.0f);
			pos.add(realSpeed);
			
			lived += delta;
			if(lived > MAX_LIFETIME) {
				active = false;
			}
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if(active) {
			g.setColor(Color.red);
			g.fillOval(pos.getX()-10, pos.getY()-10, 10, 10);	
		}
	}	
	
	public boolean isActive() {
		return active;
	}
}