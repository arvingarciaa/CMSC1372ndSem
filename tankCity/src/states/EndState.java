package states;


import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import tanks.Constants;
import tanks.Engine;
import udpModule.UDPclient;

public class EndState extends BasicGameState{
	 private UnicodeFont font;
	 private float textWidth;
	 private String text,scores;
	 private UDPclient udpclient;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		font = getNewFont("Arial", 48);
        udpclient = Engine.udpclient;
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub
		font.loadGlyphs();
		text = "GAME OVER";
		scores = "";
        textWidth = font.getWidth(text);
		font.drawString(Constants.WIDTH/2f - textWidth/2f, 100, text);

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

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}

}
