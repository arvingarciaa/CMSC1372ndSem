package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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
		font = getNewFont("Arial", 16);
		img = Resources.getImage("menustate");
		textFieldServer = new TextField(gc, gc.getDefaultFont(), 250, 230, 200, 25);
		textFieldServer.setText("localhost");
	    textFieldName = new TextField(gc, gc.getDefaultFont(), 250, 280, 200, 25);
	    textFieldName.setText("Carl");
	    textFieldPort = new TextField(gc, gc.getDefaultFont(), 250, 330, 200, 25);
	    textFieldPort.setText("9999");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		img.draw(0,0);
		g.setColor(Color.black);
		g.drawString("Server:", 180, 230);
		g.drawString("Name: ", 200, 280);
		g.drawString("Port: ", 200, 330);		
		
		g.setColor(Color.white);
		textFieldServer.render(gc, g);
		textFieldName.render(gc, g);	
		textFieldPort.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		int posX = Mouse.getX();
		int posY = Mouse.getY();
		if ((posX > 260 && posX < 412) && (posY > 209 && posY < 249)) { //for Start Game
			if (Mouse.isButtonDown(0)) {
				String clientName = textFieldName.getText();
				InetAddress clientAddress;
				try {
					clientAddress = InetAddress.getByName(textFieldServer.getText());
					int clientport = 9999;
					Engine.createConnection(clientName, clientAddress, clientport); 
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				s.enterState(States.WAIT);
			}
		} else if ((posX > 257 && posX < 414) && (posY > 159 && posY < 199)) {
			if (Mouse.isButtonDown(0)) {
				s.enterState(States.MANUAL);
			}
		} else if((posX > 296 && posX < 373) && (posY > 109 && posY < 149)) {
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
