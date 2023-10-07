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
import me.kiras.aimwhere.libraries.slick.ImageBuffer;
import me.kiras.aimwhere.libraries.slick.SlickException;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ImageBufferTest
extends BasicGame {
    private Image image;

    public ImageBufferTest() {
        super("Image Buffer Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        ImageBuffer buffer = new ImageBuffer(320, 200);
        for (int x = 0; x < 320; ++x) {
            for (int y = 0; y < 200; ++y) {
                if (y == 20) {
                    buffer.setRGBA(x, y, 255, 255, 255, 255);
                    continue;
                }
                buffer.setRGBA(x, y, x, y, 0, 255);
            }
        }
        this.image = buffer.getImage();
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        this.image.draw(50.0f, 50.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new ImageBufferTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

