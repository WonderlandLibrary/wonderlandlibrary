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

public class FlashTest
extends BasicGame {
    private Image image;
    private boolean flash;
    private GameContainer container;

    public FlashTest() {
        super("Flash Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.image = new Image("testdata/logo.tga");
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.drawString("Press space to toggle", 10.0f, 50.0f);
        if (this.flash) {
            this.image.draw(100.0f, 100.0f);
        } else {
            this.image.drawFlash(100.0f, 100.0f, this.image.getWidth(), this.image.getHeight(), new Color(1.0f, 0.0f, 1.0f, 1.0f));
        }
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new FlashTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 57) {
            boolean bl = this.flash = !this.flash;
        }
        if (key == 1) {
            this.container.exit();
        }
    }
}

