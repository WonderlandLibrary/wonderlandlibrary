/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class ImageMemTest
extends BasicGame {
    public ImageMemTest() {
        super("Image Memory Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        try {
            Image img = new Image(2400, 2400);
            img.getGraphics();
            img.destroy();
            img = new Image(2400, 2400);
            img.getGraphics();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) {
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new ImageMemTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

