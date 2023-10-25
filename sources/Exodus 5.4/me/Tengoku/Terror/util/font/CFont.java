/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFont {
    float imgSize = 512.0f;
    int fontHeight = -1;
    boolean fractionalMetrics;
    CharData[] charData = new CharData[256];
    DynamicTexture tex;
    int charOffset = 0;
    Font font;
    boolean antiAlias;

    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }

    public void drawChar(CharData[] charDataArray, char c, float f, float f2) throws ArrayIndexOutOfBoundsException {
        try {
            this.drawQuad(f, f2, charDataArray[c].width, charDataArray[c].height, charDataArray[c].storedX, charDataArray[c].storedY, charDataArray[c].width, charDataArray[c].height);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected BufferedImage generateFontImage(Font font, boolean bl, boolean bl2, CharData[] charDataArray) {
        int n = (int)this.imgSize;
        BufferedImage bufferedImage = new BufferedImage(n, n, 2);
        Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
        graphics2D.setFont(font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, n, n);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, bl2 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, bl ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, bl ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int n2 = 0;
        int n3 = 0;
        int n4 = 1;
        int n5 = 0;
        while (n5 < charDataArray.length) {
            char c = (char)n5;
            CharData charData = new CharData();
            Rectangle2D rectangle2D = fontMetrics.getStringBounds(String.valueOf(c), graphics2D);
            charData.width = rectangle2D.getBounds().width + 8;
            charData.height = rectangle2D.getBounds().height;
            if (n3 + charData.width >= n) {
                n3 = 0;
                n4 += n2;
                n2 = 0;
            }
            if (charData.height > n2) {
                n2 = charData.height;
            }
            charData.storedX = n3;
            charData.storedY = n4;
            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height;
            }
            charDataArray[n5] = charData;
            graphics2D.drawString(String.valueOf(c), n3 + 2, n4 + fontMetrics.getAscent());
            n3 += charData.width;
            ++n5;
        }
        return bufferedImage;
    }

    protected void drawQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = f5 / this.imgSize;
        float f10 = f6 / this.imgSize;
        float f11 = f7 / this.imgSize;
        float f12 = f8 / this.imgSize;
        GL11.glTexCoord2f((float)(f9 + f11), (float)f10);
        GL11.glVertex2d((double)(f + f3), (double)f2);
        GL11.glTexCoord2f((float)f9, (float)f10);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glTexCoord2f((float)f9, (float)(f10 + f12));
        GL11.glVertex2d((double)f, (double)(f2 + f4));
        GL11.glTexCoord2f((float)f9, (float)(f10 + f12));
        GL11.glVertex2d((double)f, (double)(f2 + f4));
        GL11.glTexCoord2f((float)(f9 + f11), (float)(f10 + f12));
        GL11.glVertex2d((double)(f + f3), (double)(f2 + f4));
        GL11.glTexCoord2f((float)(f9 + f11), (float)f10);
        GL11.glVertex2d((double)(f + f3), (double)f2);
    }

    public void setAntiAlias(boolean bl) {
        if (this.antiAlias != bl) {
            this.antiAlias = bl;
            this.tex = this.setupTexture(this.font, bl, this.fractionalMetrics, this.charData);
        }
    }

    public void setFont(Font font) {
        this.font = font;
        this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }

    public CFont(Font font, boolean bl, boolean bl2) {
        this.font = font;
        this.antiAlias = bl;
        this.fractionalMetrics = bl2;
        this.tex = this.setupTexture(font, bl, bl2, this.charData);
    }

    protected DynamicTexture setupTexture(Font font, boolean bl, boolean bl2, CharData[] charDataArray) {
        BufferedImage bufferedImage = this.generateFontImage(font, bl, bl2, charDataArray);
        try {
            return new DynamicTexture(bufferedImage);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void setFractionalMetrics(boolean bl) {
        if (this.fractionalMetrics != bl) {
            this.fractionalMetrics = bl;
            this.tex = this.setupTexture(this.font, this.antiAlias, bl, this.charData);
        }
    }

    protected static class CharData {
        public int storedX;
        public int width;
        public int height;
        public int storedY;

        protected CharData() {
        }
    }
}

