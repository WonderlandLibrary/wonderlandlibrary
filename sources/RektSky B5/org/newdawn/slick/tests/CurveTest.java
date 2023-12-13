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
import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

public class CurveTest
extends BasicGame {
    private Curve curve;
    private Vector2f p1 = new Vector2f(100.0f, 300.0f);
    private Vector2f c1 = new Vector2f(100.0f, 100.0f);
    private Vector2f c2 = new Vector2f(300.0f, 100.0f);
    private Vector2f p2 = new Vector2f(300.0f, 300.0f);
    private Polygon poly;

    public CurveTest() {
        super("Curve Test");
    }

    public void init(GameContainer container) throws SlickException {
        container.getGraphics().setBackground(Color.white);
        this.curve = new Curve(this.p2, this.c2, this.c1, this.p1);
        this.poly = new Polygon();
        this.poly.addPoint(500.0f, 200.0f);
        this.poly.addPoint(600.0f, 200.0f);
        this.poly.addPoint(700.0f, 300.0f);
        this.poly.addPoint(400.0f, 300.0f);
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    private void drawMarker(Graphics g2, Vector2f p2) {
        g2.drawRect(p2.x - 5.0f, p2.y - 5.0f, 10.0f, 10.0f);
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(Color.gray);
        this.drawMarker(g2, this.p1);
        this.drawMarker(g2, this.p2);
        g2.setColor(Color.red);
        this.drawMarker(g2, this.c1);
        this.drawMarker(g2, this.c2);
        g2.setColor(Color.black);
        g2.draw(this.curve);
        g2.fill(this.curve);
        g2.draw(this.poly);
        g2.fill(this.poly);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new CurveTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

