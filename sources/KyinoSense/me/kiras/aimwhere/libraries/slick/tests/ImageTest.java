/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class ImageTest
extends BasicGame {
    private Image tga;
    private Image scaleMe;
    private Image scaled;
    private Image gif;
    private Image image;
    private Image subImage;
    private Image rotImage;
    private float rot;
    public static boolean exitMe = true;

    public ImageTest() {
        super("Image Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.image = this.tga = new Image("testdata/logo.png");
        this.rotImage = new Image("testdata/logo.png");
        this.rotImage = this.rotImage.getScaledCopy(this.rotImage.getWidth() / 2, this.rotImage.getHeight() / 2);
        this.scaleMe = new Image("testdata/logo.tga", true, 2);
        this.gif = new Image("testdata/logo.gif");
        this.gif.destroy();
        this.gif = new Image("testdata/logo.gif");
        this.scaled = this.gif.getScaledCopy(120, 120);
        this.subImage = this.image.getSubImage(200, 0, 70, 260);
        this.rot = 0.0f;
        if (exitMe) {
            container.exit();
        }
        Image test = this.tga.getSubImage(50, 50, 50, 50);
        System.out.println(test.getColor(50, 50));
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.drawRect(0.0f, 0.0f, this.image.getWidth(), this.image.getHeight());
        this.image.draw(0.0f, 0.0f);
        this.image.draw(500.0f, 0.0f, 200.0f, 100.0f);
        this.scaleMe.draw(500.0f, 100.0f, 200.0f, 100.0f);
        this.scaled.draw(400.0f, 500.0f);
        Image flipped = this.scaled.getFlippedCopy(true, false);
        flipped.draw(520.0f, 500.0f);
        Image flipped2 = flipped.getFlippedCopy(false, true);
        flipped2.draw(520.0f, 380.0f);
        Image flipped3 = flipped2.getFlippedCopy(true, false);
        flipped3.draw(400.0f, 380.0f);
        for (int i = 0; i < 3; ++i) {
            this.subImage.draw(200 + i * 30, 300.0f);
        }
        g.translate(500.0f, 200.0f);
        g.rotate(50.0f, 50.0f, this.rot);
        g.scale(0.3f, 0.3f);
        this.image.draw();
        g.resetTransform();
        this.rotImage.setRotation(this.rot);
        this.rotImage.draw(100.0f, 200.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
        this.rot += (float)delta * 0.1f;
        if (this.rot > 360.0f) {
            this.rot -= 360.0f;
        }
    }

    public static void main(String[] argv) {
        boolean sharedContextTest = false;
        try {
            exitMe = false;
            if (sharedContextTest) {
                GameContainer.enableSharedContext();
                exitMe = true;
            }
            AppGameContainer container = new AppGameContainer(new ImageTest());
            container.setForceExit(!sharedContextTest);
            container.setDisplayMode(800, 600, false);
            container.start();
            if (sharedContextTest) {
                System.out.println("Exit first instance");
                exitMe = false;
                container = new AppGameContainer(new ImageTest());
                container.setDisplayMode(800, 600, false);
                container.start();
            }
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 57) {
            this.image = this.image == this.gif ? this.tga : this.gif;
        }
    }
}

