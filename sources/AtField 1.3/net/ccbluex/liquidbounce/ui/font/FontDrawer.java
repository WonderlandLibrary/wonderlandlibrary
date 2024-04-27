/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.font;

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
import net.ccbluex.liquidbounce.ui.font.ColorUtils;
import net.ccbluex.liquidbounce.ui.font.GLUtils;
import net.ccbluex.liquidbounce.ui.font.StringUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public final class FontDrawer {
    private final int halfHeight;
    private final Font font;
    private final Font runtimeFont;
    private final int imageSize;
    private final Glyph[] glyphs = new Glyph[65536];
    private static final Map RUNTIME_FONT_MAP = new HashMap();
    public int FONT_HEIGHT;
    public static boolean RuntimeFontAntiAliasing;
    private final boolean antiAliasing;
    private static final int[] COLORS;
    private static final int SHADOW_COLOR;

    public int drawString(String string, double d, double d2, int n, boolean bl) {
        GlStateManager.func_179117_G();
        if (string != null && !string.isEmpty()) {
            if (this.font.getSize() == 18) {
                this.FONT_HEIGHT = 18;
            }
            string = StringUtils.filterEmoji(string);
            this.preDraw();
            GLUtils.color(n);
            d = (d - 2.0) * 2.0;
            d2 = (d2 - 1.0) * 2.0;
            boolean bl2 = false;
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            block7: for (int i = 0; i < string.length(); ++i) {
                Glyph glyph;
                char c = string.charAt(i);
                if (c == '\u00a7') {
                    if (++i >= string.length()) {
                        glyph = this.getGlyph('\u00a7');
                        this.drawGlyph(glyph, d, d2, bl2, bl4, bl5, bl3);
                        d += (double)glyph.width;
                    }
                    if (bl) continue;
                    int n2 = 21;
                    if (i < string.length()) {
                        n2 = "0123456789abcdefklmnor".indexOf(string.charAt(i));
                    }
                    switch (n2) {
                        case 17: {
                            bl2 = true;
                            break;
                        }
                        case 18: {
                            bl4 = true;
                            break;
                        }
                        case 19: {
                            bl5 = true;
                            break;
                        }
                        case 20: {
                            bl3 = true;
                            break;
                        }
                        case 21: {
                            bl2 = false;
                            bl3 = false;
                            bl5 = false;
                            bl4 = false;
                            GLUtils.color(n);
                            break;
                        }
                        default: {
                            if (n2 >= 16) continue block7;
                            if (n2 == -1) {
                                n2 = 15;
                            }
                            int n3 = COLORS[n2];
                            GLUtils.color(ColorUtils.getRed(n3), ColorUtils.getGreen(n3), ColorUtils.getBlue(n3), ColorUtils.getAlpha(n));
                            break;
                        }
                    }
                    continue;
                }
                glyph = this.getGlyph(c);
                this.drawGlyph(glyph, d, d2, bl2, bl4, bl5, bl3);
                d += (double)glyph.width;
            }
            this.postDraw();
            return this.getStringWidth(string);
        }
        return 0;
    }

    public void DisplayFont(String string, float f, float f2, int n) {
        this.drawString(string, f, f2, n);
    }

    public int drawStringWithShadow(String string, double d, double d2, int n) {
        this.drawString(string, d + 0.5, d2 + 0.5, SHADOW_COLOR, true);
        return this.drawString(string, d, d2, n, false);
    }

    public void DisplayFont2(FontDrawer fontDrawer, String string, float f, float f2, int n, boolean bl) {
        if (bl) {
            fontDrawer.drawStringWithShadow(string, f, f2, n);
        } else {
            fontDrawer.drawString(string, f, f2, n);
        }
    }

    public void drawString(String string, double d, double d2, int n) {
        this.drawString(string, d, d2, n, false);
    }

    public void drawString(String string, float f, float f2, int n) {
        this.drawString(string, f, f2, n, false);
    }

    public FontDrawer getDefaultFont() {
        return this;
    }

    public int drawString(String string, float f, float f2, int n, boolean bl) {
        return this.drawString(string, (double)f, (double)f2, n, bl);
    }

    public void DisplayFont2(FontDrawer fontDrawer, String string, int n, int n2, int n3) {
        fontDrawer.drawString(string, n, n2, n3);
    }

    public void drawStringDirect(String string, double d, double d2, int n) {
        if (string != null && !string.isEmpty()) {
            this.preDraw();
            GLUtils.color(n);
            d = (d - 2.0) * 2.0;
            d2 = (d2 - 1.0) * 2.0;
            for (int i = 0; i < string.length(); ++i) {
                Glyph glyph = this.getGlyph(string.charAt(i));
                glyph.draw(d, d2, false);
                d += (double)glyph.width;
            }
            this.postDraw();
        }
    }

    static int access$000(FontDrawer fontDrawer) {
        return fontDrawer.imageSize;
    }

    public void drawCenteredString(String string, double d, double d2, int n, boolean bl) {
        this.drawString(string, d - (double)this.getStringWidth(string) / 2.0, d2, n, bl);
    }

    public void DisplayFont(String string, float f, float f2, int n, FontDrawer fontDrawer) {
        fontDrawer.drawString(string, f, f2, n);
    }

    public void DisplayFont(FontDrawer fontDrawer, String string, int n, int n2, int n3) {
        fontDrawer.drawString(string, n, n2, n3);
    }

    public float DisplayFontWidths(String string, FontDrawer fontDrawer) {
        return fontDrawer.getStringWidth(string);
    }

    public float DisplayFontWidths(FontDrawer fontDrawer, String string) {
        return fontDrawer.getStringWidth(string);
    }

    private Glyph createGlyph(char c) {
        String string = String.valueOf(c);
        BufferedImage bufferedImage = new BufferedImage(this.imageSize, this.imageSize, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int n = 0;
        if (this.font.canDisplay(c)) {
            FontDrawer.setRenderingHints(graphics2D, this.antiAliasing);
            graphics2D.setFont(this.font);
        } else {
            FontDrawer.setRenderingHints(graphics2D, RuntimeFontAntiAliasing);
            graphics2D.setFont(this.runtimeFont);
            n = 1;
        }
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(string, 0, this.FONT_HEIGHT - 1 + n);
        graphics2D.dispose();
        return new Glyph(this, new DynamicTexture(bufferedImage), fontMetrics.getStringBounds((String)string, (Graphics)graphics2D).getBounds().width);
    }

    public void DisplayFonts(FontDrawer fontDrawer, String string, int n, int n2, int n3) {
        fontDrawer.drawString(string, n, n2, n3);
    }

    public void drawCenteredString(String string, double d, double d2, int n) {
        this.drawString(string, d - (double)this.getStringWidth(string) / 2.0, d2, n);
    }

    static {
        COLORS = new int[32];
        SHADOW_COLOR = ColorUtils.getRGB(0, 0, 0, 50);
        RuntimeFontAntiAliasing = true;
        for (int i = 0; i < COLORS.length; ++i) {
            int n = (i >> 3 & 1) * 85;
            int n2 = (i >> 2 & 1) * 170 + n;
            int n3 = (i >> 1 & 1) * 170 + n;
            int n4 = (i & 1) * 170 + n;
            if (i == 6) {
                n2 += 85;
            }
            if (i >= 16) {
                n2 /= 4;
                n3 /= 4;
                n4 /= 4;
            }
            FontDrawer.COLORS[i] = (n2 & 0xFF) << 16 | (n3 & 0xFF) << 8 | n4 & 0xFF;
        }
    }

    public int getStringWidth(String string) {
        if (string != null && !string.isEmpty()) {
            int n = 0;
            for (int i = 0; i < string.length(); ++i) {
                n += this.getGlyph((char)string.charAt((int)i)).halfWidth;
            }
            return n + 2;
        }
        return 0;
    }

    public void drawCenteredString(String string, float f, float f2, int n, boolean bl) {
        this.drawString(string, (double)f - (double)this.getStringWidth(string) / 2.0, (double)f2, n, bl);
    }

    private Glyph getGlyph(char c) {
        Glyph glyph = this.glyphs[c];
        if (glyph == null) {
            this.glyphs[c] = glyph = this.createGlyph(c);
        }
        return glyph;
    }

    public int drawStringWithShadow(String string, float f, float f2, int n) {
        return this.drawStringWithShadow(string, (double)f, (double)f2, n);
    }

    public void DisplayFont2(String string, float f, float f2, int n, FontDrawer fontDrawer) {
        fontDrawer.drawString(string, f, f2, n);
    }

    public void drawStringWithShadowDirect(String string, double d, double d2, int n) {
        this.drawStringDirect(string, d + 0.5, d2 + 0.5, SHADOW_COLOR);
        this.drawStringDirect(string, d, d2, n);
    }

    public void DisplayFont(FontDrawer fontDrawer, String string, float f, float f2, int n) {
        fontDrawer.drawString(string, f, f2, n);
    }

    public void drawCenteredStringWithShadow(String string, double d, double d2, int n) {
        this.drawStringWithShadow(string, d - (double)this.getStringWidth(string) / 2.0, d2, n);
    }

    public int drawString(String string, int n, int n2, int n3, boolean bl) {
        return this.drawString(string, (double)n, (double)n2, n3, bl);
    }

    private static void setRenderingHints(Graphics2D graphics2D, boolean bl) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (bl) {
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        } else {
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    private Font getRuntimeFont() {
        Font font = (Font)RUNTIME_FONT_MAP.get(this.FONT_HEIGHT);
        if (font == null) {
            font = new Font("SansSerif", 0, this.FONT_HEIGHT);
            RUNTIME_FONT_MAP.put(this.FONT_HEIGHT, font);
        }
        return font;
    }

    public FontDrawer(Font font, boolean bl) {
        this.font = font;
        this.FONT_HEIGHT = font.getSize();
        this.imageSize = font.getSize() + 4;
        this.halfHeight = font.getSize() / 2;
        this.antiAliasing = bl;
        this.runtimeFont = this.getRuntimeFont();
    }

    public int getHeight() {
        return this.FONT_HEIGHT;
    }

    private void preDraw() {
        GLUtils.pushMatrix();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        if (!GL11.glIsEnabled((int)3553)) {
            GlStateManager.func_179098_w();
        }
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
    }

    public void DisplayFonts(String string, float f, float f2, int n, FontDrawer fontDrawer) {
        fontDrawer.drawString(string, f, f2, n);
    }

    private void drawGlyph(Glyph glyph, double d, double d2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (bl) {
            glyph.draw(d + 1.0, d2, bl4);
        }
        glyph.draw(d, d2, bl4);
        if (bl2) {
            double d3 = d2 + (double)this.FONT_HEIGHT / 2.0;
            FontDrawer.drawLine(d, d3 - 1.0, d + (double)glyph.width, d3 + 1.0);
        }
        if (bl3) {
            FontDrawer.drawLine(d, d2 + (double)this.FONT_HEIGHT - 1.0, d + (double)glyph.width, d2 + (double)this.FONT_HEIGHT + 1.0);
        }
    }

    public String trimStringToWidth(String string, int n, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        int n3 = bl ? string.length() - 1 : 0;
        int n4 = bl ? -1 : 1;
        boolean bl2 = false;
        boolean bl3 = false;
        for (int i = n3; i >= 0 && i < string.length() && n2 < n; i += n4) {
            char c = string.charAt(i);
            int n5 = this.getStringWidth(String.valueOf(c));
            if (bl2) {
                bl2 = false;
                if (c != 'l' && c != 'L') {
                    if (c == 'r' || c == 'R') {
                        bl3 = false;
                    }
                } else {
                    bl3 = true;
                }
            } else if (n5 < 0) {
                bl2 = true;
            } else {
                n2 += n5;
                if (bl3) {
                    ++n2;
                }
            }
            if (n2 > n) break;
            if (bl) {
                stringBuilder.insert(0, c);
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void postDraw() {
        GlStateManager.func_179084_k();
        GLUtils.popMatrix();
        GLUtils.resetColor();
    }

    public int getHalfHeight() {
        return this.halfHeight;
    }

    public Font getFont() {
        return this.font;
    }

    public void DisplayFonts(FontDrawer fontDrawer, String string, float f, float f2, int n) {
        fontDrawer.drawString(string, f, f2, n);
    }

    public int drawString(String string, int n, int n2, int n3) {
        return this.drawString(string, n, n2, n3, false);
    }

    private static void drawLine(double d, double d2, double d3, double d4) {
        double d5;
        if (d < d3) {
            d5 = d;
            d = d3;
            d3 = d5;
        }
        if (d2 < d4) {
            d5 = d2;
            d2 = d4;
            d4 = d5;
        }
        Tessellator tessellator = Tessellator.func_178181_a();
        IWorldRenderer iWorldRenderer = ClassProviderImpl.INSTANCE.getTessellatorInstance().getWorldRenderer();
        if (GL11.glIsEnabled((int)3553)) {
            GlStateManager.func_179090_x();
        }
        iWorldRenderer.begin(7, ClassProviderImpl.INSTANCE.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        iWorldRenderer.pos(d, d4, 0.0).endVertex();
        iWorldRenderer.pos(d3, d4, 0.0).endVertex();
        iWorldRenderer.pos(d3, d2, 0.0).endVertex();
        iWorldRenderer.pos(d, d2, 0.0).endVertex();
        tessellator.func_78381_a();
        if (!GL11.glIsEnabled((int)3553)) {
            GlStateManager.func_179098_w();
        }
    }

    public void DisplayFonts(String string, float f, float f2, int n) {
        this.drawString(string, f, f2, n);
    }

    final class Glyph {
        public final int halfWidth;
        final FontDrawer this$0;
        public final DynamicTexture texture;
        public final int width;

        public Glyph(FontDrawer fontDrawer, DynamicTexture dynamicTexture, int n) {
            this.this$0 = fontDrawer;
            this.texture = dynamicTexture;
            this.width = n;
            this.halfWidth = n / 2;
        }

        public void draw(double d, double d2, boolean bl) {
            GlStateManager.func_179144_i((int)this.texture.func_110552_b());
            double d3 = bl ? 2.0 : 0.0;
            GL11.glBegin((int)5);
            GL11.glTexCoord2d((double)0.0, (double)0.0);
            GL11.glVertex3d((double)(d + d3), (double)d2, (double)0.0);
            GL11.glTexCoord2d((double)0.0, (double)1.0);
            GL11.glVertex3d((double)(d - d3), (double)(d2 + (double)FontDrawer.access$000(this.this$0)), (double)0.0);
            GL11.glTexCoord2d((double)1.0, (double)0.0);
            GL11.glVertex3d((double)(d + (double)FontDrawer.access$000(this.this$0) + d3), (double)d2, (double)0.0);
            GL11.glTexCoord2d((double)1.0, (double)1.0);
            GL11.glVertex3d((double)(d + (double)FontDrawer.access$000(this.this$0) - d3), (double)(d2 + (double)FontDrawer.access$000(this.this$0)), (double)0.0);
            GL11.glEnd();
        }
    }
}

