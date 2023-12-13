// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.font;

import net.minecraft.client.resources.IResourceManager;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.awt.Font;
import java.awt.Color;
import java.util.Random;
import net.minecraft.client.gui.FontRenderer;

public class XFontRenderer extends FontRenderer
{
    public final Random fontRandom;
    private final Color[] customColorCodes;
    private final int[] colorCode;
    private XFont font;
    private XFont boldFont;
    private XFont italicFont;
    private XFont boldItalicFont;
    private String colorcodeIdentifiers;
    private boolean bidi;
    
    public XFontRenderer(final Font font, final boolean antiAlias, final int charOffset) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.fontRandom = new Random();
        this.customColorCodes = new Color[256];
        this.colorCode = new int[32];
        this.colorcodeIdentifiers = "0123456789abcdefklmnor";
        this.setFont(font, antiAlias, charOffset);
        this.customColorCodes[113] = new Color(0, 90, 163);
        this.colorcodeIdentifiers = this.setupColorcodeIdentifier();
        this.setupMinecraftColorcodes();
        this.FONT_HEIGHT = this.getHeight();
    }
    
    public int drawString(final String s, final float x, final float y, final int color) {
        return this.drawString(s, x, y, color, false);
    }
    
    public void drawStringWithShadow(final String s, final int x, final int y, final int color) {
        this.drawString(s, x + 0.75f, y + 0.75f, color, true);
        this.drawString(s, (float)x, (float)y, color, false);
    }
    
    public void drawCenteredString(final String s, final int x, final int y, final int color, final boolean shadow) {
        if (shadow) {
            this.drawStringWithShadow(s, x - this.getStringWidth(s) / 2, y, color);
        }
        else {
            this.drawString(s, x - this.getStringWidth(s) / 2, y, color);
        }
    }
    
    public void drawCenteredStringXY(final String s, final int x, final int y, final int color, final boolean shadow) {
        this.drawCenteredString(s, x, y - this.getHeight() / 2, color, shadow);
    }
    
    public void drawCenteredString(final String s, final int x, final int y, final int color) {
        this.drawStringWithShadow(s, x - this.getStringWidth(s) / 2, y, color);
    }
    
    public int drawString(final String text, final float x, final float y, final int color, final boolean shadow) {
        int result = 0;
        final String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; ++i) {
            result = this.drawLine(lines[i], x, y + i * this.getHeight(), color, shadow);
        }
        return result;
    }
    
    private int drawLine(final String text, final float x, final float y, int color, final boolean shadow) {
        if (text == null) {
            return 0;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x - 1.5, y + 0.5, 0.0);
        final boolean wasBlend = GL11.glGetBoolean(3042);
        GlStateManager.enableAlpha();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3553);
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final Color c = new Color(red, green, blue, alpha);
        if (text.contains("§")) {
            final String[] parts = text.split("§");
            Color currentColor = c;
            XFont currentFont = this.font;
            int width = 0;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;
            for (int index = 0; index < parts.length; ++index) {
                if (parts[index].length() > 0) {
                    if (index == 0) {
                        currentFont.drawString(parts[index], width, 0.0, currentColor, shadow);
                        width += currentFont.getStringWidth(parts[index]);
                    }
                    else {
                        final String words = parts[index].substring(1);
                        final char type = parts[index].charAt(0);
                        final int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                        if (colorIndex != -1) {
                            if (colorIndex < 16) {
                                final int colorcode = this.colorCode[colorIndex];
                                currentColor = this.getColor(colorcode, alpha);
                                bold = false;
                                italic = false;
                                randomCase = false;
                                underline = false;
                                strikethrough = false;
                            }
                            else if (colorIndex == 16) {
                                randomCase = true;
                            }
                            else if (colorIndex == 17) {
                                bold = true;
                            }
                            else if (colorIndex == 18) {
                                strikethrough = true;
                            }
                            else if (colorIndex == 19) {
                                underline = true;
                            }
                            else if (colorIndex == 20) {
                                italic = true;
                            }
                            else if (colorIndex == 21) {
                                bold = false;
                                italic = false;
                                randomCase = false;
                                underline = false;
                                strikethrough = false;
                                currentColor = c;
                            }
                            else if (colorIndex > 21) {
                                final Color customColor = this.customColorCodes[type];
                                currentColor = new Color(customColor.getRed() / 255.0f, customColor.getGreen() / 255.0f, customColor.getBlue() / 255.0f, alpha);
                            }
                        }
                        if (bold && italic) {
                            this.boldItalicFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, width, 0.0, currentColor, shadow);
                            currentFont = this.boldItalicFont;
                        }
                        else if (bold) {
                            this.boldFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, width, 0.0, currentColor, shadow);
                            currentFont = this.boldFont;
                        }
                        else if (italic) {
                            this.italicFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, width, 0.0, currentColor, shadow);
                            currentFont = this.italicFont;
                        }
                        else {
                            this.font.drawString(randomCase ? this.toRandom(currentFont, words) : words, width, 0.0, currentColor, shadow);
                            currentFont = this.font;
                        }
                        final float u = this.font.getHeight() / 16.0f;
                        final int h = currentFont.getStringHeight(words);
                        if (strikethrough) {
                            this.drawLine(width / 2.0 + 1.0, h / 3, (width + currentFont.getStringWidth(words)) / 2.0 + 1.0, h / 3, u);
                        }
                        if (underline) {
                            this.drawLine(width / 2.0 + 1.0, h / 2, (width + currentFont.getStringWidth(words)) / 2.0 + 1.0, h / 2, u);
                        }
                        width += currentFont.getStringWidth(words);
                    }
                }
            }
        }
        else {
            this.font.drawString(text, 0.0, 0.0, c, shadow);
        }
        if (!wasBlend) {
            GL11.glDisable(3042);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (int)(x + this.getStringWidth(text));
    }
    
    private String toRandom(final XFont font, final String text) {
        String newText = "";
        final String allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000";
        for (final char c : text.toCharArray()) {
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                final int index = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".length());
                newText += "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8£\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿®¬½¼¡«»\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261±\u2265\u2264\u2320\u2321\u00f7\u2248°\u2219·\u221a\u207f²\u25a0\u0000".toCharArray()[index];
            }
        }
        return newText;
    }
    
    public int getStringHeight(final String text) {
        if (text == null) {
            return 0;
        }
        return this.font.getStringHeight(text) / 2;
    }
    
    public int getHeight() {
        return this.font.getHeight() / 2;
    }
    
    public static String getFormatFromString(final String text) {
        String var1 = "";
        int var2 = -1;
        final int var3 = text.length();
        while ((var2 = text.indexOf(167, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                final char var4 = text.charAt(var2 + 1);
                if (isFormatColor(var4)) {
                    var1 = "§" + var4;
                }
                else {
                    if (!isFormatSpecial(var4)) {
                        continue;
                    }
                    var1 = var1 + "§" + var4;
                }
            }
        }
        return var1;
    }
    
    private static boolean isFormatSpecial(final char formatChar) {
        return (formatChar >= 'k' && formatChar <= 'o') || (formatChar >= 'K' && formatChar <= 'O') || formatChar == 'r' || formatChar == 'R';
    }
    
    public int getColorCode(final char character) {
        return this.colorCode["0123456789abcdef".indexOf(character)];
    }
    
    public void setBidiFlag(final boolean state) {
        this.bidi = state;
    }
    
    public boolean getBidiFlag() {
        return this.bidi;
    }
    
    private int sizeStringToWidth(final String str, final int wrapWidth) {
        final int var3 = str.length();
        int var4 = 0;
        int var5 = 0;
        int var6 = -1;
        boolean var7 = false;
        while (var5 < var3) {
            final char var8 = str.charAt(var5);
            Label_0167: {
                switch (var8) {
                    case '\n': {
                        --var5;
                        break Label_0167;
                    }
                    case '§': {
                        if (var5 < var3 - 1) {
                            ++var5;
                            final char var9 = str.charAt(var5);
                            if (var9 != 'l' && var9 != 'L') {
                                if (var9 == 'r' || var9 == 'R' || isFormatColor(var9)) {
                                    var7 = false;
                                }
                            }
                            else {
                                var7 = true;
                            }
                        }
                        break Label_0167;
                    }
                    case ' ': {
                        var6 = var5;
                        break;
                    }
                }
                var4 += this.getStringWidth(Character.toString(var8));
                if (var7) {
                    ++var4;
                }
            }
            if (var8 == '\n') {
                var6 = ++var5;
                break;
            }
            if (var4 > wrapWidth) {
                break;
            }
            ++var5;
        }
        return (var5 != var3 && var6 != -1 && var6 < var5) ? var6 : var5;
    }
    
    private static boolean isFormatColor(final char colorChar) {
        return (colorChar >= '0' && colorChar <= '9') || (colorChar >= 'a' && colorChar <= 'f') || (colorChar >= 'A' && colorChar <= 'F');
    }
    
    public int getCharWidth(final char c) {
        return this.getStringWidth(Character.toString(c));
    }
    
    public int getStringWidth(final String text) {
        if (text == null) {
            return 0;
        }
        if (text.contains("§")) {
            final String[] parts = text.split("§");
            XFont currentFont = this.font;
            int width = 0;
            boolean bold = false;
            boolean italic = false;
            for (int index = 0; index < parts.length; ++index) {
                if (parts[index].length() > 0) {
                    if (index == 0) {
                        width += currentFont.getStringWidth(parts[index]);
                    }
                    else {
                        final String words = parts[index].substring(1);
                        final char type = parts[index].charAt(0);
                        final int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                        if (colorIndex != -1) {
                            if (colorIndex < 16) {
                                bold = false;
                                italic = false;
                            }
                            else if (colorIndex != 16) {
                                if (colorIndex == 17) {
                                    bold = true;
                                }
                                else if (colorIndex != 18) {
                                    if (colorIndex != 19) {
                                        if (colorIndex == 20) {
                                            italic = true;
                                        }
                                        else if (colorIndex == 21) {
                                            bold = false;
                                            italic = false;
                                        }
                                    }
                                }
                            }
                        }
                        if (bold && italic) {
                            currentFont = this.boldItalicFont;
                        }
                        else if (bold) {
                            currentFont = this.boldFont;
                        }
                        else if (italic) {
                            currentFont = this.italicFont;
                        }
                        else {
                            currentFont = this.font;
                        }
                        width += currentFont.getStringWidth(words);
                    }
                }
            }
            return width / 2;
        }
        return this.font.getStringWidth(text) / 2;
    }
    
    public void setFont(final Font font, final boolean antiAlias, final int charOffset) {
        synchronized (this) {
            this.font = new XFont(font, antiAlias, charOffset);
            this.boldFont = new XFont(font.deriveFont(1), antiAlias, charOffset);
            this.italicFont = new XFont(font.deriveFont(2), antiAlias, charOffset);
            this.boldItalicFont = new XFont(font.deriveFont(3), antiAlias, charOffset);
            this.FONT_HEIGHT = this.getHeight();
        }
    }
    
    public XFont getFont() {
        return this.font;
    }
    
    public String getFontName() {
        return this.font.getFont().getFontName();
    }
    
    public int getSize() {
        return this.font.getFont().getSize();
    }
    
    public List<String> wrapWords(final String text, final double width) {
        final List<String> finalWords = new ArrayList<String>();
        if (this.getStringWidth(text) > width) {
            final String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            for (final String word : words) {
                for (int i = 0; i < word.toCharArray().length; ++i) {
                    final char c = word.toCharArray()[i];
                    if (c == '§' && i < word.toCharArray().length - 1) {
                        lastColorCode = word.toCharArray()[i + 1];
                    }
                }
                if (this.getStringWidth(currentWord + word + " ") < width) {
                    currentWord = currentWord + word + " ";
                }
                else {
                    finalWords.add(currentWord);
                    currentWord = ((lastColorCode == -1) ? (word + " ") : ("§" + lastColorCode + word + " "));
                }
            }
            if (!currentWord.equals("")) {
                if (this.getStringWidth(currentWord) < width) {
                    finalWords.add((lastColorCode == -1) ? (currentWord + " ") : ("§" + lastColorCode + currentWord + " "));
                    currentWord = "";
                }
                else {
                    for (final String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        }
        else {
            finalWords.add(text);
        }
        return finalWords;
    }
    
    public List<String> formatString(final String s, final double width) {
        final List<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        char lastColorCode = '\uffff';
        for (int i = 0; i < s.toCharArray().length; ++i) {
            final char c = s.toCharArray()[i];
            if (c == '§' && i < s.toCharArray().length - 1) {
                lastColorCode = s.toCharArray()[i + 1];
            }
            if (this.getStringWidth(currentWord + c) < width) {
                currentWord += c;
            }
            else {
                finalWords.add(currentWord);
                currentWord = ((lastColorCode == -1) ? String.valueOf(c) : ("§" + lastColorCode + String.valueOf(c)));
            }
        }
        if (!currentWord.equals("")) {
            finalWords.add(currentWord);
        }
        return finalWords;
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
    
    public boolean isAntiAliasing() {
        return this.font.isAntiAlias() && this.boldFont.isAntiAlias() && this.italicFont.isAntiAlias() && this.boldItalicFont.isAntiAlias();
    }
    
    public void setAntiAliasing(final boolean antiAlias) {
        this.font.setAntiAlias(antiAlias);
        this.boldFont.setAntiAlias(antiAlias);
        this.italicFont.setAntiAlias(antiAlias);
        this.boldItalicFont.setAntiAlias(antiAlias);
    }
    
    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            final int var6 = (index >> 3 & 0x1) * 85;
            int var7 = (index >> 2 & 0x1) * 170 + var6;
            int var8 = (index >> 1 & 0x1) * 170 + var6;
            int var9 = (index >> 0 & 0x1) * 170 + var6;
            if (index == 6) {
                var7 += 85;
            }
            if (index >= 16) {
                var7 /= 4;
                var8 /= 4;
                var9 /= 4;
            }
            this.colorCode[index] = ((var7 & 0xFF) << 16 | (var8 & 0xFF) << 8 | (var9 & 0xFF));
        }
    }
    
    public String trimStringToWidth(final String text, final int width) {
        return this.trimStringToWidth(text, width, false);
    }
    
    public String trimStringToWidth(final String text, final int width, final boolean reverse) {
        final StringBuilder var4 = new StringBuilder();
        int var5 = 0;
        final int var6 = reverse ? (text.length() - 1) : 0;
        final int var7 = reverse ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;
        for (int var10 = var6; var10 >= 0 && var10 < text.length() && var5 < width; var10 += var7) {
            final char var11 = text.charAt(var10);
            final int var12 = this.getStringWidth(Character.toString(var11));
            if (var8) {
                var8 = false;
                if (var11 != 'l' && var11 != 'L') {
                    if (var11 == 'r' || var11 == 'R') {
                        var9 = false;
                    }
                }
                else {
                    var9 = true;
                }
            }
            else if (var12 < 0) {
                var8 = true;
            }
            else {
                var5 += var12;
                if (var9) {
                    ++var5;
                }
            }
            if (var5 > width) {
                break;
            }
            if (reverse) {
                var4.insert(0, var11);
            }
            else {
                var4.append(var11);
            }
        }
        return var4.toString();
    }
    
    public List<String> listFormattedStringToWidth(final String str, final int wrapWidth) {
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }
    
    protected String wrapFormattedStringToWidth(final String str, final int wrapWidth) {
        final int var3 = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= var3) {
            return str;
        }
        final String var4 = str.substring(0, var3);
        final char var5 = str.charAt(var3);
        final boolean var6 = var5 == ' ' || var5 == '\n';
        final String var7 = getFormatFromString(var4) + str.substring(var3 + (var6 ? 1 : 0));
        return var4 + "\n" + this.wrapFormattedStringToWidth(var7, wrapWidth);
    }
    
    public Color getColor(final int colorCode, final float alpha) {
        return new Color((colorCode >> 16) / 255.0f, (colorCode >> 8 & 0xFF) / 255.0f, (colorCode & 0xFF) / 255.0f, alpha);
    }
    
    private String setupColorcodeIdentifier() {
        String minecraftColorCodes = "0123456789abcdefklmnor";
        for (int i = 0; i < this.customColorCodes.length; ++i) {
            if (this.customColorCodes[i] != null) {
                minecraftColorCodes += (char)i;
            }
        }
        return minecraftColorCodes;
    }
    
    public void onResourceManagerReload(final IResourceManager resourceManager) {
    }
}
