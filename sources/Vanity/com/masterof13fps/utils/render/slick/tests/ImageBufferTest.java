package com.masterof13fps.utils.render.slick.tests;

import com.masterof13fps.utils.render.slick.AppGameContainer;
import com.masterof13fps.utils.render.slick.BasicGame;
import com.masterof13fps.utils.render.slick.GameContainer;
import com.masterof13fps.utils.render.slick.Graphics;
import com.masterof13fps.utils.render.slick.Image;
import com.masterof13fps.utils.render.slick.ImageBuffer;
import com.masterof13fps.utils.render.slick.Input;
import com.masterof13fps.utils.render.slick.SlickException;

/**
 * A test for image buffer maniupulation rendering
 *
 * @author kevin
 */
public class ImageBufferTest extends BasicGame {
	/** The image we're currently displaying */
	private Image image;
	
	/**
	 * Create a new image buffer rendering test
	 */
	public ImageBufferTest() {
		super("Image Buffer Test");
	}
	
	/**
	 * @see BasicGame#init(GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		ImageBuffer buffer = new ImageBuffer(320,200);
		for (int x=0;x<320;x++) {
			for (int y=0;y<200;y++) {
				if (y == 20) {
					buffer.setRGBA(x, y, 255,255,255,255);
				} else {
					buffer.setRGBA(x, y, x,y,0,255);
				}
			}
		}
		image = buffer.getImage();
	}

	/**
	 * @see BasicGame#render(GameContainer, Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		image.draw(50,50);
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
		if (key == Input.KEY_ESCAPE) {
			System.exit(0);
		}
	}

	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments to pass into the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new ImageBufferTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
