package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import java.awt.Font;

public class MenuState extends BasicGameState{
	private Image img;
	private TextField textServer;
	private TextField textName;
	private UnicodeFont font;

	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		font = getNewFont("Arial", 16);
		img = new Image("res/logo.png");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		g.drawString("Server:", 180, 230);
		g.drawString("Name: ", 200, 280);
		g.drawString("Start Game", 270, 340);
		g.drawString("Instructions", 260, 375);
		g.drawString("Exit", 295, 410);
		//textServer.render(gc, g);
		//textName.render(gc, g);
		//g.setFont(font);
		img.draw(100, 20, 460, 200);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		int posX = Mouse.getX();
		int posY = Mouse.getY();
		
		if ((posX > 260 && posX < 370) && (posY > 117 && posY < 140)) { //for Start Game
			if (Mouse.isButtonDown(0)) {
				s.enterState(States.GAME);
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
