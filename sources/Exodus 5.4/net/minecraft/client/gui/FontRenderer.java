/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.text.ArabicShaping
 *  com.ibm.icu.text.ArabicShapingException
 *  com.ibm.icu.text.Bidi
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

public class FontRenderer
implements IResourceManagerReloadListener {
    protected float blue;
    protected float posX;
    public int FONT_HEIGHT = 9;
    public Random fontRandom;
    protected float green;
    protected float alpha;
    private boolean boldStyle;
    protected boolean bidiFlag;
    private final ResourceLocation locationFontTexture;
    private boolean italicStyle;
    private int textColor;
    private final TextureManager renderEngine;
    protected float red;
    private boolean strikethroughStyle;
    private boolean randomStyle;
    protected float posY;
    private byte[] glyphWidth;
    private boolean unicodeFlag;
    private int[] colorCode;
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    private boolean underlineStyle;
    private int[] charWidth = new int[256];

    private int sizeStringToWidth(String string, int n) {
        int n2 = string.length();
        int n3 = 0;
        int n4 = 0;
        int n5 = -1;
        boolean bl = false;
        while (n4 < n2) {
            char c = string.charAt(n4);
            switch (c) {
                case '\n': {
                    --n4;
                    break;
                }
                case ' ': {
                    n5 = n4;
                }
                default: {
                    n3 += this.getCharWidth(c);
                    if (!bl) break;
                    ++n3;
                    break;
                }
                case '\u00a7': {
                    char c2;
                    if (n4 >= n2 - 1) break;
                    if ((c2 = string.charAt(++n4)) != 'l' && c2 != 'L') {
                        if (c2 != 'r' && c2 != 'R' && !FontRenderer.isFormatColor(c2)) break;
                        bl = false;
                        break;
                    }
                    bl = true;
                }
            }
            if (c == '\n') {
                n5 = ++n4;
                break;
            }
            if (n3 > n) break;
            ++n4;
        }
        return n4 != n2 && n5 != -1 && n5 < n4 ? n5 : n4;
    }

    private int renderStringAligned(String string, int n, int n2, int n3, int n4, boolean bl) {
        if (this.bidiFlag) {
            int n5 = this.getStringWidth(this.bidiReorder(string));
            n = n + n3 - n5;
        }
        return this.renderString(string, n, n2, n4, bl);
    }

    private float renderDefaultChar(int n, boolean bl) {
        int n2 = n % 16 * 8;
        int n3 = n / 16 * 8;
        boolean bl2 = bl;
        this.renderEngine.bindTexture(this.locationFontTexture);
        int n4 = this.charWidth[n];
        float f = (float)n4 - 0.01f;
        GL11.glBegin((int)5);
        GL11.glTexCoord2f((float)((float)n2 / 128.0f), (float)((float)n3 / 128.0f));
        GL11.glVertex3f((float)(this.posX + (float)bl2), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)((float)n2 / 128.0f), (float)(((float)n3 + 7.99f) / 128.0f));
        GL11.glVertex3f((float)(this.posX - (float)bl2), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glTexCoord2f((float)(((float)n2 + f - 1.0f) / 128.0f), (float)((float)n3 / 128.0f));
        GL11.glVertex3f((float)(this.posX + f - 1.0f + (float)bl2), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)(((float)n2 + f - 1.0f) / 128.0f), (float)(((float)n3 + 7.99f) / 128.0f));
        GL11.glVertex3f((float)(this.posX + f - 1.0f - (float)bl2), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glEnd();
        return n4;
    }

    public String trimStringToWidth(String string, int n, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        int n3 = bl ? string.length() - 1 : 0;
        int n4 = bl ? -1 : 1;
        boolean bl2 = false;
        boolean bl3 = false;
        int n5 = n3;
        while (n5 >= 0 && n5 < string.length() && n2 < n) {
            char c = string.charAt(n5);
            int n6 = this.getCharWidth(c);
            if (bl2) {
                bl2 = false;
                if (c != 'l' && c != 'L') {
                    if (c == 'r' || c == 'R') {
                        bl3 = false;
                    }
                } else {
                    bl3 = true;
                }
            } else if (n6 < 0) {
                bl2 = true;
            } else {
                n2 += n6;
                if (bl3) {
                    ++n2;
                }
            }
            if (n2 > n) break;
            if (bl) {
                stringBuilder.insert(0, c);
            } else {
                stringBuilder.append(c);
            }
            n5 += n4;
        }
        return stringBuilder.toString();
    }

    private void renderSplitString(String string, int n, int n2, int n3, boolean bl) {
        for (String string2 : this.listFormattedStringToWidth(string, n3)) {
            this.renderStringAligned(string2, n, n2, n3, this.textColor, bl);
            n2 += this.FONT_HEIGHT;
        }
    }

    String wrapFormattedStringToWidth(String string, int n) {
        int n2 = this.sizeStringToWidth(string, n);
        if (string.length() <= n2) {
            return string;
        }
        String string2 = string.substring(0, n2);
        char c = string.charAt(n2);
        boolean bl = c == ' ' || c == '\n';
        String string3 = String.valueOf(FontRenderer.getFormatFromString(string2)) + string.substring(n2 + (bl ? 1 : 0));
        return String.valueOf(string2) + "\n" + this.wrapFormattedStringToWidth(string3, n);
    }

    public int drawStringWithShadow(String string, float f, float f2, int n) {
        return this.drawString(string, f, f2, n, true);
    }

    protected String bidiReorder(String string) {
        try {
            Bidi bidi = new Bidi(new ArabicShaping(8).shape(string), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException arabicShapingException) {
            return string;
        }
    }

    private float func_181559_a(char c, boolean bl) {
        if (c == ' ') {
            return 4.0f;
        }
        int n = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c);
        return n != -1 && !this.unicodeFlag ? this.renderDefaultChar(n, bl) : this.renderUnicodeChar(c, bl);
    }

    public int getColorCode(char c) {
        return this.colorCode["0123456789abcdef".indexOf(c)];
    }

    public void drawSplitString(String string, int n, int n2, int n3, int n4) {
        this.resetStyles();
        this.textColor = n4;
        string = this.trimStringNewline(string);
        this.renderSplitString(string, n, n2, n3, false);
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void readGlyphSizes() {
        InputStream inputStream = null;
        try {
            inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
            inputStream.read(this.glyphWidth);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        IOUtils.closeQuietly((InputStream)inputStream);
    }

    public int drawString(String string, double d, double d2, int n) {
        return this.drawString(string, (float)d, (float)d2, n, false);
    }

    private int renderString(String string, float f, float f2, int n, boolean bl) {
        if (string == null) {
            return 0;
        }
        if (this.bidiFlag) {
            string = this.bidiReorder(string);
        }
        if ((n & 0xFC000000) == 0) {
            n |= 0xFF000000;
        }
        if (bl) {
            n = (n & 0xFCFCFC) >> 2 | n & 0xFF000000;
        }
        this.red = (float)(n >> 16 & 0xFF) / 255.0f;
        this.blue = (float)(n >> 8 & 0xFF) / 255.0f;
        this.green = (float)(n & 0xFF) / 255.0f;
        this.alpha = (float)(n >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(this.red, this.blue, this.green, this.alpha);
        this.posX = f;
        this.posY = f2;
        this.renderStringAtPos(string, bl);
        return (int)this.posX;
    }

    public int drawString(String string, float f, float f2, int n, boolean bl) {
        int n2;
        GlStateManager.enableAlpha();
        this.resetStyles();
        if (bl) {
            n2 = this.renderString(string, f + 1.0f, f2 + 1.0f, n, true);
            n2 = Math.max(n2, this.renderString(string, f, f2, n, false));
        } else {
            n2 = this.renderString(string, f, f2, n, false);
        }
        return n2;
    }

    public void setBidiFlag(boolean bl) {
        this.bidiFlag = bl;
    }

    private ResourceLocation getUnicodePageLocation(int n) {
        if (unicodePageLocations[n] == null) {
            FontRenderer.unicodePageLocations[n] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", n));
        }
        return unicodePageLocations[n];
    }

    private void renderStringAtPos(String string, boolean bl) {
        int n = 0;
        while (n < string.length()) {
            int n2;
            int n3;
            char c = string.charAt(n);
            if (c == '\u00a7' && n + 1 < string.length()) {
                n3 = "0123456789abcdefklmnor".indexOf(string.toLowerCase(Locale.ENGLISH).charAt(n + 1));
                if (n3 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (n3 < 0 || n3 > 15) {
                        n3 = 15;
                    }
                    if (bl) {
                        n3 += 16;
                    }
                    this.textColor = n2 = this.colorCode[n3];
                    GlStateManager.color((float)(n2 >> 16) / 255.0f, (float)(n2 >> 8 & 0xFF) / 255.0f, (float)(n2 & 0xFF) / 255.0f, this.alpha);
                } else if (n3 == 16) {
                    this.randomStyle = true;
                } else if (n3 == 17) {
                    this.boldStyle = true;
                } else if (n3 == 18) {
                    this.strikethroughStyle = true;
                } else if (n3 == 19) {
                    this.underlineStyle = true;
                } else if (n3 == 20) {
                    this.italicStyle = true;
                } else if (n3 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                }
                ++n;
            } else {
                WorldRenderer worldRenderer;
                Tessellator tessellator;
                char c2;
                n3 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c);
                if (this.randomStyle && n3 != -1) {
                    n2 = this.getCharWidth(c);
                    while (n2 != this.getCharWidth(c2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(n3 = this.fontRandom.nextInt(256)))) {
                    }
                    c = c2;
                }
                float f = this.unicodeFlag ? 0.5f : 1.0f;
                char c3 = c2 = (c == '\u0000' || n3 == -1 || this.unicodeFlag) && bl ? (char)'\u0001' : '\u0000';
                if (c2 != '\u0000') {
                    this.posX -= f;
                    this.posY -= f;
                }
                float f2 = this.func_181559_a(c, this.italicStyle);
                if (c2 != '\u0000') {
                    this.posX += f;
                    this.posY += f;
                }
                if (this.boldStyle) {
                    this.posX += f;
                    if (c2 != '\u0000') {
                        this.posX -= f;
                        this.posY -= f;
                    }
                    this.func_181559_a(c, this.italicStyle);
                    this.posX -= f;
                    if (c2 != '\u0000') {
                        this.posX += f;
                        this.posY += f;
                    }
                    f2 += 1.0f;
                }
                if (this.strikethroughStyle) {
                    tessellator = Tessellator.getInstance();
                    worldRenderer = tessellator.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0).endVertex();
                    worldRenderer.pos(this.posX + f2, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0).endVertex();
                    worldRenderer.pos(this.posX + f2, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0).endVertex();
                    worldRenderer.pos(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }
                if (this.underlineStyle) {
                    tessellator = Tessellator.getInstance();
                    worldRenderer = tessellator.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
                    int n4 = this.underlineStyle ? -1 : 0;
                    worldRenderer.pos(this.posX + (float)n4, this.posY + (float)this.FONT_HEIGHT, 0.0).endVertex();
                    worldRenderer.pos(this.posX + f2, this.posY + (float)this.FONT_HEIGHT, 0.0).endVertex();
                    worldRenderer.pos(this.posX + f2, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                    worldRenderer.pos(this.posX + (float)n4, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }
                this.posX += (float)((int)f2);
            }
            ++n;
        }
    }

    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }

    private float renderUnicodeChar(char c, boolean bl) {
        if (this.glyphWidth[c] == 0) {
            return 0.0f;
        }
        int n = c / 256;
        this.loadGlyphTexture(n);
        int n2 = this.glyphWidth[c] >>> 4;
        int n3 = this.glyphWidth[c] & 0xF;
        float f = n2;
        float f2 = n3 + 1;
        float f3 = (float)(c % 16 * 16) + f;
        float f4 = (c & 0xFF) / 16 * 16;
        float f5 = f2 - f - 0.02f;
        float f6 = bl ? 1.0f : 0.0f;
        GL11.glBegin((int)5);
        GL11.glTexCoord2f((float)(f3 / 256.0f), (float)(f4 / 256.0f));
        GL11.glVertex3f((float)(this.posX + f6), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)(f3 / 256.0f), (float)((f4 + 15.98f) / 256.0f));
        GL11.glVertex3f((float)(this.posX - f6), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glTexCoord2f((float)((f3 + f5) / 256.0f), (float)(f4 / 256.0f));
        GL11.glVertex3f((float)(this.posX + f5 / 2.0f + f6), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)((f3 + f5) / 256.0f), (float)((f4 + 15.98f) / 256.0f));
        GL11.glVertex3f((float)(this.posX + f5 / 2.0f - f6), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glEnd();
        return (f2 - f) / 2.0f + 1.0f;
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        this.readFontTexture();
    }

    public static String getFormatFromString(String string) {
        String string2 = "";
        int n = -1;
        int n2 = string.length();
        while ((n = string.indexOf(167, n + 1)) != -1) {
            if (n >= n2 - 1) continue;
            char c = string.charAt(n + 1);
            if (FontRenderer.isFormatColor(c)) {
                string2 = "\u00a7" + c;
                continue;
            }
            if (!FontRenderer.isFormatSpecial(c)) continue;
            string2 = String.valueOf(string2) + "\u00a7" + c;
        }
        return string2;
    }

    private String trimStringNewline(String string) {
        while (string != null && string.endsWith("\n")) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    public String trimStringToWidth(String string, int n) {
        return this.trimStringToWidth(string, n, false);
    }

    public List<String> listFormattedStringToWidth(String string, int n) {
        return Arrays.asList(this.wrapFormattedStringToWidth(string, n).split("\n"));
    }

    private static boolean isFormatSpecial(char c) {
        return c >= 'k' && c <= 'o' || c >= 'K' && c <= 'O' || c == 'r' || c == 'R';
    }

    public void setUnicodeFlag(boolean bl) {
        this.unicodeFlag = bl;
    }

    public FontRenderer(GameSettings gameSettings, ResourceLocation resourceLocation, TextureManager textureManager, boolean bl) {
        this.fontRandom = new Random();
        this.glyphWidth = new byte[65536];
        this.colorCode = new int[32];
        this.locationFontTexture = resourceLocation;
        this.renderEngine = textureManager;
        this.unicodeFlag = bl;
        textureManager.bindTexture(this.locationFontTexture);
        int n = 0;
        while (n < 32) {
            int n2 = (n >> 3 & 1) * 85;
            int n3 = (n >> 2 & 1) * 170 + n2;
            int n4 = (n >> 1 & 1) * 170 + n2;
            int n5 = (n >> 0 & 1) * 170 + n2;
            if (n == 6) {
                n3 += 85;
            }
            if (gameSettings.anaglyph) {
                int n6 = (n3 * 30 + n4 * 59 + n5 * 11) / 100;
                int n7 = (n3 * 30 + n4 * 70) / 100;
                int n8 = (n3 * 30 + n5 * 70) / 100;
                n3 = n6;
                n4 = n7;
                n5 = n8;
            }
            if (n >= 16) {
                n3 /= 4;
                n4 /= 4;
                n5 /= 4;
            }
            this.colorCode[n] = (n3 & 0xFF) << 16 | (n4 & 0xFF) << 8 | n5 & 0xFF;
            ++n;
        }
        this.readGlyphSizes();
    }

    public int splitStringWidth(String string, int n) {
        return this.FONT_HEIGHT * this.listFormattedStringToWidth(string, n).size();
    }

    private static boolean isFormatColor(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
    }

    private void loadGlyphTexture(int n) {
        this.renderEngine.bindTexture(this.getUnicodePageLocation(n));
    }

    public boolean getBidiFlag() {
        return this.bidiFlag;
    }

    private void readFontTexture() {
        BufferedImage bufferedImage;
        try {
            bufferedImage = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture).getInputStream());
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        int n = bufferedImage.getWidth();
        int n2 = bufferedImage.getHeight();
        int[] nArray = new int[n * n2];
        bufferedImage.getRGB(0, 0, n, n2, nArray, 0, n);
        int n3 = n2 / 16;
        int n4 = n / 16;
        int n5 = 1;
        float f = 8.0f / (float)n4;
        int n6 = 0;
        while (n6 < 256) {
            int n7 = n6 % 16;
            int n8 = n6 / 16;
            if (n6 == 32) {
                this.charWidth[n6] = 3 + n5;
            }
            int n9 = n4 - 1;
            while (n9 >= 0) {
                int n10 = n7 * n4 + n9;
                boolean bl = true;
                int n11 = 0;
                while (n11 < n3 && bl) {
                    int n12 = (n8 * n4 + n11) * n;
                    if ((nArray[n10 + n12] >> 24 & 0xFF) != 0) {
                        bl = false;
                    }
                    ++n11;
                }
                if (!bl) break;
                --n9;
            }
            this.charWidth[n6] = (int)(0.5 + (double)((float)(++n9) * f)) + n5;
            ++n6;
        }
    }

    public int getStringWidth(String string) {
        if (string == null) {
            return 0;
        }
        int n = 0;
        boolean bl = false;
        int n2 = 0;
        while (n2 < string.length()) {
            char c = string.charAt(n2);
            int n3 = this.getCharWidth(c);
            if (n3 < 0 && n2 < string.length() - 1) {
                if ((c = string.charAt(++n2)) != 'l' && c != 'L') {
                    if (c == 'r' || c == 'R') {
                        bl = false;
                    }
                } else {
                    bl = true;
                }
                n3 = 0;
            }
            n += n3;
            if (bl && n3 > 0) {
                ++n;
            }
            ++n2;
        }
        return n;
    }

    public int getCharWidth(char c) {
        if (c == '\u00a7') {
            return -1;
        }
        if (c == ' ') {
            return 4;
        }
        int n = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c);
        if (c > '\u0000' && n != -1 && !this.unicodeFlag) {
            return this.charWidth[n];
        }
        if (this.glyphWidth[c] != 0) {
            int n2 = this.glyphWidth[c] >>> 4;
            int n3 = this.glyphWidth[c] & 0xF;
            if (n3 > 7) {
                n3 = 15;
                n2 = 0;
            }
            return (++n3 - n2) / 2 + 1;
        }
        return 0;
    }
}

