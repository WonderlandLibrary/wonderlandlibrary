/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class UnicodeFontTest
extends BasicGame {
    private UnicodeFont unicodeFont;

    public UnicodeFontTest() {
        super("Font Test");
    }

    public void init(GameContainer container) throws SlickException {
        container.setShowFPS(false);
        this.unicodeFont = new UnicodeFont("c:/windows/fonts/arial.ttf", 48, false, false);
        this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.white));
        container.getGraphics().setBackground(Color.darkGray);
    }

    public void render(GameContainer container, Graphics g2) {
        g2.setColor(Color.white);
        String text = "This is UnicodeFont!\nIt rockz. Kerning: T,";
        this.unicodeFont.drawString(10.0f, 33.0f, text);
        g2.setColor(Color.red);
        g2.drawRect(10.0f, 33.0f, this.unicodeFont.getWidth(text), this.unicodeFont.getLineHeight());
        g2.setColor(Color.blue);
        int yOffset = this.unicodeFont.getYOffset(text);
        g2.drawRect(10.0f, 33 + yOffset, this.unicodeFont.getWidth(text), this.unicodeFont.getHeight(text) - yOffset);
        this.unicodeFont.addGlyphs("~!@!#!#$%___--");
    }

    public void update(GameContainer container, int delta) throws SlickException {
        this.unicodeFont.loadGlyphs(1);
    }

    public static void main(String[] args) throws SlickException, IOException {
        Input.disableControllers();
        AppGameContainer container = new AppGameContainer(new UnicodeFontTest());
        container.setDisplayMode(512, 600, false);
        container.setTargetFrameRate(20);
        container.start();
    }
}

