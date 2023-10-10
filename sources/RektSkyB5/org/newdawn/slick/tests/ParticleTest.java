/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

public class ParticleTest
extends BasicGame {
    private ParticleSystem system;
    private int mode = 2;

    public ParticleTest() {
        super("Particle Test");
    }

    public void init(GameContainer container) throws SlickException {
        Image image = new Image("testdata/particle.tga", true);
        this.system = new ParticleSystem(image);
        this.system.addEmitter(new FireEmitter(400, 300, 45.0f));
        this.system.addEmitter(new FireEmitter(200, 300, 60.0f));
        this.system.addEmitter(new FireEmitter(600, 300, 30.0f));
    }

    public void render(GameContainer container, Graphics g2) {
        for (int i2 = 0; i2 < 100; ++i2) {
            g2.translate(1.0f, 1.0f);
            this.system.render();
        }
        g2.resetTransform();
        g2.drawString("Press space to toggle blending mode", 200.0f, 500.0f);
        g2.drawString("Particle Count: " + this.system.getParticleCount() * 100, 200.0f, 520.0f);
    }

    public void update(GameContainer container, int delta) {
        this.system.update(delta);
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 57) {
            this.mode = 1 == this.mode ? 2 : 1;
            this.system.setBlendingMode(this.mode);
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new ParticleTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

