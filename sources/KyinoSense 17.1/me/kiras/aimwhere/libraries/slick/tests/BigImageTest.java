/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.BigImage;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.SpriteSheet;

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

    @Override
    public void init(GameContainer container) throws SlickException {
        this.original = this.image = new BigImage("testdata/bigimage.tga", 2, 512);
        this.sub = this.image.getSubImage(210, 210, 200, 130);
        this.scaledSub = this.sub.getScaledCopy(2.0f);
        this.image = this.image.getScaledCopy(0.3f);
        this.imageX = this.image.getFlippedCopy(true, false);
        this.imageY = this.imageX.getFlippedCopy(true, true);
        this.bigSheet = new SpriteSheet(this.original, 16, 16);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        this.original.draw(0.0f, 0.0f, new Color(1.0f, 1.0f, 1.0f, 0.4f));
        this.image.draw(this.x, this.y);
        this.imageX.draw(this.x + 400.0f, this.y);
        this.imageY.draw(this.x, this.y + 300.0f);
        this.scaledSub.draw(this.x + 300.0f, this.y + 300.0f);
        this.bigSheet.getSprite(7, 5).draw(50.0f, 10.0f);
        g.setColor(Color.white);
        g.drawRect(50.0f, 10.0f, 64.0f, 64.0f);
        g.rotate(this.x + 400.0f, this.y + 165.0f, this.ang);
        g.drawImage(this.sub, this.x + 300.0f, this.y + 100.0f);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new BigImageTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
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

