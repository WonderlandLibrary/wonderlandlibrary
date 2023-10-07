/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.BigImage;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.SpriteSheet;

public class BigSpriteSheetTest
extends BasicGame {
    private Image original;
    private SpriteSheet bigSheet;
    private boolean oldMethod = true;

    public BigSpriteSheetTest() {
        super("Big SpriteSheet Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.original = new BigImage("testdata/bigimage.tga", 2, 256);
        this.bigSheet = new SpriteSheet(this.original, 16, 16);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        if (this.oldMethod) {
            for (int x = 0; x < 43; ++x) {
                for (int y = 0; y < 27; ++y) {
                    this.bigSheet.getSprite(x, y).draw(10 + x * 18, 50 + y * 18);
                }
            }
        } else {
            this.bigSheet.startUse();
            for (int x = 0; x < 43; ++x) {
                for (int y = 0; y < 27; ++y) {
                    this.bigSheet.renderInUse(10 + x * 18, 50 + y * 18, x, y);
                }
            }
            this.bigSheet.endUse();
        }
        g.drawString("Press space to toggle rendering method", 10.0f, 30.0f);
        container.getDefaultFont().drawString(10.0f, 100.0f, "TEST");
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new BigSpriteSheetTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyPressed(57)) {
            this.oldMethod = !this.oldMethod;
        }
    }
}

