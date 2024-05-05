/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.ScalableGame;
import me.kiras.aimwhere.libraries.slick.SlickException;

public class ScalableTest
extends BasicGame {
    public ScalableTest() {
        super("Scalable Test For Widescreen");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(new Color(0.4f, 0.6f, 0.8f));
        g.fillRect(0.0f, 0.0f, 1024.0f, 568.0f);
        g.setColor(Color.white);
        g.drawRect(5.0f, 5.0f, 1014.0f, 558.0f);
        g.setColor(Color.white);
        g.drawString(container.getInput().getMouseX() + "," + container.getInput().getMouseY(), 10.0f, 400.0f);
        g.setColor(Color.red);
        g.fillOval(container.getInput().getMouseX() - 10, container.getInput().getMouseY() - 10, 20.0f, 20.0f);
    }

    public static void main(String[] argv) {
        try {
            ScalableGame game = new ScalableGame(new ScalableTest(), 1024, 568, true){

                @Override
                protected void renderOverlay(GameContainer container, Graphics g) {
                    g.setColor(Color.white);
                    g.drawString("Outside The Game", 350.0f, 10.0f);
                    g.drawString(container.getInput().getMouseX() + "," + container.getInput().getMouseY(), 400.0f, 20.0f);
                }
            };
            AppGameContainer container = new AppGameContainer(game);
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

