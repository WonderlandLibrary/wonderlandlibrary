/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.lwjgl.opengl.GL11
 */
package jx.utils.novoline.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import jx.utils.novoline.api.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public final class SimpleFontRenderer
implements FontRenderer {
    private static final int[] COLOR_CODES = SimpleFontRenderer.setupMinecraftColorCodes();
    private static final String COLORS = "0123456789abcdefklmnor";
    private static final char COLOR_PREFIX = '\u00a7';
    private static final short CHARS = 256;
    private static final float IMG_SIZE = 512.0f;
    private static final float CHAR_OFFSET = 0.0f;
    private final CharData[] charData = new CharData[256];
    private final CharData[] boldChars = new CharData[256];
    private final CharData[] italicChars = new CharData[256];
    private final CharData[] boldItalicChars = new CharData[256];
    private final Font awtFont;
    private final boolean antiAlias;
    private final boolean fractionalMetrics;
    private DynamicTexture texturePlain;
    private DynamicTexture textureBold;
    private DynamicTexture textureItalic;
    private DynamicTexture textureItalicBold;
    private int fontHeight = -1;

    private SimpleFontRenderer(Font awtFont, boolean antiAlias, boolean fractionalMetrics) {
        this.awtFont = awtFont;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.setupBoldItalicFonts();
    }

    static FontRenderer create(Font font, boolean antiAlias, boolean fractionalMetrics) {
        return new SimpleFontRenderer(font, antiAlias, fractionalMetrics);
    }

    public static FontRenderer create(Font font) {
        return SimpleFontRenderer.create(font, true, true);
    }

    private DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
        return new DynamicTexture(this.generateFontImage(font, antiAlias, fractionalMetrics, chars));
    }

    private BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
        int imgSize = 512;
        BufferedImage bufferedImage = new BufferedImage(512, 512, 2);
        Graphics2D graphics = (Graphics2D)bufferedImage.getGraphics();
        graphics.setFont(font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, 512, 512);
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (this.fractionalMetrics) {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        for (int i = 0; i < chars.length; ++i) {
            char ch = (char)i;
            CharData charData = new CharData();
            Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), graphics);
            charData.width = dimensions.getBounds().width + 8;
            charData.height = dimensions.getBounds().height;
            if (positionX + charData.width >= 512) {
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
            chars[i] = charData;
            graphics.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width;
        }
        return bufferedImage;
    }

    private void setupBoldItalicFonts() {
        this.texturePlain = this.setupTexture(this.awtFont, this.antiAlias, this.fractionalMetrics, this.charData);
        this.textureBold = this.setupTexture(this.awtFont.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.textureItalic = this.setupTexture(this.awtFont.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.textureItalicBold = this.setupTexture(this.awtFont.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    @Override
    public float drawString(CharSequence text, double x, double y, int color, boolean dropShadow) {
        if (dropShadow) {
            float shadowWidth = this.drawStringInternal(text, x + 0.5, y + 0.5, color, true);
            return Math.max(shadowWidth, this.drawStringInternal(text, x, y, color, false));
        }
        return this.drawStringInternal(text, x, y, color, false);
    }

    @Override
    public float drawString(CharSequence text, float x, float y, int color, boolean dropShadow) {
        if (dropShadow) {
            float shadowWidth = this.drawStringInternal(text, (double)x + 0.5, (double)y + 0.5, color, true);
            return Math.max(shadowWidth, this.drawStringInternal(text, x, y, color, false));
        }
        return this.drawStringInternal(text, x, y, color, false);
    }

    private float drawStringInternal(CharSequence text, double x, double y, int color, boolean shadow) {
        x -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        if (color == 0x20FFFFFF) {
            color = 0xFFFFFF;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        CharData[] charData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean randomCase = false;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        GL11.glColor4f((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)alpha);
        GlStateManager.func_179131_c((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)alpha);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i((int)this.texturePlain.func_110552_b());
        GL11.glBindTexture((int)3553, (int)this.texturePlain.func_110552_b());
        GL11.glTexParameterf((int)3553, (int)10240, (float)9729.0f);
        boolean underline = false;
        boolean strikethrough = false;
        boolean italic = false;
        boolean bold = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7' && i + 1 < size) {
                int colorIndex = COLORS.indexOf(text.charAt(i + 1));
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.func_179144_i((int)this.texturePlain.func_110552_b());
                    charData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorCode = COLOR_CODES[colorIndex];
                    GlStateManager.func_179131_c((float)((float)(colorCode >> 16 & 0xFF) / 255.0f), (float)((float)(colorCode >> 8 & 0xFF) / 255.0f), (float)((float)(colorCode & 0xFF) / 255.0f), (float)255.0f);
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.func_179144_i((int)this.textureItalicBold.func_110552_b());
                        charData = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i((int)this.textureBold.func_110552_b());
                        charData = this.boldChars;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.func_179144_i((int)this.textureItalicBold.func_110552_b());
                        charData = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i((int)this.textureItalic.func_110552_b());
                        charData = this.italicChars;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.func_179131_c((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)255.0f);
                    GlStateManager.func_179144_i((int)this.texturePlain.func_110552_b());
                    charData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= charData.length) continue;
            GL11.glBegin((int)4);
            SimpleFontRenderer.drawChar(charData, character, (float)x, (float)y);
            GL11.glEnd();
            if (strikethrough) {
                SimpleFontRenderer.drawLine(x, y + (double)((float)charData[character].height / 2.0f), x + (double)charData[character].width - 8.0, y + (double)((float)charData[character].height / 2.0f), 1.0f);
            }
            if (underline) {
                SimpleFontRenderer.drawLine(x, y + (double)charData[character].height - 2.0, x + (double)charData[character].width - 8.0, y + (double)charData[character].height - 2.0, 1.0f);
            }
            x += (double)(charData[character].width - (character == ' ' ? 8 : 9));
        }
        GL11.glTexParameterf((int)3553, (int)10240, (float)9728.0f);
        GL11.glHint((int)3155, (int)4352);
        GL11.glPopMatrix();
        return (float)x / 2.0f;
    }

    @Override
    public String trimStringToWidth(CharSequence text, int width, boolean reverse) {
        StringBuilder builder = new StringBuilder();
        float f = 0.0f;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int k = i; k >= 0 && k < text.length() && f < (float)width; k += j) {
            char c0 = text.charAt(k);
            float f1 = this.stringWidth(String.valueOf(c0));
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0f) {
                flag = true;
            } else {
                f += f1;
                if (flag1) {
                    f += 1.0f;
                }
            }
            if (f > (float)width) break;
            if (reverse) {
                builder.insert(0, c0);
                continue;
            }
            builder.append(c0);
        }
        return builder.toString();
    }

    @Override
    public int stringWidth(CharSequence text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7' && i + 1 < size) {
                int colorIndex = COLORS.indexOf(text.charAt(i + 1));
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= currentData.length) continue;
            width += currentData[character].width - (character == ' ' ? 8 : 9);
        }
        return width / 2;
    }

    @Override
    public float charWidth(char s) {
        return (this.charData[s].width - 8) / 2;
    }

    public CharData[] getCharData() {
        return this.charData;
    }

    private static int[] setupMinecraftColorCodes() {
        int[] colorCodes = new int[32];
        for (int i = 0; i < 32; ++i) {
            int noClue = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + noClue;
            int green = (i >> 1 & 1) * 170 + noClue;
            int blue = (i & 1) * 170 + noClue;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red >>= 2;
                green >>= 2;
                blue >>= 2;
            }
            colorCodes[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
        return colorCodes;
    }

    private static void drawChar(CharData[] chars, char c, float x, float y) {
        SimpleFontRenderer.drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
    }

    private static void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
        float renderSRCX = srcX / 512.0f;
        float renderSRCY = srcY / 512.0f;
        float renderSRCWidth = srcWidth / 512.0f;
        float renderSRCHeight = srcHeight / 512.0f;
        GL11.glTexCoord2f((float)(renderSRCX + renderSRCWidth), (float)renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glTexCoord2f((float)renderSRCX, (float)renderSRCY);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glTexCoord2f((float)renderSRCX, (float)(renderSRCY + renderSRCHeight));
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f((float)renderSRCX, (float)(renderSRCY + renderSRCHeight));
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f((float)(renderSRCX + renderSRCWidth), (float)(renderSRCY + renderSRCHeight));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glTexCoord2f((float)(renderSRCX + renderSRCWidth), (float)renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
    }

    private static void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    @Override
    public String getName() {
        return this.awtFont.getFamily();
    }

    @Override
    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    @Override
    public boolean isAntiAlias() {
        return this.antiAlias;
    }

    @Override
    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }

    private static class CharData {
        private int width;
        private int height;
        private int storedX;
        private int storedY;

        private CharData() {
        }
    }
}

