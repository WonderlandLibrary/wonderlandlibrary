/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class CanvasContainerTest
extends BasicGame {
    private Image tga;
    private Image scaleMe;
    private Image scaled;
    private Image gif;
    private Image image;
    private Image subImage;
    private float rot;

    public CanvasContainerTest() {
        super("Canvas Container Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.image = this.tga = new Image("testdata/logo.tga");
        this.scaleMe = new Image("testdata/logo.tga", true, 2);
        this.gif = new Image("testdata/logo.gif");
        this.scaled = this.gif.getScaledCopy(120, 120);
        this.subImage = this.image.getSubImage(200, 0, 70, 260);
        this.rot = 0.0f;
    }

    public void render(GameContainer container, Graphics g2) {
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
        for (int i2 = 0; i2 < 3; ++i2) {
            this.subImage.draw(200 + i2 * 30, 300.0f);
        }
        g2.translate(500.0f, 200.0f);
        g2.rotate(50.0f, 50.0f, this.rot);
        g2.scale(0.3f, 0.3f);
        this.image.draw();
        g2.resetTransform();
    }

    public void update(GameContainer container, int delta) {
        this.rot += (float)delta * 0.1f;
        if (this.rot > 360.0f) {
            this.rot -= 360.0f;
        }
    }

    public static void main(String[] argv) {
        try {
            CanvasGameContainer container = new CanvasGameContainer(new CanvasContainerTest());
            Frame frame = new Frame("Test");
            frame.setLayout(new GridLayout(1, 2));
            frame.setSize(500, 500);
            frame.add(container);
            frame.addWindowListener(new WindowAdapter(){

                public void windowClosing(WindowEvent e2) {
                    System.exit(0);
                }
            });
            frame.setVisible(true);
            container.start();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void keyPressed(int key, char c2) {
        if (key == 57) {
            this.image = this.image == this.gif ? this.tga : this.gif;
        }
    }
}

