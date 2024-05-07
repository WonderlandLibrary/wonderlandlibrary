package com.masterof13fps.utils.render.slick.tests;

import com.masterof13fps.utils.render.slick.tests.states.TestState1;
import com.masterof13fps.utils.render.slick.tests.states.TestState2;
import com.masterof13fps.utils.render.slick.tests.states.TestState3;
import com.masterof13fps.utils.render.slick.AppGameContainer;
import com.masterof13fps.utils.render.slick.GameContainer;
import com.masterof13fps.utils.render.slick.SlickException;
import com.masterof13fps.utils.render.slick.state.StateBasedGame;

/**
 * A test for the multi-state based functionality
 *
 * @author kevin
 */
public class StateBasedTest extends StateBasedGame {

	/**
	 * Create a new test
	 */
	public StateBasedTest() {
		super("State Based Test");
	}
	
	/**
	 * @see StateBasedGame#initStatesList(GameContainer)
	 */
	public void initStatesList(GameContainer container) {
		addState(new TestState1());
		addState(new TestState2());
		addState(new TestState3());
	}
	
	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments to pass into the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new StateBasedTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
