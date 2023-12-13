/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
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
import optfine.Config;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

public class FontRenderer
implements IResourceManagerReloadListener {
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    private float[] charWidth = new float[256];
    public int FONT_HEIGHT = 9;
    public Random fontRandom = new Random();
    private byte[] glyphWidth = new byte[65536];
    private int[] colorCode = new int[32];
    private ResourceLocation locationFontTexture;
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
    private static final String __OBFID = "CL_00000660";
    public GameSettings gameSettings;
    public ResourceLocation locationFontTextureBase;
    public boolean enabled = true;
    public float scaleFactor = 1.0f;

    public FontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        this.gameSettings = gameSettingsIn;
        this.locationFontTextureBase = location;
        this.locationFontTexture = location;
        this.renderEngine = textureManagerIn;
        this.unicodeFlag = unicode;
        this.locationFontTexture = FontRenderer.getHdFontLocation(this.locationFontTextureBase);
        textureManagerIn.bindTexture(this.locationFontTexture);
        for (int i2 = 0; i2 < 32; ++i2) {
            int j2 = (i2 >> 3 & 1) * 85;
            int k2 = (i2 >> 2 & 1) * 170 + j2;
            int l2 = (i2 >> 1 & 1) * 170 + j2;
            int i1 = (i2 >> 0 & 1) * 170 + j2;
            if (i2 == 6) {
                k2 += 85;
            }
            if (gameSettingsIn.anaglyph) {
                int j1 = (k2 * 30 + l2 * 59 + i1 * 11) / 100;
                int k1 = (k2 * 30 + l2 * 70) / 100;
                int l1 = (k2 * 30 + i1 * 70) / 100;
                k2 = j1;
                l2 = k1;
                i1 = l1;
            }
            if (i2 >= 16) {
                k2 /= 4;
                l2 /= 4;
                i1 /= 4;
            }
            this.colorCode[i2] = (k2 & 0xFF) << 16 | (l2 & 0xFF) << 8 | i1 & 0xFF;
        }
        this.readGlyphSizes();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.locationFontTexture = FontRenderer.getHdFontLocation(this.locationFontTextureBase);
        for (int i2 = 0; i2 < unicodePageLocations.length; ++i2) {
            FontRenderer.unicodePageLocations[i2] = null;
        }
        this.readFontTexture();
    }

    private void readFontTexture() {
        float f2;
        BufferedImage bufferedimage;
        try {
            bufferedimage = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture).getInputStream());
        }
        catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
        int i2 = bufferedimage.getWidth();
        int j2 = bufferedimage.getHeight();
        int k2 = i2 / 16;
        int l2 = j2 / 16;
        this.scaleFactor = f2 = (float)i2 / 128.0f;
        int[] aint = new int[i2 * j2];
        bufferedimage.getRGB(0, 0, i2, j2, aint, 0, i2);
        for (int i1 = 0; i1 < 256; ++i1) {
            int j1 = i1 % 16;
            int k1 = i1 / 16;
            int l1 = 0;
            for (l1 = k2 - 1; l1 >= 0; --l1) {
                int i22 = j1 * k2 + l1;
                boolean flag = true;
                for (int j22 = 0; j22 < l2 && flag; ++j22) {
                    int k22 = (k1 * l2 + j22) * i2;
                    int l22 = aint[i22 + k22];
                    int i3 = l22 >> 24 & 0xFF;
                    if (i3 <= 16) continue;
                    flag = false;
                }
                if (!flag) break;
            }
            if (i1 == 65) {
                // empty if block
            }
            if (i1 == 32) {
                l1 = k2 <= 8 ? (int)(2.0f * f2) : (int)(1.5f * f2);
            }
            this.charWidth[i1] = (float)(l1 + 1) / f2 + 1.0f;
        }
        this.readCustomCharWidths();
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
        if (p_181559_1_ == ' ') {
            return this.charWidth[p_181559_1_];
        }
        int i2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_181559_1_);
        return i2 != -1 && !this.unicodeFlag ? this.renderDefaultChar(i2, p_181559_2_) : this.renderUnicodeChar(p_181559_1_, p_181559_2_);
    }

    private float renderDefaultChar(int p_78266_1_, boolean p_78266_2_) {
        int i2 = p_78266_1_ % 16 * 8;
        int j2 = p_78266_1_ / 16 * 8;
        boolean k2 = p_78266_2_;
        this.renderEngine.bindTexture(this.locationFontTexture);
        float f2 = this.charWidth[p_78266_1_];
        float f1 = 7.99f;
        GL11.glBegin(5);
        GL11.glTexCoord2f((float)i2 / 128.0f, (float)j2 / 128.0f);
        GL11.glVertex3f(this.posX + (float)k2, this.posY, 0.0f);
        GL11.glTexCoord2f((float)i2 / 128.0f, ((float)j2 + 7.99f) / 128.0f);
        GL11.glVertex3f(this.posX - (float)k2, this.posY + 7.99f, 0.0f);
        GL11.glTexCoord2f(((float)i2 + f1 - 1.0f) / 128.0f, (float)j2 / 128.0f);
        GL11.glVertex3f(this.posX + f1 - 1.0f + (float)k2, this.posY, 0.0f);
        GL11.glTexCoord2f(((float)i2 + f1 - 1.0f) / 128.0f, ((float)j2 + 7.99f) / 128.0f);
        GL11.glVertex3f(this.posX + f1 - 1.0f - (float)k2, this.posY + 7.99f, 0.0f);
        GL11.glEnd();
        return f2;
    }

    private ResourceLocation getUnicodePageLocation(int p_111271_1_) {
        if (unicodePageLocations[p_111271_1_] == null) {
            FontRenderer.unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", p_111271_1_));
            FontRenderer.unicodePageLocations[p_111271_1_] = FontRenderer.getHdFontLocation(unicodePageLocations[p_111271_1_]);
        }
        return unicodePageLocations[p_111271_1_];
    }

    private void loadGlyphTexture(int p_78257_1_) {
        this.renderEngine.bindTexture(this.getUnicodePageLocation(p_78257_1_));
    }

    private float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_) {
        if (this.glyphWidth[p_78277_1_] == 0) {
            return 0.0f;
        }
        int i2 = p_78277_1_ / 256;
        this.loadGlyphTexture(i2);
        int j2 = this.glyphWidth[p_78277_1_] >>> 4;
        int k2 = this.glyphWidth[p_78277_1_] & 0xF;
        float f2 = j2;
        float f1 = k2 + 1;
        float f22 = (float)(p_78277_1_ % 16 * 16) + f2;
        float f3 = (p_78277_1_ & 0xFF) / 16 * 16;
        float f4 = f1 - f2 - 0.02f;
        float f5 = p_78277_2_ ? 1.0f : 0.0f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(f22 / 256.0f, f3 / 256.0f);
        GL11.glVertex3f(this.posX + f5, this.posY, 0.0f);
        GL11.glTexCoord2f(f22 / 256.0f, (f3 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.posX - f5, this.posY + 7.99f, 0.0f);
        GL11.glTexCoord2f((f22 + f4) / 256.0f, f3 / 256.0f);
        GL11.glVertex3f(this.posX + f4 / 2.0f + f5, this.posY, 0.0f);
        GL11.glTexCoord2f((f22 + f4) / 256.0f, (f3 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.posX + f4 / 2.0f - f5, this.posY + 7.99f, 0.0f);
        GL11.glEnd();
        return (f1 - f2) / 2.0f + 1.0f;
    }

    public int drawStringWithShadow(String text, float x2, float y2, int color) {
        return this.drawString(text, x2, y2, color, true);
    }

    public int drawString(String text, int x2, int y2, int color) {
        return !this.enabled ? 0 : this.drawString(text, x2, y2, color, false);
    }

    public int drawString(String text, float x2, float y2, int color, boolean dropShadow) {
        int i2;
        GlStateManager.enableAlpha();
        this.resetStyles();
        if (dropShadow) {
            i2 = this.renderString(text, x2 + 1.0f, y2 + 1.0f, color, true);
            i2 = Math.max(i2, this.renderString(text, x2, y2, color, false));
        } else {
            i2 = this.renderString(text, x2, y2, color, false);
        }
        return i2;
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
        for (int i2 = 0; i2 < p_78255_1_.length(); ++i2) {
            boolean flag;
            char c0 = p_78255_1_.charAt(i2);
            if (c0 == '\u00a7' && i2 + 1 < p_78255_1_.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(i2 + 1));
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
                ++i2;
                continue;
            }
            int j2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);
            if (this.randomStyle && j2 != -1) {
                char c1;
                int k2 = this.getCharWidth(c0);
                while (k2 != this.getCharWidth(c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j2 = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length())))) {
                }
                c0 = c1;
            }
            float f1 = this.unicodeFlag ? 0.5f : 1.0f / this.scaleFactor;
            boolean bl = flag = (c0 == '\u0000' || j2 == -1 || this.unicodeFlag) && p_78255_2_;
            if (flag) {
                this.posX -= f1;
                this.posY -= f1;
            }
            float f2 = this.func_181559_a(c0, this.italicStyle);
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
                f2 += f1;
            }
            if (this.strikethroughStyle) {
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                GlStateManager.disableTexture2D();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                worldrenderer.pos(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0).endVertex();
                worldrenderer.pos(this.posX + f2, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0).endVertex();
                worldrenderer.pos(this.posX + f2, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0).endVertex();
                worldrenderer.pos(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }
            if (this.underlineStyle) {
                Tessellator tessellator1 = Tessellator.getInstance();
                WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
                GlStateManager.disableTexture2D();
                worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
                int l2 = this.underlineStyle ? -1 : 0;
                worldrenderer1.pos(this.posX + (float)l2, this.posY + (float)this.FONT_HEIGHT, 0.0).endVertex();
                worldrenderer1.pos(this.posX + f2, this.posY + (float)this.FONT_HEIGHT, 0.0).endVertex();
                worldrenderer1.pos(this.posX + f2, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                worldrenderer1.pos(this.posX + (float)l2, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                tessellator1.draw();
                GlStateManager.enableTexture2D();
            }
            this.posX += f2;
        }
    }

    private int renderStringAligned(String text, int x2, int y2, int p_78274_4_, int color, boolean dropShadow) {
        if (this.bidiFlag) {
            int i2 = this.getStringWidth(this.bidiReorder(text));
            x2 = x2 + p_78274_4_ - i2;
        }
        return this.renderString(text, x2, y2, color, dropShadow);
    }

    private int renderString(String text, float x2, float y2, int color, boolean dropShadow) {
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
        this.posX = x2;
        this.posY = y2;
        this.renderStringAtPos(text, dropShadow);
        return (int)this.posX;
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        float f2 = 0.0f;
        boolean flag = false;
        for (int i2 = 0; i2 < text.length(); ++i2) {
            char c0 = text.charAt(i2);
            float f1 = this.getCharWidthFloat(c0);
            if (f1 < 0.0f && i2 < text.length() - 1) {
                if ((c0 = text.charAt(++i2)) != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag = false;
                    }
                } else {
                    flag = true;
                }
                f1 = 0.0f;
            }
            f2 += f1;
            if (!flag || !(f1 > 0.0f)) continue;
            f2 += 1.0f;
        }
        return (int)f2;
    }

    public int getCharWidth(char character) {
        return Math.round(this.getCharWidthFloat(character));
    }

    private float getCharWidthFloat(char p_getCharWidthFloat_1_) {
        if (p_getCharWidthFloat_1_ == '\u00a7') {
            return -1.0f;
        }
        if (p_getCharWidthFloat_1_ == ' ') {
            return this.charWidth[32];
        }
        int i2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_getCharWidthFloat_1_);
        if (p_getCharWidthFloat_1_ > '\u0000' && i2 != -1 && !this.unicodeFlag) {
            return this.charWidth[i2];
        }
        if (this.glyphWidth[p_getCharWidthFloat_1_] != 0) {
            int j2 = this.glyphWidth[p_getCharWidthFloat_1_] >>> 4;
            int k2 = this.glyphWidth[p_getCharWidthFloat_1_] & 0xF;
            if (k2 > 7) {
                k2 = 15;
                j2 = 0;
            }
            return (++k2 - j2) / 2 + 1;
        }
        return 0.0f;
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        float f2 = 0.0f;
        int i2 = reverse ? text.length() - 1 : 0;
        int j2 = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int k2 = i2; k2 >= 0 && k2 < text.length() && f2 < (float)width; k2 += j2) {
            char c0 = text.charAt(k2);
            float f1 = this.getCharWidthFloat(c0);
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
                f2 += f1;
                if (flag1) {
                    f2 += 1.0f;
                }
            }
            if (f2 > (float)width) break;
            if (reverse) {
                stringbuilder.insert(0, c0);
                continue;
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.toString();
    }

    private String trimStringNewline(String text) {
        while (text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    public void drawSplitString(String str, int x2, int y2, int wrapWidth, int textColor) {
        this.resetStyles();
        this.textColor = textColor;
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x2, y2, wrapWidth, false);
    }

    private void renderSplitString(String str, int x2, int y2, int wrapWidth, boolean addShadow) {
        for (Object s2 : this.listFormattedStringToWidth(str, wrapWidth)) {
            this.renderStringAligned((String)s2, x2, y2, wrapWidth, this.textColor, addShadow);
            y2 += this.FONT_HEIGHT;
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

    public List listFormattedStringToWidth(String str, int wrapWidth) {
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }

    String wrapFormattedStringToWidth(String str, int wrapWidth) {
        int i2 = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= i2) {
            return str;
        }
        String s2 = str.substring(0, i2);
        char c0 = str.charAt(i2);
        boolean flag = c0 == ' ' || c0 == '\n';
        String s1 = FontRenderer.getFormatFromString(s2) + str.substring(i2 + (flag ? 1 : 0));
        return s2 + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
    }

    private int sizeStringToWidth(String str, int wrapWidth) {
        int j2;
        int i2 = str.length();
        float f2 = 0.0f;
        int k2 = -1;
        boolean flag = false;
        for (j2 = 0; j2 < i2; ++j2) {
            char c0 = str.charAt(j2);
            switch (c0) {
                case '\n': {
                    --j2;
                    break;
                }
                case ' ': {
                    k2 = j2;
                }
                default: {
                    f2 += this.getCharWidthFloat(c0);
                    if (!flag) break;
                    f2 += 1.0f;
                    break;
                }
                case '\u00a7': {
                    char c1;
                    if (j2 >= i2 - 1) break;
                    if ((c1 = str.charAt(++j2)) != 'l' && c1 != 'L') {
                        if (c1 != 'r' && c1 != 'R' && !FontRenderer.isFormatColor(c1)) break;
                        flag = false;
                        break;
                    }
                    flag = true;
                }
            }
            if (c0 == '\n') {
                k2 = ++j2;
                break;
            }
            if (f2 > (float)wrapWidth) break;
        }
        return j2 != i2 && k2 != -1 && k2 < j2 ? k2 : j2;
    }

    private static boolean isFormatColor(char colorChar) {
        return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
    }

    private static boolean isFormatSpecial(char formatChar) {
        return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
    }

    public static String getFormatFromString(String text) {
        String s2 = "";
        int i2 = -1;
        int j2 = text.length();
        while ((i2 = text.indexOf(167, i2 + 1)) != -1) {
            if (i2 >= j2 - 1) continue;
            char c0 = text.charAt(i2 + 1);
            if (FontRenderer.isFormatColor(c0)) {
                s2 = "\u00a7" + c0;
                continue;
            }
            if (!FontRenderer.isFormatSpecial(c0)) continue;
            s2 = s2 + "\u00a7" + c0;
        }
        return s2;
    }

    public boolean getBidiFlag() {
        return this.bidiFlag;
    }

    public int getColorCode(char character) {
        int i2 = "0123456789abcdef".indexOf(character);
        return i2 >= 0 && i2 < this.colorCode.length ? this.colorCode[i2] : 0xFFFFFF;
    }

    private void readCustomCharWidths() {
        String s1;
        String s2 = this.locationFontTexture.getResourcePath();
        if (s2.endsWith(s1 = ".png")) {
            String s22 = s2.substring(0, s2.length() - s1.length()) + ".properties";
            try {
                ResourceLocation resourcelocation = new ResourceLocation(this.locationFontTexture.getResourceDomain(), s22);
                InputStream inputstream = Config.getResourceStream(Config.getResourceManager(), resourcelocation);
                if (inputstream == null) {
                    return;
                }
                Config.log("Loading " + s22);
                Properties properties = new Properties();
                properties.load(inputstream);
                for (Object s30 : properties.keySet()) {
                    String s6;
                    float f2;
                    String s5;
                    int i2;
                    String s4;
                    String s3 = (String)s30;
                    if (!s3.startsWith(s4 = "width.") || (i2 = Config.parseInt(s5 = s3.substring(s4.length()), -1)) < 0 || i2 >= this.charWidth.length || !((f2 = Config.parseFloat(s6 = properties.getProperty(s3), -1.0f)) >= 0.0f)) continue;
                    this.charWidth[i2] = f2;
                }
            }
            catch (FileNotFoundException resourcelocation) {
            }
            catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
    }

    private static ResourceLocation getHdFontLocation(ResourceLocation p_getHdFontLocation_0_) {
        if (!Config.isCustomFonts()) {
            return p_getHdFontLocation_0_;
        }
        if (p_getHdFontLocation_0_ == null) {
            return p_getHdFontLocation_0_;
        }
        String s2 = p_getHdFontLocation_0_.getResourcePath();
        String s1 = "textures/";
        String s22 = "mcpatcher/";
        if (!s2.startsWith(s1)) {
            return p_getHdFontLocation_0_;
        }
        s2 = s2.substring(s1.length());
        s2 = s22 + s2;
        ResourceLocation resourcelocation = new ResourceLocation(p_getHdFontLocation_0_.getResourceDomain(), s2);
        return Config.hasResource(Config.getResourceManager(), resourcelocation) ? resourcelocation : p_getHdFontLocation_0_;
    }
}

