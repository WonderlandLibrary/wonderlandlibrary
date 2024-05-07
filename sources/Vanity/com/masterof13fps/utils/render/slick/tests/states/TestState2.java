package com.masterof13fps.utils.render.slick.tests.states;

import com.masterof13fps.utils.render.slick.AngelCodeFont;
import com.masterof13fps.utils.render.slick.Color;
import com.masterof13fps.utils.render.slick.Font;
import com.masterof13fps.utils.render.slick.GameContainer;
import com.masterof13fps.utils.render.slick.Graphics;
import com.masterof13fps.utils.render.slick.Image;
import com.masterof13fps.utils.render.slick.Input;
import com.masterof13fps.utils.render.slick.SlickException;
import com.masterof13fps.utils.render.slick.state.BasicGameState;
import com.masterof13fps.utils.render.slick.state.StateBasedGame;
import com.masterof13fps.utils.render.slick.state.transition.FadeInTransition;
import com.masterof13fps.utils.render.slick.state.transition.FadeOutTransition;

/**
 * A simple test state to display an image and rotate it
 *
 * @author kevin
 */
public class TestState2 extends BasicGameState {
	/** The ID given to this state */
	public static final int ID = 2;
	/** The font to write the message with */
	private Font font;
	/** The image to be display */
	private Image image;
	/** The angle we'll rotate by */
	private float ang;
	/** The game holding this state */
	private StateBasedGame game;
	
	/**
	 * @see BasicGameState#getID()
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @see BasicGameState#init(GameContainer, StateBasedGame)
	 */
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
		font = new AngelCodeFont("testdata/demo2.fnt","testdata/demo2_00.tga");
		image = new Image("testdata/logo.tga");
	}

	/**
	 * @see BasicGameState#render(GameContainer, StateBasedGame, Graphics)
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setFont(font);
		g.setColor(Color.green);
		g.drawString("This is State 2", 200, 50);
		
		g.rotate(400,300,ang);
		g.drawImage(image,400-(image.getWidth()/2),300-(image.getHeight()/2));
	}

	/**
	 * @see BasicGameState#update(GameContainer, StateBasedGame, int)
	 */
	public void update(GameContainer container, StateBasedGame game, int delta) {
		ang += delta * 0.1f;
	}
	
	/**
	 * @see BasicGameState#keyReleased(int, char)
	 */
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_1) {
			game.enterState(TestState1.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
		if (key == Input.KEY_3) {
			game.enterState(TestState3.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
	}
}
