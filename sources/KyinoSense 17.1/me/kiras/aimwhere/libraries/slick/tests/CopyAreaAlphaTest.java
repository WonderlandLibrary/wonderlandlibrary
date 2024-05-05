/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class CopyAreaAlphaTest
extends BasicGame {
    private Image textureMap;
    private Image copy;

    public CopyAreaAlphaTest() {
        super("CopyArea Alpha Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.textureMap = new Image("testdata/grass.png");
        container.getGraphics().setBackground(Color.black);
        this.copy = new Image(100, 100);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.clearAlphaMap();
        g.setDrawMode(Graphics.MODE_NORMAL);
        g.setColor(Color.white);
        g.fillOval(100.0f, 100.0f, 150.0f, 150.0f);
        this.textureMap.draw(10.0f, 50.0f);
        g.copyArea(this.copy, 100, 100);
        g.setColor(Color.red);
        g.fillRect(300.0f, 100.0f, 200.0f, 200.0f);
        this.copy.draw(350.0f, 150.0f);
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new CopyAreaAlphaTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

