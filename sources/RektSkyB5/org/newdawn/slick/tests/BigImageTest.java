/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class BigImageTest
extends BasicGame {
    private Image original;
    private Image image;
    private Image imageX;
    private Image imageY;
    private Image sub;
    private Image scaledSub;
    private float x;
    private float y;
    private float ang = 30.0f;
    private SpriteSheet bigSheet;

    public BigImageTest() {
        super("Big Image Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.original = this.image = new BigImage("testdata/bigimage.tga", 2, 512);
        this.sub = this.image.getSubImage(210, 210, 200, 130);
        this.scaledSub = this.sub.getScaledCopy(2.0f);
        this.image = this.image.getScaledCopy(0.3f);
        this.imageX = this.image.getFlippedCopy(true, false);
        this.imageY = this.imageX.getFlippedCopy(true, true);
        this.bigSheet = new SpriteSheet(this.original, 16, 16);
    }

    public void render(GameContainer container, Graphics g2) {
        this.original.draw(0.0f, 0.0f, new Color(1.0f, 1.0f, 1.0f, 0.4f));
        this.image.draw(this.x, this.y);
        this.imageX.draw(this.x + 400.0f, this.y);
        this.imageY.draw(this.x, this.y + 300.0f);
        this.scaledSub.draw(this.x + 300.0f, this.y + 300.0f);
        this.bigSheet.getSprite(7, 5).draw(50.0f, 10.0f);
        g2.setColor(Color.white);
        g2.drawRect(50.0f, 10.0f, 64.0f, 64.0f);
        g2.rotate(this.x + 400.0f, this.y + 165.0f, this.ang);
        g2.drawImage(this.sub, this.x + 300.0f, this.y + 100.0f);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new BigImageTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void update(GameContainer container, int delta) throws SlickException {
        this.ang += (float)delta * 0.1f;
        if (container.getInput().isKeyDown(203)) {
            this.x -= (float)delta * 0.1f;
        }
        if (container.getInput().isKeyDown(205)) {
            this.x += (float)delta * 0.1f;
        }
        if (container.getInput().isKeyDown(200)) {
            this.y -= (float)delta * 0.1f;
        }
        if (container.getInput().isKeyDown(208)) {
            this.y += (float)delta * 0.1f;
        }
    }
}

