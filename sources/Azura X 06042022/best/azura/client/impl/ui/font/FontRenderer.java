package best.azura.client.impl.ui.font;

import best.azura.client.impl.ui.Texture;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.src.Config;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public class FontRenderer extends net.minecraft.client.gui.FontRenderer {

    private final int[] locations = new int[256 - 32];
    private final int[] colorCode = new int[32];
    private final Texture glyphTexture;

    public FontRenderer(Font font) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
                Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getLanguageManager().isCurrentLocaleUnicode());
        BufferedImage map = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        for (int i = 32; i < 256; i++) {
            char c = (char) i;
            final BufferedImage image = createCharImage(font, c, true);
            final BufferedImage backup = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D backupGraphics = backup.createGraphics();
            backupGraphics.drawImage(map, 0, 0, null);
            backupGraphics.dispose();
            map = new BufferedImage((map.getWidth() == 1 ? 0 : map.getWidth()) + image.getWidth(), Math.max(map.getHeight(), image.getHeight()), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D mapGraphics = map.createGraphics();
            mapGraphics.drawImage(backup, 0, 0, null);
            mapGraphics.drawImage(image, backup.getWidth(), 0, null);
            locations[c - 32] = backup.getWidth();
        }
        glyphTexture = new Texture(map, true);
        FONT_HEIGHT = map.getHeight();
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) k += 85;
            if (Minecraft.getMinecraft().gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public BufferedImage createCharImage(Font font, char c, boolean antiAlias) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setFont(font);
        int charWidth = graphics.getFontMetrics().charWidth(c);
        int charHeight = graphics.getFontMetrics().getHeight();
        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if (antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setPaint(Color.WHITE);
        g.drawString(String.valueOf(c), 0, g.getFontMetrics().getAscent());
        g.dispose();
        return image;
    }

    @Override
    public int drawString(String text, double x, double y, int color) {
        return drawString(text, (float) x, (float) y, color, false);
    }

    @Override
    public int drawString(String text, float x, float y, int color, boolean isShadow) {
        if (text == null || text.isEmpty()) return 0;
        char[] chars = text.toCharArray();
        boolean skipNext = false;
        int originColor = color;
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        for (char c0 : chars) {
            if (c0 == 167) {
                skipNext = true;
                continue;
            }
            if (skipNext) {
                if (c0 == 'r') color = originColor;
                try {
                    int i1 = "0123456789abcdefklmnor".indexOf(c0);
                    if (i1 < 16) {
                        if (i1 < 0) i1 = 15;
                        if (isShadow) i1 += 16;
                        int j1 = this.colorCode[i1];
                        if (Config.isCustomColors()) j1 = CustomColors.getTextColor(i1, j1);
                        color = RenderUtil.INSTANCE.modifiedAlpha(new Color(j1), 255).getRGB();
                    }
                } catch (Exception ignored) {
                }
                skipNext = false;
                continue;
            }
            int darkerColor = (color & 16579836) >> 2 | color & -16777216;
            drawChar(c0, x, y, isShadow ? darkerColor : color);
            x += getStringWidth(Character.toString(c0));
        }
        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
        return (int) x;
    }

    private void drawChar(char c, double x, double y, int color) {
        if (!(c - 31 > 0 && c - 32 < locations.length)) return;
        final int i = locations[c - 32], next = locations[c - 31];
        RenderUtil.INSTANCE.color(color);
        final float w = (float)i / glyphTexture.getWidth(), w1 = (float)next / glyphTexture.getWidth();
        glyphTexture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(w, 0);
        glVertex2d(x, y);
        glTexCoord2f(w1, 0);
        glVertex2d(x + (next - i), y);
        glTexCoord2f(w1, 1);
        glVertex2d(x + (next - i), y + glyphTexture.getHeight());
        glTexCoord2f(w, 1);
        glVertex2d(x, y + glyphTexture.getHeight());
        glEnd();
        glyphTexture.unbind();
    }

    public int drawStringWithShadow(String text, double x, double y, double shadowLength, int color) {
        if (text == null || text.isEmpty()) return 0;
        drawString(text, (float) (x + shadowLength), (float) (y + shadowLength), color, true);
        return drawString(text, x, y, color);
    }

    @Override
    public int drawStringWithShadow(String text, double x, double y, int color) {
        return drawStringWithShadow(text, x, y, 1, color);
    }

    public int drawStringWithOutline(String text, double x, double y , double shadowLength, int color) {
        int darkerColor = (color & 16579836) >> 2 | color & -16777216;
        this.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(text), x, y + shadowLength, darkerColor);
        this.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(text), x, y - shadowLength, darkerColor);
        this.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(text), x + shadowLength, y, darkerColor);
        this.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(text), x - shadowLength, y, darkerColor);
        return this.drawString(text, x, y, color);
    }

    @Override
    public int drawStringWithOutline(String text, double x, double y, int color) {
        return drawStringWithOutline(text, x, y, 1, color);
    }

    public int getStringWidth(String text) {
        int width = 0;
        for (char c0 : EnumChatFormatting.getTextWithoutFormattingCodes(text).toCharArray()) {
            if (!(c0 - 31 > 0 && c0 - 32 < locations.length)) continue;
            //if (!glyphs.containsKey(c0)) continue;
            //width += glyphs.get(c0).getWidth();
            width += locations[c0 - 31] - locations[c0 - 32];
        }
        return width;
    }

    public int[] getGlyphLocation(char c) {
        return new int[]{0, 0};
    }

    @Override
    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    @Override
    public String trimStringToWidth(String text, int width, boolean reverse)
    {
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0F;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int k = i; k >= 0 && k < text.length() && f < (float)width; k += j)
        {
            final char c0 = text.charAt(k);
            final int[] location = this.getGlyphLocation(c0);
            /*if (texture == null) continue;
            float f1 = texture.getWidth();*/
            float f1 = 0;

            if (flag)
            {
                flag = false;

                if (c0 != 108 && c0 != 76)
                {
                    if (c0 == 114 || c0 == 82)
                    {
                        flag1 = false;
                    }
                }
                else
                {
                    flag1 = true;
                }
            }
            else if (f1 < 0.0F)
            {
                flag = true;
            }
            else
            {
                f += f1;

                if (flag1)
                {
                    ++f;
                }
            }

            if (f > (float)width)
            {
                break;
            }

            if (reverse)
            {
                stringbuilder.insert(0, c0);
            }
            else
            {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }


}