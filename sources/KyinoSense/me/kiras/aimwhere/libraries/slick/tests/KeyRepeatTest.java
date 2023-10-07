/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Input;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class KeyRepeatTest
extends BasicGame {
    private int count;
    private Input input;

    public KeyRepeatTest() {
        super("KeyRepeatTest");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.input = container.getInput();
        this.input.enableKeyRepeat(300, 100);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.drawString("Key Press Count: " + this.count, 100.0f, 100.0f);
        g.drawString("Press Space to Toggle Key Repeat", 100.0f, 150.0f);
        g.drawString("Key Repeat Enabled: " + this.input.isKeyRepeatEnabled(), 100.0f, 200.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new KeyRepeatTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
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

