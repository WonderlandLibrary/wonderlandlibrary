/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.newdawn.slick.Color
 *  org.newdawn.slick.UnicodeFont
 *  org.newdawn.slick.font.effects.ColorEffect
 */
package ClickGUIs.clickuiutils;

import java.awt.Color;
import java.awt.Font;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class TTFRenderer {
    private final UnicodeFont unicodeFont;
    private final int[] colorCodes = new int[32];
    private float spacing;

    public void drawCenteredString(String string, float f, float f2, int n) {
        this.drawString(string, f - (float)((int)this.getWidth(string) / 2), f2, n);
    }

    public float getHeight(String string) {
        return (float)this.unicodeFont.getHeight(string) / 2.0f;
    }

    public TTFRenderer(String string, int n, int n2) {
        this(string, n, n2, 0.0f);
    }

    public UnicodeFont getFont() {
        return this.unicodeFont;
    }

    public int drawString(String string, float f, float f2, int n) {
        f2 *= 2.0f;
        float f3 = f *= 2.0f;
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean bl = GL11.glIsEnabled((int)3042);
        boolean bl2 = GL11.glIsEnabled((int)2896);
        boolean bl3 = GL11.glIsEnabled((int)3553);
        if (!bl) {
            GL11.glEnable((int)3042);
        }
        if (bl2) {
            GL11.glDisable((int)2896);
        }
        if (bl3) {
            GL11.glDisable((int)3553);
        }
        int n2 = n;
        char[] cArray = string.toCharArray();
        int n3 = 0;
        char[] cArray2 = cArray;
        int n4 = cArray.length;
        int n5 = 0;
        while (n5 < n4) {
            block15: {
                block13: {
                    int n6;
                    char c;
                    block14: {
                        block12: {
                            c = cArray2[n5];
                            if (c == '\r') {
                                f = f3;
                            }
                            if (c == '\n') {
                                f2 += this.getHeight(Character.toString(c)) * 2.0f;
                            }
                            if (c == '\u00a7' || n3 != 0 && n3 != cArray.length - 1 && cArray[n3 - 1] == '\u00a7') break block12;
                            this.unicodeFont.drawString(f, f2, Character.toString(c), new org.newdawn.slick.Color(n2));
                            f += this.getWidth(Character.toString(c)) * 2.0f;
                            break block13;
                        }
                        if (c != ' ') break block14;
                        f += (float)this.unicodeFont.getSpaceWidth();
                        break block13;
                    }
                    if (c != '\u00a7' || n3 == cArray.length - 1) break block13;
                    int n7 = "0123456789abcdefg".indexOf(string.charAt(n3 + 1));
                    if (n7 < 0) break block15;
                    n2 = n6 = this.colorCodes[n7];
                }
                ++n3;
            }
            ++n5;
        }
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        if (bl3) {
            GL11.glEnable((int)3553);
            GlStateManager.bindTexture(0);
        }
        if (bl2) {
            GL11.glEnable((int)2896);
        }
        if (!bl) {
            GL11.glDisable((int)3042);
        }
        GL11.glPopMatrix();
        return (int)f;
    }

    public float getWidth(String string) {
        float f = 0.0f;
        String string2 = StringUtils.stripControlCodes(string);
        char[] cArray = string2.toCharArray();
        int n = cArray.length;
        int n2 = 0;
        while (n2 < n) {
            char c = cArray[n2];
            f += (float)this.unicodeFont.getWidth(Character.toString(c)) + this.spacing;
            ++n2;
        }
        return f / 2.0f;
    }

    public int drawStringWithShadow(String string, float f, float f2, int n) {
        this.drawString(StringUtils.stripControlCodes(string), f + 0.5f, f2 + 0.5f, 0);
        return this.drawString(string, f, f2, n);
    }

    public float getCharWidth(char c) {
        return this.unicodeFont.getWidth(String.valueOf(c));
    }

    public void drawCenteredStringWithShadow(String string, float f, float f2, int n) {
        this.drawCenteredString(StringUtils.stripControlCodes(string), f + 0.5f, f2 + 0.5f, 0);
        this.drawCenteredString(string, f, f2, n);
    }

    public TTFRenderer(String string, int n, int n2, float f) {
        this.unicodeFont = new UnicodeFont(new Font(string, n, n2));
        this.spacing = f;
        this.unicodeFont.addAsciiGlyphs();
        this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.unicodeFont.loadGlyphs();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        int n3 = 0;
        while (n3 < 32) {
            int n4 = (n3 >> 3 & 1) * 85;
            int n5 = (n3 >> 2 & 1) * 170 + n4;
            int n6 = (n3 >> 1 & 1) * 170 + n4;
            int n7 = (n3 >> 0 & 1) * 170 + n4;
            if (n3 == 6) {
                n5 += 85;
            }
            if (n3 >= 16) {
                n5 /= 4;
                n6 /= 4;
                n7 /= 4;
            }
            this.colorCodes[n3] = (n5 & 0xFF) << 16 | (n6 & 0xFF) << 8 | n7 & 0xFF;
            ++n3;
        }
    }
}

