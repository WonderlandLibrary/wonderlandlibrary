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

public class TransparentColorTest
extends BasicGame {
    private Image image;
    private Image timage;

    public TransparentColorTest() {
        super("Transparent Color Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.image = new Image("testdata/transtest.png");
        this.timage = new Image("testdata/transtest.png", new Color(94, 66, 41, 255));
    }

    public void render(GameContainer container, Graphics g2) {
        g2.setBackground(Color.red);
        this.image.draw(10.0f, 10.0f);
        this.timage.draw(10.0f, 310.0f);
    }

    public void update(GameContainer container, int delta) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new TransparentColorTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void keyPressed(int key, char c2) {
    }
}

