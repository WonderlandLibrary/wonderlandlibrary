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
import org.newdawn.slick.geom.MorphShape;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class MorphShapeTest
extends BasicGame {
    private Shape a;
    private Shape b;
    private Shape c;
    private MorphShape morph;
    private float time;

    public MorphShapeTest() {
        super("MorphShapeTest");
    }

    public void init(GameContainer container) throws SlickException {
        this.a = new Rectangle(100.0f, 100.0f, 50.0f, 200.0f);
        this.a = this.a.transform(Transform.createRotateTransform(0.1f, 100.0f, 100.0f));
        this.b = new Rectangle(200.0f, 100.0f, 50.0f, 200.0f);
        this.b = this.b.transform(Transform.createRotateTransform(-0.6f, 100.0f, 100.0f));
        this.c = new Rectangle(300.0f, 100.0f, 50.0f, 200.0f);
        this.c = this.c.transform(Transform.createRotateTransform(-0.2f, 100.0f, 100.0f));
        this.morph = new MorphShape(this.a);
        this.morph.addShape(this.b);
        this.morph.addShape(this.c);
        container.setVSync(true);
    }

    public void update(GameContainer container, int delta) throws SlickException {
        this.time += (float)delta * 0.001f;
        this.morph.setMorphTime(this.time);
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(Color.green);
        g2.draw(this.a);
        g2.setColor(Color.red);
        g2.draw(this.b);
        g2.setColor(Color.blue);
        g2.draw(this.c);
        g2.setColor(Color.white);
        g2.draw(this.morph);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new MorphShapeTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

