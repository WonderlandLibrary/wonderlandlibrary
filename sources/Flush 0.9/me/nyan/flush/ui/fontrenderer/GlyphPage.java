/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.nyan.flush.ui.fontrenderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GlyphPage {
    private int imgSize;
    private int maxFontHeight = -1;
    private final Font font;
    private final boolean antiAliasing;
    private final boolean fractionalMetrics;
    private final HashMap<Character, Glyph> glyphCharacterMap = new HashMap<>();

    private BufferedImage bufferedImage;
    private DynamicTexture loadedTexture;

    public GlyphPage(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        this.font = font;
        this.antiAliasing = antiAliasing;
        this.fractionalMetrics = fractionalMetrics;
    }

    public void generateGlyphPage(char[] chars) {
        double maxWidth = -1;
        double maxHeight = -1;

        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, antiAliasing, fractionalMetrics);

        for (char ch : chars) {
            Rectangle2D bounds = font.getStringBounds(Character.toString(ch), fontRenderContext);

            if (maxWidth < bounds.getWidth()) {
                maxWidth = bounds.getWidth();
            }

            if (maxHeight < bounds.getHeight()) {
                maxHeight = bounds.getHeight();
            }
        }

        maxWidth += 2;
        maxHeight += 2;

        imgSize = (int) Math.ceil(Math.max(
                Math.ceil(Math.sqrt(maxWidth * maxWidth * chars.length) / maxWidth),
                Math.ceil(Math.sqrt(maxHeight * maxHeight * chars.length) / maxHeight))
                * Math.max(maxWidth, maxHeight)) + 1;

        bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

        g.setFont(font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);
        g.setColor(Color.white);

        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        FontMetrics fontMetrics = g.getFontMetrics();

        int currentCharHeight = 0;
        int posX = 0;
        int posY = 1;

        for (char ch : chars) {
            Glyph glyph = new Glyph();

            Rectangle2D bounds = fontMetrics.getStringBounds(Character.toString(ch), g);

            glyph.width = bounds.getBounds().width + 8;
            glyph.height = bounds.getBounds().height;

            if (posY + glyph.height >= imgSize) {
                throw new IllegalStateException("Not all characters will fit");
            }
            if (posX + glyph.width >= imgSize) {
                posX = 0;
                posY += currentCharHeight;
                currentCharHeight = 0;
            }

            glyph.x = posX;
            glyph.y = posY;

            if (glyph.height > maxFontHeight) {
                maxFontHeight = glyph.height;
            }
            if (glyph.height > currentCharHeight) {
                currentCharHeight = glyph.height;
            }

            g.drawString(Character.toString(ch), posX + 2, posY + fontMetrics.getAscent());

            posX += glyph.width;

            glyphCharacterMap.put(ch, glyph);
        }
    }

    public void setupTexture() {
        loadedTexture = new DynamicTexture(bufferedImage);
    }

    public void bindTexture() {
        if (loadedTexture == null) {
            setupTexture();
        }

        GlStateManager.bindTexture(loadedTexture.getGlTextureId());
    }

    public void unbindTexture() {
        GlStateManager.bindTexture(0);
    }

    public float drawChar(char ch, float x, float y) {
        Glyph glyph = glyphCharacterMap.get(ch);

        if (glyph == null) {
            return 0;
        } else {
            float pageX = glyph.x / (float) imgSize;
            float pageY = glyph.y / (float) imgSize;

            float pageWidth = glyph.width / (float) imgSize;
            float pageHeight = glyph.height / (float) imgSize;

            float width = glyph.width;
            float height = glyph.height;

            GL11.glBegin(GL11.GL_TRIANGLES);

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

            return width - 8;
        }
    }

    public float getWidth(char ch) {
        return glyphCharacterMap.get(ch) == null ? 0 : glyphCharacterMap.get(ch).width;
    }

    public int getMaxFontHeight() {
        return maxFontHeight;
    }

    static class Glyph {
        private int x;
        private int y;
        private int width;
        private int height;

        Glyph() {
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}