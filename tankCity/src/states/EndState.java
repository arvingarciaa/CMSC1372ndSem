package states;

import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import entities.Player;
import tanks.Constants;
import tanks.Resources;

public class EndState extends BasicGameState{
	private Image img;
	private UnicodeFont font;
	private float textWidth;
	private String text;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		img = Resources.getImage("endstate");
		
		
	
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		img.draw(0,0);
		Font font = new Font("Arial", Font.PLAIN, 32);
		TrueTypeFont ttf = new TrueTypeFont(font, true);
		
		text = "Player 1: 15";
        textWidth = ttf.getWidth(text);
		ttf.drawString(Constants.WIDTH/2f - textWidth/2f, 280, text);

		text = "Player 2: 8";
		textWidth = ttf.getWidth(text);
		ttf.drawString(Constants.WIDTH/2f - textWidth/2f, 330, text);
		 
		text = "Player 3: 23";
		textWidth = ttf.getWidth(text);
		ttf.drawString(Constants.WIDTH/2f - textWidth/2f, 380, text);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		// TODO Auto-generated method stub
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)){
            s.enterState(States.MENU);
        }
		
		int posX = Mouse.getX();
		int posY = Mouse.getY();
		System.out.println("posX: " + posX + "posY: " + posY);
		if((posX > 241 && posX < 397) && (posY > 123 && posY < 179)) {
			if (Mouse.isButtonDown(0)) {
				System.exit(0);
			}
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.END;
	}
	
	public UnicodeFont getNewFont(String fontName, int fontSize) {
		font = new UnicodeFont(new Font(fontName, Font.BOLD, fontSize));
		font.addGlyphs("@");
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		return (font);
	}
}
