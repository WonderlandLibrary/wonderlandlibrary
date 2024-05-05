// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.fonts;

import java.util.HashMap;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.util.Map;
import java.awt.Font;

public class FontUtil
{
    public static volatile int completed;
    public static MinecraftFontRenderer fanboldmedium;
    public static MinecraftFontRenderer cleanlarge;
    public static MinecraftFontRenderer cleankindalarge;
    public static MinecraftFontRenderer cleanmedium;
    public static MinecraftFontRenderer clean;
    public static MinecraftFontRenderer cleanSmall;
    public static MinecraftFontRenderer expandedfont;
    public static MinecraftFontRenderer coolFont;
    public static MinecraftFontRenderer title;
    public static MinecraftFontRenderer csgoFont;
    public static MinecraftFontRenderer categoryFont;
    public static MinecraftFontRenderer moduleFont;
    public static MinecraftFontRenderer movementicon;
    private static Font fanboldmedium_;
    private static Font cleanlarge_;
    private static Font cleankindalarge_;
    private static Font cleanmedium_;
    private static Font clean_;
    private static Font cleanSmall_;
    private static Font expandedfont_;
    private static Font coolFont_;
    private static Font title_;
    private static Font csgoFont_;
    private static Font categoryFont_;
    private static Font moduleFont_;
    private static Font movementicon_;
    
    private static Font getFont(final Map<String, Font> locationMap, final String location, final int size) {
        Font font;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(0, (float)size);
            }
            else {
                final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Lavish/fonts/" + location)).getInputStream();
                font = Font.createFont(0, is);
                locationMap.put(location, font);
                font = font.deriveFont(0, (float)size);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, 10);
        }
        return font;
    }
    
    public static boolean hasLoaded() {
        return FontUtil.completed >= 3;
    }
    
    public static void bootstrap() {
        final HashMap<String, Font> locationMap;
        new Thread(() -> {
            locationMap = new HashMap<String, Font>();
            FontUtil.clean_ = getFont(locationMap, "Applefnt.ttf", 20);
            FontUtil.cleankindalarge_ = getFont(locationMap, "Applefnt.ttf", 26);
            FontUtil.cleanlarge_ = getFont(locationMap, "Applefnt.ttf", 35);
            FontUtil.cleanmedium_ = getFont(locationMap, "Applefnt.ttf", 20);
            FontUtil.cleanSmall_ = getFont(locationMap, "Applefnt.ttf", 17);
            FontUtil.expandedfont_ = getFont(locationMap, "bold.ttf", 38);
            FontUtil.fanboldmedium_ = getFont(locationMap, "bold.ttf", 18);
            FontUtil.coolFont_ = getFont(locationMap, "font.ttf", 30);
            FontUtil.title_ = getFont(locationMap, "title.ttf", 22);
            FontUtil.csgoFont_ = getFont(locationMap, "csgoFont.ttf", 30);
            FontUtil.movementicon_ = getFont(locationMap, "movementicon.ttf", 35);
            FontUtil.categoryFont_ = getFont(locationMap, "CategoryFont.ttf", 28);
            FontUtil.moduleFont_ = getFont(locationMap, "CategoryFont.ttf", 18);
            ++FontUtil.completed;
            return;
        }).start();
        final HashMap<String, Font> locationMap2;
        new Thread(() -> {
            locationMap2 = new HashMap<String, Font>();
            ++FontUtil.completed;
            return;
        }).start();
        final HashMap<String, Font> locationMap3;
        new Thread(() -> {
            locationMap3 = new HashMap<String, Font>();
            ++FontUtil.completed;
            return;
        }).start();
        while (!hasLoaded()) {
            try {
                Thread.sleep(5L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        FontUtil.cleankindalarge = new MinecraftFontRenderer(FontUtil.cleankindalarge_, true, true);
        FontUtil.cleanlarge = new MinecraftFontRenderer(FontUtil.cleanlarge_, true, true);
        FontUtil.cleanmedium = new MinecraftFontRenderer(FontUtil.cleanmedium_, true, true);
        FontUtil.clean = new MinecraftFontRenderer(FontUtil.clean_, true, true);
        FontUtil.cleanSmall = new MinecraftFontRenderer(FontUtil.cleanSmall_, true, true);
        FontUtil.fanboldmedium = new MinecraftFontRenderer(FontUtil.fanboldmedium_, true, true);
        FontUtil.expandedfont = new MinecraftFontRenderer(FontUtil.expandedfont_, true, true);
        FontUtil.coolFont = new MinecraftFontRenderer(FontUtil.coolFont_, true, true);
        FontUtil.title = new MinecraftFontRenderer(FontUtil.title_, true, true);
        FontUtil.csgoFont = new MinecraftFontRenderer(FontUtil.csgoFont_, true, true);
        FontUtil.movementicon = new MinecraftFontRenderer(FontUtil.movementicon_, true, true);
        FontUtil.categoryFont = new MinecraftFontRenderer(FontUtil.categoryFont_, true, true);
        FontUtil.moduleFont = new MinecraftFontRenderer(FontUtil.moduleFont_, true, true);
    }
}
