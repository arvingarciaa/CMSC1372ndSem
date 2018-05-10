package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import entities.Player;
import tanks.Constants;
import tanks.Engine;
import tanks.Resources;

import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MenuState extends BasicGameState{
	private Image img;
	private UnicodeFont font;
	private TextField textFieldServer, textFieldName, textFieldPort;
	private float textWidth;
	private String text;

	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		img = Resources.getImage("menustate");
		textFieldServer = new TextField(gc, gc.getDefaultFont(), 250, 240, 200, 32);
		textFieldServer.setText("localhost");
	    textFieldName = new TextField(gc, gc.getDefaultFont(), 250, 290, 200, 32);
	    textFieldName.setText("Carl");
	    textFieldPort = new TextField(gc, gc.getDefaultFont(), 250, 340, 200, 32);
	    textFieldPort.setText("9999");
	   
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		img.draw(0,0);
		
		Font font = new Font("Arial", Font.PLAIN, 18);
		TrueTypeFont ttf = new TrueTypeFont(font, true);	

		ttf.drawString(190, 245, "Server:", Color.black);
		ttf.drawString(195, 295, "Name:", Color.black);
		ttf.drawString(210, 343, "Port:", Color.black);		
		
		
		g.setColor(Color.white);
		textFieldServer.render(gc, g);
		textFieldName.render(gc, g);	
		textFieldPort.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		int posX = Mouse.getX();
		int posY = Mouse.getY();
		if ((posX > 248 && posX < 385) && (posY > 182 && posY < 228)) { //for Start Game
			if (Mouse.isButtonDown(0)) {
				String clientName = textFieldName.getText();
				InetAddress clientAddress;
				try {
					clientAddress = InetAddress.getByName(textFieldServer.getText());
					int clientport = 9999;
//					Engine.createConnection(clientName, clientAddress, clientport); 
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				s.enterState(States.GAME);
			}
		} else if ((posX > 208 && posX < 431) && (posY > 125 && posY < 171)) {
			if (Mouse.isButtonDown(0)) {
				s.enterState(States.MANUAL);
			}
		} else if((posX > 260 && posX < 377) && (posY > 67 && posY < 115)) {
			if (Mouse.isButtonDown(0)) {
				System.exit(0);
			}
		}
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.MENU;
	}
	
	@SuppressWarnings("unchecked")
	public UnicodeFont getNewFont(String fontName, int fontSize) {
		font = new UnicodeFont(new Font(fontName, Font.PLAIN, fontSize));
		font.addGlyphs("@");
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		return (font);
	}

}
