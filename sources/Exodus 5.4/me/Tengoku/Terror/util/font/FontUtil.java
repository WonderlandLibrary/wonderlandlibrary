/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util.font;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import me.Tengoku.Terror.util.font.MinecraftFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontUtil {
    private static Font normal_;
    public static volatile int completed;
    public static MinecraftFontRenderer normal;

    private static Font getFont(Map<String, Font> map, String string, int n) {
        Font font = null;
        if (map.containsKey(string)) {
            font = map.get(string).deriveFont(0, n);
        } else {
            try {
                InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Terror/font/" + string)).getInputStream();
                font = Font.createFont(0, inputStream);
                map.put(string, font);
                font = font.deriveFont(0, n);
            }
            catch (Exception exception) {
                exception.printStackTrace();
                System.out.println("Error loading font");
                font = new Font("default", 0, 10);
            }
        }
        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }

    public static void bootstrap() {
        new Thread(() -> {
            HashMap<String, Font> hashMap = new HashMap<String, Font>();
            normal_ = FontUtil.getFont(hashMap, "arial.ttf", 19);
            ++completed;
        }).start();
        new Thread(() -> {
            HashMap hashMap = new HashMap();
            ++completed;
        }).start();
        new Thread(() -> {
            HashMap hashMap = new HashMap();
            ++completed;
        }).start();
        while (!FontUtil.hasLoaded()) {
            try {
                Thread.sleep(5L);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        normal = new MinecraftFontRenderer(normal_, true, true);
    }
}

