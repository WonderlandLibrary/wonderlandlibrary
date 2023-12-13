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
import org.newdawn.slick.geom.Polygon;

public class PolygonTest
extends BasicGame {
    private Polygon poly;
    private boolean in;
    private float y;

    public PolygonTest() {
        super("Polygon Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.poly = new Polygon();
        this.poly.addPoint(300.0f, 100.0f);
        this.poly.addPoint(320.0f, 200.0f);
        this.poly.addPoint(350.0f, 210.0f);
        this.poly.addPoint(280.0f, 250.0f);
        this.poly.addPoint(300.0f, 200.0f);
        this.poly.addPoint(240.0f, 150.0f);
    }

    public void update(GameContainer container, int delta) throws SlickException {
        this.in = this.poly.contains(container.getInput().getMouseX(), container.getInput().getMouseY());
        this.poly.setCenterY(0.0f);
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        if (this.in) {
            g2.setColor(Color.red);
            g2.fill(this.poly);
        }
        g2.setColor(Color.yellow);
        g2.fillOval(this.poly.getCenterX() - 3.0f, this.poly.getCenterY() - 3.0f, 6.0f, 6.0f);
        g2.setColor(Color.white);
        g2.draw(this.poly);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new PolygonTest(), 640, 480, false);
            container.start();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

