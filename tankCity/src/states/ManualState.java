package states;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ManualState extends BasicGameState{
	private UnicodeFont font;
		
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		// TODO Auto-generated method stub
		font = getNewFont("Arial", 16);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		g.drawString("GAME OBJECTIVE\n\n      The objective of the game is to destroy the 3 other "
				+ "\n\rtanks and be the last tank standing."
				+ " \n\nCONTROLS\n\n     "
				+ "Press W to go UP\n"
				+ "     Press S to go DOWN\n"
				+ "     Press A to go LEFT\n"
				+ "     Press D to go RIGHT\n"
				+ "     LEFT CLICK to SHOOT\n\n"
				+ "ENDGAME\n\n      Each round will end when there is only one tank \nremaining.", 50, 50);
		g.drawString("Press ENTER to go back to MENU", 170, 420);
		g.setFont(font);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		// TODO Auto-generated method stub
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)){
            s.enterState(States.MENU);
        }
		font.loadGlyphs();
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.MANUAL;
	}

	@SuppressWarnings("unchecked")
	public UnicodeFont getNewFont(String fontName, int fontSize) {
		font = new UnicodeFont(new Font(fontName, Font.PLAIN, fontSize));
		font.addGlyphs("@");
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		return (font);
	}
}
