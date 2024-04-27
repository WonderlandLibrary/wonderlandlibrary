package me.yarukon.font;

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
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public class GlyphPage {
    private int imgSize;
    private int maxFontHeight = -1;
    private Font font;
    private boolean antiAliasing;
    private boolean fractionalMetrics;
    private HashMap<Character, Glyph> glyphCharacterMap = new HashMap();
    public int texID;
    private BufferedImage bufferedImage;

    public GlyphPage(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        this.font = font;
        this.antiAliasing = antiAliasing;
        this.fractionalMetrics = fractionalMetrics;
    }

    public void generateGlyphPage(char[] chars, boolean allChars) {
        double maxWidth = -1.0;
        double maxHeight = -1.0;
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, this.antiAliasing, this.fractionalMetrics);
        if (allChars) {
            this.imgSize = 8192;
        } else {
            for (char ch : chars) {
                Rectangle2D bounds = this.font.getStringBounds(Character.toString(ch), fontRenderContext);
                if (maxWidth < bounds.getWidth()) {
                    maxWidth = bounds.getWidth();
                }
                if (!(maxHeight < bounds.getHeight())) continue;
                maxHeight = bounds.getHeight();
            }
            this.imgSize = (int)Math.ceil(Math.max(Math.ceil(Math.sqrt((maxWidth += 2.0) * maxWidth * (double)chars.length) / maxWidth), Math.ceil(Math.sqrt((maxHeight += 2.0) * maxHeight * (double)chars.length) / maxHeight)) * Math.max(maxWidth, maxHeight)) + 1;
        }
        this.bufferedImage = new BufferedImage(this.imgSize, this.imgSize, 2);
        Graphics2D g = (Graphics2D)this.bufferedImage.getGraphics();
        g.setFont(this.font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, this.imgSize, this.imgSize);
        g.setColor(Color.white);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        FontMetrics fontMetrics = g.getFontMetrics();
        int currentCharHeight = 0;
        int posX = 0;
        int posY = 1;
        for (char ch : chars) {
            Glyph glyph = new Glyph();
            Rectangle2D bounds = fontMetrics.getStringBounds(Character.toString(ch), g);
            glyph.width = bounds.getBounds().width + 8;
            glyph.height = bounds.getBounds().height;
            if (posX + glyph.width >= this.imgSize) {
                posX = 0;
                posY += currentCharHeight;
                currentCharHeight = 0;
            }
            glyph.x = posX;
            glyph.y = posY;
            if (glyph.height > this.maxFontHeight) {
                this.maxFontHeight = glyph.height;
            }
            if (glyph.height > currentCharHeight) {
                currentCharHeight = glyph.height;
            }
            g.drawString(Character.toString(ch), posX + 2, posY + fontMetrics.getAscent());
            posX += glyph.width;
            this.glyphCharacterMap.put(Character.valueOf(ch), glyph);
        }
        try {
            this.texID = TextureUtil.uploadTextureImageAllocate((int)TextureUtil.glGenTextures(), (BufferedImage)this.bufferedImage, (boolean)true, (!allChars ? 1 : 0) != 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void bindTexture() {
        GlStateManager.bindTexture((int)this.texID);
    }

    public void unbindTexture() {
        GlStateManager.bindTexture((int)0);
    }

    public float drawChar(char ch, float x, float y) {
        Glyph glyph = this.glyphCharacterMap.get(Character.valueOf(ch));
        if (glyph == null) {
            throw new IllegalArgumentException("'" + ch + "' wasn't found");
        }
        float pageX = (float)glyph.x / (float)this.imgSize;
        float pageY = (float)glyph.y / (float)this.imgSize;
        float pageWidth = (float)glyph.width / (float)this.imgSize;
        float pageHeight = (float)glyph.height / (float)this.imgSize;
        float width = glyph.width;
        float height = glyph.height;
        GL11.glBegin((int)4);
        GL11.glTexCoord2f((float)(pageX + pageWidth), (float)pageY);
        GL11.glVertex2f((float)(x + width), (float)y);
        GL11.glTexCoord2f((float)pageX, (float)pageY);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glTexCoord2f((float)pageX, (float)(pageY + pageHeight));
        GL11.glVertex2f((float)x, (float)(y + height));
        GL11.glTexCoord2f((float)pageX, (float)(pageY + pageHeight));
        GL11.glVertex2f((float)x, (float)(y + height));
        GL11.glTexCoord2f((float)(pageX + pageWidth), (float)(pageY + pageHeight));
        GL11.glVertex2f((float)(x + width), (float)(y + height));
        GL11.glTexCoord2f((float)(pageX + pageWidth), (float)pageY);
        GL11.glVertex2f((float)(x + width), (float)y);
        GL11.glEnd();
        return width - 8.0f;
    }

    public float getWidth(char ch) {
        return this.glyphCharacterMap.get(Character.valueOf(ch)).width;
    }

    public int getMaxFontHeight() {
        return this.maxFontHeight;
    }

    public boolean isAntiAliasingEnabled() {
        return this.antiAliasing;
    }

    public boolean isFractionalMetricsEnabled() {
        return this.fractionalMetrics;
    }

    static class Glyph {
        private int x;
        private int y;
        private int width;
        private int height;

        Glyph(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        Glyph() {
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
