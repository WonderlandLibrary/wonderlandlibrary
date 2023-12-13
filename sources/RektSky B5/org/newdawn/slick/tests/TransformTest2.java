/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TransformTest2
extends BasicGame {
    private float scale = 1.0f;
    private boolean scaleUp;
    private boolean scaleDown;
    private float camX = 320.0f;
    private float camY = 240.0f;
    private boolean moveLeft;
    private boolean moveUp;
    private boolean moveRight;
    private boolean moveDown;

    public TransformTest2() {
        super("Transform Test");
    }

    public void init(GameContainer container) throws SlickException {
        container.setTargetFrameRate(100);
    }

    public void render(GameContainer contiainer, Graphics g2) {
        g2.translate(320.0f, 240.0f);
        g2.translate(-this.camX * this.scale, -this.camY * this.scale);
        g2.scale(this.scale, this.scale);
        g2.setColor(Color.red);
        for (int x2 = 0; x2 < 10; ++x2) {
            for (int y2 = 0; y2 < 10; ++y2) {
                g2.fillRect(-500 + x2 * 100, -500 + y2 * 100, 80.0f, 80.0f);
            }
        }
        g2.setColor(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        g2.fillRect(-320.0f, -240.0f, 640.0f, 480.0f);
        g2.setColor(Color.white);
        g2.drawRect(-320.0f, -240.0f, 640.0f, 480.0f);
    }

    public void update(GameContainer container, int delta) {
        if (this.scaleUp) {
            this.scale += (float)delta * 0.001f;
        }
        if (this.scaleDown) {
            this.scale -= (float)delta * 0.001f;
        }
        float moveSpeed = (float)delta * 0.4f * (1.0f / this.scale);
        if (this.moveLeft) {
            this.camX -= moveSpeed;
        }
        if (this.moveUp) {
            this.camY -= moveSpeed;
        }
        if (this.moveRight) {
            this.camX += moveSpeed;
        }
        if (this.moveDown) {
            this.camY += moveSpeed;
        }
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 16) {
            this.scaleUp = true;
        }
        if (key == 30) {
            this.scaleDown = true;
        }
        if (key == 203) {
            this.moveLeft = true;
        }
        if (key == 200) {
            this.moveUp = true;
        }
        if (key == 205) {
            this.moveRight = true;
        }
        if (key == 208) {
            this.moveDown = true;
        }
    }

    public void keyReleased(int key, char c2) {
        if (key == 16) {
            this.scaleUp = false;
        }
        if (key == 30) {
            this.scaleDown = false;
        }
        if (key == 203) {
            this.moveLeft = false;
        }
        if (key == 200) {
            this.moveUp = false;
        }
        if (key == 205) {
            this.moveRight = false;
        }
        if (key == 208) {
            this.moveDown = false;
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new TransformTest2());
            container.setDisplayMode(640, 480, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

