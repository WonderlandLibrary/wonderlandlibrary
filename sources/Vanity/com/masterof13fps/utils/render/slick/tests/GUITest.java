package com.masterof13fps.utils.render.slick.tests;

import com.masterof13fps.utils.render.slick.AngelCodeFont;
import com.masterof13fps.utils.render.slick.AppGameContainer;
import com.masterof13fps.utils.render.slick.BasicGame;
import com.masterof13fps.utils.render.slick.Color;
import com.masterof13fps.utils.render.slick.Font;
import com.masterof13fps.utils.render.slick.GameContainer;
import com.masterof13fps.utils.render.slick.Graphics;
import com.masterof13fps.utils.render.slick.Image;
import com.masterof13fps.utils.render.slick.Input;
import com.masterof13fps.utils.render.slick.SlickException;
import com.masterof13fps.utils.render.slick.gui.AbstractComponent;
import com.masterof13fps.utils.render.slick.gui.ComponentListener;
import com.masterof13fps.utils.render.slick.gui.MouseOverArea;
import com.masterof13fps.utils.render.slick.gui.TextField;
import com.masterof13fps.utils.render.slick.util.Log;

/**
 * A test for the GUI components available in Slick. Very simple stuff
 *
 * @author kevin
 */
public class GUITest extends BasicGame implements ComponentListener {
	/** The image being rendered */
	private Image image;
	/** The areas defined */
	private MouseOverArea[] areas = new MouseOverArea[4];
	/** The game container */
	private GameContainer container;
	/** The message to display */
	private String message = "Demo Menu System with stock images";
	/** The text field */
	private TextField field;
	/** The text field */
	private TextField field2;
	/** The background image */
	private Image background;
	/** The font used to render */
	private Font font;
	/** The container */
	private AppGameContainer app;
	
	/**
	 * Create a new test of GUI  rendering
	 */
	public GUITest() {
		super("GUI Test");
	}
	
	/**
	 * @see BasicGame#init(GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		if (container instanceof AppGameContainer) {
			app = (AppGameContainer) container;
			app.setIcon("testdata/icon.tga");
		}
		
		font = new AngelCodeFont("testdata/demo2.fnt","testdata/demo2_00.tga");
		field = new TextField(container, font, 150,20,500,35, new ComponentListener() {
			public void componentActivated(AbstractComponent source) {
				message = "Entered1: "+field.getText();
				field2.setFocus(true);
			}
		});
		field2 = new TextField(container, font, 150,70,500,35,new ComponentListener() {
			public void componentActivated(AbstractComponent source) {
				message = "Entered2: "+field2.getText();
				field.setFocus(true);
			}
		});
		field2.setBorderColor(Color.red);
		
		this.container = container;
		
		image = new Image("testdata/logo.tga");
		background = new Image("testdata/dungeontiles.gif");
		container.setMouseCursor("testdata/cursor.tga", 0, 0);
		
		for (int i=0;i<4;i++) {
			areas[i] = new MouseOverArea(container, image, 300, 100 + (i*100), 200, 90, this);
			areas[i].setNormalColor(new Color(1,1,1,0.8f));
			areas[i].setMouseOverColor(new Color(1,1,1,0.9f));
		}
	}

	/**
	 * @see BasicGame#render(GameContainer, Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		background.draw(0, 0, 800, 500);
		
		for (int i=0;i<4;i++) {
			areas[i].render(container, g);
		}
		field.render(container, g);
		field2.render(container, g);
		
		g.setFont(font);
		g.drawString(message, 200, 550);
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
		if (key == Input.KEY_F2) {
			app.setDefaultMouseCursor();
		}
		if (key == Input.KEY_F1) {
			if (app != null) {
				try {
					app.setDisplayMode(640,480,false);		
				} catch (SlickException e) {
					Log.error(e);
				}
			}
		}
	}
	
	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments passed to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new GUITest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see ComponentListener#componentActivated(AbstractComponent)
	 */
	public void componentActivated(AbstractComponent source) {
		System.out.println("ACTIVL : "+source);
		for (int i=0;i<4;i++) {
			if (source == areas[i]) {
				message = "Option "+(i+1)+" pressed!";
			}
		}
		if (source == field2) {
		}
	}
}
