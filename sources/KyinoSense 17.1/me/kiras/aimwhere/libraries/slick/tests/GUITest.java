/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AngelCodeFont;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.Font;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.gui.AbstractComponent;
import me.kiras.aimwhere.libraries.slick.gui.ComponentListener;
import me.kiras.aimwhere.libraries.slick.gui.MouseOverArea;
import me.kiras.aimwhere.libraries.slick.gui.TextField;
import me.kiras.aimwhere.libraries.slick.util.Log;
import net.minecraftforge.fml.common.FMLCommonHandler;

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

    @Override
    public void init(GameContainer container) throws SlickException {
        if (container instanceof AppGameContainer) {
            this.app = (AppGameContainer)container;
            this.app.setIcon("testdata/icon.tga");
        }
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
        this.field = new TextField(container, this.font, 150, 20, 500, 35, new ComponentListener(){

            @Override
            public void componentActivated(AbstractComponent source) {
                GUITest.this.message = "Entered1: " + GUITest.this.field.getText();
                GUITest.this.field2.setFocus(true);
            }
        });
        this.field2 = new TextField(container, this.font, 150, 70, 500, 35, new ComponentListener(){

            @Override
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
        for (int i = 0; i < 4; ++i) {
            this.areas[i] = new MouseOverArea(container, this.image, 300, 100 + i * 100, 200, 90, this);
            this.areas[i].setNormalColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
            this.areas[i].setMouseOverColor(new Color(1.0f, 1.0f, 1.0f, 0.9f));
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        this.background.draw(0.0f, 0.0f, 800.0f, 500.0f);
        for (int i = 0; i < 4; ++i) {
            this.areas[i].render(container, g);
        }
        this.field.render(container, g);
        this.field2.render(container, g);
        g.setFont(this.font);
        g.drawString(this.message, 200.0f, 550.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
        }
        if (key == 60) {
            this.app.setDefaultMouseCursor();
        }
        if (key == 59 && this.app != null) {
            try {
                this.app.setDisplayMode(640, 480, false);
            }
            catch (SlickException e) {
                Log.error(e);
            }
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new GUITest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void componentActivated(AbstractComponent source) {
        System.out.println("ACTIVL : " + source);
        for (int i = 0; i < 4; ++i) {
            if (source != this.areas[i]) continue;
            this.message = "Option " + (i + 1) + " pressed!";
        }
        if (source == this.field2) {
            // empty if block
        }
    }
}

