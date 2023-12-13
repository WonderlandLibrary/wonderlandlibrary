// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.font;

import java.util.HashMap;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.util.Map;
import java.awt.Font;

public class FontUtil
{
    public static volatile int completed;
    public static MinecraftFontRenderer clientfont;
    public static MinecraftFontRenderer clientmedium;
    public static MinecraftFontRenderer clientsmall;
    public static MinecraftFontRenderer summer;
    public static MinecraftFontRenderer sf;
    public static MinecraftFontRenderer oxide;
    public static MinecraftFontRenderer robo;
    public static MinecraftFontRenderer robo22;
    public static MinecraftFontRenderer roboSmall;
    public static MinecraftFontRenderer icons;
    public static MinecraftFontRenderer icons2;
    public static MinecraftFontRenderer icons3;
    public static MinecraftFontRenderer verdana10;
    public static MinecraftFontRenderer neverlose;
    public static MinecraftFontRenderer tahoma;
    public static MinecraftFontRenderer tahomaSmall;
    public static MinecraftFontRenderer tahomaVerySmall;
    public static MinecraftFontRenderer c16;
    public static MinecraftFontRenderer c22;
    public static MinecraftFontRenderer moon;
    public static MinecraftFontRenderer rain;
    public static MinecraftFontRenderer iconfont;
    public static MinecraftFontRenderer light;
    private static Font clientfont_;
    private static Font clientmedium_;
    private static Font clientsmall_;
    private static Font summer_;
    private static Font sf_;
    private static Font oxide_;
    private static Font robo_;
    private static Font robo22_;
    private static Font roboSmall_;
    private static Font icons_;
    private static Font icons2_;
    private static Font icons3_;
    private static Font verdana10_;
    private static Font neverlose_;
    private static Font tahoma_;
    private static Font tahomaSmall_;
    private static Font tahomaVerySmall_;
    private static Font c16_;
    private static Font c22_;
    private static Font moon_;
    private static Font iconfont_;
    
    private static Font getFont(final Map<String, Font> locationMap, final String location, final int size) {
        Font font = null;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(0, (float)size);
            }
            else {
                final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(location)).getInputStream();
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
            FontUtil.clientfont_ = getFont(locationMap, "resolute/font.ttf", 21);
            FontUtil.clientmedium_ = getFont(locationMap, "resolute/font.ttf", 20);
            FontUtil.clientsmall_ = getFont(locationMap, "resolute/font.ttf", 16);
            FontUtil.summer_ = getFont(locationMap, "resolute/SF.ttf", 23);
            FontUtil.sf_ = getFont(locationMap, "resolute/SF.ttf", 20);
            FontUtil.oxide_ = getFont(locationMap, "resolute/oxide.ttf", 42);
            FontUtil.robo_ = getFont(locationMap, "resolute/Roboto-Regular.ttf", 20);
            FontUtil.robo22_ = getFont(locationMap, "resolute/Roboto-Regular.ttf", 21);
            FontUtil.roboSmall_ = getFont(locationMap, "resolute/Roboto-Regular.ttf", 18);
            FontUtil.icons_ = getFont(locationMap, "resolute/icons.ttf", 40);
            FontUtil.icons2_ = getFont(locationMap, "resolute/Icon-Font.ttf", 40);
            FontUtil.icons3_ = getFont(locationMap, "resolute/icons2.ttf", 40);
            FontUtil.verdana10_ = getFont(locationMap, "resolute/Verdana.ttf", 15);
            FontUtil.neverlose_ = getFont(locationMap, "resolute/museosans-900.ttf", 20);
            FontUtil.tahoma_ = getFont(locationMap, "resolute/tahoma.ttf", 20);
            FontUtil.tahomaSmall_ = getFont(locationMap, "resolute/tahoma.ttf", 15);
            FontUtil.tahomaVerySmall_ = getFont(locationMap, "resolute/tahoma.ttf", 10);
            FontUtil.c16_ = getFont(locationMap, "resolute/ali.ttf", 16);
            FontUtil.c22_ = getFont(locationMap, "resolute/ali.ttf", 22);
            FontUtil.moon_ = getFont(locationMap, "resolute/Moon.ttf", 20);
            FontUtil.iconfont_ = getFont(locationMap, "resolute/Icon-Font.ttf", 40);
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
        FontUtil.clientfont = new MinecraftFontRenderer(FontUtil.clientfont_, true, true);
        FontUtil.clientmedium = new MinecraftFontRenderer(FontUtil.clientmedium_, true, true);
        FontUtil.clientsmall = new MinecraftFontRenderer(FontUtil.clientsmall_, true, true);
        FontUtil.summer = new MinecraftFontRenderer(FontUtil.summer_, true, true);
        FontUtil.sf = new MinecraftFontRenderer(FontUtil.sf_, true, true);
        FontUtil.oxide = new MinecraftFontRenderer(FontUtil.oxide_, true, true);
        FontUtil.robo = new MinecraftFontRenderer(FontUtil.robo_, true, true);
        FontUtil.robo22 = new MinecraftFontRenderer(FontUtil.robo22_, true, true);
        FontUtil.roboSmall = new MinecraftFontRenderer(FontUtil.roboSmall_, true, true);
        FontUtil.icons = new MinecraftFontRenderer(FontUtil.icons_, true, true);
        FontUtil.icons2 = new MinecraftFontRenderer(FontUtil.icons2_, true, true);
        FontUtil.icons3 = new MinecraftFontRenderer(FontUtil.icons3_, true, true);
        FontUtil.verdana10 = new MinecraftFontRenderer(FontUtil.verdana10_, true, true);
        FontUtil.neverlose = new MinecraftFontRenderer(FontUtil.neverlose_, true, true);
        FontUtil.tahoma = new MinecraftFontRenderer(FontUtil.tahoma_, true, true);
        FontUtil.tahomaSmall = new MinecraftFontRenderer(FontUtil.tahomaSmall_, true, true);
        FontUtil.tahomaVerySmall = new MinecraftFontRenderer(FontUtil.tahomaVerySmall_, true, true);
        FontUtil.c16 = new MinecraftFontRenderer(FontUtil.c16_, true, true);
        FontUtil.c22 = new MinecraftFontRenderer(FontUtil.c22_, true, true);
        FontUtil.moon = new MinecraftFontRenderer(FontUtil.moon_, true, true);
        FontUtil.iconfont = new MinecraftFontRenderer(FontUtil.iconfont_, true, true);
    }
}
