package dev.tenacity.util.render.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class FontUtil {

    private static final Map<String, CustomFont> FONT_CACHE_MAP = new HashMap<>();

    private FontUtil() {
    }

    public static CustomFont getFont(final String name, final int size) {
        if(FONT_CACHE_MAP.containsKey(name + size))
            return FONT_CACHE_MAP.get(name + size);

        try {
            final InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Tenacity/Fonts/" + name + ".ttf")).getInputStream();
            final Font font = Font.createFont(0, inputStream).deriveFont(Font.PLAIN, size);

            FONT_CACHE_MAP.put(name + size, new CustomFont(font));

            return FONT_CACHE_MAP.get(name + size);
        } catch(final IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadFont(final String name, final int... sizes) {
        for(final int size : sizes) {
            if(!FONT_CACHE_MAP.containsKey(name + size)) {
                try {
                    final InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Tenacity/Fonts/" + name + ".ttf")).getInputStream();
                    final Font font = Font.createFont(0, inputStream).deriveFont(Font.PLAIN, size);

                    FONT_CACHE_MAP.put(name + size, new CustomFont(font));
                } catch(final IOException | FontFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
