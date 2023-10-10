/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.text.ArabicShaping
 *  com.ibm.icu.text.ArabicShapingException
 *  com.ibm.icu.text.Bidi
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
import java.util.Iterator;
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
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    private int[] charWidth = new int[256];
    public int FONT_HEIGHT = 9;
    public Random fontRandom = new Random();
    private byte[] glyphWidth = new byte[65536];
    private int[] colorCode = new int[32];
    private final ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    private float posX;
    private float posY;
    private boolean unicodeFlag;
    private boolean bidiFlag;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;

    public FontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        this.locationFontTexture = location;
        this.renderEngine = textureManagerIn;
        this.unicodeFlag = unicode;
        textureManagerIn.bindTexture(this.locationFontTexture);
        int i = 0;
        while (true) {
            if (i >= 32) {
                this.readGlyphSizes();
                return;
            }
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (gameSettingsIn.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
            ++i;
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.readFontTexture();
    }

    private void readFontTexture() {
        BufferedImage bufferedimage;
        try {
            bufferedimage = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture).getInputStream());
        }
        catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        int k = j / 16;
        int l = i / 16;
        int i1 = 1;
        float f = 8.0f / (float)l;
        int j1 = 0;
        while (j1 < 256) {
            int i2;
            int k1 = j1 % 16;
            int l1 = j1 / 16;
            if (j1 == 32) {
                this.charWidth[j1] = 3 + i1;
            }
            for (i2 = l - 1; i2 >= 0; --i2) {
                int j2 = k1 * l + i2;
                boolean flag = true;
                for (int k2 = 0; k2 < k && flag; ++k2) {
                    int l2 = (l1 * l + k2) * i;
                    if ((aint[j2 + l2] >> 24 & 0xFF) == 0) continue;
                    flag = false;
                }
                if (!flag) break;
            }
            this.charWidth[j1] = (int)(0.5 + (double)((float)(++i2) * f)) + i1;
            ++j1;
        }
    }

    private void readGlyphSizes() {
        InputStream inputstream = null;
        try {
            inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
            inputstream.read(this.glyphWidth);
        }
        catch (IOException ioexception) {
            try {
                throw new RuntimeException(ioexception);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(inputstream);
                throw throwable;
            }
        }
        IOUtils.closeQuietly(inputstream);
    }

    private float func_181559_a(char p_181559_1_, boolean p_181559_2_) {
        float f;
        if (p_181559_1_ == ' ') {
            return 4.0f;
        }
        int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_181559_1_);
        if (i != -1 && !this.unicodeFlag) {
            f = this.renderDefaultChar(i, p_181559_2_);
            return f;
        }
        f = this.renderUnicodeChar(p_181559_1_, p_181559_2_);
        return f;
    }

    private float renderDefaultChar(int p_78266_1_, boolean p_78266_2_) {
        int i = p_78266_1_ % 16 * 8;
        int j = p_78266_1_ / 16 * 8;
        boolean k = p_78266_2_;
        this.renderEngine.bindTexture(this.locationFontTexture);
        int l = this.charWidth[p_78266_1_];
        float f = (float)l - 0.01f;
        GL11.glBegin((int)5);
        GL11.glTexCoord2f((float)((float)i / 128.0f), (float)((float)j / 128.0f));
        GL11.glVertex3f((float)(this.posX + (float)k), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)((float)i / 128.0f), (float)(((float)j + 7.99f) / 128.0f));
        GL11.glVertex3f((float)(this.posX - (float)k), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glTexCoord2f((float)(((float)i + f - 1.0f) / 128.0f), (float)((float)j / 128.0f));
        GL11.glVertex3f((float)(this.posX + f - 1.0f + (float)k), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)(((float)i + f - 1.0f) / 128.0f), (float)(((float)j + 7.99f) / 128.0f));
        GL11.glVertex3f((float)(this.posX + f - 1.0f - (float)k), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glEnd();
        return l;
    }

    private ResourceLocation getUnicodePageLocation(int p_111271_1_) {
        if (unicodePageLocations[p_111271_1_] != null) return unicodePageLocations[p_111271_1_];
        FontRenderer.unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", p_111271_1_));
        return unicodePageLocations[p_111271_1_];
    }

    private void loadGlyphTexture(int p_78257_1_) {
        this.renderEngine.bindTexture(this.getUnicodePageLocation(p_78257_1_));
    }

    private float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_) {
        if (this.glyphWidth[p_78277_1_] == 0) {
            return 0.0f;
        }
        int i = p_78277_1_ / 256;
        this.loadGlyphTexture(i);
        int j = this.glyphWidth[p_78277_1_] >>> 4;
        int k = this.glyphWidth[p_78277_1_] & 0xF;
        float f = j;
        float f1 = k + 1;
        float f2 = (float)(p_78277_1_ % 16 * 16) + f;
        float f3 = (p_78277_1_ & 0xFF) / 16 * 16;
        float f4 = f1 - f - 0.02f;
        float f5 = p_78277_2_ ? 1.0f : 0.0f;
        GL11.glBegin((int)5);
        GL11.glTexCoord2f((float)(f2 / 256.0f), (float)(f3 / 256.0f));
        GL11.glVertex3f((float)(this.posX + f5), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)(f2 / 256.0f), (float)((f3 + 15.98f) / 256.0f));
        GL11.glVertex3f((float)(this.posX - f5), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glTexCoord2f((float)((f2 + f4) / 256.0f), (float)(f3 / 256.0f));
        GL11.glVertex3f((float)(this.posX + f4 / 2.0f + f5), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)((f2 + f4) / 256.0f), (float)((f3 + 15.98f) / 256.0f));
        GL11.glVertex3f((float)(this.posX + f4 / 2.0f - f5), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glEnd();
        return (f1 - f) / 2.0f + 1.0f;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, true);
    }

    public int drawString(String text, int x, int y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        GlStateManager.enableAlpha();
        this.resetStyles();
        if (!dropShadow) return this.renderString(text, x, y, color, false);
        int i = this.renderString(text, x + 1.0f, y + 1.0f, color, true);
        return Math.max(i, this.renderString(text, x, y, color, false));
    }

    private String bidiReorder(String p_147647_1_) {
        try {
            Bidi bidi = new Bidi(new ArabicShaping(8).shape(p_147647_1_), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException var3) {
            return p_147647_1_;
        }
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void renderStringAtPos(String p_78255_1_, boolean p_78255_2_) {
        int i = 0;
        while (i < p_78255_1_.length()) {
            char c0 = p_78255_1_.charAt(i);
            if (c0 == '\u00a7' && i + 1 < p_78255_1_.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (i1 < 16) {
                    int j1;
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (i1 < 0 || i1 > 15) {
                        i1 = 15;
                    }
                    if (p_78255_2_) {
                        i1 += 16;
                    }
                    this.textColor = j1 = this.colorCode[i1];
                    GlStateManager.color((float)(j1 >> 16) / 255.0f, (float)(j1 >> 8 & 0xFF) / 255.0f, (float)(j1 & 0xFF) / 255.0f, this.alpha);
                } else if (i1 == 16) {
                    this.randomStyle = true;
                } else if (i1 == 17) {
                    this.boldStyle = true;
                } else if (i1 == 18) {
                    this.strikethroughStyle = true;
                } else if (i1 == 19) {
                    this.underlineStyle = true;
                } else if (i1 == 20) {
                    this.italicStyle = true;
                } else if (i1 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                }
                ++i;
            } else {
                boolean flag;
                int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);
                if (this.randomStyle && j != -1) {
                    char c1;
                    int k = this.getCharWidth(c0);
                    while (k != this.getCharWidth(c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length())))) {
                    }
                    c0 = c1;
                }
                float f1 = this.unicodeFlag ? 0.5f : 1.0f;
                boolean bl = flag = (c0 == '\u0000' || j == -1 || this.unicodeFlag) && p_78255_2_;
                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }
                float f = this.func_181559_a(c0, this.italicStyle);
                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }
                if (this.boldStyle) {
                    this.posX += f1;
                    if (flag) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }
                    this.func_181559_a(c0, this.italicStyle);
                    this.posX -= f1;
                    if (flag) {
                        this.posX += f1;
                        this.posY += f1;
                    }
                    f += 1.0f;
                }
                if (this.strikethroughStyle) {
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                    worldrenderer.pos(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0).endVertex();
                    worldrenderer.pos(this.posX + f, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0).endVertex();
                    worldrenderer.pos(this.posX + f, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0).endVertex();
                    worldrenderer.pos(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }
                if (this.underlineStyle) {
                    Tessellator tessellator1 = Tessellator.getInstance();
                    WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
                    int l = this.underlineStyle ? -1 : 0;
                    worldrenderer1.pos(this.posX + (float)l, this.posY + (float)this.FONT_HEIGHT, 0.0).endVertex();
                    worldrenderer1.pos(this.posX + f, this.posY + (float)this.FONT_HEIGHT, 0.0).endVertex();
                    worldrenderer1.pos(this.posX + f, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                    worldrenderer1.pos(this.posX + (float)l, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                    tessellator1.draw();
                    GlStateManager.enableTexture2D();
                }
                this.posX += (float)((int)f);
            }
            ++i;
        }
    }

    private int renderStringAligned(String text, int x, int y, int p_78274_4_, int color, boolean dropShadow) {
        if (!this.bidiFlag) return this.renderString(text, x, y, color, dropShadow);
        int i = this.getStringWidth(this.bidiReorder(text));
        x = x + p_78274_4_ - i;
        return this.renderString(text, x, y, color, dropShadow);
    }

    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if (this.bidiFlag) {
            text = this.bidiReorder(text);
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (dropShadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        this.red = (float)(color >> 16 & 0xFF) / 255.0f;
        this.blue = (float)(color >> 8 & 0xFF) / 255.0f;
        this.green = (float)(color & 0xFF) / 255.0f;
        this.alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(this.red, this.blue, this.green, this.alpha);
        this.posX = x;
        this.posY = y;
        this.renderStringAtPos(text, dropShadow);
        return (int)this.posX;
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int i = 0;
        boolean flag = false;
        int j = 0;
        while (j < text.length()) {
            char c0 = text.charAt(j);
            int k = this.getCharWidth(c0);
            if (k < 0 && j < text.length() - 1) {
                if ((c0 = text.charAt(++j)) != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag = false;
                    }
                } else {
                    flag = true;
                }
                k = 0;
            }
            i += k;
            if (flag && k > 0) {
                ++i;
            }
            ++j;
        }
        return i;
    }

    public int getCharWidth(char character) {
        if (character == '\u00a7') {
            return -1;
        }
        if (character == ' ') {
            return 4;
        }
        int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(character);
        if (character > '\u0000' && i != -1 && !this.unicodeFlag) {
            return this.charWidth[i];
        }
        if (this.glyphWidth[character] == 0) return 0;
        int j = this.glyphWidth[character] >>> 4;
        int k = this.glyphWidth[character] & 0xF;
        if (k <= 7) return (++k - j) / 2 + 1;
        k = 15;
        j = 0;
        return (++k - j) / 2 + 1;
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        int l = j;
        while (l >= 0) {
            if (l >= text.length()) return stringbuilder.toString();
            if (i >= width) return stringbuilder.toString();
            char c0 = text.charAt(l);
            int i1 = this.getCharWidth(c0);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (i1 < 0) {
                flag = true;
            } else {
                i += i1;
                if (flag1) {
                    ++i;
                }
            }
            if (i > width) {
                return stringbuilder.toString();
            }
            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
            l += k;
        }
        return stringbuilder.toString();
    }

    private String trimStringNewline(String text) {
        while (text != null) {
            if (!text.endsWith("\n")) return text;
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
        this.resetStyles();
        this.textColor = textColor;
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x, y, wrapWidth, false);
    }

    private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
        Iterator<String> iterator = this.listFormattedStringToWidth(str, wrapWidth).iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            this.renderStringAligned(s, x, y, wrapWidth, this.textColor, addShadow);
            y += this.FONT_HEIGHT;
        }
    }

    public int splitStringWidth(String p_78267_1_, int p_78267_2_) {
        return this.FONT_HEIGHT * this.listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
    }

    public void setUnicodeFlag(boolean unicodeFlagIn) {
        this.unicodeFlag = unicodeFlagIn;
    }

    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }

    public void setBidiFlag(boolean bidiFlagIn) {
        this.bidiFlag = bidiFlagIn;
    }

    public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }

    String wrapFormattedStringToWidth(String str, int wrapWidth) {
        int i = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= i) {
            return str;
        }
        String s = str.substring(0, i);
        char c0 = str.charAt(i);
        boolean flag = c0 == ' ' || c0 == '\n';
        String s1 = FontRenderer.getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
        return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
    }

    private int sizeStringToWidth(String str, int wrapWidth) {
        int n;
        int k;
        int i = str.length();
        int j = 0;
        int l = -1;
        boolean flag = false;
        for (k = 0; k < i; ++k) {
            char c0 = str.charAt(k);
            switch (c0) {
                case '\n': {
                    --k;
                    break;
                }
                case ' ': {
                    l = k;
                }
                default: {
                    j += this.getCharWidth(c0);
                    if (!flag) break;
                    ++j;
                    break;
                }
                case '\u00a7': {
                    char c1;
                    if (k >= i - 1) break;
                    if ((c1 = str.charAt(++k)) != 'l' && c1 != 'L') {
                        if (c1 != 'r' && c1 != 'R' && !FontRenderer.isFormatColor(c1)) break;
                        flag = false;
                        break;
                    }
                    flag = true;
                }
            }
            if (c0 == '\n') {
                l = ++k;
                break;
            }
            if (j > wrapWidth) break;
        }
        if (k != i && l != -1 && l < k) {
            n = l;
            return n;
        }
        n = k;
        return n;
    }

    private static boolean isFormatColor(char colorChar) {
        if (colorChar >= '0') {
            if (colorChar <= '9') return true;
        }
        if (colorChar >= 'a') {
            if (colorChar <= 'f') return true;
        }
        if (colorChar < 'A') return false;
        if (colorChar > 'F') return false;
        return true;
    }

    private static boolean isFormatSpecial(char formatChar) {
        if (formatChar >= 'k') {
            if (formatChar <= 'o') return true;
        }
        if (formatChar >= 'K') {
            if (formatChar <= 'O') return true;
        }
        if (formatChar == 'r') return true;
        if (formatChar == 'R') return true;
        return false;
    }

    public static String getFormatFromString(String text) {
        String s = "";
        int i = -1;
        int j = text.length();
        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i >= j - 1) continue;
            char c0 = text.charAt(i + 1);
            if (FontRenderer.isFormatColor(c0)) {
                s = "\u00a7" + c0;
                continue;
            }
            if (!FontRenderer.isFormatSpecial(c0)) continue;
            s = s + "\u00a7" + c0;
        }
        return s;
    }

    public boolean getBidiFlag() {
        return this.bidiFlag;
    }

    public int getColorCode(char character) {
        return this.colorCode["0123456789abcdef".indexOf(character)];
    }
}

