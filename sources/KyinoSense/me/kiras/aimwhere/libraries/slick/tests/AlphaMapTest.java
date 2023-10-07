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

public class AlphaMapTest
extends BasicGame {
    private Image alphaMap;
    private Image textureMap;

    public AlphaMapTest() {
        super("AlphaMap Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.alphaMap = new Image("testdata/alphamap.png");
        this.textureMap = new Image("testdata/grass.png");
        container.getGraphics().setBackground(Color.black);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.clearAlphaMap();
        g.setDrawMode(Graphics.MODE_NORMAL);
        this.textureMap.draw(10.0f, 50.0f);
        g.setColor(Color.red);
        g.fillRect(290.0f, 40.0f, 200.0f, 200.0f);
        g.setColor(Color.white);
        g.setDrawMode(Graphics.MODE_ALPHA_MAP);
        this.alphaMap.draw(300.0f, 50.0f);
        g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
        this.textureMap.draw(300.0f, 50.0f);
        g.setDrawMode(Graphics.MODE_NORMAL);
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new AlphaMapTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

