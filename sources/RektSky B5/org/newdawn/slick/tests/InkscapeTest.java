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
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.svg.InkscapeLoader;
import org.newdawn.slick.svg.SimpleDiagramRenderer;

public class InkscapeTest
extends BasicGame {
    private SimpleDiagramRenderer[] renderer = new SimpleDiagramRenderer[5];
    private float zoom = 1.0f;
    private float x;
    private float y;

    public InkscapeTest() {
        super("Inkscape Test");
    }

    public void init(GameContainer container) throws SlickException {
        container.getGraphics().setBackground(Color.white);
        InkscapeLoader.RADIAL_TRIANGULATION_LEVEL = 2;
        this.renderer[3] = new SimpleDiagramRenderer(InkscapeLoader.load("testdata/svg/clonetest.svg"));
        container.getGraphics().setBackground(new Color(0.5f, 0.7f, 1.0f));
    }

    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyDown(16)) {
            this.zoom += (float)delta * 0.01f;
            if (this.zoom > 10.0f) {
                this.zoom = 10.0f;
            }
        }
        if (container.getInput().isKeyDown(30)) {
            this.zoom -= (float)delta * 0.01f;
            if (this.zoom < 0.1f) {
                this.zoom = 0.1f;
            }
        }
        if (container.getInput().isKeyDown(205)) {
            this.x += (float)delta * 0.1f;
        }
        if (container.getInput().isKeyDown(203)) {
            this.x -= (float)delta * 0.1f;
        }
        if (container.getInput().isKeyDown(208)) {
            this.y += (float)delta * 0.1f;
        }
        if (container.getInput().isKeyDown(200)) {
            this.y -= (float)delta * 0.1f;
        }
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.scale(this.zoom, this.zoom);
        g2.translate(this.x, this.y);
        g2.scale(0.3f, 0.3f);
        g2.scale(3.3333333f, 3.3333333f);
        g2.translate(400.0f, 0.0f);
        g2.translate(100.0f, 300.0f);
        g2.scale(0.7f, 0.7f);
        g2.scale(1.4285715f, 1.4285715f);
        g2.scale(0.5f, 0.5f);
        g2.translate(-1100.0f, -380.0f);
        this.renderer[3].render(g2);
        g2.scale(2.0f, 2.0f);
        g2.resetTransform();
    }

    public static void main(String[] argv) {
        try {
            Renderer.setRenderer(2);
            Renderer.setLineStripRenderer(4);
            AppGameContainer container = new AppGameContainer(new InkscapeTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

