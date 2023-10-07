/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.CachedRender;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class CachedRenderTest
extends BasicGame {
    private Runnable operations;
    private CachedRender cached;
    private boolean drawCached;

    public CachedRenderTest() {
        super("Cached Render Test");
    }

    @Override
    public void init(final GameContainer container) throws SlickException {
        this.operations = new Runnable(){

            @Override
            public void run() {
                for (int i = 0; i < 100; ++i) {
                    int c = i + 100;
                    container.getGraphics().setColor(new Color(c, c, c, c));
                    container.getGraphics().drawOval(i * 5 + 50, i * 3 + 50, 100.0f, 100.0f);
                }
            }
        };
        this.cached = new CachedRender(this.operations);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.drawCached = !this.drawCached;
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.drawString("Press space to toggle caching", 10.0f, 130.0f);
        if (this.drawCached) {
            g.drawString("Drawing from cache", 10.0f, 100.0f);
            this.cached.render();
        } else {
            g.drawString("Drawing direct", 10.0f, 100.0f);
            this.operations.run();
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new CachedRenderTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

