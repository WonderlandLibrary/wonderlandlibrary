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
import me.kiras.aimwhere.libraries.slick.geom.Path;
import me.kiras.aimwhere.libraries.slick.geom.Polygon;
import me.kiras.aimwhere.libraries.slick.opengl.renderer.Renderer;

public class LineRenderTest
extends BasicGame {
    private Polygon polygon = new Polygon();
    private Path path = new Path(100.0f, 100.0f);
    private float width = 10.0f;
    private boolean antialias = true;

    public LineRenderTest() {
        super("LineRenderTest");
    }

    @Override
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

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.antialias = !this.antialias;
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setAntiAlias(this.antialias);
        g.setLineWidth(50.0f);
        g.setColor(Color.red);
        g.draw(this.path);
    }

    public static void main(String[] argv) {
        try {
            Renderer.setLineStripRenderer(4);
            Renderer.getLineStripRenderer().setLineCaps(true);
            AppGameContainer container = new AppGameContainer(new LineRenderTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

