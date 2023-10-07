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
import me.kiras.aimwhere.libraries.slick.geom.MorphShape;
import me.kiras.aimwhere.libraries.slick.geom.Rectangle;
import me.kiras.aimwhere.libraries.slick.geom.Shape;
import me.kiras.aimwhere.libraries.slick.geom.Transform;

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

    @Override
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

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        this.time += (float)delta * 0.001f;
        this.morph.setMorphTime(this.time);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(Color.green);
        g.draw(this.a);
        g.setColor(Color.red);
        g.draw(this.b);
        g.setColor(Color.blue);
        g.draw(this.c);
        g.setColor(Color.white);
        g.draw(this.morph);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new MorphShapeTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

