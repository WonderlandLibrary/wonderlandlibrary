// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.font;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.util.render.GlowUtility;
import java.awt.Color;
import java.awt.Font;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class FontRenderer extends CFont
{
    protected CharData[] boldChars;
    protected CharData[] italicChars;
    protected CharData[] boldItalicChars;
    private final int[] colorCode;
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;
    
    public FontRenderer(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.boldChars = new CharData[1104];
        this.italicChars = new CharData[1104];
        this.boldItalicChars = new CharData[1104];
        this.colorCode = new int[32];
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }
    
    public void drawStringWithDropShadow(final String text, final float x, final float y, final int color) {
        for (int i = 0; i < 5; ++i) {
            this.drawString(text, x + 0.5f * i, y + 0.5f * i, new Color(0, 0, 0, 100 - i * 20).hashCode());
        }
        this.drawString(text, x, y, color);
    }
    
    public float drawString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x, y, color, false);
    }
    
    public float drawString(final String text, final double x, final double y, final int color) {
        return this.drawString(text, x, y, color, false);
    }
    
    public float drawStringWithShadow(final String text, final float x, final float y, final int color) {
        final float shadowWidth = this.drawString(text, x + 0.5, y + 0.5, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }
    
    public float drawStringWithShadow(final String text, final double x, final double y, final int color) {
        final float shadowWidth = this.drawString(text, x + 0.5, y + 0.5, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }
    
    public float drawCenteredString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }
    
    public void drawCenteredBlurredString(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        GlowUtility.drawGlow((float)(int)((int)x - this.getStringWidth(text) / 2.0f), (int)y - 0.5f, (float)this.getStringWidth(text), (float)this.getFontHeight(), blurRadius, blurColor);
        this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }
    
    public float drawCenteredString(final String text, final double x, final double y, final int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }
    
    public float drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
        this.drawString(text, x - this.getStringWidth(text) / 2.0f + 0.45, y + 0.5, color, true);
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }
    
    public void drawStringWithOutline(final String text, final double x, final double y, final int color) {
        this.drawString(text, x - 0.5, y, 0);
        this.drawString(text, x + 0.5, y, 0);
        this.drawString(text, x, y - 0.5, 0);
        this.drawString(text, x, y + 0.5, 0);
        this.drawString(text, x, y, color);
    }
    
    public void drawCenteredStringWithOutline(final String text, final double x, final double y, final int color) {
        this.drawCenteredString(ChatFormatting.stripFormatting(text), x - 0.5, y, 0);
        this.drawCenteredString(ChatFormatting.stripFormatting(text), x + 0.5, y, 0);
        this.drawCenteredString(ChatFormatting.stripFormatting(text), x, y - 0.5, 0);
        this.drawCenteredString(ChatFormatting.stripFormatting(text), x, y + 0.5, 0);
        this.drawCenteredString(text, x, y, color);
    }
    
    public float drawCenteredStringWithShadow(final String text, final double x, final double y, final int color) {
        this.drawString(text, x - this.getStringWidth(text) / 2.0f + 0.45, y + 0.5, color, true);
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }
    
    public void drawBlurredStringWithShadow(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        GlStateManager.resetColor();
        GlowUtility.drawGlow((float)(int)x, (float)(int)y, (float)this.getStringWidth(text), (float)this.getFontHeight(), blurRadius, blurColor);
        this.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public void drawBlurredString(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        GlStateManager.resetColor();
        this.drawString(text, (float)x, (float)y, color);
        GlowUtility.drawGlow((float)(int)x, (float)(int)y, (float)this.getStringWidth(text), (float)this.getFontHeight(), blurRadius, blurColor);
    }
    
    public float drawString(final String text2, double x, double y, int color, final boolean shadow) {
        try {
            --x;
            if (text2 == null) {
                return 0.0f;
            }
            if (color == 553648127) {
                color = 16777215;
            }
            if ((color & 0xFC000000) == 0x0) {
                color |= 0xFF000000;
            }
            if (shadow) {
                color = ((color & 0xFCFCFC) >> 2 | (color & new Color(20, 20, 20, 200).getRGB()));
            }
            CharData[] currentData = this.charData;
            final float alpha = (color >> 24 & 0xFF) / 255.0f;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;
            x *= 2.0;
            y = (y - 3.0) * 2.0;
            GL11.glPushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
            final int size = text2.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());
            for (int i = 0; i < size; ++i) {
                final char character = text2.charAt(i);
                if (String.valueOf(character).equals("§")) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text2.charAt(i + 1));
                    }
                    catch (Exception var19) {
                        var19.printStackTrace();
                    }
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                        if (colorIndex < 0) {
                            colorIndex = 15;
                        }
                        if (shadow) {
                            colorIndex += 16;
                        }
                        final int colorcode = this.colorCode[colorIndex];
                        GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f, (colorcode & 0xFF) / 255.0f, alpha);
                    }
                    else if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        }
                        else {
                            GlStateManager.bindTexture(this.texBold.getGlTextureId());
                            currentData = this.boldChars;
                        }
                    }
                    else if (colorIndex == 18) {
                        strikethrough = true;
                    }
                    else if (colorIndex == 19) {
                        underline = true;
                    }
                    else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        }
                        else {
                            GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                            currentData = this.italicChars;
                        }
                    }
                    else if (colorIndex == 21) {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                    }
                    ++i;
                }
                else if (character < currentData.length) {
                    GL11.glBegin(4);
                    this.drawChar(currentData, character, (float)x, (float)y);
                    GL11.glEnd();
                    if (strikethrough) {
                        this.drawLine(x, y + currentData[character].height / 2.0f, x + currentData[character].width - 8.0, y + currentData[character].height / 2.0f, 1.0f);
                    }
                    if (underline) {
                        this.drawLine(x, y + currentData[character].height - 2.0, x + currentData[character].width - 8.0, y + currentData[character].height - 2.0, 1.0f);
                    }
                    x += currentData[character].width - 8 + this.charOffset;
                }
            }
            GL11.glPopMatrix();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return (float)x / 2.0f;
    }
    
    @Override
    public int getStringWidth(String text1) {
        text1 = text1;
        if (text1 == null) {
            return 0;
        }
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        for (int size = text1.length(), i = 0; i < size; ++i) {
            final char character = text1.charAt(i);
            if (String.valueOf(character).equals("§")) {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                }
                else if (colorIndex == 17) {
                    bold = true;
                    currentData = (italic ? this.boldItalicChars : this.boldChars);
                }
                else if (colorIndex == 20) {
                    italic = true;
                    currentData = (bold ? this.boldItalicChars : this.italicChars);
                }
                else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
            }
            else if (character < currentData.length) {
                width += currentData[character].width - 8 + this.charOffset;
            }
        }
        return width / 2;
    }
    
    public int getStringWidthCust(final String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        for (int size = text.length(), i = 0; i < size; ++i) {
            final char character = text.charAt(i);
            if (String.valueOf(character).equals("§") && i < size) {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                }
                else if (colorIndex == 17) {
                    bold = true;
                    currentData = (italic ? this.boldItalicChars : this.boldChars);
                }
                else if (colorIndex == 20) {
                    italic = true;
                    currentData = (bold ? this.boldItalicChars : this.italicChars);
                }
                else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
            }
            else if (character < currentData.length) {
                if (character >= '\0') {
                    width += currentData[character].width - 8 + this.charOffset;
                }
            }
        }
        return (width - this.charOffset) / 2;
    }
    
    @Override
    public void setFont(final Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }
    
    @Override
    public void setAntiAlias(final boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }
    
    @Override
    public void setFractionalMetrics(final boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }
    
    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }
    
    private void drawLine(final double x, final double y, final double x1, final double y1, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public List wrapWords(final String text, final double width) {
        final ArrayList<String> finalWords = new ArrayList<String>();
        if (this.getStringWidth(text) > width) {
            final String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            final String[] var8 = words;
            for (int var9 = words.length, var10 = 0; var10 < var9; ++var10) {
                final String word = var8[var10];
                for (int i = 0; i < word.toCharArray().length; ++i) {
                    final char c = word.toCharArray()[i];
                    if (String.valueOf(c).equals("§")) {
                        if (i < word.toCharArray().length - 1) {
                            lastColorCode = word.toCharArray()[i + 1];
                        }
                    }
                }
                final StringBuilder stringBuilder = new StringBuilder();
                if (this.getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width) {
                    currentWord = currentWord + word + " ";
                }
                else {
                    finalWords.add(currentWord);
                    currentWord = "" + lastColorCode + word + " ";
                }
            }
            if (currentWord.length() > 0) {
                if (this.getStringWidth(currentWord) < width) {
                    finalWords.add("" + lastColorCode + currentWord + " ");
                    currentWord = "";
                }
                else {
                    for (final Object s : this.formatString(currentWord, width)) {
                        finalWords.add((String)s);
                    }
                }
            }
        }
        else {
            finalWords.add(text);
        }
        return finalWords;
    }
    
    public List formatString(final String string, final double width) {
        final ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        char lastColorCode = '\uffff';
        final char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            if (String.valueOf(c).equals("§") && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            final StringBuilder stringBuilder = new StringBuilder();
            if (this.getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
                currentWord += c;
            }
            else {
                finalWords.add(currentWord);
                currentWord = "" + lastColorCode + c;
            }
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }
    
    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            final int noClue = (index >> 3 & 0x1) * 85;
            int red = (index >> 2 & 0x1) * 170 + noClue;
            int green = (index >> 1 & 0x1) * 170 + noClue;
            int blue = (index >> 0 & 0x1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }
    
    public String trimStringToWidth(final String text, final int width, final boolean reverse) {
        final StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0f;
        final int i = reverse ? (text.length() - 1) : 0;
        final int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag2 = false;
        for (int k = i; k >= 0 && k < text.length() && f < width; k += j) {
            final char c0 = text.charAt(k);
            final float f2 = (float)this.getStringWidthCust(Character.toString(c0));
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag2 = false;
                    }
                }
                else {
                    flag2 = true;
                }
            }
            else if (f2 < 0.0f) {
                flag = true;
            }
            else {
                f += f2;
                if (flag2) {
                    ++f;
                }
            }
            if (f > width) {
                break;
            }
            if (reverse) {
                stringbuilder.insert(0, c0);
            }
            else {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }
}
