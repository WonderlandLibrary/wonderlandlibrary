// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.font.test;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.InputStream;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.Font;
import net.augustus.font.testfontbase.FontUtil;
import java.util.HashMap;
import java.util.Map;

public class CFont
{
    private String filePath;
    private int fontSize;
    private int width;
    private int height;
    private int lineHeight;
    private Map<Integer, CharInfo> characterMap;
    
    public CFont(final String filepath, final int fontSize) {
        this.filePath = filepath;
        this.fontSize = fontSize;
        this.characterMap = new HashMap<Integer, CharInfo>();
        this.generateBitMap();
    }
    
    public void generateBitMap() {
        final InputStream is = FontUtil.class.getClassLoader().getResourceAsStream(this.filePath);
        Font font = null;
        try {
            font = Font.createFont(0, is);
            font = font.deriveFont(0, 64.0f);
        }
        catch (FontFormatException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        BufferedImage img = new BufferedImage(1, 1, 4);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        final FontMetrics fontMetrics = g2d.getFontMetrics();
        final int estimatedWith = (int)Math.sqrt(font.getNumGlyphs()) * font.getSize() + 1;
        this.width = 0;
        this.height = fontMetrics.getHeight();
        this.lineHeight = fontMetrics.getHeight();
        int x = 0;
        int y = (int)(fontMetrics.getHeight() * 1.4f);
        for (int i = 0; i < font.getNumGlyphs(); ++i) {
            if (font.canDisplay(i)) {
                final CharInfo charInfo = new CharInfo(x, y, fontMetrics.charWidth(i), fontMetrics.getHeight());
                this.characterMap.put(i, charInfo);
                this.width = Math.max(x + fontMetrics.charWidth(i), this.width);
                x += charInfo.width;
                if (x > estimatedWith) {
                    x = 0;
                    y += (int)(fontMetrics.getHeight() * 1.4f);
                    this.height += (int)(fontMetrics.getHeight() * 1.4f);
                }
            }
        }
        this.height += (int)(fontMetrics.getHeight() * 1.4f);
        g2d.dispose();
        img = new BufferedImage(this.width, this.height, 4);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setColor(Color.white);
        for (int i = 0; i < font.getNumGlyphs(); ++i) {
            if (font.canDisplay(i)) {
                final CharInfo info = this.characterMap.get(i);
                info.calculateTextureCoordinates(this.width, this.height);
                g2d.drawString("" + (char)i, info.sourceX, info.sourceY);
            }
        }
        g2d.dispose();
        try {
            final File file = new File("tmp.png");
            ImageIO.write(img, "png", file);
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
    }
}
