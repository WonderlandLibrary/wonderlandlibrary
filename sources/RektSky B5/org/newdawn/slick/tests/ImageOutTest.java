/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class ImageOutTest
extends BasicGame {
    private GameContainer container;
    private ParticleSystem fire;
    private Graphics g;
    private Image copy;
    private String message;

    public ImageOutTest() {
        super("Image Out Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.container = container;
        try {
            this.fire = ParticleIO.loadConfiguredSystem("testdata/system.xml");
        }
        catch (IOException e2) {
            throw new SlickException("Failed to load particle systems", e2);
        }
        this.copy = new Image(400, 300);
        String[] formats = ImageOut.getSupportedFormats();
        this.message = "Formats supported: ";
        for (int i2 = 0; i2 < formats.length; ++i2) {
            this.message = this.message + formats[i2];
            if (i2 >= formats.length - 1) continue;
            this.message = this.message + ",";
        }
    }

    public void render(GameContainer container, Graphics g2) {
        g2.drawString("T - TGA Snapshot", 10.0f, 50.0f);
        g2.drawString("J - JPG Snapshot", 10.0f, 70.0f);
        g2.drawString("P - PNG Snapshot", 10.0f, 90.0f);
        g2.setDrawMode(Graphics.MODE_ADD);
        g2.drawImage(this.copy, 200.0f, 300.0f);
        g2.setDrawMode(Graphics.MODE_NORMAL);
        g2.drawString(this.message, 10.0f, 400.0f);
        g2.drawRect(200.0f, 0.0f, 400.0f, 300.0f);
        g2.translate(400.0f, 250.0f);
        this.fire.render();
        this.g = g2;
    }

    private void writeTo(String fname) throws SlickException {
        this.g.copyArea(this.copy, 200, 0);
        ImageOut.write(this.copy, fname);
        this.message = "Written " + fname;
    }

    public void update(GameContainer container, int delta) throws SlickException {
        this.fire.update(delta);
        if (container.getInput().isKeyPressed(25)) {
            this.writeTo("ImageOutTest.png");
        }
        if (container.getInput().isKeyPressed(36)) {
            this.writeTo("ImageOutTest.jpg");
        }
        if (container.getInput().isKeyPressed(20)) {
            this.writeTo("ImageOutTest.tga");
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new ImageOutTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            this.container.exit();
        }
    }
}

