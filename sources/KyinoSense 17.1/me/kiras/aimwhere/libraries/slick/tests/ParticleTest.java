/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.particles.ParticleSystem;
import me.kiras.aimwhere.libraries.slick.particles.effects.FireEmitter;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ParticleTest
extends BasicGame {
    private ParticleSystem system;
    private int mode = 2;

    public ParticleTest() {
        super("Particle Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        Image image2 = new Image("testdata/particle.tga", true);
        this.system = new ParticleSystem(image2);
        this.system.addEmitter(new FireEmitter(400, 300, 45.0f));
        this.system.addEmitter(new FireEmitter(200, 300, 60.0f));
        this.system.addEmitter(new FireEmitter(600, 300, 30.0f));
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        for (int i = 0; i < 100; ++i) {
            g.translate(1.0f, 1.0f);
            this.system.render();
        }
        g.resetTransform();
        g.drawString("Press space to toggle blending mode", 200.0f, 500.0f);
        g.drawString("Particle Count: " + this.system.getParticleCount() * 100, 200.0f, 520.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
        this.system.update(delta);
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
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
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

