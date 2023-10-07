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
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CanvasContainerTest
extends BasicGame {
    private Image tga;
    private Image scaleMe;
    private Image scaled;
    private Image gif;
    private Image image;
    private Image subImage;
    private float rot;

    public CanvasContainerTest() {
        super("Canvas Container Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.image = this.tga = new Image("testdata/logo.tga");
        this.scaleMe = new Image("testdata/logo.tga", true, 2);
        this.gif = new Image("testdata/logo.gif");
        this.scaled = this.gif.getScaledCopy(120, 120);
        this.subImage = this.image.getSubImage(200, 0, 70, 260);
        this.rot = 0.0f;
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        this.image.draw(0.0f, 0.0f);
        this.image.draw(500.0f, 0.0f, 200.0f, 100.0f);
        this.scaleMe.draw(500.0f, 100.0f, 200.0f, 100.0f);
        this.scaled.draw(400.0f, 500.0f);
        Image flipped = this.scaled.getFlippedCopy(true, false);
        flipped.draw(520.0f, 500.0f);
        Image flipped2 = flipped.getFlippedCopy(false, true);
        flipped2.draw(520.0f, 380.0f);
        Image flipped3 = flipped2.getFlippedCopy(true, false);
        flipped3.draw(400.0f, 380.0f);
        for (int i = 0; i < 3; ++i) {
            this.subImage.draw(200 + i * 30, 300.0f);
        }
        g.translate(500.0f, 200.0f);
        g.rotate(50.0f, 50.0f, this.rot);
        g.scale(0.3f, 0.3f);
        this.image.draw();
        g.resetTransform();
    }

    @Override
    public void update(GameContainer container, int delta) {
        this.rot += (float)delta * 0.1f;
        if (this.rot > 360.0f) {
            this.rot -= 360.0f;
        }
    }

    public static void main(String[] argv) {
        try {
            CanvasGameContainer container = new CanvasGameContainer(new CanvasContainerTest());
            Frame frame = new Frame("Test");
            frame.setLayout(new GridLayout(1, 2));
            frame.setSize(500, 500);
            frame.add(container);
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
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 57) {
            this.image = this.image == this.gif ? this.tga : this.gif;
        }
    }
}

