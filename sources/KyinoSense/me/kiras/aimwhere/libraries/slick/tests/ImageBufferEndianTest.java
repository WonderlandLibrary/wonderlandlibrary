/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.nio.ByteOrder;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.ImageBuffer;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class ImageBufferEndianTest
extends BasicGame {
    private ImageBuffer redImageBuffer;
    private ImageBuffer blueImageBuffer;
    private Image fromRed;
    private Image fromBlue;
    private String endian;

    public ImageBufferEndianTest() {
        super("ImageBuffer Endian Test");
    }

    public static void main(String[] args2) {
        try {
            AppGameContainer container = new AppGameContainer(new ImageBufferEndianTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.drawString("Endianness is " + this.endian, 10.0f, 100.0f);
        g.drawString("Image below should be red", 10.0f, 200.0f);
        g.drawImage(this.fromRed, 10.0f, 220.0f);
        g.drawString("Image below should be blue", 410.0f, 200.0f);
        g.drawImage(this.fromBlue, 410.0f, 220.0f);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.endian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? "Big endian" : (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "Little endian" : "no idea");
        this.redImageBuffer = new ImageBuffer(100, 100);
        this.fillImageBufferWithColor(this.redImageBuffer, Color.red, 100, 100);
        this.blueImageBuffer = new ImageBuffer(100, 100);
        this.fillImageBufferWithColor(this.blueImageBuffer, Color.blue, 100, 100);
        this.fromRed = this.redImageBuffer.getImage();
        this.fromBlue = this.blueImageBuffer.getImage();
    }

    private void fillImageBufferWithColor(ImageBuffer buffer, Color c, int width, int height) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                buffer.setRGBA(x, y, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            }
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }
}

