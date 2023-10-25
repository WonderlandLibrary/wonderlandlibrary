package fun.rich.client.utils.other.font;

import fun.rich.client.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class CFontRenderer extends CFont
{
    private final int[] colorCode = new int[32];
    protected CFont.CharData[] boldChars = new CFont.CharData[256];
    protected CFont.CharData[] italicChars = new CFont.CharData[256];
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    protected DynamicTexture texBold;

    protected DynamicTexture texItalic;

    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }


    public float drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }


    public float drawStringWithShadow(String text, float x, float y, int color) {
        float shadowWidth = this.drawString(text, (double) x + 0.5D, (double) y + 0.5D, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawStringWithShadow(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 0.5D, y + 0.5D, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - (float) this.getStringWidth(text) / 2.0F, y, color);
    }

    public float drawCenteredString(String text, double x, double y, int color) {
        return this.drawString(text, (float) (x - (double) ((float) this.getStringWidth(text) / 2.0F)), (float) y, color);
    }

    public float drawCenteredStringWithShadow(String text, double x, double y, int color) {
        this.drawString(text, x - (double) ((float) this.getStringWidth(text) / 2.0F) + 0.45D, y + 0.5D, color, true);
        return this.drawString(text, (float) (x - (double) ((float) this.getStringWidth(text) / 2.0F)), (float) y, color);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow) {
        --x;
        if (text == null) {
            return 0.0F;
        } else {
            if (color == 553648127) {
                color = 16777215;
            }

            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (shadow) {
                color = (color & 16579836) >> 2 | color & (new Color(20, 20, 20, 200)).getRGB();
            }

            CFont.CharData[] currentData = this.charData;
            float alpha = (float) (color >> 24 & 255) / 255.0F;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;
            x *= 2.0D;
            y = (y - 3.0D) * 2.0D;
            GL11.glPushMatrix();
            GlStateManager.scale(0.5D, 0.5D, 0.5D);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, alpha);
            int size = text.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());

            for (int i = 0; i < size; ++i) {
                char character = text.charAt(i);
                if (String.valueOf(character).equals("§")) {
                    int colorIndex = 21;

                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                    } catch (Exception var19) {
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

                        int colorcode = this.colorCode[colorIndex];
                        GlStateManager.color((float) (colorcode >> 16 & 255) / 255.0F, (float) (colorcode >> 8 & 255) / 255.0F, (float) (colorcode & 255) / 255.0F, alpha);
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
                        GlStateManager.color((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                    }

                    ++i;
                } else if (character < currentData.length) {
                    GL11.glBegin(4);
                    this.drawChar(currentData, character, (float) x, (float) y);
                    GL11.glEnd();
                    if (strikethrough) {
                        this.drawLine(x, y + (double) ((float) currentData[character].height / 2.0F), x + (double) currentData[character].width - 8.0D, y + (double) ((float) currentData[character].height / 2.0F), 1.0F);
                    }

                    if (underline) {
                        this.drawLine(x, y + (double) currentData[character].height - 2.0D, x + (double) currentData[character].width - 8.0D, y + (double) currentData[character].height - 2.0D, 1.0F);
                    }

                    x += currentData[character].width - 8 + this.charOffset;
                }
            }

            GL11.glPopMatrix();
            return (float) x / 2.0F;
        }
    }
    public void drawBlurredString(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        GlStateManager.resetColor();
        RenderUtils.drawBlurredShadow((int) x, (int) y, (int) getStringWidth(text), (int) getFontHeight(), blurRadius, blurColor);
        drawString(text, (float) x, (float) y, color);
    }
    public void drawCenteredBlurredString(String text, double x, double y, int blurRadius, Color blurColor, int color) {
        GlStateManager.resetColor();
        RenderUtils.drawBlurredShadow((int) ((int) x - (float)this.getStringWidth(text) / 2.0f), (int) y, (int) getStringWidth(text), (int) getFontHeight(), blurRadius, blurColor);
        drawString(text, (float) (x - this.getStringWidth(text) / 2F), (float) y, color);
    }
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        } else {
            int width = 0;
            CFont.CharData[] currentData = this.charData;
            boolean bold = false;
            boolean italic = false;
            int size = text.length();

            for (int i = 0; i < size; ++i) {
                char character = text.charAt(i);
                if (String.valueOf(character).equals("§")) {
                    int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                    } else if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            currentData = this.boldItalicChars;
                        } else {
                            currentData = this.boldChars;
                        }
                    } else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            currentData = this.boldItalicChars;
                        } else {
                            currentData = this.italicChars;
                        }
                    } else if (colorIndex == 21) {
                        bold = false;
                        italic = false;
                        currentData = this.charData;
                    }

                    ++i;
                } else if (character < currentData.length) {
                    width += currentData[character].width - 8 + this.charOffset;
                }
            }

            return width / 2;
        }
    }

    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

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
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void setupMinecraftColorcodes() {
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

            this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }

    }
}