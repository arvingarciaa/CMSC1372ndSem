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
	public float playerHeight;
	public float playerWidth;
	public static int lives;
	public static int visible;
	public static int position;
	
	public Entity(){
		System.out.println("effin bs");
		init();
	}
	
//	public Entity(float x, float y, float height, float width) {
//		this.x = x;
//		this.y = y;
//		this.playerHeight = height;
//		this.playerWidth = width;
//	}
	
	public abstract void init();
	public void render(GameContainer gc, Graphics g) {
		if (image != null) {
			image.draw(x,y,playerWidth,playerHeight,color);
		}
	};
	
	public abstract void update(GameContainer gc, int delta) throws SlickException;
	
	
	
}
