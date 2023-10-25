/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.font;

import java.awt.Color;
import java.awt.Font;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.celestial.client.ui.font.CFont;
import org.lwjgl.opengl.GL11;

public class MCFontRenderer
extends CFont {
    private final int[] colorCode = new int[32];
    protected CFont.CharData[] boldChars = new CFont.CharData[1104];
    protected CFont.CharData[] italicChars = new CFont.CharData[1104];
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[1104];
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public MCFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupBoldItalicIDs();
        for (int index = 0; index < 32; ++index) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index & 1) * 170 + noClue;
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

    public static void drawSmallString(FontRenderer fontRenderer, String s, int x, int y, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        MCFontRenderer.drawStringWithOutline(fontRenderer, s, (float)(x * 2), (float)(y * 2), color);
        GL11.glPopMatrix();
    }

    public static void drawSmallString(MCFontRenderer fontRenderer, String s, int x, int y, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        MCFontRenderer.drawStringWithOutline(fontRenderer, s, (float)(x * 2), (float)(y * 2), color);
        GL11.glPopMatrix();
    }

    public static void drawStringWithOutline(MCFontRenderer fontRenderer, String text, float x, float y, int color) {
        fontRenderer.drawString(text, x - 0.8f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x + 0.8f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y - 0.8f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y + 0.8f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y, color);
    }

    public static void drawStringWithOutline(FontRenderer fontRenderer, String text, float x, float y, int color) {
        fontRenderer.drawString(text, x - 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x + 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y - 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y + 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y, color);
    }

    public static void drawCenteredStringWithOutline(FontRenderer fontRenderer, String text, float x, float y, int color) {
        fontRenderer.drawCenteredString(text, x - 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x + 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y - 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y + 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y, color);
    }

    public static float drawCenteredStringWithShadow(FontRenderer fontRenderer, String text, float x, float y, int color) {
        return fontRenderer.drawString(text, x - (float)(fontRenderer.getStringWidth(text) / 2), y, color);
    }

    public void drawCenteredStringWithOutline(MCFontRenderer fontRenderer, String text, float x, float y, int color) {
        this.drawCenteredString(text, x - 1.0f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x + 1.0f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y - 1.0f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y + 1.0f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y, color);
    }

    public float drawStringWithShadow(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 0.5, y + 0.5, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public float drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0f, y, color);
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
        return this.drawStringWithShadow(text, x - (float)this.getStringWidth(text) / 2.0f, y, color);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow) {
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
            color = (color & 0xFCFCFC) >> 2 | color & new Color(20, 20, 20, 200).getRGB();
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
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
            int size = text.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());
            for (int i = 0; i < size; ++i) {
                char character = text.charAt(i);
                if (String.valueOf(character).equals("\u00a7") && i < size) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
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
                        GlStateManager.color((float)(colorcode >> 16 & 0xFF) / 255.0f, (float)(colorcode >> 8 & 0xFF) / 255.0f, (float)(colorcode & 0xFF) / 255.0f, alpha);
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
                        GlStateManager.color((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                    }
                    ++i;
                    continue;
                }
                if (character >= currentData.length) continue;
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x, (float)y);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x, y + (double)((float)currentData[character].height / 2.0f), x + (double)currentData[character].width - 8.0, y + (double)((float)currentData[character].height / 2.0f), 1.0f);
                }
                if (underline) {
                    this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
                }
                x += (double)(currentData[character].width - 8 + this.charOffset);
            }
            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
        }
        return (float)x / 2.0f;
    }

    @Override
    public int getStringWidth(String text) {
        int width = 0;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (String.valueOf(character).equals("\u00a7")) {
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
            if (character >= currentData.length) continue;
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
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public void drawStringWithOutline(String text, double x, double y, int color) {
        this.drawString(text, x - 0.5, y, Color.BLACK.getRGB(), false);
        this.drawString(text, x + 0.5, y, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y - 0.5, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y + 0.5, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y, color, false);
    }

    public void drawCenteredStringWithOutline(String text, float x, float y, int color) {
        this.drawCenteredString(text, x - 0.5f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x + 0.5f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y - 0.5f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y + 0.5f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y, color);
    }
}

