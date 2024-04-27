/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.cnfont;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.injection.backend.ClassProviderImpl;
import net.ccbluex.liquidbounce.ui.cnfont.ColorUtils;
import net.ccbluex.liquidbounce.ui.cnfont.GLUtils;
import net.ccbluex.liquidbounce.ui.cnfont.StringUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public final class FontDrawer {
    private static final Map<Integer, Font> RUNTIME_FONT_MAP = new HashMap<Integer, Font>();
    private static final int[] COLORS = new int[32];
    private static final int SHADOW_COLOR = 0;
    public static boolean RuntimeFontAntiAliasing = true;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private final Glyph[] glyphs = new Glyph[65536];
    private final Font font;
    private final Font runtimeFont;
    private final int imageSize;
    private final int halfHeight;
    private final boolean antiAliasing;
    public int FONT_HEIGHT;

    public FontDrawer(Font font, boolean antiAliasing) {
        this.font = font;
        this.FONT_HEIGHT = font.getSize();
        this.imageSize = font.getSize() + 4;
        this.halfHeight = font.getSize() / 2;
        this.antiAliasing = antiAliasing;
        this.runtimeFont = this.getRuntimeFont();
    }

    private static void setRenderingHints(Graphics2D g, boolean antiAliasing) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (antiAliasing) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        } else {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    private static void drawLine(double left, double top, double right, double bottom) {
        double j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }
        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }
        Tessellator tessellator = Tessellator.func_178181_a();
        IWorldRenderer worldrenderer = ClassProviderImpl.INSTANCE.getTessellatorInstance().getWorldRenderer();
        if (GL11.glIsEnabled((int)3553)) {
            GlStateManager.func_179090_x();
        }
        worldrenderer.begin(7, ClassProviderImpl.INSTANCE.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.func_78381_a();
        if (!GL11.glIsEnabled((int)3553)) {
            GlStateManager.func_179098_w();
        }
    }

    public float DisplayFontWidths(FontDrawer font, String str) {
        return font.getStringWidth(str);
    }

    public Font getFont() {
        return this.font;
    }

    public float DisplayFontWidths(String str, FontDrawer font) {
        return font.getStringWidth(str);
    }

    public FontDrawer getDefaultFont() {
        return this;
    }

    public void DisplayFonts(FontDrawer font, String str, float x, float y, int color) {
        font.drawString(str, x, y, color);
    }

    public void DisplayFonts(FontDrawer font, String str, int x, int y, int color) {
        font.drawString(str, x, y, color);
    }

    public void DisplayFonts(String str, float x, float y, int color, FontDrawer font) {
        font.drawString(str, x, y, color);
    }

    public void DisplayFonts(String str, float x, float y, int color) {
        this.drawString(str, x, y, color);
    }

    public void DisplayFont(String str, float x, float y, int color) {
        this.drawString(str, x, y, color);
    }

    public void DisplayFont(FontDrawer font, String str, float x, float y, int color) {
        font.drawString(str, x, y, color);
    }

    public void DisplayFont(FontDrawer font, String str, int x, int y, int color) {
        font.drawString(str, x, y, color);
    }

    public void DisplayFont(String str, float x, float y, int color, FontDrawer font) {
        font.drawString(str, x, y, color);
    }

    public void DisplayFont2(FontDrawer font, String str, float x, float y, int color, boolean b) {
        if (b) {
            font.drawStringWithShadow(str, x, y, color);
        } else {
            font.drawString(str, x, y, color);
        }
    }

    public void DisplayFont2(FontDrawer font, String str, int x, int y, int color) {
        font.drawString(str, x, y, color);
    }

    public void DisplayFont2(String str, float x, float y, int color, FontDrawer font) {
        font.drawString(str, x, y, color);
    }

    private Font getRuntimeFont() {
        Font runtimeFont = RUNTIME_FONT_MAP.get(this.FONT_HEIGHT);
        if (runtimeFont == null) {
            runtimeFont = new Font("SansSerif", 0, this.FONT_HEIGHT);
            RUNTIME_FONT_MAP.put(this.FONT_HEIGHT, runtimeFont);
        }
        return runtimeFont;
    }

    public int getStringWidth(String s) {
        if (s != null && !s.isEmpty()) {
            int ret = 0;
            for (int i = 0; i < s.length(); ++i) {
                ret += this.getGlyph((char)s.charAt((int)i)).halfWidth;
            }
            return ret + 2;
        }
        return 0;
    }

    public int getHeight() {
        return this.FONT_HEIGHT;
    }

    public int getHalfHeight() {
        return this.halfHeight;
    }

    public void drawCenteredStringWithShadow(String s, double x, double y, int color) {
        this.drawStringWithShadow(s, x - (double)this.getStringWidth(s) / 2.0, y, color);
    }

    public int drawStringWithShadow(String s, float x, float y, int color) {
        return this.drawStringWithShadow(s, (double)x, (double)y, color);
    }

    public int drawStringWithShadow(String s, double x, double y, int color) {
        this.drawString(s, x + 0.5, y + 0.5, SHADOW_COLOR, true);
        return this.drawString(s, x, y, color, false);
    }

    public void drawStringWithShadowDirect(String s, double x, double y, int color) {
        this.drawStringDirect(s, x + 0.5, y + 0.5, SHADOW_COLOR);
        this.drawStringDirect(s, x, y, color);
    }

    public void drawCenteredString(String s, double x, double y, int color) {
        this.drawString(s, x - (double)this.getStringWidth(s) / 2.0, y, color);
    }

    public void drawCenteredString(String s, double x, double y, int color, boolean b) {
        this.drawString(s, x - (double)this.getStringWidth(s) / 2.0, y, color, b);
    }

    public void drawCenteredString(String s, float x, float y, int color, boolean b) {
        this.drawString(s, (double)x - (double)this.getStringWidth(s) / 2.0, (double)y, color, b);
    }

    public void drawStringDirect(String s, double x, double y, int color) {
        if (s != null && !s.isEmpty()) {
            this.preDraw();
            GLUtils.color(color);
            x = (x - 2.0) * 2.0;
            y = (y - 1.0) * 2.0;
            for (int i = 0; i < s.length(); ++i) {
                Glyph glyph = this.getGlyph(s.charAt(i));
                glyph.draw(x, y, false);
                x += (double)glyph.width;
            }
            this.postDraw();
        }
    }

    public void drawString(String s, double x, double y, int color) {
        this.drawString(s, x, y, color, false);
    }

    public int drawString(String s, int x, int y, int color) {
        return this.drawString(s, x, y, color, false);
    }

    public void drawString(String s, float x, float y, int color) {
        this.drawString(s, x, y, color, false);
    }

    public int drawString(String s, int x, int y, int color, boolean shadow2) {
        return this.drawString(s, (double)x, (double)y, color, shadow2);
    }

    public int drawString(String s, float x, float y, int color, boolean shadow2) {
        return this.drawString(s, (double)x, (double)y, color, shadow2);
    }

    public int drawString(String s, double x, double y, int color, boolean shadow2) {
        GlStateManager.func_179141_d();
        if (s != null && !s.isEmpty()) {
            if (this.font.getSize() == 18) {
                this.FONT_HEIGHT = 18;
            }
            s = StringUtils.filterEmoji(s);
            this.preDraw();
            this.red = (float)(color >> 16 & 0xFF) / 255.0f;
            this.blue = (float)(color >> 8 & 0xFF) / 255.0f;
            this.green = (float)(color & 0xFF) / 255.0f;
            this.alpha = (float)(color >> 24 & 0xFF) / 255.0f;
            GlStateManager.func_179131_c((float)this.red, (float)this.blue, (float)this.green, (float)this.alpha);
            x = (x - 2.0) * 2.0;
            y = (y - 1.0) * 2.0;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;
            block7: for (int i = 0; i < s.length(); ++i) {
                Glyph glyph;
                char c = s.charAt(i);
                if (c == '\u00a7') {
                    int colorIndex;
                    if (++i >= s.length()) {
                        glyph = this.getGlyph('\u00a7');
                        this.drawGlyph(glyph, x, y, bold, strikethrough, underline, italic);
                        x += (double)glyph.width;
                    }
                    if (shadow2) continue;
                    if (i < s.length()) {
                        colorIndex = "0123456789abcdefklmnor".indexOf(s.charAt(i));
                    }
                    switch (colorIndex) {
                        case 17: {
                            bold = true;
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
                            break;
                        }
                        case 21: {
                            bold = false;
                            italic = false;
                            underline = false;
                            strikethrough = false;
                            GLUtils.color(color);
                            break;
                        }
                        default: {
                            if (colorIndex >= 16) continue block7;
                            if (colorIndex == -1) {
                                // empty if block
                            }
                            int finalColor = COLORS[colorIndex];
                            GLUtils.color(ColorUtils.getRed(finalColor), ColorUtils.getGreen(finalColor), ColorUtils.getBlue(finalColor), ColorUtils.getAlpha(color));
                            break;
                        }
                    }
                    continue;
                }
                glyph = this.getGlyph(c);
                this.drawGlyph(glyph, x, y, bold, strikethrough, underline, italic);
                x += (double)glyph.width;
            }
            this.postDraw();
            return this.getStringWidth(s);
        }
        return 0;
    }

    private void drawGlyph(Glyph glyph, double x, double y, boolean bold, boolean strikethrough, boolean underline, boolean italic) {
        if (bold) {
            glyph.draw(x + 1.0, y, italic);
        }
        glyph.draw(x, y, italic);
        if (strikethrough) {
            double mid = y + (double)this.FONT_HEIGHT / 2.0;
            FontDrawer.drawLine(x, mid - 1.0, x + (double)glyph.width, mid + 1.0);
        }
        if (underline) {
            FontDrawer.drawLine(x, y + (double)this.FONT_HEIGHT - 1.0, x + (double)glyph.width, y + (double)this.FONT_HEIGHT + 1.0);
        }
    }

    private void preDraw() {
        GLUtils.pushMatrix();
        GlStateManager.func_179147_l();
        int n = 0;
        if (!GL11.glIsEnabled((int)3553)) {
            GlStateManager.func_179098_w();
        }
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
    }

    private void postDraw() {
        GlStateManager.func_179084_k();
        GLUtils.popMatrix();
        GLUtils.resetColor();
    }

    public String trimStringToWidth(String p_trimStringToWidth_1_, int p_trimStringToWidth_2_, boolean p_trimStringToWidth_3_) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = p_trimStringToWidth_3_ ? p_trimStringToWidth_1_.length() - 1 : 0;
        int k = p_trimStringToWidth_3_ ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int l = j; l >= 0 && l < p_trimStringToWidth_1_.length() && i < p_trimStringToWidth_2_; l += k) {
            char c0 = p_trimStringToWidth_1_.charAt(l);
            int i1 = this.getStringWidth(String.valueOf(c0));
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (i1 < 0) {
                flag = true;
            } else {
                i += i1;
                if (flag1) {
                    ++i;
                }
            }
            if (i > p_trimStringToWidth_2_) break;
            if (p_trimStringToWidth_3_) {
                stringbuilder.insert(0, c0);
                continue;
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.toString();
    }

    private Glyph getGlyph(char c) {
        Glyph glyph = this.glyphs[c];
        if (glyph == null) {
            this.glyphs[c] = glyph = this.createGlyph(c);
        }
        return glyph;
    }

    private Glyph createGlyph(char c) {
        String s = String.valueOf(c);
        BufferedImage image2 = new BufferedImage(this.imageSize, this.imageSize, 2);
        Graphics2D g = image2.createGraphics();
        int offset = 0;
        if (this.font.canDisplay(c)) {
            FontDrawer.setRenderingHints(g, this.antiAliasing);
            g.setFont(this.font);
        } else {
            FontDrawer.setRenderingHints(g, RuntimeFontAntiAliasing);
            g.setFont(this.runtimeFont);
            offset = 1;
        }
        FontMetrics fontMetrics = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.drawString(s, 0, this.FONT_HEIGHT - 1 + offset);
        g.dispose();
        return new Glyph(new DynamicTexture(image2), fontMetrics.getStringBounds((String)s, (Graphics)g).getBounds().width);
    }

    static {
        for (int i = 0; i < COLORS.length; ++i) {
            int offset = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + offset;
            int green = (i >> 1 & 1) * 170 + offset;
            int blue = (i & 1) * 170 + offset;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            FontDrawer.COLORS[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }

    final class Glyph {
        public final DynamicTexture texture;
        public final int width;
        public final int halfWidth;

        public Glyph(DynamicTexture texture, int width) {
            this.texture = texture;
            this.width = width;
            this.halfWidth = width / 2;
        }

        public void draw(double x, double y, boolean italic) {
            GlStateManager.func_179144_i((int)this.texture.func_110552_b());
            double offset = italic ? 2.0 : 0.0;
            GL11.glBegin((int)5);
            GL11.glTexCoord2d((double)0.0, (double)0.0);
            GL11.glVertex3d((double)(x + offset), (double)y, (double)0.0);
            GL11.glTexCoord2d((double)0.0, (double)1.0);
            GL11.glVertex3d((double)(x - offset), (double)(y + (double)FontDrawer.this.imageSize), (double)0.0);
            GL11.glTexCoord2d((double)1.0, (double)0.0);
            GL11.glVertex3d((double)(x + (double)FontDrawer.this.imageSize + offset), (double)y, (double)0.0);
            GL11.glTexCoord2d((double)1.0, (double)1.0);
            GL11.glVertex3d((double)(x + (double)FontDrawer.this.imageSize - offset), (double)(y + (double)FontDrawer.this.imageSize), (double)0.0);
            GL11.glEnd();
        }
    }
}

