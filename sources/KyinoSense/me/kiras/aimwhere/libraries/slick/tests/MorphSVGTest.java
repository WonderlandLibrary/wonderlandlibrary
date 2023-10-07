/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.svg.Diagram;
import me.kiras.aimwhere.libraries.slick.svg.InkscapeLoader;
import me.kiras.aimwhere.libraries.slick.svg.SVGMorph;
import me.kiras.aimwhere.libraries.slick.svg.SimpleDiagramRenderer;

public class MorphSVGTest
extends BasicGame {
    private SVGMorph morph;
    private Diagram base;
    private float time;
    private float x = -300.0f;

    public MorphSVGTest() {
        super("MorphShapeTest");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.base = InkscapeLoader.load("testdata/svg/walk1.svg");
        this.morph = new SVGMorph(this.base);
        this.morph.addStep(InkscapeLoader.load("testdata/svg/walk2.svg"));
        this.morph.addStep(InkscapeLoader.load("testdata/svg/walk3.svg"));
        this.morph.addStep(InkscapeLoader.load("testdata/svg/walk4.svg"));
        container.setVSync(true);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        this.morph.updateMorphTime((float)delta * 0.003f);
        this.x += (float)delta * 0.2f;
        if (this.x > 550.0f) {
            this.x = -450.0f;
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.translate(this.x, 0.0f);
        SimpleDiagramRenderer.render(g, this.morph);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new MorphSVGTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

