/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class DoubleClickTest
extends BasicGame {
    private String message = "Click or Double Click";

    public DoubleClickTest() {
        super("Double Click Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.drawString(this.message, 100.0f, 100.0f);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new DoubleClickTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        if (clickCount == 1) {
            this.message = "Single Click: " + button + " " + x + "," + y;
        }
        if (clickCount == 2) {
            this.message = "Double Click: " + button + " " + x + "," + y;
        }
    }
}

