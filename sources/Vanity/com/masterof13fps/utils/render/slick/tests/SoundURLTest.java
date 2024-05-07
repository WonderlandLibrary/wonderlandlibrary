package com.masterof13fps.utils.render.slick.tests;

import com.masterof13fps.utils.render.slick.AppGameContainer;
import com.masterof13fps.utils.render.slick.BasicGame;
import com.masterof13fps.utils.render.slick.Color;
import com.masterof13fps.utils.render.slick.GameContainer;
import com.masterof13fps.utils.render.slick.Graphics;
import com.masterof13fps.utils.render.slick.Input;
import com.masterof13fps.utils.render.slick.Music;
import com.masterof13fps.utils.render.slick.SlickException;
import com.masterof13fps.utils.render.slick.Sound;
import com.masterof13fps.utils.render.slick.util.ResourceLoader;

/**
 * A test for the sound system of the library
 * 
 * @author kevin
 * @author aaron
 */
public class SoundURLTest extends BasicGame {
	/** The sound to be played */
	private Sound sound;
	/** The sound to be played */
	private Sound charlie;
	/** The sound to be played */
	private Sound burp;
	/** The music to be played */
	private Music music;
	/** The music to be played */
	private Music musica;
	/** The music to be played */
	private Music musicb;
	/** The sound to be played */
	private Sound engine;
	/** The Volume of the playing music */
	private int volume = 1;
	
	/**
	 * Create a new test for sounds
	 */
	public SoundURLTest() {
		super("Sound URL Test");
	}
	
	/**
	 * @see BasicGame#init(GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		sound = new Sound(ResourceLoader.getResource("testdata/restart.ogg"));
		charlie = new Sound(ResourceLoader.getResource("testdata/cbrown01.wav"));
		engine = new Sound(ResourceLoader.getResource("testdata/engine.wav"));
		//music = musica = new Music("testdata/SMB-X.XM");
		music = musica = new Music(ResourceLoader.getResource("testdata/restart.ogg"), false);
		musicb = new Music(ResourceLoader.getResource("testdata/kirby.ogg"), false);
		burp = new Sound(ResourceLoader.getResource("testdata/burp.aif"));
	}

	/**
	 * @see BasicGame#render(GameContainer, Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		g.setColor(Color.white);
		g.drawString("The OGG loop is now streaming from the file, woot.",100,60);
		g.drawString("Press space for sound effect (OGG)",100,100);
		g.drawString("Press P to pause/resume music (XM)",100,130);
		g.drawString("Press E to pause/resume engine sound (WAV)",100,190);
		g.drawString("Press enter for charlie (WAV)",100,160);
		g.drawString("Press C to change music",100,210);
		g.drawString("Press B to burp (AIF)",100,240);
		g.drawString("Press + or - to change volume of music", 100, 270);
		g.setColor(Color.blue);
		g.drawString("Music Volume Level: " + volume / 10.0f, 150, 300);
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
		if (key == Input.KEY_SPACE) {
			sound.play();
		}
		if (key == Input.KEY_B) {
			burp.play();
		}
		if (key == Input.KEY_A) {
			sound.playAt(-1, 0, 0);
		}
		if (key == Input.KEY_L) {
			sound.playAt(1, 0, 0);
		}
		if (key == Input.KEY_RETURN) {
			charlie.play(1.0f,1.0f);
		}
		if (key == Input.KEY_P) {
			if (music.playing()) {
				music.pause();
			} else {
				music.resume();
			}
		}
		if (key == Input.KEY_C) {
			music.stop();
			if (music == musica) {
				music = musicb;
			} else {
				music = musica;
			}
			
			music.loop();
		}
		if (key == Input.KEY_E) {
			if (engine.playing()) {
				engine.stop();
			} else {
				engine.loop();
			}
		}
		
		if (c == '+') {
			volume += 1;
			setVolume();
		}
		
		if (c == '-') {
			volume -= 1;
			setVolume();
		}

	}
	
	/**
	 *  Convenience routine to set volume of current music 
	 */
	private void setVolume() {
		// Do bounds checking
		if(volume > 10) {
			volume = 10;
		} else if(volume < 0) {
			volume = 0;
		}
		
		music.setVolume(volume / 10.0f);
	}
	
	/**
	 * Entry point to the sound test
	 * 
	 * @param argv The arguments provided to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new SoundURLTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
