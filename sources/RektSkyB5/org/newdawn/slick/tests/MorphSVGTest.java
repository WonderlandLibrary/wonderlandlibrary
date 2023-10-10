/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.InkscapeLoader;
import org.newdawn.slick.svg.SVGMorph;
import org.newdawn.slick.svg.SimpleDiagramRenderer;

public class MorphSVGTest
extends BasicGame {
    private SVGMorph morph;
    private Diagram base;
    private float time;
    private float x = -300.0f;

    public MorphSVGTest() {
        super("MorphShapeTest");
    }

    public void init(GameContainer container) throws SlickException {
        this.base = InkscapeLoader.load("testdata/svg/walk1.svg");
        this.morph = new SVGMorph(this.base);
        this.morph.addStep(InkscapeLoader.load("testdata/svg/walk2.svg"));
        this.morph.addStep(InkscapeLoader.load("testdata/svg/walk3.svg"));
        this.morph.addStep(InkscapeLoader.load("testdata/svg/walk4.svg"));
        container.setVSync(true);
    }

    public void update(GameContainer container, int delta) throws SlickException {
        this.morph.updateMorphTime((float)delta * 0.003f);
        this.x += (float)delta * 0.2f;
        if (this.x > 550.0f) {
            this.x = -450.0f;
        }
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.translate(this.x, 0.0f);
        SimpleDiagramRenderer.render(g2, this.morph);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new MorphSVGTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

