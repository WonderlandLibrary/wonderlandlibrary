/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.lwjgl.opengl.GL11
 */
package jx.utils.CFont;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import jx.utils.CFont.CFont;
import jx.utils.LanguageManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer
extends CFont {
    protected CFont.CharData[] boldChars = new CFont.CharData[256];
    protected CFont.CharData[] italicChars = new CFont.CharData[256];
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public float drawStringWithShadow(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 0.5, y + 0.5, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawString(String text, float x, float y, int color) {
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.color(color);
        return this.drawString(text, x, y, color, false);
    }

    public float drawString(String text, int x, int y, int color) {
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.color(color);
        return this.drawString(text, x, y, color, false);
    }

    public float drawCenteredString(String text, double x, double y, int color) {
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
        return this.drawString(text, (float)(x - (double)(this.getStringWidth(text) / 2)), (float)y, color);
    }

    public float drawCenteredString(String text, float x, float y, int color) {
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
        return this.drawString(text, (float)((double)x - (double)(this.getStringWidth(text) / 2)), y, color);
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
        return this.drawStringWithShadow(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public static boolean isChinese(char c) {
        String s = String.valueOf(c);
        return !"1234567890abcdefghijklmnopqrstuvwxyz!<>@#$%^&*()-_=+[]{}|\\/'\",.~`".contains(s.toLowerCase());
    }

    public float drawCenteredStringWithShadow(String text, double x, double y, int color) {
        return this.drawStringWithShadow(text, x - (double)(this.getStringWidth(text) / 2), y, color);
    }

    public static float DisplayFont(String str, float x, float y, int color, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str = " " + str;
        for (int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("\u00a7") && iF + 1 <= str.length()) {
                color = CFontRenderer.getColor(String.valueOf(str.toCharArray()[iF + 1]));
                ++iF;
                continue;
            }
            if (!CFontRenderer.isChinese(s.charAt(0))) {
                font.drawString(s, x - 0.5f, y + 1.0f, color);
                x += (float)font.getStringWidth(s);
                continue;
            }
            Fonts.font40.drawString(s, x + 0.5f, y + 1.0f, color);
            x += (float)Fonts.font40.getStringWidth(s);
        }
        return x;
    }

    public float DisplayFont(CFontRenderer font, String str, int x, float y, int color) {
        return CFontRenderer.DisplayFont(str, x, y, color, font);
    }

    public float DisplayFonts(CFontRenderer font, String str, int x, int y, int color) {
        return CFontRenderer.DisplayFont(str, x, y, color, font);
    }

    public float DisplayFonts(String str, float x, float y, int color, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str = " " + str;
        for (int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("\u00a7") && iF + 1 <= str.length()) {
                color = CFontRenderer.getColor(String.valueOf(str.toCharArray()[iF + 1]));
                ++iF;
                continue;
            }
            if (!CFontRenderer.isChinese(s.charAt(0))) {
                font.drawString(s, x - 0.5f, y + 1.0f, color);
                x += (float)font.getStringWidth(s);
                continue;
            }
            Fonts.font40.drawString(s, x + 0.5f, y + 1.0f, color);
            x += (float)Fonts.font40.getStringWidth(s);
        }
        return x;
    }

    public float DisplayFont2(CFontRenderer font, String str, int x, int y, int color, boolean shadow) {
        if (shadow) {
            return CFontRenderer.DisplayFont(str, x, y, color, shadow, font);
        }
        return CFontRenderer.DisplayFont(str, x, y, color, font);
    }

    public static int getColor(String str) {
        switch (str.hashCode()) {
            case 48: {
                if (!str.equals("0")) break;
                return new Color(0, 0, 0).getRGB();
            }
            case 49: {
                if (!str.equals("1")) break;
                return new Color(0, 0, 189).getRGB();
            }
            case 50: {
                if (!str.equals("2")) break;
                return new Color(0, 192, 0).getRGB();
            }
            case 51: {
                if (!str.equals("3")) break;
                return new Color(0, 190, 190).getRGB();
            }
            case 52: {
                if (!str.equals("4")) break;
                return new Color(190, 0, 0).getRGB();
            }
            case 53: {
                if (!str.equals("5")) break;
                return new Color(189, 0, 188).getRGB();
            }
            case 54: {
                if (!str.equals("6")) break;
                return new Color(218, 163, 47).getRGB();
            }
            case 55: {
                if (!str.equals("7")) break;
                return new Color(190, 190, 190).getRGB();
            }
            case 56: {
                if (!str.equals("8")) break;
                return new Color(63, 63, 63).getRGB();
            }
            case 57: {
                if (!str.equals("9")) break;
                return new Color(63, 64, 253).getRGB();
            }
            case 97: {
                if (!str.equals("a")) break;
                return new Color(63, 254, 63).getRGB();
            }
            case 98: {
                if (!str.equals("b")) break;
                return new Color(62, 255, 254).getRGB();
            }
            case 99: {
                if (!str.equals("c")) break;
                return new Color(254, 61, 62).getRGB();
            }
            case 100: {
                if (!str.equals("d")) break;
                return new Color(255, 64, 255).getRGB();
            }
            case 101: {
                if (!str.equals("e")) break;
                return new Color(254, 254, 62).getRGB();
            }
            case 102: {
                if (!str.equals("f")) break;
                return new Color(255, 255, 255).getRGB();
            }
        }
        return new Color(255, 255, 255).getRGB();
    }

    public static float DisplayFont(String str, float x, float y, int color, boolean shadow, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str = " " + str;
        for (int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("\u00a7") && iF + 1 <= str.length()) {
                color = CFontRenderer.getColor(String.valueOf(str.toCharArray()[iF + 1]));
                ++iF;
                continue;
            }
            if (!CFontRenderer.isChinese(s.charAt(0))) {
                font.drawString(s, x + 0.5f, y + 1.5f, new Color(0, 0, 0, 100).getRGB());
                font.drawString(s, x - 0.5f, y + 0.5f, color);
                x += (float)font.getStringWidth(s);
                continue;
            }
            Fonts.font40.drawString(s, x + 1.5f, y + 2.0f, new Color(0, 0, 0, 50).getRGB());
            Fonts.font40.drawString(s, x + 0.5f, y + 1.0f, color);
            x += (float)Fonts.font40.getStringWidth(s);
        }
        return x;
    }

    public int DisplayFontWidths(String str, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        int x = 0;
        for (int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("\u00a7") && iF + 1 <= str.length()) {
                ++iF;
                continue;
            }
            x = !CFontRenderer.isChinese(s.charAt(0)) ? (int)((float)x + (float)font.getStringWidth(s)) : (int)((float)x + (float)Fonts.font40.getStringWidth(s));
        }
        return x + 5;
    }

    public float drawString(String text, double x, double y, int color, boolean shadow) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179084_k();
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
            color = new Color(0, 0, 0).getRGB();
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        if (render) {
            GL11.glPushMatrix();
            GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b((int)770, (int)771);
            GlStateManager.func_179131_c((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)alpha);
            int size = text.length();
            GlStateManager.func_179098_w();
            GlStateManager.func_179144_i((int)this.tex.func_110552_b());
            GL11.glBindTexture((int)3553, (int)this.tex.func_110552_b());
            for (int i = 0; i < size; ++i) {
                char character = text.charAt(i);
                if (character == '\u00a7' && i < size) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.func_179144_i((int)this.tex.func_110552_b());
                        currentData = this.charData;
                        if (colorIndex < 0 || colorIndex > 15) {
                            colorIndex = 15;
                        }
                        if (shadow) {
                            colorIndex += 16;
                        }
                        int colorcode = this.colorCode[colorIndex];
                        GlStateManager.func_179131_c((float)((float)(colorcode >> 16 & 0xFF) / 255.0f), (float)((float)(colorcode >> 8 & 0xFF) / 255.0f), (float)((float)(colorcode & 0xFF) / 255.0f), (float)alpha);
                    } else if (colorIndex == 16) {
                        randomCase = true;
                    } else if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            GlStateManager.func_179144_i((int)this.texItalicBold.func_110552_b());
                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.func_179144_i((int)this.texBold.func_110552_b());
                            currentData = this.boldChars;
                        }
                    } else if (colorIndex == 18) {
                        strikethrough = true;
                    } else if (colorIndex == 19) {
                        underline = true;
                    } else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            GlStateManager.func_179144_i((int)this.texItalicBold.func_110552_b());
                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.func_179144_i((int)this.texItalic.func_110552_b());
                            currentData = this.italicChars;
                        }
                    } else if (colorIndex == 21) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.func_179131_c((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)alpha);
                        GlStateManager.func_179144_i((int)this.tex.func_110552_b());
                        currentData = this.charData;
                    }
                    ++i;
                    continue;
                }
                if (character >= currentData.length || character < '\u0000') continue;
                GL11.glBegin((int)4);
                this.drawChar(currentData, character, (float)x, (float)y);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0, y + (double)(currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
                }
                x += (double)(currentData[character].width - 8 + this.charOffset);
            }
            GL11.glHint((int)3155, (int)4352);
            GL11.glPopMatrix();
        }
        return (float)x / 2.0f;
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7' && i < size) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
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
            if (character >= currentData.length || character < '\u0000') continue;
            width += currentData[character].width - 8 + this.charOffset;
        }
        return width / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    public List wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if ((double)this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            int lastColorCode = 65535;
            String[] arrstring = words;
            int n = words.length;
            for (int n2 = 0; n2 < n; ++n2) {
                String word = arrstring[n2];
                for (int s = 0; s < word.toCharArray().length; ++s) {
                    char c = word.toCharArray()[s];
                    if (c != '\u00a7' || s >= word.toCharArray().length - 1) continue;
                    lastColorCode = word.toCharArray()[s + 1];
                }
                StringBuilder stringBuilder = new StringBuilder();
                if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width) {
                    currentWord = currentWord + word + " ";
                    continue;
                }
                finalWords.add(currentWord);
                currentWord = 167 + lastColorCode + word + " ";
            }
            if (currentWord.length() > 0) {
                if ((double)this.getStringWidth(currentWord) < width) {
                    finalWords.add(167 + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        int lastColorCode = 65535;
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (c == '\u00a7' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
                currentWord = currentWord + c;
                continue;
            }
            finalWords.add(currentWord);
            currentWord = 167 + lastColorCode + String.valueOf(c);
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index >> 0 & 1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }
}

