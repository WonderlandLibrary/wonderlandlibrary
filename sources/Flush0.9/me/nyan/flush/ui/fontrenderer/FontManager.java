package me.nyan.flush.ui.fontrenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class FontManager {
    private final ArrayList<ClientFont> fonts = new ArrayList<>();

    public static final String[] AVAILABLE_FONTS = {
            "Roboto Bold",
            "Roboto Medium",
            "Roboto",
            "Roboto Light",
            "Roboto Thin",

            "GoogleSansDisplay Bold",
            "GoogleSansDisplay Medium",
            "GoogleSansDisplay",

            "Times New Roman",
            "Times New Roman Bold"
    };

    public static final int[] AVAILABLE_SIZES = {60, 40, 30, 28, 26, 24, 22, 20, 18, 16};

    private void addFonts(String font, int[] sizes) {
        for (int size : sizes) {
            addFont(font, size);
        }
    }

    private ClientFont addFont(String font, int size) {
        ClientFont clientFont = new ClientFont(getAWTFont(font.replace(" ", "-") + ".ttf", size), font, size);
        fonts.add(clientFont);
        return clientFont;
    }

    private Font getAWTFont(String file, int size) {
        try {
            InputStream is = Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(new ResourceLocation("flush/fonts/" + file))
                    .getInputStream();
            return Font.createFont(Font.TRUETYPE_FONT, is)
                    .deriveFont(Font.PLAIN, size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font(null, Font.PLAIN, size);
        }
    }

    public GlyphPageFontRenderer getFont(String name, int size) {
        ClientFont clientFont = fonts.stream()
                .filter(font -> font.getSize() == size && font.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> addFont(name, size));
        return clientFont.getFontRenderer();
    }

    public ArrayList<ClientFont> getFonts() {
        return fonts;
    }

    public static class ClientFont {
        private final Font awtFont;
        private final String name;
        private final int size;
        private GlyphPageFontRenderer fontRenderer;

        public ClientFont(Font awtFont, String name, int size) {
            this.awtFont = awtFont;
            this.name = name;
            this.size = size;
        }

        public Font getAwtFont() {
            return awtFont;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        public GlyphPageFontRenderer getFontRenderer() {
            if (fontRenderer == null) {
                fontRenderer = GlyphPageFontRenderer.create(awtFont);
            }
            return fontRenderer;
        }
    }
}
