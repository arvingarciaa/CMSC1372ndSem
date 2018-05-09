package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import tanks.Resources;

public class ManualState extends BasicGameState{
	private Image img;
		
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		img = Resources.getImage("instructions");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		img.draw(0,0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)){
            s.enterState(States.MENU);
        }
	}

	@Override
	public int getID() {
		return States.MANUAL;
	}
}
