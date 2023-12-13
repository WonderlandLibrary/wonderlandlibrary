package me.nyan.flush.ui.fontrenderer;

import me.nyan.flush.Flush;
import me.nyan.flush.module.impl.misc.NameProtect;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class GlyphPageFontRenderer {
    /**
     * Current X coordinate at which to draw the next character.
     */
    private float posX;
    /**
     * Current Y coordinate at which to draw the next character.
     */
    private float posY;
    /**
     * Array of RGB triplets defining the 16 standard chat colors followed by 16 darker version of the same colors for
     * drop shadows.
     */
    private final int[] colorCode = new int[32];
    /**
     * Used to specify new red value for the current color.
     */
    private float red;
    /**
     * Used to specify new blue value for the current color.
     */
    private float blue;
    /**
     * Used to specify new green value for the current color.
     */
    private float green;
    /**
     * Used to speify new alpha value for the current color.
     */
    private float alpha;
    /**
     * Set if the "l" style (bold) is active in currently rendering string
     */
    private boolean boldStyle;
    /**
     * Set if the "o" style (italic) is active in currently rendering string
     */
    private boolean italicStyle;
    /**
     * Set if the "n" style (underlined) is active in currently rendering string
     */
    private boolean underlineStyle;
    /**
     * Set if the "m" style (strikethrough) is active in currently rendering string
     */
    private boolean strikethroughStyle;

    private final GlyphPage glyphPage;

    private boolean textureSetup;

    public GlyphPageFontRenderer(GlyphPage glyphPage) {
        this.glyphPage = glyphPage;

        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6)
                k += 85;

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public static GlyphPageFontRenderer create(Font font) {
        try {
            char[] chars = new char[256];

            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) i;
            }

            GlyphPage glyphPage = new GlyphPage(font.deriveFont(Font.PLAIN), true, true);
            glyphPage.generateGlyphPage(chars);
            return new GlyphPageFontRenderer(glyphPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Draws the specified string.
     */
    public int drawString(String text, float x, float y, int color, boolean shadow) {
        resetStyles();

        if (shadow) {
            renderString(EnumChatFormatting.getTextWithoutFormattingCodes(text), x + 1, y + 1,
                    (color & 16579836) >> 2 | color & -16777216);
        }

        return renderString(text, x, y, color);
    }

    public int drawString(String text, float x, float y, int color) {
        return drawString(text, x, y, color, false);
    }

    public int drawXCenteredString(String text, float x, float y, int color, boolean shadow) {
        return drawString(text, x - getStringWidth(text) / 2f, y, color, shadow);
    }

    public int drawXCenteredString(String text, float x, float y, int color) {
        return drawXCenteredString(text, x, y, color, false);
    }

    public int drawYCenteredString(String text, float x, float y, int color, boolean shadow) {
        return drawString(text, x, y - getFontHeight() / 2f, color, shadow);
    }

    public int drawYCenteredString(String text, float x, float y, int color) {
        return drawYCenteredString(text, x, y, color, false);
    }

    public int drawXYCenteredString(String text, float x, float y, int color, boolean shadow) {
        return drawString(text, x - getStringWidth(text) / 2f, y - getFontHeight() / 2f, color, shadow);
    }

    public int drawXYCenteredString(String text, float x, float y, int color) {
        return drawXYCenteredString(text, x, y, color, false);
    }

    /**
     * Render single line string by setting GL color, current (posX,posY), and calling renderStringAtPos()
     */
    private int renderString(String text, float x, float y, int color) {
        if (text == null) {
            return 0;
        }

        NameProtect nameProtect = Flush.getInstance().getModuleManager().getModule(NameProtect.class);
        if (nameProtect.isEnabled() && Minecraft.getMinecraft().thePlayer != null &&
                EnumChatFormatting.getTextWithoutFormattingCodes(text).contains(Minecraft.getMinecraft().thePlayer.getName())) {
            text = text.replace(Minecraft.getMinecraft().thePlayer.getName(), nameProtect.getCustomName());
        }

        if ((color & -67108864) == 0) {
            color |= -16777216;
        }

        red = (float) (color >> 16 & 255) / 255.0F;
        blue = (float) (color >> 8 & 255) / 255.0F;
        green = (float) (color & 255) / 255.0F;
        alpha = (float) (color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, blue, green, alpha);
        posX = x * 2.0f;
        posY = y * 2.0f;
        renderStringAtPos(text);
        GlStateManager.color(1, 1, 1, 1);
        return (int) posX;
    }

    /**
     * Render a single line string at the current (posX,posY) and update posX
     */
    private void renderStringAtPos(String text) {
        GlyphPage glyphPage = preRender();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == 167 && i + 1 < text.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase().charAt(i + 1));

                if (i1 < 16) {
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;

                    if (i1 < 0)
                        i1 = 15;

                    int j1 = colorCode[i1];

                    GlStateManager.color((float) (j1 >> 16) / 255.0F, (float) (j1 >> 8 & 255) / 255.0F, (float) (j1 & 255) / 255.0F, alpha);
                } else if (i1 == 17) {
                    boldStyle = true;
                } else if (i1 == 18) {
                    strikethroughStyle = true;
                } else if (i1 == 19) {
                    underlineStyle = true;
                } else if (i1 == 20) {
                    italicStyle = true;
                } else {
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;

                    GlStateManager.color(red, blue, green, alpha);
                }

                ++i;
            } else {
                doDraw(glyphPage.drawChar(c, posX, posY), glyphPage);
            }
        }
        postRender(glyphPage);
    }

    private GlyphPage preRender() {
        GlyphPage glyphPage = getCurrentGlyphPage();
        GlStateManager.pushMatrix();

        GlStateManager.scale(0.5, 0.5, 1);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();

        glyphPage.bindTexture();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        return glyphPage;
    }

    public float drawChar(char c, float x, float y, int color, boolean shadow) {
        GlyphPage glyphPage = preRender();

        GlStateManager.enableAlpha();

        if (shadow) {
            RenderUtils.glColor((color & 16579836) >> 2 | color & -16777216);
            glyphPage.drawChar(c, x * 2 + 1, y * 2 + 1);
        }

        RenderUtils.glColor(color);
        float i = glyphPage.drawChar(c, x * 2, y * 2) / 2F;

        RenderUtils.glColor(-1);
        GlStateManager.disableAlpha();

        postRender(glyphPage);
        return i;
    }

    public float drawChar(char c, float x, float y, int color) {
        return drawChar(c, x, y, color, false);
    }

    private void postRender(GlyphPage glyphPage) {
        glyphPage.unbindTexture();
        GlStateManager.popMatrix();
    }

    private void doDraw(float f, GlyphPage glyphPage) {
        if (strikethroughStyle) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(posX, posY + (float) (glyphPage.getMaxFontHeight() / 2), 0.0D).endVertex();
            worldrenderer.pos(posX + f, posY + (float) (glyphPage.getMaxFontHeight() / 2), 0.0D).endVertex();
            worldrenderer.pos(posX + f, posY + (float) (glyphPage.getMaxFontHeight() / 2) - 1.0F, 0.0D).endVertex();
            worldrenderer.pos(posX, posY + (float) (glyphPage.getMaxFontHeight() / 2) - 1.0F, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }

        if (underlineStyle) {
            Tessellator tessellator1 = Tessellator.getInstance();
            WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
            int l = underlineStyle ? -1 : 0;
            worldrenderer1.pos(posX + (float) l, posY + (float) glyphPage.getMaxFontHeight(), 0.0D).endVertex();
            worldrenderer1.pos(posX + f, posY + (float) glyphPage.getMaxFontHeight(), 0.0D).endVertex();
            worldrenderer1.pos(posX + f, posY + (float) glyphPage.getMaxFontHeight() - 1.0F, 0.0D).endVertex();
            worldrenderer1.pos(posX + (float) l, posY + (float) glyphPage.getMaxFontHeight() - 1.0F, 0.0D).endVertex();
            tessellator1.draw();
            GlStateManager.enableTexture2D();
        }

        posX += f;
    }


    private GlyphPage getCurrentGlyphPage() {
        return glyphPage;
    }

    /**
     * Reset all style flag fields in the class to false; called at the start of string rendering
     */
    private void resetStyles() {
        boldStyle = false;
        italicStyle = false;
        underlineStyle = false;
        strikethroughStyle = false;
    }

    public int getFontHeight() {
        return glyphPage.getMaxFontHeight() / 2;
    }

    public float getCharWidth(char c) {
        return (getCurrentGlyphPage().getWidth(c) - 8) / 2F;
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        text = EnumChatFormatting.getTextWithoutFormattingCodes(text);

        int width = 0;

        GlyphPage currentPage;

        int size = text.length();

        boolean on = false;

        for (int i = 0; i < size; i++) {
            char character = text.charAt(i);

            if (character == '§') {
                on = true;
            } else if (on && character >= '0' && character <= 'r') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);

                if (colorIndex < 16) {
                    boldStyle = false;
                    italicStyle = false;
                }else if (colorIndex == 17)
                    boldStyle = true;
                else if (colorIndex == 20)
                    italicStyle = true;
                else if (colorIndex == 21) {
                    boldStyle = false;
                    italicStyle = false;
                }

                i++;
                on = false;
            } else {
                if (on) i--;

                character = text.charAt(i);

                currentPage = getCurrentGlyphPage();

                width += currentPage.getWidth(character) - 8;
            }
        }

        return width / 2;
    }

    /**
     * Trims a string to fit a specified Width.
     */
    public String trimStringToWidth(String text, int width) {
        return trimStringToWidth(text, width, false);
    }

    /**
     * Trims a string to a specified width, and will reverse it if par3 is set.
     */
    public String trimStringToWidth(String text, int maxWidth, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();

        boolean on = false;

        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        int width = 0;

        GlyphPage currentPage;

        for (int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
            char character = text.charAt(i);

            if (character == '§') {
                on = true;
            } else if (on && character >= '0' && character <= 'r') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);

                if (colorIndex < 16) {
                    boldStyle = false;
                    italicStyle = false;
                } else if (colorIndex == 17)
                    boldStyle = true;
                else if (colorIndex == 20)
                    italicStyle = true;
                else if (colorIndex == 21) {
                    boldStyle = false;
                    italicStyle = false;
                }

                i++;
                on = false;
            } else {
                if (on) {
                    i--;
                }

                character = text.charAt(i);

                currentPage = getCurrentGlyphPage();

                width += (currentPage.getWidth(character) - 8) / 2;
            }

            if (i > width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, character);
            } else {
                stringbuilder.append(character);
            }
        }

        return stringbuilder.toString();
    }
}