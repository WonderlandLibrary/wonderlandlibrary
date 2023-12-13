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
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.opengl.renderer.Renderer;

public class LineRenderTest
extends BasicGame {
    private Polygon polygon = new Polygon();
    private Path path = new Path(100.0f, 100.0f);
    private float width = 10.0f;
    private boolean antialias = true;

    public LineRenderTest() {
        super("LineRenderTest");
    }

    public void init(GameContainer container) throws SlickException {
        this.polygon.addPoint(100.0f, 100.0f);
        this.polygon.addPoint(200.0f, 80.0f);
        this.polygon.addPoint(320.0f, 150.0f);
        this.polygon.addPoint(230.0f, 210.0f);
        this.polygon.addPoint(170.0f, 260.0f);
        this.path.curveTo(200.0f, 200.0f, 200.0f, 100.0f, 100.0f, 200.0f);
        this.path.curveTo(400.0f, 100.0f, 400.0f, 200.0f, 200.0f, 100.0f);
        this.path.curveTo(500.0f, 500.0f, 400.0f, 200.0f, 200.0f, 100.0f);
    }

    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.antialias = !this.antialias;
        }
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setAntiAlias(this.antialias);
        g2.setLineWidth(50.0f);
        g2.setColor(Color.red);
        g2.draw(this.path);
    }

    public static void main(String[] argv) {
        try {
            Renderer.setLineStripRenderer(4);
            Renderer.getLineStripRenderer().setLineCaps(true);
            AppGameContainer container = new AppGameContainer(new LineRenderTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

