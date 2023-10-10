/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.manager;

import com.wallhacks.losebypass.gui.tabs.HudTab;
import com.wallhacks.losebypass.systems.clientsetting.clientsettings.ClickGuiConfig;
import com.wallhacks.losebypass.utils.MC;
import com.wallhacks.losebypass.utils.font.GameFontRenderer;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.Locale;
import net.minecraft.client.Minecraft;

public class FontManager
implements MC {
    private final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH);
    public String fontName = "Tahoma";
    public int fontSize = 17;
    private GameFontRenderer font = new GameFontRenderer(new Font(this.fontName, 0, this.fontSize), true, false);
    private GameFontRenderer hudFont;
    private GameFontRenderer largeFont;
    private final GameFontRenderer thickFont;
    private final GameFontRenderer badaboom;

    public FontManager() {
        this.hudFont = new GameFontRenderer(FontManager.getClientFont("Thick.ttf", ((Integer)HudTab.hudSettings.fontSize.getValue()).intValue()), true, false);
        this.largeFont = new GameFontRenderer(new Font(this.fontName, 0, 27), true, false);
        this.thickFont = new GameFontRenderer(FontManager.getClientFont("Thick.ttf", 20.0f), true, false);
        this.badaboom = new GameFontRenderer(FontManager.getClientFont("badaboom.ttf", 27.0f), true, false);
    }

    public String[] getFonts() {
        return this.fonts;
    }

    public void setHudFont(int size) {
        this.hudFont = new GameFontRenderer(FontManager.getClientFont("Thick.ttf", size), true, false);
    }

    public void setFont() {
        this.font = new GameFontRenderer(new Font(this.fontName, 0, this.fontSize), true, false);
        this.largeFont = new GameFontRenderer(new Font(this.fontName, 0, 23), true, false);
    }

    public GameFontRenderer getHudFont() {
        return this.hudFont;
    }

    public GameFontRenderer getFont() {
        return this.font;
    }

    public GameFontRenderer getBadaboom() {
        return this.badaboom;
    }

    public GameFontRenderer getLargeFont() {
        return this.largeFont;
    }

    public GameFontRenderer getThickFont() {
        return this.thickFont;
    }

    public void reset() {
        this.setFont("Tahoma");
        this.setFontSize(17);
        this.setFont();
    }

    public boolean setFont(String fontName) {
        String[] stringArray = this.fonts;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String font = stringArray[n2];
            if (fontName.equalsIgnoreCase(font)) {
                this.fontName = font;
                this.setFont();
                return true;
            }
            ++n2;
        }
        return false;
    }

    public int drawString(String text, int x, int y, int color) {
        if (FontManager.getCustomFontEnabled()) {
            this.font.drawString(text, x, y, color);
            return x + this.getTextWidth(text);
        }
        Minecraft.getMinecraft().standardGalacticFontRenderer.drawString(text, x, y, color, FontManager.getFontShadow());
        return x + this.getTextWidth(text);
    }

    public int getTextWidth(String text) {
        if (!FontManager.getCustomFontEnabled()) return Minecraft.getMinecraft().standardGalacticFontRenderer.getStringWidth(text);
        return this.font.getStringWidth(text);
    }

    public int getTextHeight() {
        if (!FontManager.getCustomFontEnabled()) return Minecraft.getMinecraft().standardGalacticFontRenderer.FONT_HEIGHT;
        return this.font.getStringHeight();
    }

    public void setFontSize(int size) {
        this.fontSize = size;
        this.setFont();
    }

    public static Font getClientFont(String fontName, float size) {
        try {
            InputStream inputStream = FontManager.class.getResourceAsStream("/assets/minecraft/fonts/" + fontName);
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, size);
            inputStream.close();
            return awtClientFont;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Font("default", 0, (int)size);
        }
    }

    public static boolean getCustomFontEnabled() {
        return ClickGuiConfig.getInstance().getCustomFontEnabled();
    }

    public static boolean getFontShadow() {
        return ClickGuiConfig.getInstance().getFontShadow();
    }
}

