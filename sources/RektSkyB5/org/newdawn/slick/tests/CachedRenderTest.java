/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CachedRender;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CachedRenderTest
extends BasicGame {
    private Runnable operations;
    private CachedRender cached;
    private boolean drawCached;

    public CachedRenderTest() {
        super("Cached Render Test");
    }

    public void init(final GameContainer container) throws SlickException {
        this.operations = new Runnable(){

            public void run() {
                for (int i2 = 0; i2 < 100; ++i2) {
                    int c2 = i2 + 100;
                    container.getGraphics().setColor(new Color(c2, c2, c2, c2));
                    container.getGraphics().drawOval(i2 * 5 + 50, i2 * 3 + 50, 100.0f, 100.0f);
                }
            }
        };
        this.cached = new CachedRender(this.operations);
    }

    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.drawCached = !this.drawCached;
        }
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(Color.white);
        g2.drawString("Press space to toggle caching", 10.0f, 130.0f);
        if (this.drawCached) {
            g2.drawString("Drawing from cache", 10.0f, 100.0f);
            this.cached.render();
        } else {
            g2.drawString("Drawing direct", 10.0f, 100.0f);
            this.operations.run();
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new CachedRenderTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

