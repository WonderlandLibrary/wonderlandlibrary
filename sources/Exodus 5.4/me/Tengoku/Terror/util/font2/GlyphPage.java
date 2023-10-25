/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util.font2;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class GlyphPage {
    private int imgSize;
    private BufferedImage bufferedImage;
    private Font font;
    private HashMap<Character, Glyph> glyphCharacterMap = new HashMap();
    private boolean antiAliasing;
    private DynamicTexture loadedTexture;
    private boolean fractionalMetrics;
    private int maxFontHeight = -1;

    public int getMaxFontHeight() {
        return this.maxFontHeight;
    }

    public GlyphPage(Font font, boolean bl, boolean bl2) {
        this.font = font;
        this.antiAliasing = bl;
        this.fractionalMetrics = bl2;
    }

    public void unbindTexture() {
        GlStateManager.bindTexture(0);
    }

    public void bindTexture() {
        GlStateManager.bindTexture(this.loadedTexture.getGlTextureId());
    }

    public float getWidth(char c) {
        return this.glyphCharacterMap.get(Character.valueOf(c)).width;
    }

    public void generateGlyphPage(char[] cArray) {
        double d = -1.0;
        double d2 = -1.0;
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, this.antiAliasing, this.fractionalMetrics);
        char[] cArray2 = cArray;
        int n = cArray.length;
        int n2 = 0;
        while (n2 < n) {
            char c = cArray2[n2];
            Rectangle2D rectangle2D = this.font.getStringBounds(Character.toString(c), fontRenderContext);
            if (d < rectangle2D.getWidth()) {
                d = rectangle2D.getWidth();
            }
            if (d2 < rectangle2D.getHeight()) {
                d2 = rectangle2D.getHeight();
            }
            ++n2;
        }
        this.imgSize = (int)Math.ceil(Math.max(Math.ceil(Math.sqrt((d += 2.0) * d * (double)cArray.length) / d), Math.ceil(Math.sqrt((d2 += 2.0) * d2 * (double)cArray.length) / d2)) * Math.max(d, d2)) + 1;
        this.bufferedImage = new BufferedImage(this.imgSize, this.imgSize, 2);
        Graphics2D graphics2D = (Graphics2D)this.bufferedImage.getGraphics();
        graphics2D.setFont(this.font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, this.imgSize, this.imgSize);
        graphics2D.setColor(Color.white);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        n = 0;
        int n3 = 0;
        int n4 = 1;
        char[] cArray3 = cArray;
        int n5 = cArray.length;
        int n6 = 0;
        while (n6 < n5) {
            char c = cArray3[n6];
            Glyph glyph = new Glyph();
            Rectangle2D rectangle2D = fontMetrics.getStringBounds(Character.toString(c), graphics2D);
            glyph.width = rectangle2D.getBounds().width + 8;
            glyph.height = rectangle2D.getBounds().height;
            if (n4 + glyph.height >= this.imgSize) {
                throw new IllegalStateException("Not all characters will fit");
            }
            if (n3 + glyph.width >= this.imgSize) {
                n3 = 0;
                n4 += n;
                n = 0;
            }
            glyph.x = n3;
            glyph.y = n4;
            if (glyph.height > this.maxFontHeight) {
                this.maxFontHeight = glyph.height;
            }
            if (glyph.height > n) {
                n = glyph.height;
            }
            graphics2D.drawString(Character.toString(c), n3 + 2, n4 + fontMetrics.getAscent());
            n3 += glyph.width;
            this.glyphCharacterMap.put(Character.valueOf(c), glyph);
            ++n6;
        }
    }

    public boolean isFractionalMetricsEnabled() {
        return this.fractionalMetrics;
    }

    public boolean isAntiAliasingEnabled() {
        return this.antiAliasing;
    }

    public void setupTexture() {
        this.loadedTexture = new DynamicTexture(this.bufferedImage);
    }

    public float drawChar(char c, float f, float f2) {
        Glyph glyph = this.glyphCharacterMap.get(Character.valueOf(c));
        if (glyph == null) {
            throw new IllegalArgumentException("'" + c + "' wasn't found");
        }
        float f3 = (float)glyph.x / (float)this.imgSize;
        float f4 = (float)glyph.y / (float)this.imgSize;
        float f5 = (float)glyph.width / (float)this.imgSize;
        float f6 = (float)glyph.height / (float)this.imgSize;
        float f7 = glyph.width;
        float f8 = glyph.height;
        GL11.glBegin((int)4);
        GL11.glTexCoord2f((float)(f3 + f5), (float)f4);
        GL11.glVertex2f((float)(f + f7), (float)f2);
        GL11.glTexCoord2f((float)f3, (float)f4);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glTexCoord2f((float)f3, (float)(f4 + f6));
        GL11.glVertex2f((float)f, (float)(f2 + f8));
        GL11.glTexCoord2f((float)f3, (float)(f4 + f6));
        GL11.glVertex2f((float)f, (float)(f2 + f8));
        GL11.glTexCoord2f((float)(f3 + f5), (float)(f4 + f6));
        GL11.glVertex2f((float)(f + f7), (float)(f2 + f8));
        GL11.glTexCoord2f((float)(f3 + f5), (float)f4);
        GL11.glVertex2f((float)(f + f7), (float)f2);
        GL11.glEnd();
        return f7 - 8.0f;
    }

    static class Glyph {
        private int height;
        private int y;
        private int width;
        private int x;

        public int getY() {
            return this.y;
        }

        Glyph(int n, int n2, int n3, int n4) {
            this.x = n;
            this.y = n2;
            this.width = n3;
            this.height = n4;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public int getX() {
            return this.x;
        }

        Glyph() {
        }
    }
}

