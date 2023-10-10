/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick;

import java.io.UnsupportedEncodingException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

public class SpriteSheetFont
implements Font {
    private SpriteSheet font;
    private char startingCharacter;
    private int charWidth;
    private int charHeight;
    private int horizontalCount;
    private int numChars;

    public SpriteSheetFont(SpriteSheet font, char startingCharacter) {
        this.font = font;
        this.startingCharacter = startingCharacter;
        this.horizontalCount = font.getHorizontalCount();
        int verticalCount = font.getVerticalCount();
        this.charWidth = font.getWidth() / this.horizontalCount;
        this.charHeight = font.getHeight() / verticalCount;
        this.numChars = this.horizontalCount * verticalCount;
    }

    public void drawString(float x2, float y2, String text) {
        this.drawString(x2, y2, text, Color.white);
    }

    public void drawString(float x2, float y2, String text, Color col) {
        this.drawString(x2, y2, text, col, 0, text.length() - 1);
    }

    public void drawString(float x2, float y2, String text, Color col, int startIndex, int endIndex) {
        try {
            byte[] data = text.getBytes("US-ASCII");
            for (int i2 = 0; i2 < data.length; ++i2) {
                int index = data[i2] - this.startingCharacter;
                if (index >= this.numChars) continue;
                int xPos = index % this.horizontalCount;
                int yPos = index / this.horizontalCount;
                if (i2 < startIndex && i2 > endIndex) continue;
                this.font.getSprite(xPos, yPos).draw(x2 + (float)(i2 * this.charWidth), y2, col);
            }
        }
        catch (UnsupportedEncodingException e2) {
            Log.error(e2);
        }
    }

    public int getHeight(String text) {
        return this.charHeight;
    }

    public int getWidth(String text) {
        return this.charWidth * text.length();
    }

    public int getLineHeight() {
        return this.charHeight;
    }
}

