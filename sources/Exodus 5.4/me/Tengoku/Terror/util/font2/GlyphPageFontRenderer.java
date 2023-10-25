/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util.font2;

import java.awt.Font;
import java.util.Locale;
import java.util.Random;
import me.Tengoku.Terror.util.font2.GlyphPage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GlyphPageFontRenderer {
    private float blue;
    private boolean italicStyle;
    private GlyphPage boldGlyphPage;
    private int[] colorCode;
    private boolean boldStyle;
    private int textColor;
    private boolean underlineStyle;
    private float posY;
    private float alpha;
    private boolean strikethroughStyle;
    private GlyphPage italicGlyphPage;
    private boolean randomStyle;
    private float green;
    private GlyphPage regularGlyphPage;
    public Random fontRandom = new Random();
    private GlyphPage boldItalicGlyphPage;
    private float posX;
    private float red;

    private void doDraw(float f, GlyphPage glyphPage) {
        WorldRenderer worldRenderer;
        Tessellator tessellator;
        if (this.strikethroughStyle) {
            tessellator = Tessellator.getInstance();
            worldRenderer = tessellator.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
            worldRenderer.pos(this.posX, this.posY + (float)(glyphPage.getMaxFontHeight() / 2), 0.0).endVertex();
            worldRenderer.pos(this.posX + f, this.posY + (float)(glyphPage.getMaxFontHeight() / 2), 0.0).endVertex();
            worldRenderer.pos(this.posX + f, this.posY + (float)(glyphPage.getMaxFontHeight() / 2) - 1.0f, 0.0).endVertex();
            worldRenderer.pos(this.posX, this.posY + (float)(glyphPage.getMaxFontHeight() / 2) - 1.0f, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        if (this.underlineStyle) {
            tessellator = Tessellator.getInstance();
            worldRenderer = tessellator.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
            int n = this.underlineStyle ? -1 : 0;
            worldRenderer.pos(this.posX + (float)n, this.posY + (float)glyphPage.getMaxFontHeight(), 0.0).endVertex();
            worldRenderer.pos(this.posX + f, this.posY + (float)glyphPage.getMaxFontHeight(), 0.0).endVertex();
            worldRenderer.pos(this.posX + f, this.posY + (float)glyphPage.getMaxFontHeight() - 1.0f, 0.0).endVertex();
            worldRenderer.pos(this.posX + (float)n, this.posY + (float)glyphPage.getMaxFontHeight() - 1.0f, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        this.posX += f;
    }

    public String trimStringToWidth(String string, int n) {
        return this.trimStringToWidth(string, n, false);
    }

    private int renderString(String string, float f, float f2, int n, boolean bl) {
        if (string == null) {
            return 0;
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
        this.posX = f * 2.0f;
        this.posY = f2 * 2.0f;
        this.renderStringAtPos(string, bl);
        return (int)(this.posX / 4.0f);
    }

    public GlyphPageFontRenderer(GlyphPage glyphPage, GlyphPage glyphPage2, GlyphPage glyphPage3, GlyphPage glyphPage4) {
        this.colorCode = new int[32];
        this.regularGlyphPage = glyphPage;
        this.boldGlyphPage = glyphPage2;
        this.italicGlyphPage = glyphPage3;
        this.boldItalicGlyphPage = glyphPage4;
        int n = 0;
        while (n < 32) {
            int n2 = (n >> 3 & 1) * 85;
            int n3 = (n >> 2 & 1) * 170 + n2;
            int n4 = (n >> 1 & 1) * 170 + n2;
            int n5 = (n & 1) * 170 + n2;
            if (n == 6) {
                n3 += 85;
            }
            if (n >= 16) {
                n3 /= 4;
                n4 /= 4;
                n5 /= 4;
            }
            this.colorCode[n] = (n3 & 0xFF) << 16 | (n4 & 0xFF) << 8 | n5 & 0xFF;
            ++n;
        }
    }

    public int getStringWidth(String string) {
        if (string == null) {
            return 0;
        }
        int n = 0;
        int n2 = string.length();
        boolean bl = false;
        int n3 = 0;
        while (n3 < n2) {
            char c = string.charAt(n3);
            if (c == '\u00a7') {
                bl = true;
            } else if (bl && c >= '0' && c <= 'r') {
                int n4 = "0123456789abcdefklmnor".indexOf(c);
                if (n4 < 16) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                } else if (n4 == 17) {
                    this.boldStyle = true;
                } else if (n4 == 20) {
                    this.italicStyle = true;
                } else if (n4 == 21) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                }
                ++n3;
                bl = false;
            } else {
                if (bl) {
                    --n3;
                }
                c = string.charAt(n3);
                GlyphPage glyphPage = this.getCurrentGlyphPage();
                n = (int)((float)n + (glyphPage.getWidth(c) - 8.0f));
            }
            ++n3;
        }
        return n / 2;
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private GlyphPage getCurrentGlyphPage() {
        if (this.boldStyle && this.italicStyle) {
            return this.boldItalicGlyphPage;
        }
        if (this.boldStyle) {
            return this.boldGlyphPage;
        }
        if (this.italicStyle) {
            return this.italicGlyphPage;
        }
        return this.regularGlyphPage;
    }

    private void renderStringAtPos(String string, boolean bl) {
        GlyphPage glyphPage = this.getCurrentGlyphPage();
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableTexture2D();
        glyphPage.bindTexture();
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        int n = 0;
        while (n < string.length()) {
            char c = string.charAt(n);
            if (c == '\u00a7' && n + 1 < string.length()) {
                int n2 = "0123456789abcdefklmnor".indexOf(string.toLowerCase(Locale.ENGLISH).charAt(n + 1));
                if (n2 < 16) {
                    int n3;
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (n2 < 0) {
                        n2 = 15;
                    }
                    if (bl) {
                        n2 += 16;
                    }
                    this.textColor = n3 = this.colorCode[n2];
                    GlStateManager.color((float)(n3 >> 16) / 255.0f, (float)(n3 >> 8 & 0xFF) / 255.0f, (float)(n3 & 0xFF) / 255.0f, this.alpha);
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
                } else {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                }
                ++n;
            } else {
                glyphPage = this.getCurrentGlyphPage();
                glyphPage.bindTexture();
                float f = glyphPage.drawChar(c, this.posX, this.posY);
                this.doDraw(f, glyphPage);
            }
            ++n;
        }
        glyphPage.unbindTexture();
        GL11.glPopMatrix();
    }

    public String trimStringToWidth(String string, int n, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl2 = false;
        int n2 = bl ? string.length() - 1 : 0;
        int n3 = bl ? -1 : 1;
        int n4 = 0;
        int n5 = n2;
        while (n5 >= 0 && n5 < string.length() && n5 < n) {
            char c = string.charAt(n5);
            if (c == '\u00a7') {
                bl2 = true;
            } else if (bl2 && c >= '0' && c <= 'r') {
                int n6 = "0123456789abcdefklmnor".indexOf(c);
                if (n6 < 16) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                } else if (n6 == 17) {
                    this.boldStyle = true;
                } else if (n6 == 20) {
                    this.italicStyle = true;
                } else if (n6 == 21) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                }
                ++n5;
                bl2 = false;
            } else {
                if (bl2) {
                    --n5;
                }
                c = string.charAt(n5);
                GlyphPage glyphPage = this.getCurrentGlyphPage();
                n4 = (int)((float)n4 + (glyphPage.getWidth(c) - 8.0f) / 2.0f);
            }
            if (n5 > n4) break;
            if (bl) {
                stringBuilder.insert(0, c);
            } else {
                stringBuilder.append(c);
            }
            n5 += n3;
        }
        return stringBuilder.toString();
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

    public int getFontHeight() {
        return this.regularGlyphPage.getMaxFontHeight() / 2;
    }

    public static GlyphPageFontRenderer create(String string, int n, boolean bl, boolean bl2, boolean bl3) {
        char[] cArray = new char[256];
        int n2 = 0;
        while (n2 < cArray.length) {
            cArray[n2] = (char)n2;
            ++n2;
        }
        GlyphPage glyphPage = new GlyphPage(new Font(string, 0, n), true, true);
        glyphPage.generateGlyphPage(cArray);
        glyphPage.setupTexture();
        GlyphPage glyphPage2 = glyphPage;
        GlyphPage glyphPage3 = glyphPage;
        GlyphPage glyphPage4 = glyphPage;
        if (bl) {
            glyphPage2 = new GlyphPage(new Font(string, 1, n), true, true);
            glyphPage2.generateGlyphPage(cArray);
            glyphPage2.setupTexture();
        }
        if (bl2) {
            glyphPage3 = new GlyphPage(new Font(string, 2, n), true, true);
            glyphPage3.generateGlyphPage(cArray);
            glyphPage3.setupTexture();
        }
        if (bl3) {
            glyphPage4 = new GlyphPage(new Font(string, 3, n), true, true);
            glyphPage4.generateGlyphPage(cArray);
            glyphPage4.setupTexture();
        }
        return new GlyphPageFontRenderer(glyphPage, glyphPage2, glyphPage3, glyphPage4);
    }
}

