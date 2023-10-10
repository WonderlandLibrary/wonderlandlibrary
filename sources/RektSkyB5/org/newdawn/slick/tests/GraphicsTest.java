/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.util.FastTrig;

public class GraphicsTest
extends BasicGame {
    private boolean clip;
    private float ang;
    private Image image;
    private Polygon poly;
    private GameContainer container;

    public GraphicsTest() {
        super("Graphics Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.image = new Image("testdata/logo.tga", true);
        Image temp = new Image("testdata/palette_tool.png");
        container.setMouseCursor(temp, 0, 0);
        container.setIcons(new String[]{"testdata/icon.tga"});
        container.setTargetFrameRate(100);
        this.poly = new Polygon();
        float len = 100.0f;
        for (int x2 = 0; x2 < 360; x2 += 30) {
            len = len == 100.0f ? 50.0f : 100.0f;
            this.poly.addPoint((float)FastTrig.cos(Math.toRadians(x2)) * len, (float)FastTrig.sin(Math.toRadians(x2)) * len);
        }
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(Color.white);
        g2.setAntiAlias(true);
        for (int x2 = 0; x2 < 360; x2 += 10) {
            g2.drawLine(700.0f, 100.0f, (int)(700.0 + Math.cos(Math.toRadians(x2)) * 100.0), (int)(100.0 + Math.sin(Math.toRadians(x2)) * 100.0));
        }
        g2.setAntiAlias(false);
        g2.setColor(Color.yellow);
        g2.drawString("The Graphics Test!", 300.0f, 50.0f);
        g2.setColor(Color.white);
        g2.drawString("Space - Toggles clipping", 400.0f, 80.0f);
        g2.drawString("Frame rate capped to 100", 400.0f, 120.0f);
        if (this.clip) {
            g2.setColor(Color.gray);
            g2.drawRect(100.0f, 260.0f, 400.0f, 100.0f);
            g2.setClip(100, 260, 400, 100);
        }
        g2.setColor(Color.yellow);
        g2.translate(100.0f, 120.0f);
        g2.fill(this.poly);
        g2.setColor(Color.blue);
        g2.setLineWidth(3.0f);
        g2.draw(this.poly);
        g2.setLineWidth(1.0f);
        g2.translate(0.0f, 230.0f);
        g2.draw(this.poly);
        g2.resetTransform();
        g2.setColor(Color.magenta);
        g2.drawRoundRect(10.0f, 10.0f, 100.0f, 100.0f, 10);
        g2.fillRoundRect(10.0f, 210.0f, 100.0f, 100.0f, 10);
        g2.rotate(400.0f, 300.0f, this.ang);
        g2.setColor(Color.green);
        g2.drawRect(200.0f, 200.0f, 200.0f, 200.0f);
        g2.setColor(Color.blue);
        g2.fillRect(250.0f, 250.0f, 100.0f, 100.0f);
        g2.drawImage(this.image, 300.0f, 270.0f);
        g2.setColor(Color.red);
        g2.drawOval(100.0f, 100.0f, 200.0f, 200.0f);
        g2.setColor(Color.red.darker());
        g2.fillOval(300.0f, 300.0f, 150.0f, 100.0f);
        g2.setAntiAlias(true);
        g2.setColor(Color.white);
        g2.setLineWidth(5.0f);
        g2.drawOval(300.0f, 300.0f, 150.0f, 100.0f);
        g2.setAntiAlias(true);
        g2.resetTransform();
        if (this.clip) {
            g2.clearClip();
        }
    }

    public void update(GameContainer container, int delta) {
        this.ang += (float)delta * 0.1f;
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 57) {
            this.clip = !this.clip;
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new GraphicsTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

