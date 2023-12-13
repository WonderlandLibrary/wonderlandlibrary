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

public class AlphaMapTest
extends BasicGame {
    private Image alphaMap;
    private Image textureMap;

    public AlphaMapTest() {
        super("AlphaMap Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.alphaMap = new Image("testdata/alphamap.png");
        this.textureMap = new Image("testdata/grass.png");
        container.getGraphics().setBackground(Color.black);
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.clearAlphaMap();
        g2.setDrawMode(Graphics.MODE_NORMAL);
        this.textureMap.draw(10.0f, 50.0f);
        g2.setColor(Color.red);
        g2.fillRect(290.0f, 40.0f, 200.0f, 200.0f);
        g2.setColor(Color.white);
        g2.setDrawMode(Graphics.MODE_ALPHA_MAP);
        this.alphaMap.draw(300.0f, 50.0f);
        g2.setDrawMode(Graphics.MODE_ALPHA_BLEND);
        this.textureMap.draw(300.0f, 50.0f);
        g2.setDrawMode(Graphics.MODE_NORMAL);
    }

    public void keyPressed(int key, char c2) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new AlphaMapTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

