package tanks;


import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Resources {
	private static Map<String, Image> images;
	
	public Resources() {
		images = new HashMap<String,Image>();
		
		try {
			images.put("GREEN_up", loadImage("res/up2.png"));
			images.put("GREEN_down", loadImage("res/down2.png"));
			images.put("GREEN_left", loadImage("res/left2.png"));
			images.put("GREEN_right", loadImage("res/right2.png"));
			images.put("BLUE_up", loadImage("res/blue_up.png"));
			images.put("BLUE_down", loadImage("res/blue_down.png"));
			images.put("BLUE_left", loadImage("res/blue_left.png"));
			images.put("BLUE_right", loadImage("res/blue_right.png"));
			images.put("PINK_up", loadImage("res/pink_up.png"));
			images.put("PINK_down", loadImage("res/pink_down.png"));
			images.put("PINK_left", loadImage("res/pink_left.png"));
			images.put("PINK_right", loadImage("res/pink_right.png"));
			images.put("RED_up", loadImage("res/red_up.png"));
			images.put("RED_down", loadImage("res/red_down.png"));
			images.put("RED_left", loadImage("res/red_left.png"));
			images.put("RED_right", loadImage("res/red_right.png"));
			images.put("GRAY_up", loadImage("res/gray_up.png"));
			images.put("GRAY_down", loadImage("res/gray_down.png"));
			images.put("GRAY_left", loadImage("res/gray_left.png"));
			images.put("GRAY_right", loadImage("res/gray_right.png"));
			images.put("GREEN_grass", loadImage("res/grass.png"));
			images.put("star1", loadImage("res/star1.png"));
			images.put("star2", loadImage("res/star2.png"));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Image loadImage(String path) throws SlickException {
		return new Image(path, false, Image.FILTER_NEAREST);
	}
	
	public static Image getImage(String name) {
		return images.get(name);
	}
	
	public static Image getImage(String tankColor, String name) {
		return images.get(tankColor+"_"+name);
	}
}
