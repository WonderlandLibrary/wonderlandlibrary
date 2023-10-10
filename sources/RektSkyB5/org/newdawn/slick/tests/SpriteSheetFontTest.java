/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;
import org.newdawn.slick.util.Log;

public class SpriteSheetFontTest
extends BasicGame {
    private Font font;
    private static AppGameContainer container;

    public SpriteSheetFontTest() {
        super("SpriteSheetFont Test");
    }

    public void init(GameContainer container) throws SlickException {
        SpriteSheet sheet = new SpriteSheet("testdata/spriteSheetFont.png", 32, 32);
        this.font = new SpriteSheetFont(sheet, ' ');
    }

    public void render(GameContainer container, Graphics g2) {
        g2.setBackground(Color.gray);
        this.font.drawString(80.0f, 5.0f, "A FONT EXAMPLE", Color.red);
        this.font.drawString(100.0f, 50.0f, "A MORE COMPLETE LINE");
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 57) {
            try {
                container.setDisplayMode(640, 480, false);
            }
            catch (SlickException e2) {
                Log.error(e2);
            }
        }
    }

    public static void main(String[] argv) {
        try {
            container = new AppGameContainer(new SpriteSheetFontTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

