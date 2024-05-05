/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.geom.Polygon;
import me.kiras.aimwhere.libraries.slick.geom.Rectangle;
import me.kiras.aimwhere.libraries.slick.geom.ShapeRenderer;
import me.kiras.aimwhere.libraries.slick.geom.TexCoordGenerator;
import me.kiras.aimwhere.libraries.slick.geom.Vector2f;

public class TexturePaintTest
extends BasicGame {
    private Polygon poly = new Polygon();
    private Image image;
    private Rectangle texRect = new Rectangle(50.0f, 50.0f, 100.0f, 100.0f);
    private TexCoordGenerator texPaint;

    public TexturePaintTest() {
        super("Texture Paint Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.poly.addPoint(120.0f, 120.0f);
        this.poly.addPoint(420.0f, 100.0f);
        this.poly.addPoint(620.0f, 420.0f);
        this.poly.addPoint(300.0f, 320.0f);
        this.image = new Image("testdata/rocks.png");
        this.texPaint = new TexCoordGenerator(){

            @Override
            public Vector2f getCoordFor(float x, float y) {
                float tx = (TexturePaintTest.this.texRect.getX() - x) / TexturePaintTest.this.texRect.getWidth();
                float ty = (TexturePaintTest.this.texRect.getY() - y) / TexturePaintTest.this.texRect.getHeight();
                return new Vector2f(tx, ty);
            }
        };
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.texture(this.poly, this.image);
        ShapeRenderer.texture(this.poly, this.image, this.texPaint);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new TexturePaintTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

