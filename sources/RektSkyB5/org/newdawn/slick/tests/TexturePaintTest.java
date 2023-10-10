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
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Vector2f;

public class TexturePaintTest
extends BasicGame {
    private Polygon poly = new Polygon();
    private Image image;
    private Rectangle texRect = new Rectangle(50.0f, 50.0f, 100.0f, 100.0f);
    private TexCoordGenerator texPaint;

    public TexturePaintTest() {
        super("Texture Paint Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.poly.addPoint(120.0f, 120.0f);
        this.poly.addPoint(420.0f, 100.0f);
        this.poly.addPoint(620.0f, 420.0f);
        this.poly.addPoint(300.0f, 320.0f);
        this.image = new Image("testdata/rocks.png");
        this.texPaint = new TexCoordGenerator(){

            public Vector2f getCoordFor(float x2, float y2) {
                float tx = (TexturePaintTest.this.texRect.getX() - x2) / TexturePaintTest.this.texRect.getWidth();
                float ty = (TexturePaintTest.this.texRect.getY() - y2) / TexturePaintTest.this.texRect.getHeight();
                return new Vector2f(tx, ty);
            }
        };
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(Color.white);
        g2.texture(this.poly, this.image);
        ShapeRenderer.texture(this.poly, this.image, this.texPaint);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new TexturePaintTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

