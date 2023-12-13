/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class BigSpriteSheetTest
extends BasicGame {
    private Image original;
    private SpriteSheet bigSheet;
    private boolean oldMethod = true;

    public BigSpriteSheetTest() {
        super("Big SpriteSheet Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.original = new BigImage("testdata/bigimage.tga", 2, 256);
        this.bigSheet = new SpriteSheet(this.original, 16, 16);
    }

    public void render(GameContainer container, Graphics g2) {
        if (this.oldMethod) {
            for (int x2 = 0; x2 < 43; ++x2) {
                for (int y2 = 0; y2 < 27; ++y2) {
                    this.bigSheet.getSprite(x2, y2).draw(10 + x2 * 18, 50 + y2 * 18);
                }
            }
        } else {
            this.bigSheet.startUse();
            for (int x3 = 0; x3 < 43; ++x3) {
                for (int y3 = 0; y3 < 27; ++y3) {
                    this.bigSheet.renderInUse(10 + x3 * 18, 50 + y3 * 18, x3, y3);
                }
            }
            this.bigSheet.endUse();
        }
        g2.drawString("Press space to toggle rendering method", 10.0f, 30.0f);
        container.getDefaultFont().drawString(10.0f, 100.0f, "TEST");
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new BigSpriteSheetTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.oldMethod = !this.oldMethod;
        }
    }
}

