// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.font;

import org.lwjgl.opengl.GL11;
import ru.fluger.client.helpers.render.RenderHelper;
import java.awt.Color;
import java.awt.Font;

public class MCFontRenderer extends CFont
{
    private final int[] colorCode;
    protected CharData[] boldChars;
    protected CharData[] italicChars;
    protected CharData[] boldItalicChars;
    protected cdg texBold;
    protected cdg texItalic;
    protected cdg texItalicBold;
    String colorcodeIdentifiers;
    
    public MCFontRenderer(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.colorCode = new int[32];
        this.boldChars = new CharData[256];
        this.italicChars = new CharData[256];
        this.boldItalicChars = new CharData[256];
        this.colorcodeIdentifiers = "0123456789abcdefklmnor";
        this.setupBoldItalicIDs();
        for (int index = 0; index < 32; ++index) {
            final int noClue = (index >> 3 & 0x1) * 85;
            int red = (index >> 2 & 0x1) * 170 + noClue;
            int green = (index >> 1 & 0x1) * 170 + noClue;
            int blue = (index & 0x1) * 170 + noClue;
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
    
    public void drawSmoothString(final String text, final double x2, final float y2, final int color) {
        this.drawString(text, x2, y2, color, false, 8.3f, true);
    }
    
    public static void drawStringWithOutline(final MCFontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        fontRenderer.drawString(text, x - 0.8f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x + 0.8f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y - 0.8f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y + 0.8f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y, color);
    }
    
    public static void drawStringWithOutline(final bip fontRenderer, final String text, final float x, final float y, final int color) {
        fontRenderer.drawString(text, x - 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x + 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y - 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y + 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawString(text, x, y, color);
    }
    
    public static void drawCenteredStringWithOutline(final bip fontRenderer, final String text, final float x, final float y, final int color) {
        fontRenderer.drawCenteredString(text, x - 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x + 1.0f, y, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y - 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y + 1.0f, Color.BLACK.getRGB());
        fontRenderer.drawCenteredString(text, x, y, color);
    }
    
    public static float drawCenteredStringWithShadow(final bip fontRenderer, final String text, final float x, final float y, final int color) {
        return (float)fontRenderer.drawString(text, x - fontRenderer.a(text) / 2, y, color);
    }
    
    public void drawCenteredStringWithOutline(final MCFontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        this.drawCenteredString(text, x - 1.0f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x + 1.0f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y - 1.0f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y + 1.0f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y, color);
    }
    
    public float drawStringWithShadow(final String text, final double x, final double y, final int color) {
        final float shadowWidth = this.drawString(text, x + 0.9, y + 0.7, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }
    
    public void drawBlurredStringWithShadow(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        bus.I();
        RenderHelper.drawBlurredShadow((float)(int)x, (float)(int)y, (float)this.getStringWidth(text), (float)this.getFontHeight(), blurRadius, blurColor);
        this.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public void drawBlurredString(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        bus.I();
        RenderHelper.drawBlurredShadow((float)(int)x, (float)(int)y, (float)this.getStringWidth(text), (float)this.getFontHeight(), blurRadius, blurColor);
        this.drawString(text, (float)x, (float)y, color);
    }
    
    public void drawCenteredBlurredString(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        bus.I();
        RenderHelper.drawBlurredShadow((float)(int)((int)x - this.getStringWidth(text) / 2.0f), (float)(int)y, (float)this.getStringWidth(text), (float)this.getFontHeight(), blurRadius, blurColor);
        this.drawString(text, (float)(x - this.getStringWidth(text) / 2.0f), (float)y, color);
    }
    
    public void drawCenteredBlurredStringWithShadow(final String text, final double x, final double y, final int blurRadius, final Color blurColor, final int color) {
        bus.I();
        RenderHelper.drawBlurredShadow((float)(int)((int)x - this.getStringWidth(text) / 2.0f), (float)(int)y, (float)this.getStringWidth(text), (float)this.getFontHeight(), blurRadius, blurColor);
        this.drawStringWithShadow(text, (float)(x - this.getStringWidth(text) / 2.0f), (float)y, color);
    }
    
    public float drawString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x, y, color, false);
    }
    
    public float drawCenteredString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }
    
    public float drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2, y, color);
    }
    
    public float drawString(final String text, double x, double y, int color, final boolean shadow, final float kerning, final boolean smooth) {
        if (text == null) {
            return 0.0f;
        }
        if (shadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        CharData[] currentData = this.charData;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        x = (x - 1.0) * 2.0;
        y = (y - 3.0) * 2.0;
        GL11.glPushMatrix();
        bus.a(0.5, 0.5, 0.5);
        bus.m();
        bus.b(770, 771);
        RenderHelper.color(color);
        bus.y();
        bus.i(this.tex.b());
        RenderHelper.bindTexture(this.tex.b());
        if (smooth) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }
        else {
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }
        for (int index = 0; index < text.length(); ++index) {
            final char character = text.charAt(index);
            if (character == '§') {
                int colorIndex = 21;
                try {
                    colorIndex = this.colorcodeIdentifiers.indexOf(text.charAt(index + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    bus.i(this.tex.b());
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    RenderHelper.color(this.colorCode[colorIndex], alpha);
                }
                else {
                    switch (colorIndex) {
                        case 17: {
                            bold = true;
                            if (italic) {
                                bus.i(this.texItalicBold.b());
                                currentData = this.boldItalicChars;
                                break;
                            }
                            bus.i(this.texBold.b());
                            currentData = this.boldChars;
                            break;
                        }
                        case 18: {
                            strikethrough = true;
                            break;
                        }
                        case 19: {
                            underline = true;
                            break;
                        }
                        case 20: {
                            italic = true;
                            if (bold) {
                                bus.i(this.texItalicBold.b());
                                currentData = this.boldItalicChars;
                                break;
                            }
                            bus.i(this.texItalic.b());
                            currentData = this.italicChars;
                            break;
                        }
                        default: {
                            bold = false;
                            italic = false;
                            underline = false;
                            strikethrough = false;
                            RenderHelper.color(color);
                            bus.i(this.tex.b());
                            currentData = this.charData;
                            break;
                        }
                    }
                }
                ++index;
            }
            else if (character < currentData.length) {
                this.drawLetter(x, y, currentData, strikethrough, underline, character);
                x += currentData[character].width - kerning + this.charOffset;
            }
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        bus.i(0);
        return (float)x / 2.0f;
    }
    
    private void drawLetter(final double x, final double y, final CharData[] currentData, final boolean strikethrough, final boolean underline, final char character) {
        GL11.glBegin(4);
        this.drawChar(currentData, character, (float)x, (float)y);
        GL11.glEnd();
        if (strikethrough) {
            this.drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0, y + currentData[character].height / 2);
        }
        if (underline) {
            this.drawLine(x, y + currentData[character].height - 2.0, x + currentData[character].width - 8.0, y + currentData[character].height - 2.0);
        }
    }
    
    public float drawString(final String text, double x, double y, int color, final boolean shadow) {
        --x;
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
        bus.a(0.5, 0.5, 0.5);
        bus.m();
        bus.b(770, 771);
        bus.c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
        final int size = text.length();
        bus.y();
        bus.i(this.tex.b());
        GL11.glBindTexture(3553, this.tex.b());
        for (int i = 0; i < size; ++i) {
            final char character = text.charAt(i);
            if (String.valueOf(character).equals("§")) {
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
                    underline = false;
                    strikethrough = false;
                    bus.i(this.tex.b());
                    currentData = this.charData;
                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    final int colorcode = this.colorCode[colorIndex];
                    bus.c((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f, (colorcode & 0xFF) / 255.0f, alpha);
                }
                else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        bus.i(this.texItalicBold.b());
                        currentData = this.boldItalicChars;
                    }
                    else {
                        bus.i(this.texBold.b());
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
                        bus.i(this.texItalicBold.b());
                        currentData = this.boldItalicChars;
                    }
                    else {
                        bus.i(this.texItalic.b());
                        currentData = this.italicChars;
                    }
                }
                else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    bus.c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
                    bus.i(this.tex.b());
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
        return (float)(x / 2.0);
    }
    
    @Override
    public int getStringWidth(final String text) {
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        for (int size = text.length(), i = 0; i < size; ++i) {
            final char character = text.charAt(i);
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
    
    public void drawSmoothStringWithShadow(final String text, final double x2, final float y2, final int color) {
        this.drawString(text, x2 + 0.5, y2 + 0.5f, color, true, 8.3f, true);
        this.drawString(text, x2, y2, color, false, 8.3f, true);
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
    
    private void drawLine(final double x2, final double y2, final double x1, final double y1) {
        GL11.glDisable(3553);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public void drawStringWithOutline(final String text, final double x, final double y, final int color) {
        this.drawString(text, x - 0.5, y, Color.BLACK.getRGB(), false);
        this.drawString(text, x + 0.5, y, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y - 0.5, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y + 0.5, Color.BLACK.getRGB(), false);
        this.drawString(text, x, y, color, false);
    }
    
    public void drawCenteredStringWithOutline(final String text, final float x, final float y, final int color) {
        this.drawCenteredString(text, x - 0.5f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x + 0.5f, y, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y - 0.5f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y + 0.5f, Color.BLACK.getRGB());
        this.drawCenteredString(text, x, y, color);
    }
}
