/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.Font;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.SpriteSheet;
import me.kiras.aimwhere.libraries.slick.SpriteSheetFont;
import me.kiras.aimwhere.libraries.slick.util.Log;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpriteSheetFontTest
extends BasicGame {
    private Font font;
    private static AppGameContainer container;

    public SpriteSheetFontTest() {
        super("SpriteSheetFont Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        SpriteSheet sheet = new SpriteSheet("testdata/spriteSheetFont.png", 32, 32);
        this.font = new SpriteSheetFont(sheet, ' ');
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.setBackground(Color.gray);
        this.font.drawString(80.0f, 5.0f, "A FONT EXAMPLE", Color.red);
        this.font.drawString(100.0f, 50.0f, "A MORE COMPLETE LINE");
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
        }
        if (key == 57) {
            try {
                container.setDisplayMode(640, 480, false);
            }
            catch (SlickException e) {
                Log.error(e);
            }
        }
    }

    public static void main(String[] argv) {
        try {
            container = new AppGameContainer(new SpriteSheetFontTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

