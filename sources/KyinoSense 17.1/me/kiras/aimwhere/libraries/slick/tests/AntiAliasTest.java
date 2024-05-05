/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class AntiAliasTest
extends BasicGame {
    public AntiAliasTest() {
        super("AntiAlias Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.getGraphics().setBackground(Color.green);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setAntiAlias(true);
        g.setColor(Color.red);
        g.drawOval(100.0f, 100.0f, 100.0f, 100.0f);
        g.fillOval(300.0f, 100.0f, 100.0f, 100.0f);
        g.setAntiAlias(false);
        g.setColor(Color.red);
        g.drawOval(100.0f, 300.0f, 100.0f, 100.0f);
        g.fillOval(300.0f, 300.0f, 100.0f, 100.0f);
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new AntiAliasTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

