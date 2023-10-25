/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.util.font;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import me.Tengoku.Terror.util.font.CFont;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class MinecraftFontRenderer
extends CFont {
    DynamicTexture texItalicBold;
    DynamicTexture texItalic;
    int[] colorCode;
    String colorcodeIdentifiers = "0123456789abcdefklmnor";
    DynamicTexture texBold;
    CFont.CharData[] boldChars = new CFont.CharData[256];
    CFont.CharData[] boldItalicChars;
    CFont.CharData[] italicChars = new CFont.CharData[256];

    public List<String> formatString(String string, double d) {
        ArrayList<String> arrayList = new ArrayList<String>();
        StringBuilder stringBuilder = new StringBuilder();
        char c = '\uffff';
        char[] cArray = string.toCharArray();
        int n = 0;
        while (n < cArray.length) {
            char c2 = cArray[n];
            if (c2 == '\u00a7' && n < cArray.length - 1) {
                c = cArray[n + 1];
            }
            StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(stringBuilder.toString()));
            if (this.getStringWidth(stringBuilder2.append(c2).toString()) < d) {
                stringBuilder.append(c2);
            } else {
                arrayList.add(stringBuilder.toString());
                stringBuilder = new StringBuilder("\ufffd" + c + c2);
            }
            ++n;
        }
        if (stringBuilder.length() > 0) {
            arrayList.add(stringBuilder.toString());
        }
        return arrayList;
    }

    public int drawNoBSString(String string, double d, float f, int n) {
        return (int)this.drawNoBSString(string, d, f, n, false);
    }

    public double getPasswordWidth(String string) {
        return this.getStringWidth(string.replaceAll(".", "."), 8.0f);
    }

    public String trimStringToWidth(String string, int n, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        float f = 0.0f;
        int n2 = bl ? string.length() - 1 : 0;
        int n3 = bl ? -1 : 1;
        boolean bl2 = false;
        boolean bl3 = false;
        int n4 = n2;
        while (n4 >= 0 && n4 < string.length() && f < (float)n) {
            char c = string.charAt(n4);
            float f2 = this.getCharWidthFloat(c);
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
            } else {
                stringBuilder.append(c);
            }
            n4 += n3;
        }
        return stringBuilder.toString();
    }

    public int drawString(String string, double d, float f, int n) {
        return (int)this.drawString(string, d, f, n, false, 8.3f);
    }

    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    public int drawPassword(String string, double d, float f, int n) {
        return (int)this.drawString(string.replaceAll(".", "."), d, f, n, false, 8.0f);
    }

    public float drawString(String string, double d, double d2, int n, boolean bl, float f) {
        d -= 1.0;
        if (string == null) {
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
        CFont.CharData[] charDataArray = this.charData;
        float f2 = (float)(n >> 24 & 0xFF) / 255.0f;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;
        boolean bl7 = true;
        d *= 2.0;
        d2 = (d2 - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, f2);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture((int)3553, (int)this.tex.getGlTextureId());
        int n2 = 0;
        while (n2 < string.length()) {
            char c = string.charAt(n2);
            if (c == '\u00a7') {
                int n3 = 21;
                try {
                    n3 = "0123456789abcdefklmnor".indexOf(string.charAt(n2 + 1));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (n3 < 16) {
                    bl3 = false;
                    bl4 = false;
                    bl2 = false;
                    bl6 = false;
                    bl5 = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    charDataArray = this.charData;
                    if (n3 < 0) {
                        n3 = 15;
                    }
                    if (bl) {
                        n3 += 16;
                    }
                    int n4 = this.colorCode[n3];
                    GlStateManager.color((float)(n4 >> 16 & 0xFF) / 255.0f, (float)(n4 >> 8 & 0xFF) / 255.0f, (float)(n4 & 0xFF) / 255.0f, f2);
                } else if (n3 == 16) {
                    bl2 = true;
                } else if (n3 == 17) {
                    bl3 = true;
                    if (bl4) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        charDataArray = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texBold.getGlTextureId());
                        charDataArray = this.boldChars;
                    }
                } else if (n3 == 18) {
                    bl5 = true;
                } else if (n3 == 19) {
                    bl6 = true;
                } else if (n3 == 20) {
                    bl4 = true;
                    if (bl3) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        charDataArray = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                        charDataArray = this.italicChars;
                    }
                } else {
                    bl3 = false;
                    bl4 = false;
                    bl2 = false;
                    bl6 = false;
                    bl5 = false;
                    GlStateManager.color((float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, f2);
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    charDataArray = this.charData;
                }
                ++n2;
            } else if (c < charDataArray.length) {
                GL11.glBegin((int)4);
                this.drawChar(charDataArray, c, (float)d, (float)d2);
                GL11.glEnd();
                if (bl5) {
                    this.drawLine(d, d2 + (double)(charDataArray[c].height / 2), d + (double)charDataArray[c].width - 8.0, d2 + (double)(charDataArray[c].height / 2), 1.0f);
                }
                if (bl6) {
                    this.drawLine(d, d2 + (double)charDataArray[c].height - 2.0, d + (double)charDataArray[c].width - 8.0, d2 + (double)charDataArray[c].height - 2.0, 1.0f);
                }
                d += (double)((float)charDataArray[c].width - f + (float)this.charOffset);
            }
            ++n2;
        }
        GL11.glHint((int)3155, (int)4352);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return (float)d / 2.0f;
    }

    @Override
    public void setFractionalMetrics(boolean bl) {
        super.setFractionalMetrics(bl);
        this.setupBoldItalicIDs();
    }

    public float drawCenteredStringWithShadow(String string, float f, float f2, int n) {
        return this.drawStringWithShadow(string, f - (float)(this.getStringWidth(string) / 2.0), f2, n);
    }

    @Override
    public void setAntiAlias(boolean bl) {
        super.setAntiAlias(bl);
        this.setupBoldItalicIDs();
    }

    private float getCharWidthFloat(char c) {
        if (c == '\u00a7') {
            return -1.0f;
        }
        if (c == ' ') {
            return 2.0f;
        }
        int n = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u001f01RS^_tu~\u0007\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u05d2\ufffd\ufffd\ufffd\ufffd\ufffd\u046a\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0002$abVUcQW]\\[\u0010\u00144,\u001c\u0000<^_ZTif`PlghdeYXRSkj\u0018\f\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u00fc\u0126\ufffd\ufffd\ufffd\u001e\u0005\b)a\ufffded !\ufffdH\ufffd\u0019\ufffd\u001a\u007f\ufffd\ufffd\u0000".indexOf(c);
        if (c > '\u0000' && n != -1) {
            return (float)this.charData[n].width / 2.0f - 4.0f;
        }
        if ((float)this.charData[c].width / 2.0f - 4.0f != 0.0f) {
            int n2 = (int)((float)this.charData[c].width / 2.0f - 4.0f) >>> 4;
            int n3 = (int)((float)this.charData[c].width / 2.0f - 4.0f) & 0xF;
            return (++n3 - (n2 &= 0xF)) / 2 + 1;
        }
        return 0.0f;
    }

    public float drawSmoothString(String string, double d, double d2, int n, boolean bl) {
        d -= 1.0;
        if (string == null) {
            return 0.0f;
        }
        CFont.CharData[] charDataArray = this.charData;
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;
        boolean bl7 = true;
        d *= 2.0;
        d2 = (d2 - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, f);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture((int)3553, (int)this.tex.getGlTextureId());
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        int n2 = 0;
        while (n2 < string.length()) {
            char c = string.charAt(n2);
            if (c == '\u00a7') {
                int n3 = 21;
                try {
                    n3 = "0123456789abcdefklmnor".indexOf(string.charAt(n2 + 1));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (n3 < 16) {
                    bl3 = false;
                    bl4 = false;
                    bl2 = false;
                    bl6 = false;
                    bl5 = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    charDataArray = this.charData;
                    if (n3 < 0) {
                        n3 = 15;
                    }
                    if (bl) {
                        n3 += 16;
                    }
                    int n4 = this.colorCode[n3];
                    GlStateManager.color((float)(n4 >> 16 & 0xFF) / 255.0f, (float)(n4 >> 8 & 0xFF) / 255.0f, (float)(n4 & 0xFF) / 255.0f, f);
                } else if (n3 == 16) {
                    bl2 = true;
                } else if (n3 == 17) {
                    bl3 = true;
                    if (bl4) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        charDataArray = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texBold.getGlTextureId());
                        charDataArray = this.boldChars;
                    }
                } else if (n3 == 18) {
                    bl5 = true;
                } else if (n3 == 19) {
                    bl6 = true;
                } else if (n3 == 20) {
                    bl4 = true;
                    if (bl3) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        charDataArray = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                        charDataArray = this.italicChars;
                    }
                } else {
                    bl3 = false;
                    bl4 = false;
                    bl2 = false;
                    bl6 = false;
                    bl5 = false;
                    GlStateManager.color((float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, f);
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    charDataArray = this.charData;
                }
                ++n2;
            } else if (c < charDataArray.length) {
                GL11.glBegin((int)4);
                this.drawChar(charDataArray, c, (float)d, (float)d2);
                GL11.glEnd();
                if (bl5) {
                    this.drawLine(d, d2 + (double)(charDataArray[c].height / 2), d + (double)charDataArray[c].width - 8.0, d2 + (double)(charDataArray[c].height / 2), 1.0f);
                }
                if (bl6) {
                    this.drawLine(d, d2 + (double)charDataArray[c].height - 2.0, d + (double)charDataArray[c].width - 8.0, d2 + (double)charDataArray[c].height - 2.0, 1.0f);
                }
                d += (double)((float)charDataArray[c].width - 8.3f + (float)this.charOffset);
            }
            ++n2;
        }
        GL11.glHint((int)3155, (int)4352);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return (float)d / 2.0f;
    }

    public String trimStringToWidthPassword(String string, int n, boolean bl) {
        string = string.replaceAll(".", ".");
        return this.trimStringToWidth(string, n, bl);
    }

    public List<String> wrapWords(String string, double d) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (this.getStringWidth(string) > d) {
            String[] stringArray = string.split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            char c = '\uffff';
            String[] stringArray2 = stringArray;
            int n = stringArray.length;
            int n2 = 0;
            while (n2 < n) {
                String string2 = stringArray2[n2];
                int n3 = 0;
                while (n3 < string2.toCharArray().length) {
                    char c2 = string2.toCharArray()[n3];
                    if (c2 == '\u00a7' && n3 < string2.toCharArray().length - 1) {
                        c = string2.toCharArray()[n3 + 1];
                    }
                    ++n3;
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                if (this.getStringWidth(stringBuilder2.append((Object)stringBuilder).append(string2).append(" ").toString()) < d) {
                    stringBuilder.append(string2).append(" ");
                } else {
                    arrayList.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder("\ufffd" + c + string2 + " ");
                }
                ++n2;
            }
            if (stringBuilder.length() > 0) {
                if (this.getStringWidth(stringBuilder.toString()) < d) {
                    arrayList.add("\ufffd" + c + stringBuilder + " ");
                    stringBuilder = new StringBuilder();
                } else {
                    arrayList.addAll(this.formatString(stringBuilder.toString(), d));
                }
            }
        } else {
            arrayList.add(string);
        }
        return arrayList;
    }

    public float drawNoBSString(String string, double d, double d2, int n, boolean bl) {
        d -= 1.0;
        if (string == null) {
            return 0.0f;
        }
        CFont.CharData[] charDataArray = this.charData;
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;
        boolean bl7 = true;
        d *= 2.0;
        d2 = (d2 - 3.0) * 2.0;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, f);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture((int)3553, (int)this.tex.getGlTextureId());
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        int n2 = 0;
        while (n2 < string.length()) {
            char c = string.charAt(n2);
            if (c == '\u00a7') {
                int n3 = 21;
                try {
                    n3 = "0123456789abcdefklmnor".indexOf(string.charAt(n2 + 1));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (n3 < 16) {
                    bl3 = false;
                    bl4 = false;
                    bl2 = false;
                    bl6 = false;
                    bl5 = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    charDataArray = this.charData;
                    if (n3 < 0) {
                        n3 = 15;
                    }
                    if (bl) {
                        n3 += 16;
                    }
                    int n4 = this.colorCode[n3];
                    GlStateManager.color((float)(n4 >> 16 & 0xFF) / 255.0f, (float)(n4 >> 8 & 0xFF) / 255.0f, (float)(n4 & 0xFF) / 255.0f, f);
                } else if (n3 == 16) {
                    bl2 = true;
                } else if (n3 == 17) {
                    bl3 = true;
                    if (bl4) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        charDataArray = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texBold.getGlTextureId());
                        charDataArray = this.boldChars;
                    }
                } else if (n3 == 18) {
                    bl5 = true;
                } else if (n3 == 19) {
                    bl6 = true;
                } else if (n3 == 20) {
                    bl4 = true;
                    if (bl3) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        charDataArray = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                        charDataArray = this.italicChars;
                    }
                } else {
                    bl3 = false;
                    bl4 = false;
                    bl2 = false;
                    bl6 = false;
                    bl5 = false;
                    GlStateManager.color((float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, f);
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    charDataArray = this.charData;
                }
                ++n2;
            } else if (c < charDataArray.length) {
                GL11.glBegin((int)4);
                this.drawChar(charDataArray, c, (float)d, (float)d2);
                GL11.glEnd();
                if (bl5) {
                    this.drawLine(d, d2 + (double)(charDataArray[c].height / 2), d + (double)charDataArray[c].width - 8.0, d2 + (double)(charDataArray[c].height / 2), 1.0f);
                }
                if (bl6) {
                    this.drawLine(d, d2 + (double)charDataArray[c].height - 2.0, d + (double)charDataArray[c].width - 8.0, d2 + (double)charDataArray[c].height - 2.0, 1.0f);
                }
                d += (double)((float)charDataArray[c].width - 8.3f + (float)this.charOffset);
            }
            ++n2;
        }
        GL11.glHint((int)3155, (int)4352);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return (float)d / 2.0f;
    }

    private void drawLine(double d, double d2, double d3, double d4, float f) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    public float drawCenteredString(String string, float f, float f2, int n) {
        return this.drawString(string, f - (float)(this.getStringWidth(string) / 2.0), f2, n);
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    public float drawNoBSCenteredString(String string, float f, float f2, int n) {
        return this.drawNoBSString(string, f - (float)(this.getStringWidth(string) / 2.0), f2, n);
    }

    public int drawStringWithShadow(String string, double d, float f, int n) {
        float f2 = this.drawString(string, d + (double)0.9f, (double)f + 0.5, n, true, 8.3f);
        return (int)Math.max(f2, this.drawString(string, d, f, n, false, 8.3f));
    }

    public MinecraftFontRenderer(Font font, boolean bl, boolean bl2) {
        super(font, bl, bl2);
        this.boldItalicChars = new CFont.CharData[256];
        this.colorCode = new int[32];
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public int drawSmoothString(String string, double d, float f, int n) {
        return (int)this.drawSmoothString(string, d, f, n, false);
    }

    public double getStringWidth(String string) {
        if (string == null) {
            return 0.0;
        }
        float f = 0.0f;
        CFont.CharData[] charDataArray = this.charData;
        boolean bl = false;
        boolean bl2 = false;
        int n = 0;
        while (n < string.length()) {
            char c = string.charAt(n);
            if (c == '\u00a7') {
                int n2 = "0123456789abcdefklmnor".indexOf(c);
                bl = false;
                bl2 = false;
                ++n;
            } else if (c < charDataArray.length) {
                f += (float)charDataArray[c].width - 8.3f + (float)this.charOffset;
            }
            ++n;
        }
        return f / 2.0f;
    }

    public double getStringWidth(String string, float f) {
        if (string == null) {
            return 0.0;
        }
        float f2 = 0.0f;
        CFont.CharData[] charDataArray = this.charData;
        boolean bl = false;
        boolean bl2 = false;
        int n = 0;
        while (n < string.length()) {
            char c = string.charAt(n);
            if (c == '\u00a7') {
                int n2 = "0123456789abcdefklmnor".indexOf(c);
                bl = false;
                bl2 = false;
                ++n;
            } else if (c < charDataArray.length) {
                f2 += (float)charDataArray[c].width - f + (float)this.charOffset;
            }
            ++n;
        }
        return f2 / 2.0f;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    public String trimStringToWidth(String string, int n) {
        return this.trimStringToWidth(string, n, false);
    }

    private void setupMinecraftColorcodes() {
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
}

