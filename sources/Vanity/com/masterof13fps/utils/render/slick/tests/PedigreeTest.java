package com.masterof13fps.utils.render.slick.tests;

import java.io.IOException;

import com.masterof13fps.utils.render.slick.AppGameContainer;
import com.masterof13fps.utils.render.slick.BasicGame;
import com.masterof13fps.utils.render.slick.GameContainer;
import com.masterof13fps.utils.render.slick.Graphics;
import com.masterof13fps.utils.render.slick.Image;
import com.masterof13fps.utils.render.slick.Input;
import com.masterof13fps.utils.render.slick.SlickException;
import com.masterof13fps.utils.render.slick.particles.ConfigurableEmitter;
import com.masterof13fps.utils.render.slick.particles.ParticleIO;
import com.masterof13fps.utils.render.slick.particles.ParticleSystem;

/**
 * A test for loading editing particle systems
 *
 * @author kevin
 */
public class PedigreeTest extends BasicGame {
	/** The image we're currently displaying */
	private Image image;
	/** The game container */
	private GameContainer container;
	/** The smoke trail particle system */
	private ParticleSystem trail;
	/** The fire particle system */
	private ParticleSystem fire;
	/** The rocket x position */
	private float rx;
	/** The rocket y position */
	private float ry = 900;
	
	/**
	 * Create a new image rendering test
	 */
	public PedigreeTest() {
		super("Pedigree Test");
	}
	
	/**
	 * @see BasicGame#init(GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		
		try {
			fire = ParticleIO.loadConfiguredSystem("testdata/system.xml");
			trail = ParticleIO.loadConfiguredSystem("testdata/smoketrail.xml");
			
		} catch (IOException e) {
			throw new SlickException("Failed to load particle systems", e);
		}
		image = new Image("testdata/rocket.png");
	
		spawnRocket();
	}

	/**
	 * Spawn a test rocket 
	 */
	private void spawnRocket() {
		ry = 700;
		rx = (float) ((Math.random()*600) + 100);
	}

	/**
	 * @see BasicGame#render(GameContainer, Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		((ConfigurableEmitter) trail.getEmitter(0)).setPosition(rx+14,ry+35);
		trail.render();
		image.draw((int) rx,(int) ry);
		
		g.translate(400, 300);
		fire.render();
	}

	/**
	 * @see BasicGame#update(GameContainer, int)
	 */
	public void update(GameContainer container, int delta) {
		fire.update(delta);
		trail.update(delta);
		
		ry -= delta * 0.25f;
		if (ry < -100) {
			spawnRocket();
		}
	}

	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		
		for (int i=0;i<fire.getEmitterCount();i++) {
			((ConfigurableEmitter) fire.getEmitter(i)).setPosition(x - 400, y - 300, true);
		}
	}

	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments to pass into the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new PedigreeTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			container.exit();
		}
	}
}
