package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.mojang.realmsclient.gui.ChatFormatting;
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
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;
import net.optifine.util.FontUtils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

public class FontRenderer
        implements IResourceManagerReloadListener {
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    private float[] charWidth = new float[256];
    public int FONT_HEIGHT = 9;
    public Random fontRandom = new Random();
    protected byte[] glyphWidth = new byte[65536];
    protected int[] colorCode = new int[32];
    private ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    protected float posX;
    protected float posY;
    private boolean unicodeFlag;
    private boolean bidiFlag;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    protected boolean randomStyle;
    protected boolean boldStyle;
    protected boolean italicStyle;
    protected boolean underlineStyle;
    protected boolean strikethroughStyle;
    private static final String __OBFID = "CL_00000660";
    public GameSettings gameSettings;
    public ResourceLocation locationFontTextureBase;
    public boolean enabled = true;
    public float offsetBold = 1.0f;

    public FontRenderer(GameSettings gameSettings, ResourceLocation resourceLocation, TextureManager textureManager, boolean bl2) {
        this.gameSettings = gameSettings;
        this.locationFontTextureBase = resourceLocation;
        this.locationFontTexture = resourceLocation;
        this.renderEngine = textureManager;
        this.unicodeFlag = bl2;
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        this.bindTexture(this.locationFontTexture);
        for (int i2 = 0; i2 < 32; ++i2) {
            int n2 = (i2 >> 3 & 1) * 85;
            int n3 = (i2 >> 2 & 1) * 170 + n2;
            int n4 = (i2 >> 1 & 1) * 170 + n2;
            int n5 = (i2 >> 0 & 1) * 170 + n2;
            if (i2 == 6) {
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
            if (i2 >= 16) {
                n3 /= 4;
                n4 /= 4;
                n5 /= 4;
            }
            this.colorCode[i2] = (n3 & 0xFF) << 16 | (n4 & 0xFF) << 8 | n5 & 0xFF;
        }
        this.readGlyphSizes();
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        for (int i2 = 0; i2 < unicodePageLocations.length; ++i2) {
            FontRenderer.unicodePageLocations[i2] = null;
        }
        this.readFontTexture();
        this.readGlyphSizes();
    }

    private void readFontTexture() {
        BufferedImage bufferedImage;
        try {
            bufferedImage = TextureUtil.readBufferedImage(this.getResourceInputStream(this.locationFontTexture));
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        Properties properties = FontUtils.readFontProperties(this.locationFontTexture);
        int n2 = bufferedImage.getWidth();
        int n3 = bufferedImage.getHeight();
        int n4 = n2 / 16;
        int n5 = n3 / 16;
        float f2 = (float)n2 / 128.0f;
        float f3 = Config.limit(f2, 1.0f, 2.0f);
        this.offsetBold = 1.0f / f3;
        float f4 = FontUtils.readFloat(properties, "offsetBold", -1.0f);
        if (f4 >= 0.0f) {
            this.offsetBold = f4;
        }
        int[] arrn = new int[n2 * n3];
        bufferedImage.getRGB(0, 0, n2, n3, arrn, 0, n2);
        for (int i2 = 0; i2 < 256; ++i2) {
            int n6 = i2 % 16;
            int n7 = i2 / 16;
            int n8 = 0;
            for (n8 = n4 - 1; n8 >= 0; --n8) {
                int n9 = n6 * n4 + n8;
                boolean bl2 = true;
                for (int i3 = 0; i3 < n5 && bl2; ++i3) {
                    int n10 = (n7 * n5 + i3) * n2;
                    int n11 = arrn[n9 + n10];
                    int n12 = n11 >> 24 & 0xFF;
                    if (n12 <= 16) continue;
                    bl2 = false;
                }
                if (!bl2) break;
            }
            if (i2 == 65) {
                // empty if block
            }
            if (i2 == 32) {
                n8 = n4 <= 8 ? (int)(2.0f * f2) : (int)(1.5f * f2);
            }
            this.charWidth[i2] = (float)(n8 + 1) / f2 + 1.0f;
        }
        FontUtils.readCustomCharWidths(properties, this.charWidth);
    }

    private void readGlyphSizes() {
        InputStream inputStream = null;
        try {
            inputStream = this.getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
            inputStream.read(this.glyphWidth);
        }
        catch (IOException iOException) {
            try {
                throw new RuntimeException(iOException);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(inputStream);
                throw throwable;
            }
        }
        IOUtils.closeQuietly(inputStream);
    }

    private float func_181559_a(char c2, boolean bl2) {
        if (c2 == ' ') {
            return !this.unicodeFlag ? this.charWidth[c2] : 4.0f;
        }
        int n2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c2);
        return n2 != -1 && !this.unicodeFlag ? this.renderDefaultChar(n2, bl2) : this.renderUnicodeChar(c2, bl2);
    }

    protected float renderDefaultChar(int p_78266_1_, boolean p_78266_2_)
    {
        int i = p_78266_1_ % 16 * 8;
        int j = p_78266_1_ / 16 * 8;
        int k = p_78266_2_ ? 1 : 0;
        this.bindTexture(this.locationFontTexture);
        float f = this.charWidth[p_78266_1_];
        float f1 = 7.99F;
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f((float)i / 128.0F, (float)j / 128.0F);
        GL11.glVertex3f(this.posX + (float)k, this.posY, 0.0F);
        GL11.glTexCoord2f((float)i / 128.0F, ((float)j + 7.99F) / 128.0F);
        GL11.glVertex3f(this.posX - (float)k, this.posY + 7.99F, 0.0F);
        GL11.glTexCoord2f(((float)i + f1 - 1.0F) / 128.0F, (float)j / 128.0F);
        GL11.glVertex3f(this.posX + f1 - 1.0F + (float)k, this.posY, 0.0F);
        GL11.glTexCoord2f(((float)i + f1 - 1.0F) / 128.0F, ((float)j + 7.99F) / 128.0F);
        GL11.glVertex3f(this.posX + f1 - 1.0F - (float)k, this.posY + 7.99F, 0.0F);
        GL11.glEnd();
        return f;
    }

    private ResourceLocation getUnicodePageLocation(int n2) {
        if (unicodePageLocations[n2] == null) {
            FontRenderer.unicodePageLocations[n2] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", n2));
            FontRenderer.unicodePageLocations[n2] = FontUtils.getHdFontLocation(unicodePageLocations[n2]);
        }
        return unicodePageLocations[n2];
    }

    private void loadGlyphTexture(int n2) {
        this.bindTexture(this.getUnicodePageLocation(n2));
    }

    protected float renderUnicodeChar(char c2, boolean bl2) {
        if (this.glyphWidth[c2] == 0) {
            return 0.0f;
        }
        int n2 = c2 / 256;
        this.loadGlyphTexture(n2);
        int n3 = this.glyphWidth[c2] >>> 4;
        int n4 = this.glyphWidth[c2] & 0xF;
        float f2 = n3 &= 0xF;
        float f3 = n4 + 1;
        float f4 = (float)(c2 % 16 * 16) + f2;
        float f5 = (c2 & 0xFF) / 16 * 16;
        float f6 = f3 - f2 - 0.02f;
        float f7 = bl2 ? 1.0f : 0.0f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(f4 / 256.0f, f5 / 256.0f);
        GL11.glVertex3f(this.posX + f7, this.posY, 0.0f);
        GL11.glTexCoord2f(f4 / 256.0f, (f5 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.posX - f7, this.posY + 7.99f, 0.0f);
        GL11.glTexCoord2f((f4 + f6) / 256.0f, f5 / 256.0f);
        GL11.glVertex3f(this.posX + f6 / 2.0f + f7, this.posY, 0.0f);
        GL11.glTexCoord2f((f4 + f6) / 256.0f, (f5 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.posX + f6 / 2.0f - f7, this.posY + 7.99f, 0.0f);
        GL11.glEnd();
        return (f3 - f2) / 2.0f + 1.0f;
    }

    public int drawCenteredString(String string, float x, float y, int color) {
        string = replaceText(string);
        return this.drawString(string, x - this.getStringWidthInt(string) / 2, y, color);
    }

    public int drawTotalCenteredString(String string, float x, float y, int color) {
        string = replaceText(string);
        return this.drawString(string, x - this.getStringWidthInt(string) / 2, y - this.FONT_HEIGHT / 2, color);
    }

    public int drawCenteredStringWithShadow(String string, float x, float y, int color) {
        string = replaceText(string);
        return this.drawString(string, x - this.getStringWidthInt(string) / 2, y, color, true);
    }

    public int drawTotalCenteredStringWithShadow(String string, float x, float y, int color) {
        string = replaceText(string);
        return this.drawString(string, x - this.getStringWidthInt(string) / 2, y - this.FONT_HEIGHT / 2, color, true);
    }

    public int drawStringWithShadow(String string, float x, float y, int color) {
        string = replaceText(string);
        return this.drawString(string, x, y, color, true);
    }

    public int drawString(String string, float x, float y, int color) {
        string = replaceText(string);
        return !this.enabled ? 0 : this.drawString(string, x, y, color, false);
    }

    public int drawString(String string, float x, float y, int color, boolean hasAlpha) {
        string = replaceText(string);
        int n3;
        this.enableAlpha();
        this.resetStyles();
        if (hasAlpha) {
            n3 = this.renderString(string, x + 1.0f, y + 1.0f, color, true);
            n3 = Math.max(n3, this.renderString(string, x, y, color, false));
        } else {
            n3 = this.renderString(string, x, y, color, false);
        }
        return n3;
    }

    public void drawHeightCenteredStringWithShadow(String text, float x, float y, int color) {
        drawStringWithShadow(text, x, y -this.FONT_HEIGHT / 2f, color);
    }

    public void drawHeightCenteredString(String text, float x, float y, int color) {
        drawString(text, x, y -this.FONT_HEIGHT / 2f, color);
    }

    private String bidiReorder(String string) {
        try {
            Bidi bidi = new Bidi(new ArabicShaping(8).shape(string), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException arabicShapingException) {
            return string;
        }
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void renderStringAtPos(String string, boolean bl2) {
        string = replaceText(string);
        for (int i2 = 0; i2 < string.length(); ++i2) {
            WorldRenderer worldRenderer;
            Tessellator tessellator;
            char c2;
            int n2;
            char c3 = string.charAt(i2);
            if (c3 == '\u00a7' && i2 + 1 < string.length()) {
                n2 = "0123456789abcdefklmnor".indexOf(string.toLowerCase().charAt(i2 + 1));
                if (n2 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (n2 < 0 || n2 > 15) {
                        n2 = 15;
                    }
                    if (bl2) {
                        n2 += 16;
                    }
                    int n3 = this.colorCode[n2];
                    if (Config.isCustomColors()) {
                        n3 = CustomColors.getTextColor(n2, n3);
                    }
                    this.textColor = n3;
                    this.setColor((float)(n3 >> 16) / 255.0f, (float)(n3 >> 8 & 0xFF) / 255.0f, (float)(n3 & 0xFF) / 255.0f, this.alpha);
                } else if (n2 == 16) {
                    this.randomStyle = true;
                } else if (n2 == 17) {
                    this.boldStyle = true;
                } else if (n2 == 18) {
                    this.strikethroughStyle = true;
                } else if (n2 == 19) {
                    this.underlineStyle = true;
                } else if (n2 == 20) {
                    this.italicStyle = true;
                } else if (n2 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }
                ++i2;
                continue;
            }
            n2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c3);
            if (this.randomStyle && n2 != -1) {
                int n4 = this.getCharWidth(c3);
                while (n4 != this.getCharWidth(c2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(n2 = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length())))) {
                }
                c3 = c2;
            }
            float f2 = n2 != -1 && !this.unicodeFlag ? this.offsetBold : 0.5f;
            char c4 = c2 = (c3 == '\u0000' || n2 == -1 || this.unicodeFlag) && bl2 ? (char)'\u0001' : '\u0000';
            if (c2 != '\u0000') {
                this.posX -= f2;
                this.posY -= f2;
            }
            float f3 = this.func_181559_a(c3, this.italicStyle);
            if (c2 != '\u0000') {
                this.posX += f2;
                this.posY += f2;
            }
            if (this.boldStyle) {
                this.posX += f2;
                if (c2 != '\u0000') {
                    this.posX -= f2;
                    this.posY -= f2;
                }
                this.func_181559_a(c3, this.italicStyle);
                this.posX -= f2;
                if (c2 != '\u0000') {
                    this.posX += f2;
                    this.posY += f2;
                }
                f3 += f2;
            }
            if (this.strikethroughStyle) {
                tessellator = Tessellator.getInstance();
                worldRenderer = tessellator.getWorldRenderer();
                GlStateManager.disableTexture2D();
                worldRenderer.begin(7, DefaultVertexFormats.POSITION);
                worldRenderer.pos(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0).endVertex();
                worldRenderer.pos(this.posX + f3, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0).endVertex();
                worldRenderer.pos(this.posX + f3, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0).endVertex();
                worldRenderer.pos(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }
            if (this.underlineStyle) {
                tessellator = Tessellator.getInstance();
                worldRenderer = tessellator.getWorldRenderer();
                GlStateManager.disableTexture2D();
                worldRenderer.begin(7, DefaultVertexFormats.POSITION);
                int n5 = this.underlineStyle ? -1 : 0;
                worldRenderer.pos(this.posX + (float)n5, this.posY + (float)this.FONT_HEIGHT, 0.0).endVertex();
                worldRenderer.pos(this.posX + f3, this.posY + (float)this.FONT_HEIGHT, 0.0).endVertex();
                worldRenderer.pos(this.posX + f3, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                worldRenderer.pos(this.posX + (float)n5, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }
            this.posX += f3;
        }
    }

    private int renderStringAligned(String string, int n2, int n3, int n4, int n5, boolean bl2) {
        if (this.bidiFlag) {
            float n6 = this.getStringWidthInt(this.bidiReorder(string));
            n2 = (int) (n2 + n4 - n6);
        }
        return this.renderString(string, n2, n3, n5, bl2);
    }

    private int renderString(String string, float f2, float f3, int n2, boolean bl2) {
        if (string == null) {
            return 0;
        }
        string = replaceText(string);
        if (this.bidiFlag) {
            string = this.bidiReorder(string);
        }
        if ((n2 & 0xFC000000) == 0) {
            n2 |= 0xFF000000;
        }
        if (bl2) {
            n2 = (n2 & 0xFCFCFC) >> 2 | n2 & 0xFF000000;
        }
        this.red = (float)(n2 >> 16 & 0xFF) / 255.0f;
        this.blue = (float)(n2 >> 8 & 0xFF) / 255.0f;
        this.green = (float)(n2 & 0xFF) / 255.0f;
        this.alpha = (float)(n2 >> 24 & 0xFF) / 255.0f;
        this.setColor(this.red, this.blue, this.green, this.alpha);
        this.posX = f2;
        this.posY = f3;
        this.renderStringAtPos(string, bl2);
        return (int)this.posX;
    }


    public int getStringWidthInt(String s) {
        return (int) this.getStringWidth(s);
    }

    public float getStringWidth(String string) {
        if (string == null) {
            return 0;
        }
        string = replaceText(string);
        float f2 = 0.0f;
        boolean bl2 = false;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            float f3 = this.getCharWidthFloat(c2);
            if (f3 < 0.0f && i2 < string.length() - 1) {
                if ((c2 = string.charAt(++i2)) != 'l' && c2 != 'L') {
                    if (c2 == 'r' || c2 == 'R') {
                        bl2 = false;
                    }
                } else {
                    bl2 = true;
                }
                f3 = 0.0f;
            }
            f2 += f3;
            if (!bl2 || !(f3 > 0.0f)) continue;
            f2 += this.unicodeFlag ? 1.0f : this.offsetBold;
        }
        return (int)f2;
    }

    public String replaceText(String string) {
        if(Minecraft.getMinecraft() != null && Minecraft.getMinecraft().getCurrentServerData() != null) {
            if(Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("loyisa")) {
                /// §1Atani§r TabioXt §aVanilla
                string = string.replace("§1Atani", ChatFormatting.GOLD.toString() + ChatFormatting.BOLD.toString() + "ATANI");
            }
        }
        return string;
    }

    public int getCharWidth(char c2) {
        return Math.round(this.getCharWidthFloat(c2));
    }

    private float getCharWidthFloat(char c2) {
        if (c2 == '\u00a7') {
            return -1.0f;
        }
        if (c2 == ' ') {
            return this.charWidth[32];
        }
        int n2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c2);
        if (c2 > '\u0000' && n2 != -1 && !this.unicodeFlag) {
            return this.charWidth[n2];
        }
        if (this.glyphWidth[c2] != 0) {
            int n3 = this.glyphWidth[c2] >>> 4;
            int n4 = this.glyphWidth[c2] & 0xF;
            return (++n4 - (n3 &= 0xF)) / 2 + 1;
        }
        return 0.0f;
    }

    public String trimStringToWidth(String string, int n2) {
        return this.trimStringToWidth(string, n2, false);
    }

    public String trimStringToWidth(String string, int n2, boolean bl2) {
        StringBuilder stringBuilder = new StringBuilder();
        float f2 = 0.0f;
        int n3 = bl2 ? string.length() - 1 : 0;
        int n4 = bl2 ? -1 : 1;
        boolean bl3 = false;
        boolean bl4 = false;
        for (int i2 = n3; i2 >= 0 && i2 < string.length() && f2 < (float)n2; i2 += n4) {
            char c2 = string.charAt(i2);
            float f3 = this.getCharWidthFloat(c2);
            if (bl3) {
                bl3 = false;
                if (c2 != 'l' && c2 != 'L') {
                    if (c2 == 'r' || c2 == 'R') {
                        bl4 = false;
                    }
                } else {
                    bl4 = true;
                }
            } else if (f3 < 0.0f) {
                bl3 = true;
            } else {
                f2 += f3;
                if (bl4) {
                    f2 += 1.0f;
                }
            }
            if (f2 > (float)n2) break;
            if (bl2) {
                stringBuilder.insert(0, c2);
                continue;
            }
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    private String trimStringNewline(String string) {
        while (string != null && string.endsWith("\n")) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    public void drawSplitString(String string, int n2, int n3, int n4, int n5) {
        this.resetStyles();
        this.textColor = n5;
        string = this.trimStringNewline(string);
        this.renderSplitString(string, n2, n3, n4, false);
    }

    private void renderSplitString(String string, int n2, int n3, int n4, boolean bl2) {
        for (Object e2 : this.listFormattedStringToWidth(string, n4)) {
            this.renderStringAligned((String)e2, n2, n3, n4, this.textColor, bl2);
            n3 += this.FONT_HEIGHT;
        }
    }

    public int splitStringWidth(String string, int n2) {
        return this.FONT_HEIGHT * this.listFormattedStringToWidth(string, n2).size();
    }

    public void setUnicodeFlag(boolean bl2) {
        this.unicodeFlag = bl2;
    }

    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }

    public void setBidiFlag(boolean bl2) {
        this.bidiFlag = bl2;
    }

    public List<String> listFormattedStringToWidth(String string, int n2) {
        return Arrays.asList(this.wrapFormattedStringToWidth(string, n2).split("\n"));
    }

    String wrapFormattedStringToWidth(String string, int n2) {
        int n3 = this.sizeStringToWidth(string, n2);
        if (string.length() <= n3) {
            return string;
        }
        String string2 = string.substring(0, n3);
        char c2 = string.charAt(n3);
        boolean bl2 = c2 == ' ' || c2 == '\n';
        String string3 = FontRenderer.getFormatFromString(string2) + string.substring(n3 + (bl2 ? 1 : 0));
        return string2 + "\n" + this.wrapFormattedStringToWidth(string3, n2);
    }

    private int sizeStringToWidth(String string, int n2) {
        int n3;
        int n4 = string.length();
        float f2 = 0.0f;
        int n5 = -1;
        boolean bl2 = false;
        for (n3 = 0; n3 < n4; ++n3) {
            char c2 = string.charAt(n3);
            switch (c2) {
                case '\n': {
                    --n3;
                    break;
                }
                case ' ': {
                    n5 = n3;
                }
                default: {
                    f2 += this.getCharWidthFloat(c2);
                    if (!bl2) break;
                    f2 += 1.0f;
                    break;
                }
                case '\u00a7': {
                    char c3;
                    if (n3 >= n4 - 1) break;
                    if ((c3 = string.charAt(++n3)) != 'l' && c3 != 'L') {
                        if (c3 != 'r' && c3 != 'R' && !FontRenderer.isFormatColor(c3)) break;
                        bl2 = false;
                        break;
                    }
                    bl2 = true;
                }
            }
            if (c2 == '\n') {
                n5 = ++n3;
                break;
            }
            if (f2 > (float)n2) break;
        }
        return n3 != n4 && n5 != -1 && n5 < n3 ? n5 : n3;
    }

    private static boolean isFormatColor(char c2) {
        return c2 >= '0' && c2 <= '9' || c2 >= 'a' && c2 <= 'f' || c2 >= 'A' && c2 <= 'F';
    }

    private static boolean isFormatSpecial(char c2) {
        return c2 >= 'k' && c2 <= 'o' || c2 >= 'K' && c2 <= 'O' || c2 == 'r' || c2 == 'R';
    }

    public static String getFormatFromString(String string) {
        String string2 = "";
        int n2 = -1;
        int n3 = string.length();
        while ((n2 = string.indexOf(167, n2 + 1)) != -1) {
            if (n2 >= n3 - 1) continue;
            char c2 = string.charAt(n2 + 1);
            if (FontRenderer.isFormatColor(c2)) {
                string2 = "\u00a7" + c2;
                continue;
            }
            if (!FontRenderer.isFormatSpecial(c2)) continue;
            string2 = string2 + "\u00a7" + c2;
        }
        return string2;
    }

    public boolean getBidiFlag() {
        return this.bidiFlag;
    }

    public int getColorCode(char c2) {
        int n2 = "0123456789abcdef".indexOf(c2);
        if (n2 >= 0 && n2 < this.colorCode.length) {
            int n3 = this.colorCode[n2];
            if (Config.isCustomColors()) {
                n3 = CustomColors.getTextColor(n2, n3);
            }
            return n3;
        }
        return 0xFFFFFF;
    }

    protected void setColor(float f2, float f3, float f4, float f5) {
        GlStateManager.color(f2, f3, f4, f5);
    }

    protected void enableAlpha() {
        GlStateManager.enableAlpha();
    }

    protected void bindTexture(ResourceLocation resourceLocation) {
        this.renderEngine.bindTexture(resourceLocation);
    }

    protected InputStream getResourceInputStream(ResourceLocation resourceLocation) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream();
    }

    public Font getFont() {
        return null;
    }

    public int getHeight() {
        return this.FONT_HEIGHT;
    }
}

