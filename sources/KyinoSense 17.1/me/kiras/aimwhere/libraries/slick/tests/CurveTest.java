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
import me.kiras.aimwhere.libraries.slick.geom.Curve;
import me.kiras.aimwhere.libraries.slick.geom.Polygon;
import me.kiras.aimwhere.libraries.slick.geom.Vector2f;

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

    @Override
    public void init(GameContainer container) throws SlickException {
        container.getGraphics().setBackground(Color.white);
        this.curve = new Curve(this.p2, this.c2, this.c1, this.p1);
        this.poly = new Polygon();
        this.poly.addPoint(500.0f, 200.0f);
        this.poly.addPoint(600.0f, 200.0f);
        this.poly.addPoint(700.0f, 300.0f);
        this.poly.addPoint(400.0f, 300.0f);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    private void drawMarker(Graphics g, Vector2f p) {
        g.drawRect(p.x - 5.0f, p.y - 5.0f, 10.0f, 10.0f);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(Color.gray);
        this.drawMarker(g, this.p1);
        this.drawMarker(g, this.p2);
        g.setColor(Color.red);
        this.drawMarker(g, this.c1);
        this.drawMarker(g, this.c2);
        g.setColor(Color.black);
        g.draw(this.curve);
        g.fill(this.curve);
        g.draw(this.poly);
        g.fill(this.poly);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new CurveTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

