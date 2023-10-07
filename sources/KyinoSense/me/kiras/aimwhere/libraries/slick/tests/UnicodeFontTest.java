/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.io.IOException;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Input;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.UnicodeFont;
import me.kiras.aimwhere.libraries.slick.font.effects.ColorEffect;

public class UnicodeFontTest
extends BasicGame {
    private UnicodeFont unicodeFont;

    public UnicodeFontTest() {
        super("Font Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.setShowFPS(false);
        this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.white));
        container.getGraphics().setBackground(Color.darkGray);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.setColor(Color.white);
        String text = "This is UnicodeFont!\nIt rockz. Kerning: T,";
        this.unicodeFont.drawString(10.0f, 33.0f, text);
        g.setColor(Color.red);
        g.drawRect(10.0f, 33.0f, this.unicodeFont.getWidth(text), this.unicodeFont.getLineHeight());
        g.setColor(Color.blue);
        int yOffset = this.unicodeFont.getYOffset(text);
        g.drawRect(10.0f, 33 + yOffset, this.unicodeFont.getWidth(text), this.unicodeFont.getHeight(text) - yOffset);
        this.unicodeFont.addGlyphs("~!@!#!#$%___--");
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        this.unicodeFont.loadGlyphs(1);
    }

    public static void main(String[] args2) throws SlickException, IOException {
        Input.disableControllers();
        AppGameContainer container = new AppGameContainer(new UnicodeFontTest());
        container.setDisplayMode(512, 600, false);
        container.setTargetFrameRate(20);
        container.start();
    }
}

