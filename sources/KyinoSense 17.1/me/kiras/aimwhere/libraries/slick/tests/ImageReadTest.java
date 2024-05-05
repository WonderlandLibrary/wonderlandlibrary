/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class ImageReadTest
extends BasicGame {
    private Image image;
    private Color[] read = new Color[6];
    private Graphics g;

    public ImageReadTest() {
        super("Image Read Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.image = new Image("testdata/testcard.png");
        this.read[0] = this.image.getColor(0, 0);
        this.read[1] = this.image.getColor(30, 40);
        this.read[2] = this.image.getColor(55, 70);
        this.read[3] = this.image.getColor(80, 90);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        this.g = g;
        this.image.draw(100.0f, 100.0f);
        g.setColor(Color.white);
        g.drawString("Move mouse over test image", 200.0f, 20.0f);
        g.setColor(this.read[0]);
        g.drawString(this.read[0].toString(), 100.0f, 300.0f);
        g.setColor(this.read[1]);
        g.drawString(this.read[1].toString(), 150.0f, 320.0f);
        g.setColor(this.read[2]);
        g.drawString(this.read[2].toString(), 200.0f, 340.0f);
        g.setColor(this.read[3]);
        g.drawString(this.read[3].toString(), 250.0f, 360.0f);
        if (this.read[4] != null) {
            g.setColor(this.read[4]);
            g.drawString("On image: " + this.read[4].toString(), 100.0f, 250.0f);
        }
        if (this.read[5] != null) {
            g.setColor(Color.white);
            g.drawString("On screen: " + this.read[5].toString(), 100.0f, 270.0f);
        }
    }

    @Override
    public void update(GameContainer container, int delta) {
        int mx = container.getInput().getMouseX();
        int my = container.getInput().getMouseY();
        this.read[4] = mx >= 100 && my >= 100 && mx < 200 && my < 200 ? this.image.getColor(mx - 100, my - 100) : Color.black;
        this.read[5] = this.g.getPixel(mx, my);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new ImageReadTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

