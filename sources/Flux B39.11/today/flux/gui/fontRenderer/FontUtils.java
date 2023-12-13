package today.flux.gui.fontRenderer;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.utility.ColorUtils;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class FontUtils {
    public float FONT_HEIGHT = 0;
    private final FluxFont unicodeFont;
    private final int[] colorCodes = new int[32];

    private float kerning;

    public HashMap<String, Float> widthMap = new HashMap<String, Float>();
    public HashMap<String, Float> heightMap = new HashMap<String, Float>();

    public FontUtils(String fontName, int fontType, int size, boolean allChar) {
        this(fontName, fontType, size, 0, allChar);
    }

    public FontUtils(String fontName, int fontType, int size, int kerning, boolean allChar) {
    	this(fontName, fontType, size, kerning, allChar, 0);
    }
    
    public FontUtils(String fontName, int fontType, int size, int kerning, boolean allChar, int yAddon) {
        this.unicodeFont = new FluxFont(getFont(fontName, fontType, size), true, kerning, allChar, yAddon);
        this.kerning = 0;

        for (int i = 0; i < 32; i++) {
            int shadow = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + shadow;
            int green = (i >> 1 & 1) * 170 + shadow;
            int blue = (i & 1) * 170 + shadow;

            if (i == 6) {
                red += 85;
            }

            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
        this.FONT_HEIGHT = this.getHeight("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    private Font getFont(String fontName, int fontType, int size) {
        Font font = null;

        try {
            InputStream ex = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("flux/font/" + fontName)).getInputStream();
            font = Font.createFont(0, ex);
            font = font.deriveFont(fontType, size);
        } catch (Exception var3) {
            var3.printStackTrace();
            System.err.println("Failed to load custom font");
        }

        return font;
    }

    public int drawString(String text, float x, float y, int color) {
    	if (color == 16777215) color = ColorUtils.WHITE.c;
        return drawStringWithAlpha(text, x, y, color, (((color >> 24) & 0xFF) / 255f));
    }

    public void drawLimitedString(String text, float x, float y, int color, float maxWidth) {
        drawLimitedStringWithAlpha(text, x, y, color, (((color >> 24) & 0xFF) / 255f), maxWidth);
    }

    public void drawLimitedStringWithAlpha(String text, float x, float y, int color, float alpha, float maxWidth) {
        text = processString(text);
        x *= 2.0F;
        y *= 2.0F;
        float originalX = x;
        float curWidth = 0;

        GL11.glPushMatrix();
        GL11.glScaled(0.5F, 0.5F, 0.5F);

        final boolean wasBlend = glGetBoolean(GL_BLEND);
        GlStateManager.enableAlpha();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);

        int currentColor = color;
        char[] characters = text.toCharArray();

        int index = 0;
        for (char c : characters) {
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += getHeight(Character.toString(c)) * 2.0F;
            }
            if (c != '\247' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '\247')) {
                if (index >= 1 && characters[index - 1] == '\247') continue;
                glPushMatrix();
                unicodeFont.drawString(Character.toString(c), x, y, RenderUtil.reAlpha(currentColor, alpha), false);
                glPopMatrix();

                curWidth += (getStringWidth(Character.toString(c)) * 2.0F);
                x += (getStringWidth(Character.toString(c)) * 2.0F);

                if(curWidth > maxWidth) {
                    break;
                }

            } else if (c == ' ') {
                x += unicodeFont.getWidth(" ");
            } else if (c == '\247' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                if (codeIndex < 0) continue;

                if (codeIndex < 16) {
                    currentColor = this.colorCodes[codeIndex];
                } else if (codeIndex == 21) {
                    currentColor = Color.WHITE.getRGB();
                }
            }

            index++;
        }

        if (!wasBlend)
            glDisable(GL_BLEND);
        glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }

    public int drawStringWithAlpha(String text, float x, float y, final int color, final float alpha) {
        text = processString(text);
        x *= 2.0F;
        y *= 2.0F;
        float originalX = x;

        GL11.glPushMatrix();
        GL11.glScaled(0.5F, 0.5F, 0.5F);

        final boolean wasBlend = glGetBoolean(GL_BLEND);
        GlStateManager.enableAlpha();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);

        int currentColor = color;
        char[] characters = text.toCharArray();

        int index = 0;
        for (char c : characters) {
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += getHeight(Character.toString(c)) * 2.0F;
            }
            if (c != '\247' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '\247')) {
                if (index >= 1 && characters[index - 1] == '\247') continue;
                glPushMatrix();
                unicodeFont.drawString(Character.toString(c), x, y, RenderUtil.reAlpha(currentColor, alpha), false);
                glPopMatrix();
                x += (getStringWidth(Character.toString(c)) * 2.0F);
            } else if (c == ' ') {
                x += unicodeFont.getWidth(" ");
            } else if (c == '\247' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                if (codeIndex < 0) continue;

                if (codeIndex < 16) {
                    currentColor = this.colorCodes[codeIndex];
                } else if (codeIndex == 21) {
                    currentColor = Color.WHITE.getRGB();
                }
            }

            index++;
        }

        if (!wasBlend)
            glDisable(GL_BLEND);
        glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);

        return (int) x;
    }

    private String processString(String text) {
        String str = "";
        for (char c : text.toCharArray()) {
            if ((c < 50000 || c > 60000) && c != 9917) str += c;
        }
        text = str.replace("\247r", "").replace('▬', '=').replace('❤', '♥').replace('⋆', '☆').replace('☠', '☆').replace('✰', '☆').replace("✫", "☆").replace("✙", "+");
        text = text.replace('⬅', '←').replace('⬆', '↑').replace('⬇', '↓').replace('➡', '→').replace('⬈', '↗').replace('⬋', '↙').replace('⬉', '↖').replace('⬊', '↘');
        return text;
    }

    public void drawStringWithGudShadow(String text, float x, float y, int color) {
        drawString(StringUtils.stripControlCodes(text), x + 1F, y + 1F, this.getShadowColor(color).getRGB());
        drawString(text, x, y, color);
    }

    public void drawStringWithShadowForChat(String text, float x, float y, int color) {
        drawString(StringUtils.stripControlCodes(text), x + 1f, y + 1f, this.getShadowColor(color).getRGB());
        drawString(text, x, y, color);
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        drawStringWithAlpha(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.4F, 0x000000, ((color >> 24) & 0xFF) / 255f);
        return drawString(text, x, y, color);
    }

    public void drawStringWithSuperShadow(String text, float x, float y, int color) {
        drawStringWithAlpha(StringUtils.stripControlCodes(text), x + 0.8F, y + 0.8F, 0x000000, ((color >> 24) & 0xFF) / 255f);
        drawString(text, x, y, color);
    }

    public int drawCenteredString(String text, float x, float y, int color) {
        return drawString(text, x - getStringWidth(text) / 2, y, color);
    }

    public void drawCenteredStringWithAlpha(String text, float x, float y, int color, float alpha) {
        drawStringWithAlpha(text, x - getStringWidth(text) / 2, y, color, alpha);
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        drawCenteredString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0xFF000000);
        drawCenteredString(text, x, y, color);
    }

    private Color getShadowColor(int hex) {
        float a = (float) (hex >> 24 & 255) / 255.0f;
        float r = (float) (hex >> 16 & 255) / 255.0f;
        float g = (float) (hex >> 8 & 255) / 255.0f;
        float b = (float) (hex & 255) / 255.0f;
        return new Color(r * 0.2f, g * 0.2f, b * 0.2f, a * 0.9f);
    }

    public void drawOutlinedString(String str, float x, float y, int internalCol, int externalCol) {
        this.drawString(str, x - 0.5f, y, externalCol);
        this.drawString(str, x + 0.5f, y, externalCol);
        this.drawString(str, x, y - 0.5f, externalCol);
        this.drawString(str, x, y + 0.5f, externalCol);
        this.drawString(str, x, y, internalCol);
    }

    public float getStringWidth(String s) {
        if (this.widthMap.containsKey(s)) {
            return this.widthMap.get(s);
        } else {
            float width = 0.0F;
            String str = StringUtils.stripControlCodes(s);
            for (char c : str.toCharArray()) {
                width += unicodeFont.getWidth(Character.toString(c)) + this.kerning;
            }
            this.widthMap.put(s, width / 2f);
            return width / 2.0F;
        }
    }

    public float getCharWidth(char c) {
        return unicodeFont.getWidth(String.valueOf(c));
    }

    public float getHeight(String s) {
        if (this.heightMap.containsKey(s)) {
            return this.heightMap.get(s).floatValue();
        } else {
            float height = unicodeFont.getHeight(s) / 2.0F;
            this.heightMap.put(s, height);
            return height;
        }
    }

    public float getHeight() {
        return unicodeFont.getHeight("FluxClientIsThatBestClarinet.") / 2.0F;
    }

    public FluxFont getFont() {
        return this.unicodeFont;
    }

    /**
     * Trims a string to fit a specified Width.
     */
    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    /**
     * Trims a string to a specified width, and will reverse it if par3 is set.
     */
    public String trimStringToWidth(String text, int width, boolean reverse) {
        text = processString(text);
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0F;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int k = i; k >= 0 && k < text.length() && f < (float) width; k += j) {
            char c0 = text.charAt(k);
            float f1 = this.getCharWidth(c0);

            if (flag) {
                flag = false;

                if (c0 != 108 && c0 != 76) {
                    if (c0 == 114 || c0 == 82) {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0F) {
                flag = true;
            } else {
                f += f1 / ((text.contains("=====") ? 2.2 : 2));

                if (flag1) {
                    ++f;
                }
            }

            if (f > width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }

    public String trimStringToWidth(String text, float width, boolean reverse) {
        text = processString(text);
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0F;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int k = i; k >= 0 && k < text.length() && f < width; k += j) {
            char c0 = text.charAt(k);
            float f1 = this.getCharWidth(c0);

            if (flag) {
                flag = false;

                if (c0 != 108 && c0 != 76) {
                    if (c0 == 114 || c0 == 82) {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0F) {
                flag = true;
            } else {
                f += f1 / ((text.contains("=====") ? 2.2 : 2));

                if (flag1) {
                    ++f;
                }
            }

            if (f > width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }
}