// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Iterator;
import java.util.Set;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;

public class FontUtils
{
    public static Properties readFontProperties(final ResourceLocation locationFontTexture) {
        final String fontFileName = locationFontTexture.getResourcePath();
        final Properties props = new Properties();
        final String suffix = ".png";
        if (!fontFileName.endsWith(suffix)) {
            return props;
        }
        final String fileName = String.valueOf(fontFileName.substring(0, fontFileName.length() - suffix.length())) + ".properties";
        try {
            final ResourceLocation e = new ResourceLocation(locationFontTexture.getResourceDomain(), fileName);
            final InputStream in = Config.getResourceStream(Config.getResourceManager(), e);
            if (in == null) {
                return props;
            }
            Config.log("Loading " + fileName);
            props.load(in);
        }
        catch (FileNotFoundException ex) {}
        catch (IOException var8) {
            var8.printStackTrace();
        }
        return props;
    }
    
    public static void readCustomCharWidths(final Properties props, final float[] charWidth) {
        final Set keySet = props.keySet();
        for (final String key : keySet) {
            final String prefix = "width.";
            if (key.startsWith(prefix)) {
                final String numStr = key.substring(prefix.length());
                final int num = Config.parseInt(numStr, -1);
                if (num < 0 || num >= charWidth.length) {
                    continue;
                }
                final String value = props.getProperty(key);
                final float width = Config.parseFloat(value, -1.0f);
                if (width < 0.0f) {
                    continue;
                }
                charWidth[num] = width;
            }
        }
    }
    
    public static float readFloat(final Properties props, final String key, final float defOffset) {
        final String str = props.getProperty(key);
        if (str == null) {
            return defOffset;
        }
        final float offset = Config.parseFloat(str, Float.MIN_VALUE);
        if (offset == Float.MIN_VALUE) {
            Config.warn("Invalid value for " + key + ": " + str);
            return defOffset;
        }
        return offset;
    }
    
    public static ResourceLocation getHdFontLocation(final ResourceLocation fontLoc) {
        if (!Config.isCustomFonts()) {
            return fontLoc;
        }
        if (fontLoc == null) {
            return fontLoc;
        }
        String fontName = fontLoc.getResourcePath();
        final String texturesStr = "textures/";
        final String mcpatcherStr = "mcpatcher/";
        if (!fontName.startsWith(texturesStr)) {
            return fontLoc;
        }
        fontName = fontName.substring(texturesStr.length());
        fontName = String.valueOf(mcpatcherStr) + fontName;
        final ResourceLocation fontLocHD = new ResourceLocation(fontLoc.getResourceDomain(), fontName);
        return Config.hasResource(Config.getResourceManager(), fontLocHD) ? fontLocHD : fontLoc;
    }
}
