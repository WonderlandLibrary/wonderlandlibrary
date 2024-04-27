/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.lwjgl.opengl.GL11
 */
package liying.fonts.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import liying.fonts.api.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

final class SimpleFontRenderer
implements FontRenderer {
    private static final short CHARS;
    private final boolean fractionalMetrics;
    private DynamicTexture textureItalicBold;
    private static final String COLORS;
    private int fontHeight = -1;
    private static final float IMG_SIZE = 512.0f;
    private final CharData[] boldChars;
    private static final float CHAR_OFFSET = 0.0f;
    private final CharData[] italicChars;
    private DynamicTexture textureBold;
    private final boolean antiAlias;
    private final CharData[] charData = new CharData[256];
    private final Font awtFont;
    private final CharData[] boldItalicChars;
    private static final char COLOR_PREFIX;
    private DynamicTexture textureItalic;
    private DynamicTexture texturePlain;
    private static final int[] COLOR_CODES;

    @Override
    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }

    private static void drawChar(CharData[] charDataArray, char c, float f, float f2) {
        SimpleFontRenderer.drawQuad(f, f2, CharData.access$100(charDataArray[c]), CharData.access$200(charDataArray[c]), CharData.access$300(charDataArray[c]), CharData.access$400(charDataArray[c]), CharData.access$100(charDataArray[c]), CharData.access$200(charDataArray[c]));
    }

    public static FontRenderer create(Font font) {
        return SimpleFontRenderer.create(font, true, true);
    }

    @Override
    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    private static void drawQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = f5 / 512.0f;
        float f10 = f6 / 512.0f;
        float f11 = f7 / 512.0f;
        float f12 = f8 / 512.0f;
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

    private float drawStringInternal(CharSequence charSequence, double d, double d2, int n, boolean bl) {
        d -= 1.0;
        if (charSequence == null) {
            return 0.0f;
        }
        if (n == 0x20FFFFFF) {
            n = 0xFFFFFF;
        }
        if ((n & 0xFC000000) == 0) {
            n |= 0xFF000000;
        }
        if (bl) {
            n = (n & 0xFCFCFC) >> 2 | n & 0xFF000000;
        }
        CharData[] charDataArray = this.charData;
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        boolean bl2 = false;
        d *= 2.0;
        d2 = (d2 - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        GL11.glColor4f((float)((float)(n >> 16 & 0xFF) / 255.0f), (float)((float)(n >> 8 & 0xFF) / 255.0f), (float)((float)(n & 0xFF) / 255.0f), (float)f);
        GlStateManager.func_179131_c((float)((float)(n >> 16 & 0xFF) / 255.0f), (float)((float)(n >> 8 & 0xFF) / 255.0f), (float)((float)(n & 0xFF) / 255.0f), (float)f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i((int)this.texturePlain.func_110552_b());
        GL11.glBindTexture((int)3553, (int)this.texturePlain.func_110552_b());
        GL11.glTexParameterf((int)3553, (int)10240, (float)9729.0f);
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;
        int n2 = charSequence.length();
        for (int i = 0; i < n2; ++i) {
            char c = charSequence.charAt(i);
            if (c == '\u00a7' && i + 1 < n2) {
                int n3 = "0123456789abcdefklmnor".indexOf(charSequence.charAt(i + 1));
                if (n3 < 16) {
                    bl6 = false;
                    bl5 = false;
                    bl3 = false;
                    bl4 = false;
                    GlStateManager.func_179144_i((int)this.texturePlain.func_110552_b());
                    charDataArray = this.charData;
                    if (n3 < 0) {
                        n3 = 15;
                    }
                    if (bl) {
                        n3 += 16;
                    }
                    int n4 = COLOR_CODES[n3];
                    GlStateManager.func_179131_c((float)((float)(n4 >> 16 & 0xFF) / 255.0f), (float)((float)(n4 >> 8 & 0xFF) / 255.0f), (float)((float)(n4 & 0xFF) / 255.0f), (float)255.0f);
                } else if (n3 == 17) {
                    bl6 = true;
                    if (bl5) {
                        GlStateManager.func_179144_i((int)this.textureItalicBold.func_110552_b());
                        charDataArray = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i((int)this.textureBold.func_110552_b());
                        charDataArray = this.boldChars;
                    }
                } else if (n3 == 18) {
                    bl4 = true;
                } else if (n3 == 19) {
                    bl3 = true;
                } else if (n3 == 20) {
                    bl5 = true;
                    if (bl6) {
                        GlStateManager.func_179144_i((int)this.textureItalicBold.func_110552_b());
                        charDataArray = this.boldItalicChars;
                    } else {
                        GlStateManager.func_179144_i((int)this.textureItalic.func_110552_b());
                        charDataArray = this.italicChars;
                    }
                } else if (n3 == 21) {
                    bl6 = false;
                    bl5 = false;
                    bl3 = false;
                    bl4 = false;
                    GlStateManager.func_179131_c((float)((float)(n >> 16 & 0xFF) / 255.0f), (float)((float)(n >> 8 & 0xFF) / 255.0f), (float)((float)(n & 0xFF) / 255.0f), (float)255.0f);
                    GlStateManager.func_179144_i((int)this.texturePlain.func_110552_b());
                    charDataArray = this.charData;
                }
                ++i;
                continue;
            }
            if (c >= charDataArray.length) continue;
            GL11.glBegin((int)4);
            SimpleFontRenderer.drawChar(charDataArray, c, (float)d, (float)d2);
            GL11.glEnd();
            if (bl4) {
                SimpleFontRenderer.drawLine(d, d2 + (double)((float)CharData.access$200(charDataArray[c]) / 2.0f), d + (double)CharData.access$100(charDataArray[c]) - 8.0, d2 + (double)((float)CharData.access$200(charDataArray[c]) / 2.0f), 1.0f);
            }
            if (bl3) {
                SimpleFontRenderer.drawLine(d, d2 + (double)CharData.access$200(charDataArray[c]) - 2.0, d + (double)CharData.access$100(charDataArray[c]) - 8.0, d2 + (double)CharData.access$200(charDataArray[c]) - 2.0, 1.0f);
            }
            d += (double)(CharData.access$100(charDataArray[c]) - (c == ' ' ? 8 : 9));
        }
        GL11.glTexParameterf((int)3553, (int)10240, (float)9728.0f);
        GL11.glHint((int)3155, (int)4352);
        GL11.glPopMatrix();
        return (float)d / 2.0f;
    }

    private void setupBoldItalicFonts() {
        this.texturePlain = this.setupTexture(this.awtFont, this.antiAlias, this.fractionalMetrics, this.charData);
        this.textureBold = this.setupTexture(this.awtFont.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.textureItalic = this.setupTexture(this.awtFont.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.textureItalicBold = this.setupTexture(this.awtFont.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    @Override
    public float drawString(CharSequence charSequence, float f, float f2, int n, boolean bl) {
        if (bl) {
            float f3 = this.drawStringInternal(charSequence, (double)f + 0.5, (double)f2 + 0.5, n, true);
            return Math.max(f3, this.drawStringInternal(charSequence, f, f2, n, false));
        }
        return this.drawStringInternal(charSequence, f, f2, n, false);
    }

    @Override
    public boolean isAntiAlias() {
        return this.antiAlias;
    }

    private BufferedImage generateFontImage(Font font, boolean bl, boolean bl2, CharData[] charDataArray) {
        int n = 512;
        BufferedImage bufferedImage = new BufferedImage(512, 512, 2);
        Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
        graphics2D.setFont(font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, 512, 512);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (this.fractionalMetrics) {
            graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        } else {
            graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int n2 = 0;
        int n3 = 0;
        int n4 = 1;
        for (int i = 0; i < charDataArray.length; ++i) {
            char c = (char)i;
            CharData charData = new CharData(null);
            Rectangle2D rectangle2D = fontMetrics.getStringBounds(String.valueOf(c), graphics2D);
            CharData.access$102(charData, rectangle2D.getBounds().width + 8);
            CharData.access$202(charData, rectangle2D.getBounds().height);
            if (n3 + CharData.access$100(charData) >= 512) {
                n3 = 0;
                n4 += n2;
                n2 = 0;
            }
            if (CharData.access$200(charData) > n2) {
                n2 = CharData.access$200(charData);
            }
            CharData.access$302(charData, n3);
            CharData.access$402(charData, n4);
            if (CharData.access$200(charData) > this.fontHeight) {
                this.fontHeight = CharData.access$200(charData);
            }
            charDataArray[i] = charData;
            graphics2D.drawString(String.valueOf(c), n3 + 2, n4 + fontMetrics.getAscent());
            n3 += CharData.access$100(charData);
        }
        return bufferedImage;
    }

    private static void drawLine(double d, double d2, double d3, double d4, float f) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    static {
        COLOR_PREFIX = (char)167;
        CHARS = (short)256;
        COLORS = "0123456789abcdefklmnor";
        COLOR_CODES = SimpleFontRenderer.setupMinecraftColorCodes();
    }

    private DynamicTexture setupTexture(Font font, boolean bl, boolean bl2, CharData[] charDataArray) {
        return new DynamicTexture(this.generateFontImage(font, bl, bl2, charDataArray));
    }

    @Override
    public float drawString(CharSequence charSequence, double d, double d2, int n, boolean bl) {
        if (bl) {
            float f = this.drawStringInternal(charSequence, d + 0.5, d2 + 0.5, n, true);
            return Math.max(f, this.drawStringInternal(charSequence, d, d2, n, false));
        }
        return this.drawStringInternal(charSequence, d, d2, n, false);
    }

    @Override
    public String getName() {
        return this.awtFont.getFamily();
    }

    private static int[] setupMinecraftColorCodes() {
        int[] nArray = new int[32];
        for (int i = 0; i < 32; ++i) {
            int n = (i >> 3 & 1) * 85;
            int n2 = (i >> 2 & 1) * 170 + n;
            int n3 = (i >> 1 & 1) * 170 + n;
            int n4 = (i & 1) * 170 + n;
            if (i == 6) {
                n2 += 85;
            }
            if (i >= 16) {
                n2 >>= 2;
                n3 >>= 2;
                n4 >>= 2;
            }
            nArray[i] = (n2 & 0xFF) << 16 | (n3 & 0xFF) << 8 | n4 & 0xFF;
        }
        return nArray;
    }

    public CharData[] getCharData() {
        return this.charData;
    }

    @Override
    public float charWidth(char c) {
        return (CharData.access$100(this.charData[c]) - 8) / 2;
    }

    @Override
    public String trimStringToWidth(CharSequence charSequence, int n, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        float f = 0.0f;
        int n2 = bl ? charSequence.length() - 1 : 0;
        int n3 = bl ? -1 : 1;
        boolean bl2 = false;
        boolean bl3 = false;
        for (int i = n2; i >= 0 && i < charSequence.length() && f < (float)n; i += n3) {
            char c = charSequence.charAt(i);
            float f2 = this.stringWidth(String.valueOf(c));
            if (bl2) {
                bl2 = false;
                if (c != 'l' && c != 'L') {
                    if (c == 'r' || c == 'R') {
                        bl3 = false;
                    }
                } else {
                    bl3 = true;
                }
            } else if (f2 < 0.0f) {
                bl2 = true;
            } else {
                f += f2;
                if (bl3) {
                    f += 1.0f;
                }
            }
            if (f > (float)n) break;
            if (bl) {
                stringBuilder.insert(0, c);
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private SimpleFontRenderer(Font font, boolean bl, boolean bl2) {
        this.boldChars = new CharData[256];
        this.italicChars = new CharData[256];
        this.boldItalicChars = new CharData[256];
        this.awtFont = font;
        this.antiAlias = bl;
        this.fractionalMetrics = bl2;
        this.setupBoldItalicFonts();
    }

    @Override
    public int stringWidth(CharSequence charSequence) {
        if (charSequence == null) {
            return 0;
        }
        int n = 0;
        CharData[] charDataArray = this.charData;
        boolean bl = false;
        boolean bl2 = false;
        int n2 = charSequence.length();
        for (int i = 0; i < n2; ++i) {
            char c = charSequence.charAt(i);
            if (c == '\u00a7' && i + 1 < n2) {
                int n3 = "0123456789abcdefklmnor".indexOf(charSequence.charAt(i + 1));
                if (n3 < 16) {
                    bl = false;
                    bl2 = false;
                } else if (n3 == 17) {
                    bl = true;
                    charDataArray = bl2 ? this.boldItalicChars : this.boldChars;
                } else if (n3 == 20) {
                    bl2 = true;
                    charDataArray = bl ? this.boldItalicChars : this.italicChars;
                } else if (n3 == 21) {
                    bl = false;
                    bl2 = false;
                    charDataArray = this.charData;
                }
                ++i;
                continue;
            }
            if (c >= charDataArray.length) continue;
            n += CharData.access$100(charDataArray[c]) - (c == ' ' ? 8 : 9);
        }
        return n / 2;
    }

    static FontRenderer create(Font font, boolean bl, boolean bl2) {
        return new SimpleFontRenderer(font, bl, bl2);
    }

    private static class CharData {
        private int storedY;
        private int width;
        private int height;
        private int storedX;

        static int access$400(CharData charData) {
            return charData.storedY;
        }

        static int access$100(CharData charData) {
            return charData.width;
        }

        static int access$200(CharData charData) {
            return charData.height;
        }

        static int access$202(CharData charData, int n) {
            charData.height = n;
            return charData.height;
        }

        static int access$402(CharData charData, int n) {
            charData.storedY = n;
            return charData.storedY;
        }

        static int access$302(CharData charData, int n) {
            charData.storedX = n;
            return charData.storedX;
        }

        CharData(1 var1_1) {
            this();
        }

        static int access$300(CharData charData) {
            return charData.storedX;
        }

        static int access$102(CharData charData, int n) {
            charData.width = n;
            return charData.width;
        }

        private CharData() {
        }
    }
}

