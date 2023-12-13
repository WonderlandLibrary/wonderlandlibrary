/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class GradientImageTest
extends BasicGame {
    private Image image1;
    private Image image2;
    private GradientFill fill;
    private Shape shape;
    private Polygon poly;
    private GameContainer container;
    private float ang;
    private boolean rotating = false;

    public GradientImageTest() {
        super("Gradient Image Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.image1 = new Image("testdata/grass.png");
        this.image2 = new Image("testdata/rocks.png");
        this.fill = new GradientFill(-64.0f, 0.0f, new Color(1.0f, 1.0f, 1.0f, 1.0f), 64.0f, 0.0f, new Color(0, 0, 0, 0));
        this.shape = new Rectangle(336.0f, 236.0f, 128.0f, 128.0f);
        this.poly = new Polygon();
        this.poly.addPoint(320.0f, 220.0f);
        this.poly.addPoint(350.0f, 200.0f);
        this.poly.addPoint(450.0f, 200.0f);
        this.poly.addPoint(480.0f, 220.0f);
        this.poly.addPoint(420.0f, 400.0f);
        this.poly.addPoint(400.0f, 390.0f);
    }

    public void render(GameContainer container, Graphics g2) {
        g2.drawString("R - Toggle Rotationg", 10.0f, 50.0f);
        g2.drawImage(this.image1, 100.0f, 236.0f);
        g2.drawImage(this.image2, 600.0f, 236.0f);
        g2.translate(0.0f, -150.0f);
        g2.rotate(400.0f, 300.0f, this.ang);
        g2.texture(this.shape, this.image2);
        g2.texture(this.shape, this.image1, this.fill);
        g2.resetTransform();
        g2.translate(0.0f, 150.0f);
        g2.rotate(400.0f, 300.0f, this.ang);
        g2.texture(this.poly, this.image2);
        g2.texture((Shape)this.poly, this.image1, this.fill);
        g2.resetTransform();
    }

    public void update(GameContainer container, int delta) {
        if (this.rotating) {
            this.ang += (float)delta * 0.1f;
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new GradientImageTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void keyPressed(int key, char c2) {
        if (key == 19) {
            boolean bl = this.rotating = !this.rotating;
        }
        if (key == 1) {
            this.container.exit();
        }
    }
}

