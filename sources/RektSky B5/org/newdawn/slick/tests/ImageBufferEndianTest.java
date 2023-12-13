/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.nio.ByteOrder;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

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

    public static void main(String[] args) {
        try {
            AppGameContainer container = new AppGameContainer(new ImageBufferEndianTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(Color.white);
        g2.drawString("Endianness is " + this.endian, 10.0f, 100.0f);
        g2.drawString("Image below should be red", 10.0f, 200.0f);
        g2.drawImage(this.fromRed, 10.0f, 220.0f);
        g2.drawString("Image below should be blue", 410.0f, 200.0f);
        g2.drawImage(this.fromBlue, 410.0f, 220.0f);
    }

    public void init(GameContainer container) throws SlickException {
        this.endian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? "Big endian" : (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "Little endian" : "no idea");
        this.redImageBuffer = new ImageBuffer(100, 100);
        this.fillImageBufferWithColor(this.redImageBuffer, Color.red, 100, 100);
        this.blueImageBuffer = new ImageBuffer(100, 100);
        this.fillImageBufferWithColor(this.blueImageBuffer, Color.blue, 100, 100);
        this.fromRed = this.redImageBuffer.getImage();
        this.fromBlue = this.blueImageBuffer.getImage();
    }

    private void fillImageBufferWithColor(ImageBuffer buffer, Color c2, int width, int height) {
        for (int x2 = 0; x2 < width; ++x2) {
            for (int y2 = 0; y2 < height; ++y2) {
                buffer.setRGBA(x2, y2, c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
            }
        }
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }
}

