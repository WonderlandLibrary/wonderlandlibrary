/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class KeyRepeatTest
extends BasicGame {
    private int count;
    private Input input;

    public KeyRepeatTest() {
        super("KeyRepeatTest");
    }

    public void init(GameContainer container) throws SlickException {
        this.input = container.getInput();
        this.input.enableKeyRepeat(300, 100);
    }

    public void render(GameContainer container, Graphics g2) {
        g2.drawString("Key Press Count: " + this.count, 100.0f, 100.0f);
        g2.drawString("Press Space to Toggle Key Repeat", 100.0f, 150.0f);
        g2.drawString("Key Repeat Enabled: " + this.input.isKeyRepeatEnabled(), 100.0f, 200.0f);
    }

    public void update(GameContainer container, int delta) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new KeyRepeatTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void keyPressed(int key, char c2) {
        ++this.count;
        if (key == 57) {
            if (this.input.isKeyRepeatEnabled()) {
                this.input.disableKeyRepeat();
            } else {
                this.input.enableKeyRepeat(300, 100);
            }
        }
    }
}

