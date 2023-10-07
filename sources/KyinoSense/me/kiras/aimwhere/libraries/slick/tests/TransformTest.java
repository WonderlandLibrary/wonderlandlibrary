/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TransformTest
extends BasicGame {
    private float scale = 1.0f;
    private boolean scaleUp;
    private boolean scaleDown;

    public TransformTest() {
        super("Transform Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.setTargetFrameRate(100);
    }

    @Override
    public void render(GameContainer contiainer, Graphics g) {
        g.translate(320.0f, 240.0f);
        g.scale(this.scale, this.scale);
        g.setColor(Color.red);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                g.fillRect(-500 + x * 100, -500 + y * 100, 80.0f, 80.0f);
            }
        }
        g.setColor(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        g.fillRect(-320.0f, -240.0f, 640.0f, 480.0f);
        g.setColor(Color.white);
        g.drawRect(-320.0f, -240.0f, 640.0f, 480.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
        if (this.scaleUp) {
            this.scale += (float)delta * 0.001f;
        }
        if (this.scaleDown) {
            this.scale -= (float)delta * 0.001f;
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
        }
        if (key == 16) {
            this.scaleUp = true;
        }
        if (key == 30) {
            this.scaleDown = true;
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == 16) {
            this.scaleUp = false;
        }
        if (key == 30) {
            this.scaleDown = false;
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new TransformTest());
            container.setDisplayMode(640, 480, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

