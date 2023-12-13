/*
 * Decompiled with CFR 0.152.
 */
package cc.hyperium.utils;

import cc.hyperium.utils.ChatColor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import tk.rektsky.Client;

public class HyperiumFontRenderer {
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("\u00a7[0123456789abcdefklmnor]");
    public final int FONT_HEIGHT = 9;
    private final int[] colorCodes = new int[]{0, 170, 43520, 43690, 0xAA0000, 0xAA00AA, 0xFFAA00, 0xAAAAAA, 0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF};
    private final Map<String, Float> cachedStringWidth = new HashMap<String, Float>();
    private float antiAliasingFactor;
    private UnicodeFont unicodeFont;
    private int prevScaleFactor = new ScaledResolution(Client.mc).getScaleFactor();
    private String name;
    private float size;

    public HyperiumFontRenderer(String fontName, float fontSize) {
        this.name = fontName;
        this.size = fontSize;
        ScaledResolution resolution = new ScaledResolution(Client.mc);
        try {
            this.prevScaleFactor = resolution.getScaleFactor();
            this.unicodeFont = new UnicodeFont(this.getFontByName(fontName).deriveFont(fontSize * (float)this.prevScaleFactor / 2.0f));
            this.unicodeFont.addAsciiGlyphs();
            this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
            this.unicodeFont.loadGlyphs();
        }
        catch (FontFormatException | IOException | SlickException e2) {
            e2.printStackTrace();
        }
        this.antiAliasingFactor = resolution.getScaleFactor();
    }

    public HyperiumFontRenderer(Font fontName, float fontSize) {
        this.name = fontName.getName();
        this.size = fontSize;
        ScaledResolution resolution = new ScaledResolution(Client.mc);
        try {
            this.prevScaleFactor = resolution.getScaleFactor();
            this.unicodeFont = new UnicodeFont(fontName.deriveFont(fontSize * (float)this.prevScaleFactor / 2.0f));
            this.unicodeFont.addAsciiGlyphs();
            this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
            this.unicodeFont.loadGlyphs();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
        this.antiAliasingFactor = resolution.getScaleFactor();
    }

    public HyperiumFontRenderer(Font font) {
        this(font.getFontName(), (float)font.getSize());
    }

    public HyperiumFontRenderer(String fontName, int fontType, int size) {
        this(new Font(fontName, fontType, size));
    }

    private Font getFontByName(String name) throws IOException, FontFormatException {
        if (name.equalsIgnoreCase("roboto condensed")) {
            return HyperiumFontRenderer.getFontFromInput("/assets/hyperium/fonts/RobotoCondensed-Regular.ttf");
        }
        if (name.equalsIgnoreCase("roboto")) {
            return HyperiumFontRenderer.getFontFromInput("/assets/hyperium/fonts/Roboto-Regular.ttf");
        }
        if (name.equalsIgnoreCase("roboto medium")) {
            return HyperiumFontRenderer.getFontFromInput("/assets/hyperium/fonts/Roboto-Medium.ttf");
        }
        if (name.equalsIgnoreCase("montserrat")) {
            return HyperiumFontRenderer.getFontFromInput("/assets/hyperium/fonts/Montserrat-Regular.ttf");
        }
        if (name.equalsIgnoreCase("segoeui") || name.equalsIgnoreCase("segoeui light")) {
            return HyperiumFontRenderer.getFontFromInput("/assets/hyperium/fonts/SegoeUI-Light.ttf");
        }
        if (name.equalsIgnoreCase("raleway")) {
            return HyperiumFontRenderer.getFontFromInput("/assets/hyperium/fonts/Raleway-SemiBold.ttf");
        }
        return HyperiumFontRenderer.getFontFromInput("/assets/hyperium/fonts/SegoeUI-Light.ttf");
    }

    public static Font getDefaultFont() {
        try {
            return HyperiumFontRenderer.getFontFromInput("/assets/minecraft/rektsky/Comfortaa-Regular.ttf");
        }
        catch (Exception e2) {
            return null;
        }
    }

    public static Font getCasper() {
        try {
            return HyperiumFontRenderer.getFontFromInput("/assets/minecraft/rektsky/fonts/Casper.ttf");
        }
        catch (Exception e2) {
            return null;
        }
    }

    public static Font getCasperBold() {
        try {
            return HyperiumFontRenderer.getFontFromInput("/assets/minecraft/rektsky/fonts/Casper_Bold.ttf");
        }
        catch (Exception e2) {
            return null;
        }
    }

    public static Font getSkidmaFont() {
        try {
            return HyperiumFontRenderer.getFontFromInput("/assets/minecraft/rektsky/fonts/HelveticaNeue-Light.ttf");
        }
        catch (Exception ex) {
            return null;
        }
    }

    private static Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont(0, HyperiumFontRenderer.class.getResourceAsStream(path));
    }

    public void drawStringScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0.0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        this.drawString(text, 0.0f, 0.0f, color);
        GL11.glPopMatrix();
    }

    public int drawString(String text, float x2, float y2, int color) {
        if (text == null) {
            return 0;
        }
        ScaledResolution resolution = new ScaledResolution(Client.mc);
        try {
            if (resolution.getScaleFactor() != this.prevScaleFactor) {
                this.prevScaleFactor = resolution.getScaleFactor();
                this.unicodeFont = new UnicodeFont(this.getFontByName(this.name).deriveFont(this.size * (float)this.prevScaleFactor / 2.0f));
                this.unicodeFont.addAsciiGlyphs();
                this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
                this.unicodeFont.loadGlyphs();
            }
        }
        catch (FontFormatException | IOException | SlickException e2) {
            e2.printStackTrace();
        }
        this.antiAliasingFactor = resolution.getScaleFactor();
        GL11.glPushMatrix();
        GlStateManager.scale(1.0f / this.antiAliasingFactor, 1.0f / this.antiAliasingFactor, 1.0f / this.antiAliasingFactor);
        y2 *= this.antiAliasingFactor;
        float originalX = x2 *= this.antiAliasingFactor;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
        int currentColor = color;
        char[] characters = text.toCharArray();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        String[] parts = COLOR_CODE_PATTERN.split(text);
        int index = 0;
        for (String s2 : parts) {
            char colorCode;
            for (String s22 : s2.split("\n")) {
                for (String s3 : s22.split("\r")) {
                    this.unicodeFont.drawString(x2, y2, s3, new Color(currentColor));
                    x2 += (float)this.unicodeFont.getWidth(s3);
                    if ((index += s3.length()) >= characters.length || characters[index] != '\r') continue;
                    x2 = originalX;
                    ++index;
                }
                if (index >= characters.length || characters[index] != '\n') continue;
                x2 = originalX;
                y2 += this.getHeight(s22) * 2.0f;
                ++index;
            }
            if (index >= characters.length || (colorCode = characters[index]) != '\u00a7') continue;
            char colorChar = characters[index + 1];
            int codeIndex = "0123456789abcdef".indexOf(colorChar);
            if (codeIndex < 0) {
                if (colorChar == 'r') {
                    currentColor = color;
                }
            } else {
                currentColor = this.colorCodes[codeIndex];
            }
            index += 2;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.bindTexture(0);
        GlStateManager.popMatrix();
        return (int)x2;
    }

    public int drawStringWithShadow(String text, float x2, float y2, int color) {
        this.drawString(StringUtils.stripControlCodes(text), x2 + 0.5f, y2 + 0.5f, 0);
        return this.drawString(text, x2, y2, color);
    }

    public void drawCenteredString(String text, float x2, float y2, int color) {
        this.drawString(text, x2 - (float)((int)this.getWidth(text) >> 1), y2, color);
    }

    public void drawCenteredTextScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0.0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        this.drawCenteredString(text, 0.0f, 0.0f, color);
        GL11.glPopMatrix();
    }

    public void drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
        this.drawCenteredString(StringUtils.stripControlCodes(text), x2 + 0.5f, y2 + 0.5f, color);
        this.drawCenteredString(text, x2, y2, color);
    }

    public float getWidth(String text) {
        if (this.cachedStringWidth.size() > 1000) {
            this.cachedStringWidth.clear();
        }
        return this.cachedStringWidth.computeIfAbsent(text, e2 -> Float.valueOf((float)this.unicodeFont.getWidth(ChatColor.stripColor(text)) / this.antiAliasingFactor)).floatValue();
    }

    public float getCharWidth(char c2) {
        return this.unicodeFont.getWidth(String.valueOf(c2));
    }

    public float getHeight(String s2) {
        return (float)this.unicodeFont.getHeight(s2) / 2.0f;
    }

    public UnicodeFont getFont() {
        return this.unicodeFont;
    }

    public void drawSplitString(ArrayList<String> lines, int x2, int y2, int color) {
        this.drawString(String.join((CharSequence)"\n\r", lines), x2, y2, color);
    }

    public List<String> splitString(String text, int wrapWidth) {
        ArrayList<String> lines = new ArrayList<String>();
        String[] splitText = text.split(" ");
        StringBuilder currentString = new StringBuilder();
        for (String word : splitText) {
            String potential = currentString + " " + word;
            if (this.getWidth(potential) >= (float)wrapWidth) {
                lines.add(currentString.toString());
                currentString = new StringBuilder();
            }
            currentString.append(word).append(" ");
        }
        lines.add(currentString.toString());
        return lines;
    }
}

