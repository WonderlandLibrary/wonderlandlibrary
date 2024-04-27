package net.ccbluex.liquidbounce.Gui.cfont;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer extends CFont {
    public static final int FONT_HEIGHT = 0;
    protected CFont.CharData[] boldChars = new CFont.CharData[256];
    protected CFont.CharData[] italicChars = new CFont.CharData[256];
    private static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;
    private byte[] glyphWidth = new byte[65536];
    private float[] charWidth = new float[256];
    private boolean unicodeFlag;

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public float drawStringWithShadow(String text, double x2, double y2, int color) {
        float shadowWidth = this.drawString(text, x2 + 1.0, y2 + 1, color, true);
        return Math.max(shadowWidth, this.drawString(text, x2, y2, color, false));
    }

    public float drawStringWithShadow1(String text, double x2, double y2, int color) {
        float shadowWidth = this.drawString(text, x2 + 2, y2 + 2, color, true);
        return Math.max(shadowWidth, this.drawString(text, x2, y2, color, false));
    }


    public float Ex(String text, double x2, double y2, int color) {
        float shadowWidth = this.drawString(text, x2 + 0.5, y2 + 0.5, color, true);
        return Math.max(shadowWidth, this.drawString(text, x2, y2, color, false));
    }

    public float drawString(String text, double d, float y2, int color) {
        return this.drawString(text, d, y2, color, false);
    }

    public float drawCenteredString(String text, float x2, float y2, int color) {
        return this.drawString(text, x2 - (float) (this.getStringWidth(text) / 2), y2, color);
    }

    public float drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
        return this.drawStringWithShadow(text, x2 - (float) (this.getStringWidth(text) / 2), y2, color);
    }

    public float drawCenteredStringWithShadow(String text, double x2, double y2, int color) {
        return this.drawStringWithShadow(text, x2 - (double) (this.getStringWidth(text) / 2), y2, color);
    }

    public float drawString(String text, double x2, double y2, int color, boolean shadow) {
        x2 -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        if (color == 553648127) {
            color = 16777215;
        }
        if ((color & -67108864) == 0) {
            color |= -16777216;
        }
        if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float) (color >> 24 & 255) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x2 *= 2.0;
        y2 = (y2 - 3.0) * 2.0;
        if (render) {
            GL11.glPushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((float) (color >> 16 & 255) / 255.0f, (float) (color >> 8 & 255) / 255.0f,
                    (float) (color & 255) / 255.0f, alpha);
            int size = text.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());
            int i2 = 0;
            while (i2 < size) {
                char character = text.charAt(i2);
                if (character == '\u00a7' && i2 < size) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i2 + 1));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                        if (colorIndex < 0 || colorIndex > 15) {
                            colorIndex = 15;
                        }
                        if (shadow) {
                            colorIndex += 16;
                        }
                        int colorcode = this.colorCode[colorIndex];
                        GlStateManager.color((float) (colorcode >> 16 & 255) / 255.0f,
                                (float) (colorcode >> 8 & 255) / 255.0f, (float) (colorcode & 255) / 255.0f, alpha);
                    } else if (colorIndex == 16) {
                        randomCase = true;
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
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.color((float) (color >> 16 & 255) / 255.0f, (float) (color >> 8 & 255) / 255.0f,
                                (float) (color & 255) / 255.0f, alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                    }
                    ++i2;
                } else if (character < currentData.length && character >= '\u0000') {
                    GL11.glBegin(4);
                    this.drawChar(currentData, character, (float) x2, (float) y2);
                    GL11.glEnd();
                    if (strikethrough) {
                        this.drawLine(x2, y2 + (double) (currentData[character].height / 2),
                                x2 + (double) currentData[character].width - 8.0,
                                y2 + (double) (currentData[character].height / 2), 1.0f);
                    }
                    if (underline) {
                        this.drawLine(x2, y2 + (double) currentData[character].height - 2.0,
                                x2 + (double) currentData[character].width - 8.0,
                                y2 + (double) currentData[character].height - 2.0, 1.0f);
                    }
                    x2 += (double) (currentData[character].width - 8 + this.charOffset);
                }
                ++i2;
            }
            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
        }
        return (float) x2 / 2.0f;
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
        int i2 = 0;
        while (i2 < size) {
            char character = text.charAt(i2);
            if (character == '\u00a7' && i2 < size) {
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
                ++i2;
            } else if (character < currentData.length && character >= '\u0000') {
                width += currentData[character].width - 8 + this.charOffset;
            }
            ++i2;
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
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics,
                this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics,
                this.italicChars);
    }

    private void drawLine(double x2, double y2, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if ((double) this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            int lastColorCode = 65535;
            String[] arrstring = words;
            int n2 = arrstring.length;
            int n22 = 0;
            while (n22 < n2) {
                String word = arrstring[n22];
                int i2 = 0;
                while (i2 < word.toCharArray().length) {
                    char c2 = word.toCharArray()[i2];
                    if (c2 == '\u00a7' && i2 < word.toCharArray().length - 1) {
                        lastColorCode = word.toCharArray()[i2 + 1];
                    }
                    ++i2;
                }
                if ((double) this.getStringWidth(String.valueOf(String.valueOf(currentWord)) + word + " ") < width) {
                    currentWord = String.valueOf(String.valueOf(currentWord)) + word + " ";
                } else {
                    finalWords.add(currentWord);
                    currentWord = String.valueOf(String.valueOf(167 + lastColorCode)) + word + " ";
                }
                ++n22;
            }
            if (currentWord.length() > 0) {
                if ((double) this.getStringWidth(currentWord) < width) {
                    finalWords.add(String.valueOf(String.valueOf(167 + lastColorCode)) + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s2 : this.formatString(currentWord, width)) {
                        finalWords.add(s2);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        int lastColorCode = 65535;
        char[] chars = string.toCharArray();
        int i2 = 0;
        while (i2 < chars.length) {
            char c2 = chars[i2];
            if (c2 == '\u00a7' && i2 < chars.length - 1) {
                lastColorCode = chars[i2 + 1];
            }
            if ((double) this.getStringWidth(String.valueOf(String.valueOf(currentWord)) + c2) < width) {
                currentWord = String.valueOf(String.valueOf(currentWord)) + c2;
            } else {
                finalWords.add(currentWord);
                currentWord = String.valueOf(String.valueOf(167 + lastColorCode)) + String.valueOf(c2);
            }
            ++i2;
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
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
            this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
            ++index;
        }
    }

    public static int getFontHeight() {
        return fontRenderer.FONT_HEIGHT;
    }

}
