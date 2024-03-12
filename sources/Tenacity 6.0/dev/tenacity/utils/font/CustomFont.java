// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.font;

import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;
import java.awt.Graphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.RenderingHints;
import java.util.Locale;
import java.util.Collection;
import java.util.Arrays;
import dev.tenacity.utils.tuples.mutable.MutablePair;
import java.util.Iterator;
import dev.tenacity.utils.misc.MathUtils;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.GLUtil;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.module.impl.render.Streamer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;

public class CustomFont implements AbstractFontRenderer
{
    private static final char COLOR_INVOKER = '§';
    private static final int MARGIN = 4;
    private static int[] colorCode;
    private static final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    private final Font font;
    private CustomFont boldFont;
    private final FontData regular;
    private final FontData italic;
    private int fontHeight;
    private static final float KERNING = 8.2f;
    private CharacterData[] regularData;
    private CharacterData[] boldData;
    private CharacterData[] italicsData;
    private final List<String> lines;
    
    public CustomFont(final Font font) {
        this.regular = new FontData(0);
        this.italic = new FontData(2);
        this.lines = new ArrayList<String>();
        this.generateColorCodes();
        this.font = font;
        this.setupTexture(this.regular);
        this.setupTexture(this.italic);
    }
    
    private void setupTexture(final FontData fontData) {
        final BufferedImage fakeImage = new BufferedImage(1, 1, 2);
        final Graphics2D graphics = (Graphics2D)fakeImage.getGraphics();
        final Font currentFont = (fontData.textType == 0) ? this.font : this.font.deriveFont(fontData.textType);
        graphics.setFont(currentFont);
        this.handleSprites(fontData, currentFont, graphics);
    }
    
    public void drawSmoothString(final String text, final double x2, final float y2, final int color) {
        this.drawString(text, x2, y2, color, false, 8.2f, true);
    }
    
    public void drawSmoothStringWithShadow(final String text, final double x2, final float y2, final int color) {
        this.drawString(text, x2 + 0.5, y2 + 0.5f, color, true, 8.2f, true);
        this.drawString(text, x2, y2, color, false, 8.2f, true);
    }
    
    @Override
    public int drawCenteredString(final String name, final float x, final float y, final int color) {
        return this.drawString(name, x - this.getStringWidth(name) / 2.0f, y, color);
    }
    
    @Override
    public void drawCenteredString(final String name, final float x, final float y, final Color color) {
        this.drawCenteredString(name, x, y, color.getRGB());
    }
    
    public void drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
        this.drawStringWithShadow(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }
    
    @Override
    public int drawStringWithShadow(final String name, final float x, final float y, final int color) {
        this.drawString(name, x + 0.5f, y + 0.5f, color, true, 8.2f, false);
        return (int)this.drawString(name, x, y, color, false, 8.2f, false);
    }
    
    @Override
    public void drawStringWithShadow(final String name, final float x, final float y, final Color color) {
        this.drawStringWithShadow(name, x, y, color.getRGB());
    }
    
    @Override
    public int drawString(final String text, final float x, final float y, final int color, final boolean shadow) {
        if (shadow) {
            return this.drawStringWithShadow(text, x, y, color);
        }
        return (int)this.drawString(text, x, y, color, false, 8.2f, false);
    }
    
    @Override
    public int drawString(final String name, final float x, final float y, final int color) {
        return this.drawString(name, x, y, color, false);
    }
    
    @Override
    public void drawString(final String name, final float x, final float y, final Color color) {
        this.drawString(name, x, y, color.getRGB(), false);
    }
    
    public float drawString(String text, final double x, final double y, int color, final boolean shadow, final float kerning, final boolean smooth) {
        if (text == null) {
            return 0.0f;
        }
        text = Streamer.filter(text);
        if (shadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GLUtil.startBlend();
        RenderUtil.resetColor();
        RenderUtil.color(color);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.regular.texture.getGlTextureId());
        if (smooth) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }
        else {
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }
        final float returnVal = this.drawCustomChars(text, x, y, kerning, color, shadow);
        GL11.glHint(3155, 4352);
        GlStateManager.popMatrix();
        RenderUtil.resetColor();
        GlStateManager.bindTexture(0);
        return returnVal;
    }
    
    private float drawCustomChars(final String text, double x, double y, final float kerning, final int color, final boolean shadow) {
        x = (x - 1.0) * 2.0;
        y = (y - 3.0) * 2.0;
        FontData currentData = this.regular;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.bindTexture(this.regular.texture.getGlTextureId());
                    currentData = this.regular;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    RenderUtil.color(CustomFont.colorCode[colorIndex], alpha);
                }
                else {
                    switch (colorIndex) {
                        case 17: {
                            if (!this.hasBoldFont()) {
                                break;
                            }
                            bold = true;
                            if (italic) {
                                GlStateManager.bindTexture(this.boldFont.italic.texture.getGlTextureId());
                                currentData = this.boldFont.italic;
                                break;
                            }
                            GlStateManager.bindTexture(this.boldFont.regular.texture.getGlTextureId());
                            currentData = this.boldFont.regular;
                            break;
                        }
                        case 18: {
                            strikethrough = true;
                            break;
                        }
                        case 19: {
                            underline = true;
                            break;
                        }
                        case 20: {
                            italic = true;
                            if (bold && this.hasBoldFont()) {
                                GlStateManager.bindTexture(this.boldFont.italic.texture.getGlTextureId());
                                currentData = this.boldFont.italic;
                                break;
                            }
                            GlStateManager.bindTexture(this.italic.texture.getGlTextureId());
                            currentData = this.italic;
                            break;
                        }
                        default: {
                            bold = false;
                            italic = false;
                            underline = false;
                            strikethrough = false;
                            RenderUtil.color(color);
                            GlStateManager.bindTexture(this.regular.texture.getGlTextureId());
                            currentData = this.regular;
                            break;
                        }
                    }
                }
                ++index;
            }
            else if (character < currentData.chars.length) {
                this.drawLetter(x, y, currentData, strikethrough, underline, character);
                x += MathUtils.roundToHalf(currentData.chars[character].width - 8.2f);
            }
        }
        return (float)(x / 2.0);
    }
    
    public void drawLetter(final double x, final double y, final FontData currentData, final boolean strikethrough, final boolean underline, final char character) {
        GL11.glBegin(4);
        final CharData charData = currentData.chars[character];
        this.drawQuad((float)x, (float)y, charData.width, (float)charData.height, (float)charData.storedX, (float)charData.storedY, currentData.imageSize.getFirst(), currentData.imageSize.getSecond());
        GL11.glEnd();
        if (strikethrough) {
            this.drawLine(x, y + charData.height / 2, x + charData.width - 8.0, y + charData.height / 2);
        }
        if (underline) {
            this.drawLine(x + 2.5, y + charData.height - 1.0, x + charData.width - 6.0, y + charData.height - 1.0);
        }
    }
    
    protected void drawQuad(final float x2, final float y2, final float width, final float height, final float srcX, final float srcY, final float imgWidth, final float imgHeight) {
        final float renderSRCX = srcX / imgWidth;
        final float renderSRCY = srcY / imgHeight;
        final float renderSRCWidth = width / imgWidth;
        final float renderSRCHeight = height / imgHeight;
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d((double)(x2 + width), (double)y2);
        GL11.glTexCoord2f(renderSRCX, renderSRCY);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)x2, (double)(y2 + height));
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)x2, (double)(y2 + height));
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)(x2 + width), (double)(y2 + height));
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d((double)(x2 + width), (double)y2);
    }
    
    @Override
    public float getMiddleOfBox(final float height) {
        return height / 2.0f - this.getHeight() / 2.0f;
    }
    
    @Override
    public String trimStringToWidth(final String text, final int width) {
        return this.trimStringToWidth(text, width, false);
    }
    
    @Override
    public String trimStringToWidth(final String text, final int width, final boolean reverse) {
        if (text == null) {
            return "";
        }
        final StringBuilder buffer = new StringBuilder();
        float lineWidth = 0.0f;
        final int offset = reverse ? (text.length() - 1) : 0;
        final int increment = reverse ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;
        for (int index = offset; index >= 0 && index < text.length() && lineWidth < width; index += increment) {
            final char character = text.charAt(index);
            final float charWidth = this.getCharWidthFloat(character);
            if (var8) {
                var8 = false;
                if (character != 'l' && character != 'L') {
                    if (character == 'r' || character == 'R') {
                        var9 = false;
                    }
                }
                else {
                    var9 = true;
                }
            }
            else if (charWidth < 0.0f) {
                var8 = true;
            }
            else {
                lineWidth += charWidth;
                if (var9) {
                    ++lineWidth;
                }
            }
            if (lineWidth > width) {
                break;
            }
            if (reverse) {
                buffer.insert(0, character);
            }
            else {
                buffer.append(character);
            }
        }
        return buffer.toString();
    }
    
    private float getCharWidthFloat(final char c) {
        if (c == '§') {
            return -1.0f;
        }
        if (c == ' ') {
            return 2.0f;
        }
        final int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".indexOf(c);
        if (c > '\0' && var2 != -1) {
            return this.regular.chars[var2].width / 2.0f - 4.0f;
        }
        if (c < this.regular.chars.length && this.regular.chars[c].width / 2.0f - 4.0f != 0.0f) {
            int var3 = (int)(this.regular.chars[c].width / 2.0f - 4.0f) >>> 4;
            int var4 = (int)(this.regular.chars[c].width / 2.0f - 4.0f) & 0xF;
            var3 &= 0xF;
            return (float)((++var4 - var3) / 2 + 1);
        }
        return 0.0f;
    }
    
    @Override
    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }
    
    @Override
    public float getStringWidth(final String text) {
        return (float)this.getStringWidth(text, 8.2f);
    }
    
    public double getStringWidth(String text, final float kerning) {
        if (text == null) {
            return 0.0;
        }
        text = Streamer.filter(text);
        float width = 0.0f;
        CharData[] currentData = this.regular.chars;
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character == '§') {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                switch (colorIndex) {
                    case 17: {
                        if (this.hasBoldFont()) {
                            currentData = this.boldFont.regular.chars;
                            break;
                        }
                        break;
                    }
                    case 20: {
                        currentData = this.regular.chars;
                        break;
                    }
                    default: {
                        currentData = this.regular.chars;
                        break;
                    }
                }
                ++index;
            }
            else if (character < currentData.length) {
                width += currentData[character].width - kerning;
            }
        }
        return width / 2.0f;
    }
    
    public boolean hasBoldFont() {
        return this.boldFont != null;
    }
    
    public List<String> getWrappedLines(final String text, final float x, final float width, final float heightIncrement) {
        this.wrapTextToLines(text, x, width);
        return this.lines;
    }
    
    public float drawWrappedText(final String text, final float x, final float y, final int color, final float width, final float heightIncrement) {
        this.wrapTextToLines(text, x, width);
        float newY = y;
        for (final String s : this.lines) {
            RenderUtil.resetColor();
            this.drawString(s, x, newY, color);
            newY += this.getHeight() + heightIncrement;
        }
        return newY - y;
    }
    
    public MutablePair<Float, Float> drawNewLineText(final String text, final float x, final float y, final int color, final float heightIncrement) {
        this.wrapTextToNewLine(text, x);
        String longest = "";
        float newY = y;
        for (final String s : this.lines) {
            if (this.getStringWidth(s) > this.getStringWidth(longest)) {
                longest = s;
            }
            RenderUtil.resetColor();
            this.drawString(s, x, newY, color);
            newY += this.getHeight() + heightIncrement;
        }
        return MutablePair.of(this.getStringWidth(longest), newY - y);
    }
    
    private void wrapTextToNewLine(final String text, final float x) {
        this.lines.clear();
        this.lines.addAll(Arrays.asList(text.trim().split("\n")));
    }
    
    private void wrapTextToLines(final String text, final float x, final float width) {
        this.lines.clear();
        final String[] words = text.trim().split(" ");
        StringBuilder line = new StringBuilder();
        for (final String word : words) {
            final float totalWidth = this.getStringWidth((Object)line + " " + word);
            if (x + totalWidth >= x + width) {
                this.lines.add(line.toString());
                line = new StringBuilder(word).append(" ");
            }
            else {
                line.append(word).append(" ");
            }
        }
        this.lines.add(line.toString());
    }
    
    private void drawLine(final double x2, final double y2, final double x1, final double y1) {
        GL11.glDisable(3553);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public float getWidth(final String text) {
        float width = 0.0f;
        CharacterData[] characterData = this.regularData;
        for (int length = text.length(), i = 0; i < length; ++i) {
            final char character = text.charAt(i);
            final char previous = (i > 0) ? text.charAt(i - 1) : '.';
            if (previous != '§') {
                if (character == '§' && i < length) {
                    final int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                    if (index == 17) {
                        characterData = this.boldData;
                    }
                    else if (index == 20) {
                        characterData = this.italicsData;
                    }
                    else if (index == 21) {
                        characterData = this.regularData;
                    }
                }
                else if (character <= '\u00ff') {
                    final CharacterData charData = characterData[character];
                    width += (charData.width - 8.0f) / 2.0f;
                }
            }
        }
        return width + 2.0f;
    }
    
    private void generateColorCodes() {
        if (CustomFont.colorCode == null) {
            CustomFont.colorCode = new int[32];
            for (int i = 0; i < 32; ++i) {
                final int noClue = (i >> 3 & 0x1) * 85;
                int red = (i >> 2 & 0x1) * 170 + noClue;
                int green = (i >> 1 & 0x1) * 170 + noClue;
                int blue = (i & 0x1) * 170 + noClue;
                if (i == 6) {
                    red += 85;
                }
                if (i >= 16) {
                    red /= 4;
                    green /= 4;
                    blue /= 4;
                }
                CustomFont.colorCode[i] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
            }
        }
    }
    
    private void handleSprites(final FontData fontData, final Font currentFont, final Graphics2D graphics2D) {
        this.handleSprites(fontData, currentFont, graphics2D, false);
    }
    
    private void handleSprites(final FontData fontData, final Font currentFont, final Graphics2D graphics2D, final boolean drawString) {
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        int index = 0;
        final FontMetrics fontMetrics = graphics2D.getFontMetrics();
        if (drawString) {
            final BufferedImage image = new BufferedImage(fontData.imageSize.getFirst(), fontData.imageSize.getSecond(), 2);
            final Graphics2D graphics = (Graphics2D)image.getGraphics();
            graphics.setFont(currentFont);
            graphics.setColor(new Color(255, 255, 255, 0));
            graphics.fillRect(0, 0, fontData.imageSize.getFirst(), fontData.imageSize.getSecond());
            graphics.setColor(Color.WHITE);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (final CharData data : fontData.chars) {
                final char c = (char)index;
                graphics.drawString(String.valueOf(c), data.storedX + 2, data.storedY + fontMetrics.getAscent());
                ++index;
            }
            fontData.texture = new DynamicTexture(image);
        }
        else {
            while (index < fontData.chars.length) {
                final char c2 = (char)index;
                final CharData charData = new CharData();
                final Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(c2), graphics2D);
                charData.width = dimensions.getBounds().width + 8.2f;
                charData.height = dimensions.getBounds().height;
                if (positionX + charData.width >= fontData.imageSize.getFirst()) {
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
                fontData.chars[index] = charData;
                positionX += (int)charData.width;
                fontData.imageSize.setSecond(positionY + fontMetrics.getAscent());
                ++index;
            }
            this.handleSprites(fontData, currentFont, graphics2D, true);
        }
    }
    
    public void setBoldFont(final CustomFont boldFont) {
        this.boldFont = boldFont;
    }
    
    public CustomFont getBoldFont() {
        return this.boldFont;
    }
    
    static class CharacterData
    {
        public float width;
        public float height;
        private int textureId;
        
        public CharacterData(final float width, final float height, final int textureId) {
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }
    }
    
    private static class FontData
    {
        private final CharData[] chars;
        private final int textType;
        private DynamicTexture texture;
        private final MutablePair<Integer, Integer> imageSize;
        
        public FontData(final int textType) {
            this.chars = new CharData[256];
            this.imageSize = MutablePair.of(512, 0);
            this.textType = textType;
        }
    }
    
    private static class CharData
    {
        private float width;
        private int height;
        private int storedX;
        private int storedY;
    }
}
