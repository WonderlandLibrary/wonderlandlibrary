/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class DoubleClickTest
extends BasicGame {
    private String message = "Click or Double Click";

    public DoubleClickTest() {
        super("Double Click Test");
    }

    public void init(GameContainer container) throws SlickException {
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.drawString(this.message, 100.0f, 100.0f);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new DoubleClickTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    public void mouseClicked(int button, int x2, int y2, int clickCount) {
        if (clickCount == 1) {
            this.message = "Single Click: " + button + " " + x2 + "," + y2;
        }
        if (clickCount == 2) {
            this.message = "Double Click: " + button + " " + x2 + "," + y2;
        }
    }
}

