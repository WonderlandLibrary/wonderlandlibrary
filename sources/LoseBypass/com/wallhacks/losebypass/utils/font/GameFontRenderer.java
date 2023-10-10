/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.utils.font;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.manager.FontManager;
import com.wallhacks.losebypass.utils.ColorUtil;
import com.wallhacks.losebypass.utils.font.CustomFont;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class GameFontRenderer
extends CustomFont {
    private final int[] colorCode = new int[32];
    protected CustomFont.CharData[] boldChars = new CustomFont.CharData[256];
    protected CustomFont.CharData[] italicChars = new CustomFont.CharData[256];
    protected CustomFont.CharData[] boldItalicChars = new CustomFont.CharData[256];
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public GameFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public void drawString(String text, float x, float y, int color) {
        if (LoseBypass.fontManager != null && FontManager.getCustomFontEnabled()) {
            this.drawText(text, x + 0.4f, y + 0.3f, new Color(0, 0, 0, 150).getRGB());
        }
        ColorUtil.glColor(new Color(color));
        this.drawText(text, x, y, color);
    }

    public void drawText(String textIn, double xI, double yI, int color) {
        double x = xI;
        double y = yI;
        x -= 1.0;
        if (textIn == null) {
            return;
        }
        if (color == 0x20FFFFFF) {
            color = 0xFFFFFF;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        CustomFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        if (!render) return;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
        int size = textIn.length();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture((int)3553, (int)this.tex.getGlTextureId());
        int i = 0;
        while (true) {
            if (i >= size) {
                GL11.glHint((int)3155, (int)4352);
                GL11.glPopMatrix();
                return;
            }
            char character = textIn.charAt(i);
            if (character == '\u00a7' && i < size) {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(textIn.charAt(i + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0 || colorIndex > 15) {
                        colorIndex = 15;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((float)(colorcode >> 16 & 0xFF) / 255.0f, (float)(colorcode >> 8 & 0xFF) / 255.0f, (float)(colorcode & 0xFF) / 255.0f, alpha);
                } else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texBold.getGlTextureId());
                        currentData = this.boldChars;
                    }
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        currentData = this.boldItalicChars;
                    } else {
                        GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                        currentData = this.italicChars;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                }
                ++i;
            } else if (character < currentData.length && character >= '\u0000') {
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
            ++i;
        }
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CustomFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        int i = 0;
        while (i < size) {
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
            } else if (character < currentData.length && character >= '\u0000') {
                width += currentData[character].width - 8 + this.charOffset;
            }
            ++i;
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
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
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

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if (!((double)this.getStringWidth(text) > width)) {
            finalWords.add(text);
            return finalWords;
        }
        String[] words = text.split(" ");
        String currentWord = "";
        char lastColorCode = '\uffff';
        Object object = words;
        int n = ((String[])object).length;
        int n2 = 0;
        while (true) {
            String word;
            if (n2 < n) {
                word = object[n2];
            } else {
                if (currentWord.length() <= 0) return finalWords;
                if ((double)this.getStringWidth(currentWord) < width) {
                    finalWords.add("\u00a7" + lastColorCode + currentWord + " ");
                    return finalWords;
                }
                object = this.formatString(currentWord, width).iterator();
                while (object.hasNext()) {
                    String s = (String)object.next();
                    finalWords.add(s);
                }
                return finalWords;
            }
            for (int i = 0; i < word.toCharArray().length; ++i) {
                char c = word.toCharArray()[i];
                if (c != '\u00a7' || i >= word.toCharArray().length - 1) continue;
                lastColorCode = word.toCharArray()[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width) {
                currentWord = currentWord + word + " ";
            } else {
                finalWords.add(currentWord);
                currentWord = "\u00a7" + lastColorCode + word + " ";
            }
            ++n2;
        }
    }

    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        char lastColorCode = '\uffff';
        char[] chars = string.toCharArray();
        int i = 0;
        while (true) {
            StringBuilder stringBuilder;
            if (i >= chars.length) {
                if (currentWord.length() <= 0) return finalWords;
                finalWords.add(currentWord);
                return finalWords;
            }
            char c = chars[i];
            if (c == '\u00a7' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            if ((double)this.getStringWidth((stringBuilder = new StringBuilder()).append(currentWord).append(c).toString()) < width) {
                currentWord = currentWord + c;
            } else {
                finalWords.add(currentWord);
                currentWord = "\u00a7" + lastColorCode + c;
            }
            ++i;
        }
    }

    private void setupMinecraftColorcodes() {
        int index = 0;
        while (index < 32) {
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
            ++index;
        }
    }
}

