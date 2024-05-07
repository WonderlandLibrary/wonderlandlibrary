package com.masterof13fps.utils.render.slick.tests;

import com.masterof13fps.utils.render.slick.openal.SoundStore;
import com.masterof13fps.utils.render.slick.AppGameContainer;
import com.masterof13fps.utils.render.slick.BasicGame;
import com.masterof13fps.utils.render.slick.Color;
import com.masterof13fps.utils.render.slick.GameContainer;
import com.masterof13fps.utils.render.slick.Graphics;
import com.masterof13fps.utils.render.slick.Input;
import com.masterof13fps.utils.render.slick.Music;
import com.masterof13fps.utils.render.slick.SlickException;

/**
 * A test for the sound system (positioning) of the library
 * 
 * @author kevin
 */
public class SoundPositionTest extends BasicGame {
	/** the GameContainer instance for this game/testcase */
	private GameContainer myContainer;
	/** The music to be played */
	private Music music;
	
	/** The IDs of the sources used for each engine noise */
	private int[] engines = new int[3];
	
	/**
	 * Create a new test for sounds
	 */
	public SoundPositionTest() {
		super("Music Position Test");
	}
	
	/**
	 * @see BasicGame#init(GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		SoundStore.get().setMaxSources(32);
		
		myContainer = container;
		music = new Music("testdata/kirby.ogg", true);
		music.play();
	}

	/**
	 * @see BasicGame#render(GameContainer, Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		g.setColor(Color.white);
		g.drawString("Position: "+music.getPosition(), 100,100);
		g.drawString("Space - Pause/Resume", 100,130);
		g.drawString("Right Arrow - Advance 5 seconds", 100, 145);
	}

	/**
	 * @see BasicGame#update(GameContainer, int)
	 */
	public void update(GameContainer container, int delta) {
	}

	/**
	 * @see BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_SPACE) {
			if (music.playing()) {
				music.pause();
			} else {
				music.resume();
			}
		}
		if (key == Input.KEY_RIGHT) {
			music.setPosition(music.getPosition()+5);
		}
	}
	
	/**
	 * Entry point to the sound test
	 * 
	 * @param argv The arguments provided to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new SoundPositionTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
