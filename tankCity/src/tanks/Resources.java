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
			images.put("up", loadImage("res/up2.png"));
			images.put("down", loadImage("res/down2.png"));
			images.put("left", loadImage("res/left2.png"));
			images.put("right", loadImage("res/right2.png"));
			images.put("grass", loadImage("res/grass.png"));
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
}
