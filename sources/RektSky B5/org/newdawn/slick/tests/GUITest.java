/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.util.Log;

public class GUITest
extends BasicGame
implements ComponentListener {
    private Image image;
    private MouseOverArea[] areas = new MouseOverArea[4];
    private GameContainer container;
    private String message = "Demo Menu System with stock images";
    private TextField field;
    private TextField field2;
    private Image background;
    private Font font;
    private AppGameContainer app;

    public GUITest() {
        super("GUI Test");
    }

    public void init(GameContainer container) throws SlickException {
        if (container instanceof AppGameContainer) {
            this.app = (AppGameContainer)container;
            this.app.setIcon("testdata/icon.tga");
        }
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
        this.field = new TextField(container, this.font, 150, 20, 500, 35, new ComponentListener(){

            public void componentActivated(AbstractComponent source) {
                GUITest.this.message = "Entered1: " + GUITest.this.field.getText();
                GUITest.this.field2.setFocus(true);
            }
        });
        this.field2 = new TextField(container, this.font, 150, 70, 500, 35, new ComponentListener(){

            public void componentActivated(AbstractComponent source) {
                GUITest.this.message = "Entered2: " + GUITest.this.field2.getText();
                GUITest.this.field.setFocus(true);
            }
        });
        this.field2.setBorderColor(Color.red);
        this.container = container;
        this.image = new Image("testdata/logo.tga");
        this.background = new Image("testdata/dungeontiles.gif");
        container.setMouseCursor("testdata/cursor.tga", 0, 0);
        for (int i2 = 0; i2 < 4; ++i2) {
            this.areas[i2] = new MouseOverArea(container, this.image, 300, 100 + i2 * 100, 200, 90, this);
            this.areas[i2].setNormalColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
            this.areas[i2].setMouseOverColor(new Color(1.0f, 1.0f, 1.0f, 0.9f));
        }
    }

    public void render(GameContainer container, Graphics g2) {
        this.background.draw(0.0f, 0.0f, 800.0f, 500.0f);
        for (int i2 = 0; i2 < 4; ++i2) {
            this.areas[i2].render(container, g2);
        }
        this.field.render(container, g2);
        this.field2.render(container, g2);
        g2.setFont(this.font);
        g2.drawString(this.message, 200.0f, 550.0f);
    }

    public void update(GameContainer container, int delta) {
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 60) {
            this.app.setDefaultMouseCursor();
        }
        if (key == 59 && this.app != null) {
            try {
                this.app.setDisplayMode(640, 480, false);
            }
            catch (SlickException e2) {
                Log.error(e2);
            }
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new GUITest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void componentActivated(AbstractComponent source) {
        System.out.println("ACTIVL : " + source);
        for (int i2 = 0; i2 < 4; ++i2) {
            if (source != this.areas[i2]) continue;
            this.message = "Option " + (i2 + 1) + " pressed!";
        }
        if (source == this.field2) {
            // empty if block
        }
    }
}

