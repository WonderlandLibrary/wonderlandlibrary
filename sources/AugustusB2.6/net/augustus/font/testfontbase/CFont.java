// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.font.testfontbase;

import org.lwjgl.opengl.GL11;
import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.Font;

public class CFont
{
    float imgSize;
    CharData[] charData;
    Font font;
    boolean antiAlias;
    boolean fractionalMetrics;
    int fontHeight;
    int charOffset;
    DynamicTexture tex;
    
    public CFont(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        this.imgSize = 512.0f;
        this.charData = new CharData[256];
        this.fontHeight = -1;
        this.charOffset = 0;
        this.font = font;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.tex = this.setupTexture(font, antiAlias, fractionalMetrics, this.charData);
    }
    
    protected DynamicTexture setupTexture(final Font font, final boolean antiAlias, final boolean fractionalMetrics, final CharData[] chars) {
        final BufferedImage img = this.generateFontImage(font, antiAlias, fractionalMetrics, chars);
        try {
            return new DynamicTexture(img);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected BufferedImage generateFontImage(final Font font, final boolean antiAlias, final boolean fractionalMetrics, final CharData[] chars) {
        final int imgSize = (int)this.imgSize;
        final BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
        final Graphics2D graphics = (Graphics2D)bufferedImage.getGraphics();
        graphics.setFont(font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, imgSize, imgSize);
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        final FontMetrics fontMetrics = graphics.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        for (int index = 0; index < chars.length; ++index) {
            final char c = (char)index;
            final CharData charData = new CharData();
            final Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(c), graphics);
            charData.width = dimensions.getBounds().width + 8;
            charData.height = dimensions.getBounds().height;
            if (positionX + charData.width >= imgSize) {
                positionX = 0;
                positionY += charHeight;
                charHeight = 0;
            }
            if (charData.height > charHeight) {
                charHeight = charData.height;
            }
            charData.storedX = positionX;
            charData.storedY = positionY;
            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height;
            }
            chars[index] = charData;
            graphics.drawString(String.valueOf(c), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width;
        }
        return bufferedImage;
    }
    
    public void drawChar(final CharData[] chars, final char c, final float x, final float y) throws ArrayIndexOutOfBoundsException {
        try {
            this.drawQuad(x, y, (float)chars[c].width, (float)chars[c].height, (float)chars[c].storedX, (float)chars[c].storedY, (float)chars[c].width, (float)chars[c].height);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void drawQuad(final float x2, final float y2, final float width, final float height, final float srcX, final float srcY, final float srcWidth, final float srcHeight) {
        final float renderSRCX = srcX / this.imgSize;
        final float renderSRCY = srcY / this.imgSize;
        final float renderSRCWidth = srcWidth / this.imgSize;
        final float renderSRCHeight = srcHeight / this.imgSize;
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x2 + width, y2);
        GL11.glTexCoord2f(renderSRCX, renderSRCY);
        GL11.glVertex2d(x2, y2);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x2, y2 + height);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x2, y2 + height);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x2 + width, y2 + height);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x2 + width, y2);
    }
    
    public void setAntiAlias(final boolean antiAlias) {
        if (this.antiAlias != antiAlias) {
            this.antiAlias = antiAlias;
            this.tex = this.setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
        }
    }
    
    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }
    
    public void setFractionalMetrics(final boolean fractionalMetrics) {
        if (this.fractionalMetrics != fractionalMetrics) {
            this.fractionalMetrics = fractionalMetrics;
            this.tex = this.setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
        }
    }
    
    public void setFont(final Font font) {
        this.font = font;
        this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }
    
    protected static class CharData
    {
        public int width;
        public int height;
        public int storedX;
        public int storedY;
    }
}
