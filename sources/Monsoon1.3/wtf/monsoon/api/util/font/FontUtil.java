package wtf.monsoon.api.util.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAtomicOperationOnVolatileField")
public class FontUtil {
    public static volatile int completed;
    public static MinecraftFontRenderer monsoon_regular, monsoon_arrayList, large, moon;
    private static Font monsoon_regular_, monsoon_arrayList_, large_, moon_;

    private static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font = null;

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("monsoon/font/" + location)).getInputStream();
                font = Font.createFont(0, is);
                locationMap.put(location, font);
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, +10);
        }

        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }

    public static void bootstrap() {
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            monsoon_regular_ = getFont(locationMap, "whitneybook.otf", 19);
            monsoon_arrayList_ = getFont(locationMap, "whitneybook.otf", 20);
            large_ = getFont(locationMap, "whitneysemibold.otf", 50);
            moon_ = getFont(locationMap, "product_sans.ttf", 19);
            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();

        while (!hasLoaded()) {
            try {
                //noinspection BusyWait
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        monsoon_regular = new MinecraftFontRenderer(monsoon_regular_, true, true);
        monsoon_arrayList = new MinecraftFontRenderer(monsoon_arrayList_, true, true);
        large = new MinecraftFontRenderer(large_, true, true);
        moon = new MinecraftFontRenderer(moon_, true, true);
    }
}