/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

public class ImageBufferTest
extends BasicGame {
    private Image image;

    public ImageBufferTest() {
        super("Image Buffer Test");
    }

    public void init(GameContainer container) throws SlickException {
        ImageBuffer buffer = new ImageBuffer(320, 200);
        for (int x2 = 0; x2 < 320; ++x2) {
            for (int y2 = 0; y2 < 200; ++y2) {
                if (y2 == 20) {
                    buffer.setRGBA(x2, y2, 255, 255, 255, 255);
                    continue;
                }
                buffer.setRGBA(x2, y2, x2, y2, 0, 255);
            }
        }
        this.image = buffer.getImage();
    }

    public void render(GameContainer container, Graphics g2) {
        this.image.draw(50.0f, 50.0f);
    }

    public void update(GameContainer container, int delta) {
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new ImageBufferTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

