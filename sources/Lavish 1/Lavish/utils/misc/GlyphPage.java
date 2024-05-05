// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.misc;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Font;

public class GlyphPage
{
    private int imgSize;
    private int maxFontHeight;
    private Font font;
    private boolean antiAliasing;
    private boolean fractionalMetrics;
    private HashMap<Character, Glyph> glyphCharacterMap;
    private BufferedImage bufferedImage;
    private DynamicTexture loadedTexture;
    
    public GlyphPage(final Font font, final boolean antiAliasing, final boolean fractionalMetrics) {
        this.maxFontHeight = -1;
        this.glyphCharacterMap = new HashMap<Character, Glyph>();
        this.font = font;
        this.antiAliasing = antiAliasing;
        this.fractionalMetrics = fractionalMetrics;
    }
    
    public void generateGlyphPage(final char[] chars) {
        double maxWidth = -1.0;
        double maxHeight = -1.0;
        final AffineTransform affineTransform = new AffineTransform();
        final FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, this.antiAliasing, this.fractionalMetrics);
        for (final char ch : chars) {
            final Rectangle2D bounds = this.font.getStringBounds(Character.toString(ch), fontRenderContext);
            if (maxWidth < bounds.getWidth()) {
                maxWidth = bounds.getWidth();
            }
            if (maxHeight < bounds.getHeight()) {
                maxHeight = bounds.getHeight();
            }
        }
        maxWidth += 2.0;
        maxHeight += 2.0;
        this.imgSize = (int)Math.ceil(Math.max(Math.ceil(Math.sqrt(maxWidth * maxWidth * chars.length) / maxWidth), Math.ceil(Math.sqrt(maxHeight * maxHeight * chars.length) / maxHeight)) * Math.max(maxWidth, maxHeight)) + 1;
        this.bufferedImage = new BufferedImage(this.imgSize, this.imgSize, 2);
        final Graphics2D g = (Graphics2D)this.bufferedImage.getGraphics();
        g.setFont(this.font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, this.imgSize, this.imgSize);
        g.setColor(Color.white);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        final FontMetrics fontMetrics = g.getFontMetrics();
        int currentCharHeight = 0;
        int posX = 0;
        int posY = 1;
        for (final char ch2 : chars) {
            final Glyph glyph = new Glyph();
            final Rectangle2D bounds2 = fontMetrics.getStringBounds(Character.toString(ch2), g);
            Glyph.access$0(glyph, bounds2.getBounds().width + 8);
            Glyph.access$1(glyph, bounds2.getBounds().height);
            if (posY + glyph.height >= this.imgSize) {
                throw new IllegalStateException("Not all characters will fit");
            }
            if (posX + glyph.width >= this.imgSize) {
                posX = 0;
                posY += currentCharHeight;
                currentCharHeight = 0;
            }
            Glyph.access$4(glyph, posX);
            Glyph.access$5(glyph, posY);
            if (glyph.height > this.maxFontHeight) {
                this.maxFontHeight = glyph.height;
            }
            if (glyph.height > currentCharHeight) {
                currentCharHeight = glyph.height;
            }
            g.drawString(Character.toString(ch2), posX + 2, posY + fontMetrics.getAscent());
            posX += glyph.width;
            this.glyphCharacterMap.put(ch2, glyph);
        }
    }
    
    public void setupTexture() {
        this.loadedTexture = new DynamicTexture(this.bufferedImage);
    }
    
    public void bindTexture() {
        GlStateManager.bindTexture(this.loadedTexture.getGlTextureId());
    }
    
    public void unbindTexture() {
        GlStateManager.bindTexture(0);
    }
    
    public float drawChar(final char ch, final float x, final float y) {
        final Glyph glyph = this.glyphCharacterMap.get(ch);
        if (glyph == null) {
            throw new IllegalArgumentException("'" + ch + "' wasn't found");
        }
        final float pageX = glyph.x / (float)this.imgSize;
        final float pageY = glyph.y / (float)this.imgSize;
        final float pageWidth = glyph.width / (float)this.imgSize;
        final float pageHeight = glyph.height / (float)this.imgSize;
        final float width = (float)glyph.width;
        final float height = (float)glyph.height;
        GL11.glBegin(4);
        GL11.glTexCoord2f(pageX + pageWidth, pageY);
        GL11.glVertex2f(x + width, y);
        GL11.glTexCoord2f(pageX, pageY);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(pageX, pageY + pageHeight);
        GL11.glVertex2f(x, y + height);
        GL11.glTexCoord2f(pageX, pageY + pageHeight);
        GL11.glVertex2f(x, y + height);
        GL11.glTexCoord2f(pageX + pageWidth, pageY + pageHeight);
        GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(pageX + pageWidth, pageY);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        return width - 8.0f;
    }
    
    public float getWidth(final char ch) {
        return (float)this.glyphCharacterMap.get(ch).width;
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
    
    static class Glyph
    {
        private int x;
        private int y;
        private int width;
        private int height;
        
        Glyph(final int x, final int y, final int width, final int height) {
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
        
        static /* synthetic */ void access$0(final Glyph glyph, final int width) {
            glyph.width = width;
        }
        
        static /* synthetic */ void access$1(final Glyph glyph, final int height) {
            glyph.height = height;
        }
        
        static /* synthetic */ void access$4(final Glyph glyph, final int x) {
            glyph.x = x;
        }
        
        static /* synthetic */ void access$5(final Glyph glyph, final int y) {
            glyph.y = y;
        }
    }
}
