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
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.opengl.renderer.Renderer;

public class GradientTest
extends BasicGame {
    private GameContainer container;
    private GradientFill gradient;
    private GradientFill gradient2;
    private GradientFill gradient4;
    private Rectangle rect;
    private Rectangle center;
    private RoundedRectangle round;
    private RoundedRectangle round2;
    private Polygon poly;
    private float ang;

    public GradientTest() {
        super("Gradient Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.rect = new Rectangle(400.0f, 100.0f, 200.0f, 150.0f);
        this.round = new RoundedRectangle(150.0f, 100.0f, 200.0f, 150.0f, 50.0f);
        this.round2 = new RoundedRectangle(150.0f, 300.0f, 200.0f, 150.0f, 50.0f);
        this.center = new Rectangle(350.0f, 250.0f, 100.0f, 100.0f);
        this.poly = new Polygon();
        this.poly.addPoint(400.0f, 350.0f);
        this.poly.addPoint(550.0f, 320.0f);
        this.poly.addPoint(600.0f, 380.0f);
        this.poly.addPoint(620.0f, 450.0f);
        this.poly.addPoint(500.0f, 450.0f);
        this.gradient = new GradientFill(0.0f, -75.0f, Color.red, 0.0f, 75.0f, Color.yellow, true);
        this.gradient2 = new GradientFill(0.0f, -75.0f, Color.blue, 0.0f, 75.0f, Color.white, true);
        this.gradient4 = new GradientFill(-50.0f, -40.0f, Color.green, 50.0f, 40.0f, Color.cyan, true);
    }

    public void render(GameContainer container, Graphics g2) {
        g2.rotate(400.0f, 300.0f, this.ang);
        g2.fill(this.rect, this.gradient);
        g2.fill(this.round, this.gradient);
        g2.fill(this.poly, this.gradient2);
        g2.fill(this.center, this.gradient4);
        g2.setAntiAlias(true);
        g2.setLineWidth(10.0f);
        g2.draw(this.round2, this.gradient2);
        g2.setLineWidth(2.0f);
        g2.draw(this.poly, this.gradient);
        g2.setAntiAlias(false);
        g2.fill(this.center, this.gradient4);
        g2.setAntiAlias(true);
        g2.setColor(Color.black);
        g2.draw(this.center);
        g2.setAntiAlias(false);
    }

    public void update(GameContainer container, int delta) {
        this.ang += (float)delta * 0.01f;
    }

    public static void main(String[] argv) {
        try {
            Renderer.setRenderer(2);
            AppGameContainer container = new AppGameContainer(new GradientTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            this.container.exit();
        }
    }
}

