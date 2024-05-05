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
import me.kiras.aimwhere.libraries.slick.geom.Polygon;

public class PolygonTest
extends BasicGame {
    private Polygon poly;
    private boolean in;
    private float y;

    public PolygonTest() {
        super("Polygon Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.poly = new Polygon();
        this.poly.addPoint(300.0f, 100.0f);
        this.poly.addPoint(320.0f, 200.0f);
        this.poly.addPoint(350.0f, 210.0f);
        this.poly.addPoint(280.0f, 250.0f);
        this.poly.addPoint(300.0f, 200.0f);
        this.poly.addPoint(240.0f, 150.0f);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        this.in = this.poly.contains(container.getInput().getMouseX(), container.getInput().getMouseY());
        this.poly.setCenterY(0.0f);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        if (this.in) {
            g.setColor(Color.red);
            g.fill(this.poly);
        }
        g.setColor(Color.yellow);
        g.fillOval(this.poly.getCenterX() - 3.0f, this.poly.getCenterY() - 3.0f, 6.0f, 6.0f);
        g.setColor(Color.white);
        g.draw(this.poly);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new PolygonTest(), 640, 480, false);
            container.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

