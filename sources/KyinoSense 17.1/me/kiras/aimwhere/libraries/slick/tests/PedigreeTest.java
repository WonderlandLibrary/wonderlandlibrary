/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.io.IOException;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.particles.ConfigurableEmitter;
import me.kiras.aimwhere.libraries.slick.particles.ParticleIO;
import me.kiras.aimwhere.libraries.slick.particles.ParticleSystem;

public class PedigreeTest
extends BasicGame {
    private Image image;
    private GameContainer container;
    private ParticleSystem trail;
    private ParticleSystem fire;
    private float rx;
    private float ry = 900.0f;

    public PedigreeTest() {
        super("Pedigree Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        try {
            this.fire = ParticleIO.loadConfiguredSystem("testdata/system.xml");
            this.trail = ParticleIO.loadConfiguredSystem("testdata/smoketrail.xml");
        }
        catch (IOException e) {
            throw new SlickException("Failed to load particle systems", e);
        }
        this.image = new Image("testdata/rocket.png");
        this.spawnRocket();
    }

    private void spawnRocket() {
        this.ry = 700.0f;
        this.rx = (float)(Math.random() * 600.0 + 100.0);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        ((ConfigurableEmitter)this.trail.getEmitter(0)).setPosition(this.rx + 14.0f, this.ry + 35.0f);
        this.trail.render();
        this.image.draw((int)this.rx, (int)this.ry);
        g.translate(400.0f, 300.0f);
        this.fire.render();
    }

    @Override
    public void update(GameContainer container, int delta) {
        this.fire.update(delta);
        this.trail.update(delta);
        this.ry -= (float)delta * 0.25f;
        if (this.ry < -100.0f) {
            this.spawnRocket();
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        super.mousePressed(button, x, y);
        for (int i = 0; i < this.fire.getEmitterCount(); ++i) {
            ((ConfigurableEmitter)this.fire.getEmitter(i)).setPosition(x - 400, y - 300, true);
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new PedigreeTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            this.container.exit();
        }
    }
}

