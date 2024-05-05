/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AngelCodeFont;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import org.lwjgl.opengl.GL11;

public class DistanceFieldTest
extends BasicGame {
    private AngelCodeFont font;

    public DistanceFieldTest() {
        super("DistanceMapTest Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.font = new AngelCodeFont("testdata/distance.fnt", "testdata/distance-dis.png");
        container.getGraphics().setBackground(Color.black);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        String text = "abc";
        this.font.drawString(610.0f, 100.0f, text);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glAlphaFunc((int)518, (float)0.5f);
        this.font.drawString(610.0f, 150.0f, text);
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        g.translate(-50.0f, -130.0f);
        g.scale(10.0f, 10.0f);
        this.font.drawString(0.0f, 0.0f, text);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glAlphaFunc((int)518, (float)0.5f);
        this.font.drawString(0.0f, 26.0f, text);
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        g.resetTransform();
        g.setColor(Color.lightGray);
        g.drawString("Original Size on Sheet", 620.0f, 210.0f);
        g.drawString("10x Scale Up", 40.0f, 575.0f);
        g.setColor(Color.darkGray);
        g.drawRect(40.0f, 40.0f, 560.0f, 530.0f);
        g.drawRect(610.0f, 105.0f, 150.0f, 100.0f);
        g.setColor(Color.white);
        g.drawString("512x512 Font Sheet", 620.0f, 300.0f);
        g.drawString("NEHE Charset", 620.0f, 320.0f);
        g.drawString("4096x4096 (8x) Source Image", 620.0f, 340.0f);
        g.drawString("ScanSize = 20", 620.0f, 360.0f);
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new DistanceFieldTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

