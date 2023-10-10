package tech.atani.client.feature.font.renderer.old;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.src.Config;
import net.optifine.CustomColors;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.render.color.ColorUtil;

@Native
public final class ClassicFontRenderer extends FontRenderer implements Methods {
    public static final int IMAGE_SIZE = 1524;
    private final DynamicTexture boldTexture;
    private final DynamicTexture italicTexture;
    private final DynamicTexture boldAndItalicTexture;
    private final Glyph[] basicGlyphs = new Glyph[256];
    private final Glyph[] boldGlyphs = new Glyph[256];
    private final Glyph[] italicGlyphs = new Glyph[256];
    private final Glyph[] boldAndItalicGlyphs = new Glyph[256];
    private final Font font;
    private DynamicTexture basicTexture;

    public ClassicFontRenderer(Font font, boolean bl2) {
        super(ClassicFontRenderer.mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), mc.isUnicode());
        this.basicTexture = this.generateFontTexture(font, this.basicGlyphs, bl2);
        this.boldTexture = this.generateFontTexture(font.deriveFont(1), this.boldGlyphs, bl2);
        this.italicTexture = this.generateFontTexture(font.deriveFont(2), this.italicGlyphs, bl2);
        this.boldAndItalicTexture = this.generateFontTexture(font.deriveFont(3), this.boldAndItalicGlyphs, bl2);
        this.font = font;
        this.FONT_HEIGHT = this.getHeight();
    }

    private DynamicTexture generateFontTexture(Font font, Glyph[] arrglyph, boolean bl2) {
        BufferedImage bufferedImage = new BufferedImage(1024, 1024, 2);
        Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, 1024, 1024);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, bl2 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < arrglyph.length; ++i2) {
            char c2 = (char)i2;
            Glyph glyph = new Glyph();
            Rectangle2D rectangle2D = fontMetrics.getStringBounds(Character.toString(c2), graphics2D);
            int n4 = rectangle2D.getBounds().width + 8;
            int n5 = rectangle2D.getBounds().height;
            if (n2 + n4 >= 1024) {
                n2 = 0;
                n3 += n5;
            }
            arrglyph[c2] = glyph;
            glyph.setPosX(n2);
            glyph.setPosY(n3);
            glyph.setWidth(n4);
            glyph.setHeight(n5);
            graphics2D.drawString(Character.toString(c2), n2 + 1, n3 + fontMetrics.getAscent());
            n2 += n4;
        }
        return new DynamicTexture(bufferedImage);
    }

    private void drawCharacter(char c2, Glyph[] arrglyph, float f2, float f3) {
        int n2 = arrglyph[c2].getWidth();
        int n3 = arrglyph[c2].getHeight();
        float f4 = (float)arrglyph[c2].getPosX() / 1024.0f;
        float f5 = (float)arrglyph[c2].getPosY() / 1024.0f;
        float f6 = (float)n2 / 1024.0f;
        float f7 = (float)n3 / 1024.0f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(f4, f5);
        GL11.glVertex2f(f2, f3);
        GL11.glTexCoord2f(f4, f5 + f7);
        GL11.glVertex2f(f2, f3 + (float)n3);
        GL11.glTexCoord2f(f4 + f6, f5);
        GL11.glVertex2f(f2 + (float)n2, f3);
        GL11.glTexCoord2f(f4 + f6, f5 + f7);
        GL11.glVertex2f(f2 + (float)n2, f3 + (float)n3);
        GL11.glEnd();
    }

    private int renderString(String string, float f2, float f3, int n2, boolean bl2, boolean bl3) {
        if (string == null) {
            return 0;
        }
        string = replaceText(string);
        if (bl2) {
            n2 = (n2 & (bl3 ? 0x50000000 : 0xFCFCFC)) >> 2 | n2 & 0xFF000000;
        }
        f2 = f2 * 2.0f - 2.0f;
        f3 = f3 * 2.0f - 4.0f;
        Glyph[] arrglyph = this.basicGlyphs;
        DynamicTexture dynamicTexture = this.basicTexture;
        float f4 = ColorUtil.colorToRGBA(n2)[3];
        this.setColor(ColorUtil.colorToRGBA(n2)[0], ColorUtil.colorToRGBA(n2)[1], ColorUtil.colorToRGBA(n2)[2], f4);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.blendFunc(770, 771);
        for (int i2 = 0; i2 < string.length(); ++i2) {
            int n3;
            int n4;
            char c2 = string.charAt(i2);
            if (c2 == '\u00a7' && i2 + 1 < string.length()) {
                if (string.charAt(i2 + 1) == 'r') {
                    this.underlineStyle = false;
                    this.boldStyle = false;
                    this.randomStyle = false;
                    this.strikethroughStyle = false;
                    this.italicStyle = false;
                    arrglyph = this.basicGlyphs;
                    dynamicTexture = this.basicTexture;
                    this.setColor(ColorUtil.colorToRGBA(n2)[0], ColorUtil.colorToRGBA(n2)[1], ColorUtil.colorToRGBA(n2)[2], f4);
                }
                if ((n4 = "0123456789abcdefklmnor".indexOf(string.toLowerCase().charAt(i2 + 1))) < 16) {
                    dynamicTexture = this.basicTexture;
                    arrglyph = this.basicGlyphs;
                    this.randomStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.boldStyle = false;
                    n4 = n4 < 0 ? 15 : (bl2 ? n4 + 16 : n4);
                    n3 = Config.isCustomColors() ? CustomColors.getTextColor(n4, this.colorCode[n4]) : this.colorCode[n4];
                    this.setColor(ColorUtil.colorToRGBA(n3)[0], ColorUtil.colorToRGBA(n3)[1], ColorUtil.colorToRGBA(n3)[2], f4);
                } else {
                    switch (n4) {
                        case 16: {
                            this.randomStyle = true;
                            break;
                        }
                        case 17: {
                            this.boldStyle = true;
                            if (this.italicStyle) {
                                arrglyph = this.boldAndItalicGlyphs;
                                dynamicTexture = this.boldAndItalicTexture;
                                break;
                            }
                            arrglyph = this.boldGlyphs;
                            dynamicTexture = this.boldTexture;
                            break;
                        }
                        case 18: {
                            this.strikethroughStyle = true;
                            break;
                        }
                        case 19: {
                            this.underlineStyle = true;
                            break;
                        }
                        case 20: {
                            this.italicStyle = true;
                            if (this.boldStyle) {
                                arrglyph = this.boldAndItalicGlyphs;
                                dynamicTexture = this.boldAndItalicTexture;
                                break;
                            }
                            arrglyph = this.italicGlyphs;
                            dynamicTexture = this.italicTexture;
                            break;
                        }
                        case 21: {
                            this.underlineStyle = false;
                            this.boldStyle = false;
                            this.randomStyle = false;
                            this.strikethroughStyle = false;
                            this.italicStyle = false;
                            arrglyph = this.basicGlyphs;
                            dynamicTexture = this.basicTexture;
                            this.setColor(ColorUtil.colorToRGBA(n2)[0], ColorUtil.colorToRGBA(n2)[1], ColorUtil.colorToRGBA(n2)[2], f4);
                        }
                    }
                }
                ++i2;
                continue;
            }
            if (c2 < arrglyph.length) {
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.bindTexture(dynamicTexture.getGlTextureId());
                this.drawCharacter(c2, arrglyph, f2, f3);
                n4 = arrglyph[c2].getWidth() - 8;
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                if (this.strikethroughStyle) {
                    GlStateManager.disableTexture2D();
                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(f2, f3 + (float)(this.font.getSize() / 2), 0.0).endVertex();
                    worldRenderer.pos(f2 + (float)n4, f3 + (float)(this.font.getSize() / 2), 0.0).endVertex();
                    worldRenderer.pos(f2 + (float)n4, f3 + (float)(this.font.getSize() / 2) - 1.0f, 0.0).endVertex();
                    worldRenderer.pos(f2, f3 + (float)(this.font.getSize() / 2) - 1.0f, 0.0).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }
                if (this.underlineStyle) {
                    GlStateManager.disableTexture2D();
                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
                    int n5 = this.underlineStyle ? -1 : 0;
                    worldRenderer.pos(f2 + (float)n5, f3 + (float)this.font.getSize() + 5.0f, 0.0).endVertex();
                    worldRenderer.pos(f2 + (float)n4, f3 + (float)this.font.getSize() + 5.0f, 0.0).endVertex();
                    worldRenderer.pos(f2 + (float)n4, f3 + (float)this.font.getSize() + 3.0f, 0.0).endVertex();
                    worldRenderer.pos(f2 + (float)n5, f3 + (float)this.font.getSize() + 3.0f, 0.0).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }
                f2 += (float)n4;
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                continue;
            }
            n4 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c2);
            if (this.randomStyle && n4 != -1) {
                char c3;
                n3 = this.getCharWidth(c2);
                while (n3 != this.getCharWidth(c3 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(n4 = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length())))) {
                }
                c2 = c3;
            }
            this.posX = f2 / 2.0f + 1.5f;
            this.posY = f3 / 2.0f + 1.5f;
            float f5 = this.renderUnicodeChar(c2, false);
            f2 += f5 + 6.0f;
        }
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        return (int)f2 / 2;
    }

    @Override
    public float getStringWidth(String string) {
        if (string == null) {
            return 0;
        }
        string = replaceText(string);
        int n2 = 0;
        Glyph[] arrglyph = this.basicGlyphs;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            int n3;
            char c2 = string.charAt(i2);
            if (c2 == '\u00a7' && i2 + 1 < string.length()) {
                n3 = "0123456789abcdefklmnor".indexOf(string.toLowerCase().charAt(i2 + 1));
                if (n3 < 16) {
                    arrglyph = this.basicGlyphs;
                    this.randomStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.boldStyle = false;
                } else {
                    switch (n3) {
                        case 16: {
                            this.randomStyle = true;
                            break;
                        }
                        case 17: {
                            this.boldStyle = true;
                            if (this.italicStyle) {
                                arrglyph = this.boldAndItalicGlyphs;
                                break;
                            }
                            arrglyph = this.boldGlyphs;
                            break;
                        }
                        case 18: {
                            this.strikethroughStyle = true;
                            break;
                        }
                        case 19: {
                            this.underlineStyle = true;
                            break;
                        }
                        case 20: {
                            this.italicStyle = true;
                            if (this.boldStyle) {
                                arrglyph = this.boldAndItalicGlyphs;
                                break;
                            }
                            arrglyph = this.italicGlyphs;
                            break;
                        }
                        case 21: {
                            this.underlineStyle = false;
                            this.boldStyle = false;
                            this.randomStyle = false;
                            this.strikethroughStyle = false;
                            this.italicStyle = false;
                            arrglyph = this.basicGlyphs;
                        }
                    }
                }
                ++i2;
                continue;
            }
            if (c2 < arrglyph.length) {
                n2 += arrglyph[c2].getWidth() - 8;
                continue;
            }
            n3 = this.glyphWidth[c2] >>> 4;
            int n4 = this.glyphWidth[c2] & 0xF;
            float f2 = n3 &= 0xF;
            float f3 = n4 + 1;
            n2 = (int)((float)n2 + ((f3 - f2) / 2.0f + 1.0f + 6.0f));
        }
        return n2 / 2;
    }

    @Override
    public final int getCharWidth(char c2) {
        return super.getCharWidth(c2);
    }

    @Override
    public final int drawStringWithShadow(String string, float x, float y, int color) {
        string = replaceText(string);
        this.renderString(string, x + 1.0f, y + 1.0f, color, true, true);
        return this.renderString(string, x, y, color, false, false);
    }

    @Override
    public final int drawString(String string, float x, float y, int color) {
        string = replaceText(string);
        return this.renderString(string, x, y, color, false, false);
    }

    @Override
    public final int drawString(String string, float x, float y, int color, boolean hasAlpha) {
        string = replaceText(string);
        if (hasAlpha) {
            this.renderString(string, x + 1.0f, y + 1.0f, color, true, true);
        }
        return this.renderString(string, x, y, color, false, false);
    }

    @Override
    public final int getHeight() {
        return this.font.getSize() / 2;
    }

    @Override
    public final Font getFont() {
        return this.font;
    }

    public final void setFont(Font font, boolean bl2) {
        this.basicTexture = this.generateFontTexture(font, this.basicGlyphs, bl2);
    }
}

