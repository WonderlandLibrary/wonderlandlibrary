/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class FlashTest
extends BasicGame {
    private Image image;
    private boolean flash;
    private GameContainer container;

    public FlashTest() {
        super("Flash Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.image = new Image("testdata/logo.tga");
    }

    public void render(GameContainer container, Graphics g2) {
        g2.drawString("Press space to toggle", 10.0f, 50.0f);
        if (this.flash) {
            this.image.draw(100.0f, 100.0f);
        } else {
            this.image.drawFlash(100.0f, 100.0f, this.image.getWidth(), this.image.getHeight(), new Color(1.0f, 0.0f, 1.0f, 1.0f));
        }
    }

    public void update(GameContainer container, int delta) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new FlashTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void keyPressed(int key, char c2) {
        if (key == 57) {
            boolean bl = this.flash = !this.flash;
        }
        if (key == 1) {
            this.container.exit();
        }
    }
}

