/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.io.IOException;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.particles.ConfigurableEmitter;
import me.kiras.aimwhere.libraries.slick.particles.ParticleIO;
import me.kiras.aimwhere.libraries.slick.particles.ParticleSystem;

public class DuplicateEmitterTest
extends BasicGame {
    private GameContainer container;
    private ParticleSystem explosionSystem;
    private ConfigurableEmitter explosionEmitter;

    public DuplicateEmitterTest() {
        super("DuplicateEmitterTest");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        try {
            this.explosionSystem = ParticleIO.loadConfiguredSystem("testdata/endlessexplosion.xml");
            this.explosionEmitter = (ConfigurableEmitter)this.explosionSystem.getEmitter(0);
            this.explosionEmitter.setPosition(400.0f, 100.0f);
            for (int i = 0; i < 5; ++i) {
                ConfigurableEmitter newOne = this.explosionEmitter.duplicate();
                if (newOne == null) {
                    throw new SlickException("Failed to duplicate explosionEmitter");
                }
                newOne.name = newOne.name + "_" + i;
                newOne.setPosition((i + 1) * 133, 400.0f);
                this.explosionSystem.addEmitter(newOne);
            }
        }
        catch (IOException e) {
            throw new SlickException("Failed to load particle systems", e);
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        this.explosionSystem.update(delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        this.explosionSystem.render();
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            this.container.exit();
        }
        if (key == 37) {
            this.explosionEmitter.wrapUp();
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new DuplicateEmitterTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

