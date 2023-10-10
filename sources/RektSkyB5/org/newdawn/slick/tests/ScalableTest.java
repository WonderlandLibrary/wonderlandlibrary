/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

public class ScalableTest
extends BasicGame {
    public ScalableTest() {
        super("Scalable Test For Widescreen");
    }

    public void init(GameContainer container) throws SlickException {
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        g2.setColor(new Color(0.4f, 0.6f, 0.8f));
        g2.fillRect(0.0f, 0.0f, 1024.0f, 568.0f);
        g2.setColor(Color.white);
        g2.drawRect(5.0f, 5.0f, 1014.0f, 558.0f);
        g2.setColor(Color.white);
        g2.drawString(container.getInput().getMouseX() + "," + container.getInput().getMouseY(), 10.0f, 400.0f);
        g2.setColor(Color.red);
        g2.fillOval(container.getInput().getMouseX() - 10, container.getInput().getMouseY() - 10, 20.0f, 20.0f);
    }

    public static void main(String[] argv) {
        try {
            ScalableGame game = new ScalableGame(new ScalableTest(), 1024, 568, true){

                protected void renderOverlay(GameContainer container, Graphics g2) {
                    g2.setColor(Color.white);
                    g2.drawString("Outside The Game", 350.0f, 10.0f);
                    g2.drawString(container.getInput().getMouseX() + "," + container.getInput().getMouseY(), 400.0f, 20.0f);
                }
            };
            AppGameContainer container = new AppGameContainer(game);
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

