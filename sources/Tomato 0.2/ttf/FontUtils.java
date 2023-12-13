package cow.milkgod.cheese.ttf;

import net.minecraft.client.*;
import org.newdawn.slick.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.font.effects.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.*;

public class FontUtils
{
    private Minecraft mc;
    private final UnicodeFont unicodeFont;
    private final int[] colorCodes;
    
    public FontUtils(final String fontName, final int fontType, final int size) {
        this.mc = Minecraft.getMinecraft();
        this.colorCodes = new int[32];
        this.unicodeFont = new UnicodeFont(new Font(fontName, fontType, size));
        try {
            this.unicodeFont.addAsciiGlyphs();
            this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
            this.unicodeFont.loadGlyphs();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 32; ++i) {
            final int shadow = (i >> 3 & 0x1) * 85;
            int red = (i >> 2 & 0x1) * 170 + shadow;
            int green = (i >> 1 & 0x1) * 170 + shadow;
            int blue = (i >> 0 & 0x1) * 170 + shadow;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCodes[i] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }
    
    public void drawString(final String text, float x, float y, final int color) {
        x *= 2.0f;
        y *= 2.0f;
        final float originalX = x;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        int currentColor = color;
        final char[] characters = text.toCharArray();
        int index = 0;
        char[] array;
        for (int length = (array = characters).length, i = 0; i < length; ++i) {
            final char c = array[i];
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += this.getHeight(Character.toString(c)) * 2.0f;
            }
            if (c != '§' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '§')) {
                this.unicodeFont.drawString(x, y, Character.toString(c), new org.newdawn.slick.Color(currentColor));
                x += this.getWidth(Character.toString(c)) * 2.0f;
            }
            else if (c == ' ') {
                x += this.unicodeFont.getSpaceWidth();
            }
            else if (c == '§' && index != characters.length - 1) {
                final int codeIndex = "0123456789abcdef".indexOf(text.charAt(index + 1));
                if (codeIndex < 0) {
                    continue;
                }
                final int col = currentColor = this.colorCodes[codeIndex];
            }
            ++index;
        }
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public void drawStringWithShadow(final String text, final float x, final float y, final int color) {
        this.drawString(StringUtils.stripControlCodes(text), x + 0.5f, y + 0.5f, 0);
        this.drawString(text, x, y, color);
    }
    
    public void drawCenteredString(final String text, final float x, final float y, final int color) {
        this.drawString(text, x / 2.0f - this.getWidth(text) / 2.0f, y, color);
    }
    
    public void drawCenterdStringWidthShadow(final String text, final float x, final float y, final int color) {
        this.drawString(StringUtils.stripControlCodes(text), x + 0.5f, y + 0.5f, color);
        this.drawString(text, x, y, color);
    }
    
    public float getWidth(final String s) {
        float width = 0.0f;
        final String s2 = StringUtils.stripControlCodes(s);
        char[] charArray;
        for (int length = (charArray = s2.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            width += this.unicodeFont.getWidth(Character.toString(c));
        }
        return width / 2.0f;
    }
    
    public int getWidthInt(final String s) {
        int width = 0;
        final String s2 = StringUtils.stripControlCodes(s);
        char[] charArray;
        for (int length = (charArray = s2.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            width += this.unicodeFont.getWidth(Character.toString(c));
        }
        return width / 2;
    }
    
    public float getHeight(final String s) {
        return this.unicodeFont.getHeight(s) / 2.0f;
    }
    
    public UnicodeFont getFont() {
        return this.unicodeFont;
    }
}
