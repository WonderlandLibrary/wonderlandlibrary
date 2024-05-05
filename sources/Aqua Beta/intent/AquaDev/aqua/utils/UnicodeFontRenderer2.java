package intent.AquaDev.aqua.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class UnicodeFontRenderer2 {
    public static UnicodeFontRenderer2 instance;
    public final int FONT_HEIGHT = 9;
    private final int[] colorCodes = new int[32];
    private final float kerning;
    private final Map<String, Float> cachedStringWidth = new HashMap();
    private final float antiAliasingFactor;
    private UnicodeFont unicodeFont;

    private UnicodeFontRenderer2(String fontName, int fontType, float fontSize, float kerning, float antiAliasingFactor) {
        this.antiAliasingFactor = antiAliasingFactor;
        try {
            this.unicodeFont = new UnicodeFont(this.getFontByName(fontName).deriveFont(fontSize * this.antiAliasingFactor));
        }
        catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        this.kerning = kerning;
        this.unicodeFont.addAsciiGlyphs();
        this.unicodeFont.getEffects().add((Object)new ColorEffect(java.awt.Color.WHITE));
        try {
            this.unicodeFont.loadGlyphs();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
    }

    private UnicodeFontRenderer2(Font font, float kerning, float antiAliasingFactor) {
        this.antiAliasingFactor = antiAliasingFactor;
        this.unicodeFont = new UnicodeFont(new Font(font.getName(), font.getStyle(), (int)((float)font.getSize() * antiAliasingFactor)));
        this.kerning = kerning;
        this.unicodeFont.addAsciiGlyphs();
        this.unicodeFont.getEffects().add((Object)new ColorEffect(java.awt.Color.WHITE));
        try {
            this.unicodeFont.loadGlyphs();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
    }

    public static UnicodeFontRenderer2 getFontOnPC(String name, int size) {
        return UnicodeFontRenderer2.getFontOnPC(name, size, 0);
    }

    public static UnicodeFontRenderer2 getFontOnPC(String name, int size, int fontType) {
        return UnicodeFontRenderer2.getFontOnPC(name, size, fontType, 0.0f);
    }

    public static UnicodeFontRenderer2 getFontOnPC(String name, int size, int fontType, float kerning) {
        return UnicodeFontRenderer2.getFontOnPC(name, size, fontType, kerning, 3.0f);
    }

    public static UnicodeFontRenderer2 getFontOnPC(String name, int size, int fontType, float kerning, float antiAliasingFactor) {
        return new UnicodeFontRenderer2(new Font(name, fontType, size), kerning, antiAliasingFactor);
    }

    public static UnicodeFontRenderer2 getFontFromAssets(String name, int size) {
        return UnicodeFontRenderer2.getFontOnPC(name, size, 0);
    }

    public static UnicodeFontRenderer2 getFontFromAssets(String name, int size, int fontType) {
        return UnicodeFontRenderer2.getFontOnPC(name, fontType, size, 0.0f);
    }

    public static UnicodeFontRenderer2 getFontFromAssets(String name, int size, float kerning, int fontType) {
        return UnicodeFontRenderer2.getFontFromAssets(name, size, fontType, kerning, 3.0f);
    }

    public static UnicodeFontRenderer2 getFontFromAssets(String name, int size, int fontType, float kerning, float antiAliasingFactor) {
        return new UnicodeFontRenderer2(name, fontType, size, kerning, antiAliasingFactor);
    }

    private Font getFontByName(String name) throws IOException, FontFormatException {
        return this.getFontFromInput("/assets/minecraft/Aqua/fonts/" + name + ".ttf");
    }

    private Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont((int)0, (InputStream)((InputStream)Objects.requireNonNull((Object)UnicodeFontRenderer2.class.getResourceAsStream(path))));
    }

    public void drawStringScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)givenX, (double)givenY, (double)0.0);
        GL11.glScaled((double)givenScale, (double)givenScale, (double)givenScale);
        this.drawString(text, 0.0f, 0.0f, color);
        GL11.glPopMatrix();
    }

    public void drawStringScaledShadow(String text, int givenX, int givenY, int color, double givenScale, float x, float y) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)givenX, (double)givenY, (double)0.0);
        GL11.glScaled((double)givenScale, (double)givenScale, (double)givenScale);
        this.drawString(StringUtils.stripControlCodes((String)text), x + 0.5f, y + 0.5f, 0);
        GL11.glPopMatrix();
    }

    public int drawString(String text, float x, float y, int color) {
        if (text == null) {
            return 0;
        }
        y *= 2.0f;
        float originalX = x *= 2.0f;
        GL11.glPushMatrix();
        GlStateManager.scale((float)(1.0f / this.antiAliasingFactor), (float)(1.0f / this.antiAliasingFactor), (float)(1.0f / this.antiAliasingFactor));
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= this.antiAliasingFactor;
        y *= this.antiAliasingFactor;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        GlStateManager.color((float)red, (float)green, (float)blue, (float)alpha);
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean lighting = GL11.glIsEnabled((int)2896);
        boolean texture = GL11.glIsEnabled((int)3553);
        boolean alphaTest = GL11.glIsEnabled((int)3008);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (lighting) {
            GL11.glDisable((int)2896);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        if (alphaTest) {
            GL11.glEnable((int)3008);
        }
        int currentColor = color;
        char[] characters = text.toCharArray();
        int index = 0;
        for (char c : characters) {
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += this.getHeight(Character.toString((char)c)) * 2.0f;
            }
            if (c != '\u00a7' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '\u00a7')) {
                this.unicodeFont.drawString(x, y, Character.toString((char)c), new Color(currentColor));
                x += this.getWidth(Character.toString((char)c)) * 2.0f * this.antiAliasingFactor;
            } else if (c == ' ') {
                x += (float)this.unicodeFont.getSpaceWidth();
            } else if (c == '\u00a7' && index != characters.length - 1) {
                int codeIndex = "0123456789abcdefg".indexOf((int)text.charAt(index + 1));
                if (codeIndex < 0) continue;
                currentColor = this.colorCodes[codeIndex];
            }
            ++index;
        }
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (lighting) {
            GL11.glEnable((int)2896);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        if (alphaTest) {
            GL11.glEnable((int)3008);
        }
        GlStateManager.bindTexture((int)0);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
        return (int)x / 2;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(StringUtils.stripControlCodes((String)text), x + 0.5f, y + 0.5f, 0);
        return this.drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        this.drawString(text, x - (float)((int)this.getWidth(text) / 2), y, color);
    }

    public void drawCenteredTextScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)givenX, (double)givenY, (double)0.0);
        GL11.glScaled((double)givenScale, (double)givenScale, (double)givenScale);
        this.drawCenteredString(text, 0.0f, 0.0f, color);
        GL11.glPopMatrix();
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        this.drawCenteredString(StringUtils.stripControlCodes((String)text), x + 0.5f, y + 0.5f, color);
        this.drawCenteredString(text, x, y, color);
    }

    public float getWidth(String s) {
        if (this.cachedStringWidth.size() > 1000) {
            this.cachedStringWidth.clear();
        }
        return ((Float)this.cachedStringWidth.computeIfAbsent((Object)s, e -> {
            float width = 0.0f;
            String str = StringUtils.stripControlCodes((String)s);
            for (char c : str.toCharArray()) {
                width += (float)this.unicodeFont.getWidth(Character.toString((char)c)) + this.kerning;
            }
            return Float.valueOf((float)(width / 2.0f / this.antiAliasingFactor));
        })).floatValue();
    }

    public int getStringWidth(String text) {
        if (EnumChatFormatting.getTextWithoutFormattingCodes((String)text) == null) {
            return 0;
        }
        int i = 0;
        boolean flag = false;
        for (int j = 0; j < EnumChatFormatting.getTextWithoutFormattingCodes((String)text).length(); ++j) {
            char c0 = EnumChatFormatting.getTextWithoutFormattingCodes((String)text).charAt(j);
            float k = this.getWidth(EnumChatFormatting.getTextWithoutFormattingCodes((String)String.valueOf((char)c0)));
            if (k < 0.0f && j < EnumChatFormatting.getTextWithoutFormattingCodes((String)text).length() - 1) {
                c0 = EnumChatFormatting.getTextWithoutFormattingCodes((String)text).charAt(++j);
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag = false;
                    }
                } else {
                    flag = true;
                }
                k = 0.0f;
            }
            i = (int)((float)i + k);
            if (!flag || !(k > 0.0f)) continue;
            ++i;
        }
        return i;
    }

    public float getCharWidth(char c) {
        return this.unicodeFont.getWidth(String.valueOf((char)c));
    }

    public float getHeight(String s) {
        return (float)this.unicodeFont.getHeight(s) / 2.0f;
    }

    public UnicodeFont getFont() {
        return this.unicodeFont;
    }

    public String trimStringToWidth(String par1Str, int par2) {
        StringBuilder builder = new StringBuilder();
        float var5 = 0.0f;
        int var6 = 0;
        int var7 = 1;
        boolean var8 = false;
        boolean var9 = false;
        for (int var10 = var6; var10 >= 0 && var10 < par1Str.length() && var5 < (float)par2; var10 += var7) {
            char var11 = par1Str.charAt(var10);
            float var12 = this.getCharWidth(var11);
            if (var8) {
                var8 = false;
                if (var11 != 'l' && var11 != 'L') {
                    if (var11 == 'r' || var11 == 'R') {
                        var9 = false;
                    }
                } else {
                    var9 = true;
                }
            } else if (var12 < 0.0f) {
                var8 = true;
            } else {
                var5 += var12;
                if (var9) {
                    var5 += 1.0f;
                }
            }
            if (var5 > (float)par2) break;
            builder.append(var11);
        }
        return builder.toString();
    }

    public void drawSplitString(ArrayList<String> lines, int x, int y, int color) {
        this.drawString(String.join((CharSequence)"\n\r", lines), x, y, color);
    }

    public List<String> splitString(String text, int wrapWidth) {
        ArrayList lines = new ArrayList();
        String[] splitText = text.split(" ");
        StringBuilder currentString = new StringBuilder();
        for (String word : splitText) {
            String potential = currentString + " " + word;
            if (this.getWidth(potential) >= (float)wrapWidth) {
                lines.add((Object)currentString.toString());
                currentString = new StringBuilder();
            }
            currentString.append(word).append(" ");
        }
        lines.add((Object)currentString.toString());
        return lines;
    }

    public static UnicodeFontRenderer2 getInstance() {
        return instance;
    }
}
