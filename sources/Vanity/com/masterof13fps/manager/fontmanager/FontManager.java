package com.masterof13fps.manager.fontmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static final Map<String, UnicodeFontRenderer> FONT_RENDERER_HASH_MAP = new HashMap<>();

    public void initFonts() {
    }

    private static Font font(String name, int size, FontExtension fe) {
        Font font = null;
        try {
            InputStream ex = Minecraft.mc().getResourceManager()
                    .getResource(new ResourceLocation("client/fonts/" + name + "." + fe.name().toLowerCase())).getInputStream();
            font = Font.createFont(0, ex);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Font not loaded.  Using serif font.");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public enum FontExtension {
        TTF, OTF;
    }

    /**
     * @param fontName font identifying name
     * @param size     the size of the font
     * @param fontType Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC (Bold and Italic)
     * @return new font instance
     * @author Trol
     * <p>
     * Gets the font, if absent creates a new one and puts it into a hashmap
     */
    public UnicodeFontRenderer font(String fontName, int size, int fontType) {
        final String id = fontName + "-" + size + "-" + fontType;
        return FONT_RENDERER_HASH_MAP.computeIfAbsent(id, (idx) -> new UnicodeFontRenderer(new Font(fontName, fontType, size), true, 8));
    }
}