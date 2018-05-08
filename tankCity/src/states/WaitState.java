package states;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import tanks.Constants;
import tanks.Engine;
import tanks.Resources;
import udpModule.UDPclient;

public class WaitState extends BasicGameState{
	 private UnicodeFont font;
	 private float textWidth;
	 private String text = "";
	 private UDPclient udpclient;
	 private int timer;
	 private Image img;
	private float textHeight;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		font = getNewFont("Arial", 35);
		img = Resources.getImage("waitstate");
        udpclient = Engine.udpclient;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		img.draw(0,0);
		g.setColor(Color.black);
		font.loadGlyphs();
		if(timer>0 && timer <=500) {
			text = "Waiting for other players";
		} else if (timer>501 && timer<=1000) {
			text = "Waiting for other players.";
		} else if (timer>1001 && timer<=1500) {
			text = "Waiting for other players..";
		} else if (timer>1500 && timer<=2000) {
			text = "Waiting for other players...";
		} else if (timer > 2000) {
			timer = 0;
		}
        textWidth = font.getWidth(text);
        textHeight = font.getHeight(text);
        g.setFont(font);
		g.drawString(text, Constants.WIDTH/2f - textWidth/2f, Constants.TOTAL_HEIGHT/2f - textHeight/2f);

	}
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.WAIT;
	}
	public UnicodeFont getNewFont(String fontName, int fontSize) {
		font = new UnicodeFont(new Font(fontName, Font.BOLD, fontSize));
		font.addGlyphs("@");
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		return (font);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int delta) throws SlickException {
		// TODO Auto-generated method stub
		timer+=delta;
	}

}
