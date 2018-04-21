package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;



public abstract class Entity{
	
	public Image image;
	public Color color;
	
	public float x;
	public float y;
	public float height;
	public float width;
	public static int lives;
	public static int visible;
	public static int position;
	
	public Entity() throws SlickException{
		init();
	}
	
	public Entity(float x, float y, float height, float width) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}
	
	public abstract void init() throws SlickException;
	public void render(GameContainer gc, Graphics g) {
		if (image != null) {
			image.draw(x,y,width,height,color);
		}
	};
	
	public abstract void update(GameContainer gc, int delta) throws SlickException;
	
	
	
}