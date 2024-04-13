package dev.tenacity.util.font;

import dev.tenacity.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MinecraftFontRenderer extends CFont {
    CharData[] boldChars = new CharData[256],
            italicChars = new CharData[256],
            boldItalicChars = new CharData[256];
    int[] colorCode = new int[32];
    DynamicTexture texBold, texItalic, texItalicBold;
    String colorcodeIdentifiers = "0123456789abcdefklmnor";

    public MinecraftFontRenderer(Font font) {
        super(font, true, true);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public MinecraftFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public int drawStringWithShadow(String text, double x2, double y2, int color) {

        float shadowWidth = this.drawString(text, x2 + 0.5f, y2 + 0.5f, color, true, 8.3f, false);

        return (int) Math.max(shadowWidth, this.drawString(text, x2, y2, color, false, 8.3f, false));
    }

    public int drawString(String text, double x2, double y2, int color) {
        return (int) this.drawString(text, x2, y2, color, false, 8.3f, false);
    }

    public int drawPassword(String text, double x2, float y2, int color) {
        return (int) this.drawString(text.replaceAll("\\.", "."), x2, y2, color, false, 8f, false);
    }

    public void drawSmoothString(String text, double x2, float y2, int color) {
        this.drawString(text, x2, y2, color, false, 8.3f, true);
    }

    public void drawSmoothStringWithShadow(String text, double x2, float y2, int color) {
        this.drawString(text, x2 + 0.5f, y2 + 0.5f, color, true, 8.3f, true);

         this.drawString(text, x2, y2, color, false, 8.3f, true);
    }

    public double getPasswordWidth(String text) {
        return this.getStringWidth(text.replaceAll("\\.", "."), 8);
    }

    public float drawCenteredString(String text, float x2, float y2, int color) {
        return this.drawString(text, x2 - (float) (this.getStringWidth(text) / 2), y2, color);
    }

    public float drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
        return this.drawStringWithShadow(text, x2 - (float) (this.getStringWidth(text) / 2), y2, color);
    }

    public float getMiddleOfBox(float boxHeight) {
        return boxHeight / 2f - getHeight() / 2f;
    }


    public float drawString(String text, double x, double y, int color, boolean shadow, float kerning, boolean smooth) {
        if (text == null) {
            return 0;
        }

        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }

        CharData[] currentData = this.charData;
        float alpha = (float) (color >> 24 & 255) / 255f;
        boolean bold = false,
                italic = false,
                strikethrough = false,
                underline = false;
        x = (x - 1) * 2;
        y = (y - 3) * 2;
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderUtil.resetColor();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        RenderUtil.bindTexture(this.tex.getGlTextureId());
        if(smooth) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }

        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);

            // If you need to apply color codes
            if (character == '§') {
                int colorIndex = 21;

                try {
                    colorIndex = colorcodeIdentifiers.indexOf(text.charAt(index + 1));
                } catch (Exception e) {
                    e.printStackTrace();
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

                    RenderUtil.resetColor();
                } else {
                    switch (colorIndex) {
                        case 17:
                            bold = true;

                            if (italic) {
                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                currentData = this.boldItalicChars;
                            } else {
                                GlStateManager.bindTexture(this.texBold.getGlTextureId());
                                currentData = this.boldChars;
                            }
                            break;
                        case 18:
                            strikethrough = true;
                            break;
                        case 19:
                            underline = true;
                            break;
                        case 20:
                            italic = true;

                            if (bold) {
                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                currentData = this.boldItalicChars;
                            } else {
                                GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                                currentData = this.italicChars;
                            }
                            break;
                        default:
                            bold = false;
                            italic = false;
                            underline = false;
                            strikethrough = false;
                            RenderUtil.resetColor();
                            GlStateManager.bindTexture(this.tex.getGlTextureId());
                            currentData = this.charData;
                            break;
                    }
                }

                ++index;
            } else if (character < currentData.length) {
                drawLetter(x, y, currentData, strikethrough, underline, character);
                x += currentData[character].width - kerning + this.charOffset;
            }
        }
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.bindTexture(0);
        return (float) x / 2f;
    }

    private void drawLetter(double x, double y, CharData[] currentData, boolean strikethrough, boolean underline, char character) {
        GL11.glBegin(GL11.GL_TRIANGLES);
        this.drawChar(currentData, character, (float) x, (float) y);
        GL11.glEnd();

        if (strikethrough) {
            this.drawLine(x, y + (double) (currentData[character].height / 2), x + (double) currentData[character].width - 8,
                    y + (double) (currentData[character].height / 2));
        }
        if (underline) {
            this.drawLine(x, y + (double) currentData[character].height - 2, x + (double) currentData[character].width - 8,
                    y + (double) currentData[character].height - 2);
        }
    }


    public double getStringWidth(String text) {
        if (text == null) {
            return 0;
        }

        float width = 0;
        CharData[] currentData = charData;

        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);

            if (character == '\u00a7') {
                ++index;
            } else if (character < currentData.length) {
                width += currentData[character].width - 8.3f + charOffset;
            }
        }

        return width / 2;
    }

    public double getStringWidth(String text, float kerning) {
        if (text == null) {
            return 0;
        }

        float width = 0;
        CharData[] currentData = charData;

        for (int index = 0; index < text.length(); index++) {
            char c = text.charAt(index);

            if (c == '\u00a7') {
                ++index;
            } else if (c < currentData.length) {
                width += currentData[c].width - kerning + charOffset;
            }
        }

        return width / 2;
    }

    public int getHeight() {
        return (this.fontHeight - 8) / 2;
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
        this.texBold = this.setupTexture(this.font.deriveFont(Font.BOLD), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(Font.BOLD | Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void drawLine(double x2, double y2, double x1, double y1) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth((float) 1);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void wrapText(String text, float x, float y, int color, float width, float heightIncrement) {
        List<String> lines = new ArrayList<>();
        String[] words = text.trim().split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            float totalWidth = (float) getStringWidth(line + " " + word);

            if (x + totalWidth >= x + width) {
                lines.add(line.toString());
                line = new StringBuilder(word).append(" ");
                continue;
            }

            line.append(word).append(" ");
        }
        lines.add(line.toString());

        float newY = y;
        for (String s : lines) {
            RenderUtil.resetColor();
            drawString(s, x, newY, color);
            newY += getHeight() + heightIncrement;
        }
    }


    private void setupMinecraftColorcodes() {
        int index = 0;

        while (index < 32) {
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
            ++index;
        }
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidthPassword(String text, int width, boolean custom) {
        text = text.replaceAll("\\.", ".");
        return this.trimStringToWidth(text, width, custom);
    }

    private float getCharWidthFloat(char c) {
        if (c == 167) {
            return -1;
        } else if (c == 32) {
            return 2;
        } else {
            int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
                    .indexOf(c);

            if (c > 0 && var2 != -1) {
                return ((charData[var2].width / 2.f) - 4.f);
            } else if (((charData[c].width / 2.f) - 4.f) != 0) {
                int var3 = ((int) ((charData[c].width / 2.f) - 4.f)) >>> 4;
                int var4 = ((int) ((charData[c].width / 2.f) - 4.f)) & 15;
                var3 &= 15;
                ++var4;
                return (float) ((var4 - var3) / 2 + 1);
            } else {
                return 0;
            }
        }
    }

    public String trimStringToWidth(String text, int width, boolean custom) {
        StringBuilder buffer = new StringBuilder();
        float lineWidth = 0.0F;
        int offset = custom ? text.length() - 1 : 0;
        int increment = custom ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;

        for (int index = offset; index >= 0 && index < text.length() && lineWidth < (float) width; index += increment) {
            char character = text.charAt(index);
            float charWidth = this.getCharWidthFloat(character);

            if (var8) {
                var8 = false;

                if (character != 108 && character != 76) {
                    if (character == 114 || character == 82) {
                        var9 = false;
                    }
                } else {
                    var9 = true;
                }
            } else if (charWidth < 0) {
                var8 = true;
            } else {
                lineWidth += charWidth;

                if (var9) {
                    ++lineWidth;
                }
            }

            if (lineWidth > (float) width) {
                break;
            }

            if (custom) {
                buffer.insert(0, character);
            } else {
                buffer.append(character);
            }
        }

        return buffer.toString();
    }
}
