/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.CanvasGameContainer;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.util.Log;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CanvasSizeTest
extends BasicGame {
    public CanvasSizeTest() {
        super("Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        System.out.println(container.getWidth() + ", " + container.getHeight());
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    public static void main(String[] args2) {
        try {
            CanvasGameContainer container = new CanvasGameContainer(new CanvasSizeTest());
            container.setSize(640, 480);
            Frame frame = new Frame("Test");
            frame.setLayout(new GridLayout(1, 2));
            frame.add(container);
            frame.pack();
            frame.addWindowListener(new WindowAdapter(){

                @Override
                public void windowClosing(WindowEvent e) {
                    FMLCommonHandler.instance().exitJava(0, true);
                }
            });
            frame.setVisible(true);
            container.start();
        }
        catch (Exception e) {
            Log.error(e);
        }
    }
}

