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

public class LameTest
extends BasicGame {
    private Polygon poly = new Polygon();
    private Image image;

    public LameTest() {
        super("Lame Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.poly.addPoint(100.0f, 100.0f);
        this.poly.addPoint(120.0f, 100.0f);
        this.poly.addPoint(120.0f, 120.0f);
        this.poly.addPoint(100.0f, 120.0f);
        this.image = new Image("testdata/rocks.png");
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(Color.white);
        g2.texture(this.poly, this.image);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new LameTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

