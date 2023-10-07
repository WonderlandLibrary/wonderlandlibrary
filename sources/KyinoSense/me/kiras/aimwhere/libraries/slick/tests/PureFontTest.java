/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AngelCodeFont;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Font;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PureFontTest
extends BasicGame {
    private Font font;
    private Image image;
    private static AppGameContainer container;

    public PureFontTest() {
        super("Hiero Font Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.image = new Image("testdata/sky.jpg");
        this.font = new AngelCodeFont("testdata/hiero.fnt", "testdata/hiero.png");
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        this.image.draw(0.0f, 0.0f, 800.0f, 600.0f);
        this.font.drawString(100.0f, 32.0f, "On top of old smokey, all");
        this.font.drawString(100.0f, 80.0f, "covered with sand..");
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
        }
    }

    public static void main(String[] argv) {
        try {
            container = new AppGameContainer(new PureFontTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

