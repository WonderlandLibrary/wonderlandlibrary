/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ClipTest
extends BasicGame {
    private float ang = 0.0f;
    private boolean world;
    private boolean clip;

    public ClipTest() {
        super("Clip Test");
    }

    public void init(GameContainer container) throws SlickException {
    }

    public void update(GameContainer container, int delta) throws SlickException {
        this.ang += (float)delta * 0.01f;
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(Color.white);
        g2.drawString("1 - No Clipping", 100.0f, 10.0f);
        g2.drawString("2 - Screen Clipping", 100.0f, 30.0f);
        g2.drawString("3 - World Clipping", 100.0f, 50.0f);
        if (this.world) {
            g2.drawString("WORLD CLIPPING ENABLED", 200.0f, 80.0f);
        }
        if (this.clip) {
            g2.drawString("SCREEN CLIPPING ENABLED", 200.0f, 80.0f);
        }
        g2.rotate(400.0f, 400.0f, this.ang);
        if (this.world) {
            g2.setWorldClip(350.0f, 302.0f, 100.0f, 196.0f);
        }
        if (this.clip) {
            g2.setClip(350, 302, 100, 196);
        }
        g2.setColor(Color.red);
        g2.fillOval(300.0f, 300.0f, 200.0f, 200.0f);
        g2.setColor(Color.blue);
        g2.fillRect(390.0f, 200.0f, 20.0f, 400.0f);
        g2.clearClip();
        g2.clearWorldClip();
    }

    public void keyPressed(int key, char c2) {
        if (key == 2) {
            this.world = false;
            this.clip = false;
        }
        if (key == 3) {
            this.world = false;
            this.clip = true;
        }
        if (key == 4) {
            this.world = true;
            this.clip = false;
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new ClipTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

