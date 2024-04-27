package me.utils.fluxfont;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import me.utils.fluxfont.FluxFont;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

public class FontUtils {
    public float FONT_HEIGHT = 0.0f;
    private final FluxFont unicodeFont;
    private final int[] colorCodes = new int[32];
    private float kerning;
    public HashMap<String, Float> widthMap = new HashMap();
    public HashMap<String, Float> heightMap = new HashMap();

    public FontUtils(String fontName, int fontType, int size, boolean allChar) {
        this(fontName, fontType, size, 0, allChar);
    }

    public FontUtils(String fontName, int fontType, int size, int kerning, boolean allChar) {
        this(fontName, fontType, size, kerning, allChar, 0);
    }

    public FontUtils(String fontName, int fontType, int size, int kerning, boolean allChar, int yAddon) {
        this.unicodeFont = new FluxFont(this.getFont(fontName, fontType, size), true, kerning, allChar, yAddon);
        this.kerning = 0.0f;
        for (int i = 0; i < 32; ++i) {
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
            this.colorCodes[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
        this.FONT_HEIGHT = this.getHeight("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    private Font getFont(String fontName, int fontType, int size) {
        Font font = null;
        try {
            InputStream ex = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("flux/font/" + fontName)).getInputStream();
            font = Font.createFont(0, ex);
            font = font.deriveFont(fontType, size);
        }
        catch (Exception var3) {
            var3.printStackTrace();
            System.err.println("Failed to load custom font");
        }
        return font;
    }

    public int drawString(String text, float x, float y, int color) {
        if (color == 0xFFFFFF) {
            color = Color.WHITE.getRGB();
        }
        return this.drawStringWithAlpha(text, x, y, color, (float)(color >> 24 & 0xFF) / 255.0f);
    }

    public void drawLimitedString(String text, float x, float y, int color, float maxWidth) {
        this.drawLimitedStringWithAlpha(text, x, y, color, (float)(color >> 24 & 0xFF) / 255.0f, maxWidth);
    }

    public void drawLimitedStringWithAlpha(String text, float x, float y, int color, float alpha, float maxWidth) {
        text = this.processString(text);
        y *= 2.0f;
        float originalX = x *= 2.0f;
        float curWidth = 0.0f;
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean wasBlend = GL11.glGetBoolean((int)3042);
        GlStateManager.enableAlpha();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3553);
        int currentColor = color;
        char[] characters = text.toCharArray();
        int index = 0;
        for (char c : characters) {
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += this.getHeight(Character.toString(c)) * 2.0f;
            }
            if (c != '§' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '§')) {
                if (index >= 1 && characters[index - 1] == '§') continue;
                GL11.glPushMatrix();
                this.unicodeFont.drawString(Character.toString(c), (double)x, (double)y, RenderUtils.reAlpha(currentColor, alpha), false);
                GL11.glPopMatrix();
                curWidth += this.getStringWidth(Character.toString(c)) * 2.0f;
                x += this.getStringWidth(Character.toString(c)) * 2.0f;
                if (curWidth > maxWidth) {
                    break;
                }
            } else if (c == ' ') {
                x += (float)this.unicodeFont.getWidth(" ");
            } else if (c == '§' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                if (codeIndex < 0) continue;
                if (codeIndex < 16) {
                    currentColor = this.colorCodes[codeIndex];
                } else if (codeIndex == 21) {
                    currentColor = Color.WHITE.getRGB();
                }
            }
            ++index;
        }
        if (!wasBlend) {
            GL11.glDisable((int)3042);
        }
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public int drawStringWithAlpha(String text, float x, float y, int color, float alpha) {
        text = this.processString(text);
        y *= 2.0f;
        float originalX = x *= 2.0f;
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean wasBlend = GL11.glGetBoolean((int)3042);
        GlStateManager.enableAlpha();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3553);
        int currentColor = color;
        char[] characters = text.toCharArray();
        int index = 0;
        for (char c : characters) {
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += this.getHeight(Character.toString(c)) * 2.0f;
            }
            if (c != '§' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '§')) {
                if (index >= 1 && characters[index - 1] == '§') continue;
                GL11.glPushMatrix();
                this.unicodeFont.drawString(Character.toString(c), (double)x, (double)y, RenderUtils.reAlpha(currentColor, alpha), false);
                GL11.glPopMatrix();
                x += this.getStringWidth(Character.toString(c)) * 2.0f;
            } else if (c == ' ') {
                x += (float)this.unicodeFont.getWidth(" ");
            } else if (c == '§' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                if (codeIndex < 0) continue;
                if (codeIndex < 16) {
                    currentColor = this.colorCodes[codeIndex];
                } else if (codeIndex == 21) {
                    currentColor = Color.WHITE.getRGB();
                }
            }
            ++index;
        }
        if (!wasBlend) {
            GL11.glDisable((int)3042);
        }
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return (int)x;
    }

    private String processString(String text) {
        String str = "";
        for (char c : text.toCharArray()) {
            if (c >= '썐' && c <= '' || c == '⚽') continue;
            str = str + c;
        }
        text = str.replace("§r", "").replace('▬', '=').replace('❤', '♥').replace('⋆', '☆').replace('☠', '☆').replace('✰', '☆').replace("✫", "☆").replace("✙", "+");
        text = text.replace('⬅', '←').replace('⬆', '↑').replace('⬇', '↓').replace('➡', '→').replace('⬈', '↗').replace('⬋', '↙').replace('⬉', '↖').replace('⬊', '↘');
        return text;
    }

    public void drawStringWithGudShadow(String text, float x, float y, int color) {
        this.drawString(StringUtils.stripControlCodes((String)text), x + 1.0f, y + 1.0f, this.getShadowColor(color).getRGB());
        this.drawString(text, x, y, color);
    }

    public void drawStringWithShadowForChat(String text, float x, float y, int color) {
        this.drawString(StringUtils.stripControlCodes((String)text), x + 1.0f, y + 1.0f, this.getShadowColor(color).getRGB());
        this.drawString(text, x, y, color);
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        this.drawStringWithAlpha(StringUtils.stripControlCodes((String)text), x + 0.5f, y + 0.4f, 0, (float)(color >> 24 & 0xFF) / 255.0f);
        return this.drawString(text, x, y, color);
    }

    public void drawStringWithSuperShadow(String text, float x, float y, int color) {
        this.drawStringWithAlpha(StringUtils.stripControlCodes((String)text), x + 0.8f, y + 0.8f, 0, (float)(color >> 24 & 0xFF) / 255.0f);
        this.drawString(text, x, y, color);
    }

    public int drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - this.getStringWidth(text) / 2.0f, y, color);
    }

    public void drawCenteredStringWithAlpha(String text, float x, float y, int color, float alpha) {
        this.drawStringWithAlpha(text, x - this.getStringWidth(text) / 2.0f, y, color, alpha);
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        this.drawCenteredString(StringUtils.stripControlCodes((String)text), x + 0.5f, y + 0.5f, -16777216);
        this.drawCenteredString(text, x, y, color);
    }

    private Color getShadowColor(int hex) {
        float a = (float)(hex >> 24 & 0xFF) / 255.0f;
        float r = (float)(hex >> 16 & 0xFF) / 255.0f;
        float g = (float)(hex >> 8 & 0xFF) / 255.0f;
        float b = (float)(hex & 0xFF) / 255.0f;
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
            return this.widthMap.get(s).floatValue();
        }
        float width = 0.0f;
        String str = StringUtils.stripControlCodes((String)s);
        for (char c : str.toCharArray()) {
            width += (float)this.unicodeFont.getWidth(Character.toString(c)) + this.kerning;
        }
        this.widthMap.put(s, Float.valueOf(width / 2.0f));
        return width / 2.0f;
    }

    public float getCharWidth(char c) {
        return this.unicodeFont.getWidth(String.valueOf(c));
    }

    public float getHeight(String s) {
        if (this.heightMap.containsKey(s)) {
            return this.heightMap.get(s).floatValue();
        }
        float height = (float)this.unicodeFont.getHeight(s) / 2.0f;
        this.heightMap.put(s, Float.valueOf(height));
        return height;
    }

    public float getHeight() {
        return (float)this.unicodeFont.getHeight("FluxClientIsThatBestClarinet.") / 2.0f;
    }

    public FluxFont getFont() {
        return this.unicodeFont;
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        text = this.processString(text);
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0f;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int k = i; k >= 0 && k < text.length() && f < (float)width; k += j) {
            char c0 = text.charAt(k);
            float f1 = this.getCharWidth(c0);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0f) {
                flag = true;
            } else {
                f = (float)((double)f + (double)f1 / (text.contains("=====") ? 2.2 : 2.0));
                if (flag1) {
                    f += 1.0f;
                }
            }
            if (f > (float)width) break;
            if (reverse) {
                stringbuilder.insert(0, c0);
                continue;
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.toString();
    }

    public String trimStringToWidth(String text, float width, boolean reverse) {
        text = this.processString(text);
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0f;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int k = i; k >= 0 && k < text.length() && f < width; k += j) {
            char c0 = text.charAt(k);
            float f1 = this.getCharWidth(c0);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0f) {
                flag = true;
            } else {
                f = (float)((double)f + (double)f1 / (text.contains("=====") ? 2.2 : 2.0));
                if (flag1) {
                    f += 1.0f;
                }
            }
            if (f > width) break;
            if (reverse) {
                stringbuilder.insert(0, c0);
                continue;
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.toString();
    }
}
