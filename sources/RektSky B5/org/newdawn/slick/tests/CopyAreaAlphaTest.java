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

public class CopyAreaAlphaTest
extends BasicGame {
    private Image textureMap;
    private Image copy;

    public CopyAreaAlphaTest() {
        super("CopyArea Alpha Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.textureMap = new Image("testdata/grass.png");
        container.getGraphics().setBackground(Color.black);
        this.copy = new Image(100, 100);
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.clearAlphaMap();
        g2.setDrawMode(Graphics.MODE_NORMAL);
        g2.setColor(Color.white);
        g2.fillOval(100.0f, 100.0f, 150.0f, 150.0f);
        this.textureMap.draw(10.0f, 50.0f);
        g2.copyArea(this.copy, 100, 100);
        g2.setColor(Color.red);
        g2.fillRect(300.0f, 100.0f, 200.0f, 200.0f);
        this.copy.draw(350.0f, 150.0f);
    }

    public void keyPressed(int key, char c2) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new CopyAreaAlphaTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

