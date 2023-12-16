// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.minecraft.src.Config;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;

public class FontUtils
{
    public static Properties readFontProperties(final ResourceLocation locationFontTexture) {
        final String s = locationFontTexture.getResourcePath();
        final Properties properties = new PropertiesOrdered();
        final String s2 = ".png";
        if (!s.endsWith(s2)) {
            return properties;
        }
        final String s3 = String.valueOf(s.substring(0, s.length() - s2.length())) + ".properties";
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(locationFontTexture.getResourceDomain(), s3);
            final InputStream inputstream = Config.getResourceStream(Config.getResourceManager(), resourcelocation);
            if (inputstream == null) {
                return properties;
            }
            Config.log("Loading " + s3);
            properties.load(inputstream);
            inputstream.close();
        }
        catch (final FileNotFoundException ex) {}
        catch (final IOException ioexception) {
            ioexception.printStackTrace();
        }
        return properties;
    }
    
    public static void readCustomCharWidths(final Properties props, final float[] charWidth) {
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s = (String)o;
            final String s2 = "width.";
            if (s.startsWith(s2)) {
                final String s3 = s.substring(s2.length());
                final int i = Config.parseInt(s3, -1);
                if (i < 0 || i >= charWidth.length) {
                    continue;
                }
                final String s4 = props.getProperty(s);
                final float f = Config.parseFloat(s4, -1.0f);
                if (f < 0.0f) {
                    continue;
                }
                charWidth[i] = f;
            }
        }
    }
    
    public static float readFloat(final Properties props, final String key, final float defOffset) {
        final String s = props.getProperty(key);
        if (s == null) {
            return defOffset;
        }
        final float f = Config.parseFloat(s, Float.MIN_VALUE);
        if (f == Float.MIN_VALUE) {
            Config.warn("Invalid value for " + key + ": " + s);
            return defOffset;
        }
        return f;
    }
    
    public static boolean readBoolean(final Properties props, final String key, final boolean defVal) {
        final String s = props.getProperty(key);
        if (s == null) {
            return defVal;
        }
        final String s2 = s.toLowerCase().trim();
        if (s2.equals("true") || s2.equals("on")) {
            return true;
        }
        if (!s2.equals("false") && !s2.equals("off")) {
            Config.warn("Invalid value for " + key + ": " + s);
            return defVal;
        }
        return false;
    }
    
    public static ResourceLocation getHdFontLocation(final ResourceLocation fontLoc) {
        if (!Config.isCustomFonts()) {
            return fontLoc;
        }
        if (fontLoc == null) {
            return fontLoc;
        }
        if (!Config.isMinecraftThread()) {
            return fontLoc;
        }
        String s = fontLoc.getResourcePath();
        final String s2 = "textures/";
        final String s3 = "mcpatcher/";
        if (!s.startsWith(s2)) {
            return fontLoc;
        }
        s = s.substring(s2.length());
        s = String.valueOf(s3) + s;
        final ResourceLocation resourcelocation = new ResourceLocation(fontLoc.getResourceDomain(), s);
        return Config.hasResource(Config.getResourceManager(), resourcelocation) ? resourcelocation : fontLoc;
    }
}
