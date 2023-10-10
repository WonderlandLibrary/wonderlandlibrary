/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class CanvasSizeTest
extends BasicGame {
    public CanvasSizeTest() {
        super("Test");
    }

    public void init(GameContainer container) throws SlickException {
        System.out.println(container.getWidth() + ", " + container.getHeight());
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public static void main(String[] args) {
        try {
            CanvasGameContainer container = new CanvasGameContainer(new CanvasSizeTest());
            container.setSize(640, 480);
            Frame frame = new Frame("Test");
            frame.setLayout(new GridLayout(1, 2));
            frame.add(container);
            frame.pack();
            frame.addWindowListener(new WindowAdapter(){

                public void windowClosing(WindowEvent e2) {
                    System.exit(0);
                }
            });
            frame.setVisible(true);
            container.start();
        }
        catch (Exception e2) {
            Log.error(e2);
        }
    }
}

