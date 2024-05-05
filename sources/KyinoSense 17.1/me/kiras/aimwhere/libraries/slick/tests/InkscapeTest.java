/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.Renderer;
import me.kiras.aimwhere.libraries.slick.svg.InkscapeLoader;
import me.kiras.aimwhere.libraries.slick.svg.SimpleDiagramRenderer;

public class InkscapeTest
extends BasicGame {
    private SimpleDiagramRenderer[] renderer = new SimpleDiagramRenderer[5];
    private float zoom = 1.0f;
    private float x;
    private float y;

    public InkscapeTest() {
        super("Inkscape Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.getGraphics().setBackground(Color.white);
        InkscapeLoader.RADIAL_TRIANGULATION_LEVEL = 2;
        this.renderer[3] = new SimpleDiagramRenderer(InkscapeLoader.load("testdata/svg/clonetest.svg"));
        container.getGraphics().setBackground(new Color(0.5f, 0.7f, 1.0f));
    }

    @Override
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

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.scale(this.zoom, this.zoom);
        g.translate(this.x, this.y);
        g.scale(0.3f, 0.3f);
        g.scale(3.3333333f, 3.3333333f);
        g.translate(400.0f, 0.0f);
        g.translate(100.0f, 300.0f);
        g.scale(0.7f, 0.7f);
        g.scale(1.4285715f, 1.4285715f);
        g.scale(0.5f, 0.5f);
        g.translate(-1100.0f, -380.0f);
        this.renderer[3].render(g);
        g.scale(2.0f, 2.0f);
        g.resetTransform();
    }

    public static void main(String[] argv) {
        try {
            Renderer.setRenderer(2);
            Renderer.setLineStripRenderer(4);
            AppGameContainer container = new AppGameContainer(new InkscapeTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

