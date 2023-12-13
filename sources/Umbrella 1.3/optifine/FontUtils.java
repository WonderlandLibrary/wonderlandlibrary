/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import net.minecraft.util.ResourceLocation;
import optifine.Config;

public class FontUtils {
    public static Properties readFontProperties(ResourceLocation locationFontTexture) {
        String fontFileName = locationFontTexture.getResourcePath();
        Properties props = new Properties();
        String suffix = ".png";
        if (!fontFileName.endsWith(suffix)) {
            return props;
        }
        String fileName = String.valueOf(fontFileName.substring(0, fontFileName.length() - suffix.length())) + ".properties";
        try {
            ResourceLocation e = new ResourceLocation(locationFontTexture.getResourceDomain(), fileName);
            InputStream in = Config.getResourceStream(Config.getResourceManager(), e);
            if (in == null) {
                return props;
            }
            Config.log("Loading " + fileName);
            props.load(in);
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException var8) {
            var8.printStackTrace();
        }
        return props;
    }

    public static void readCustomCharWidths(Properties props, float[] charWidth) {
        Set<Object> keySet = props.keySet();
        for (String string : keySet) {
            String value;
            float width;
            String numStr;
            int num;
            String prefix;
            if (!string.startsWith(prefix = "width.") || (num = Config.parseInt(numStr = string.substring(prefix.length()), -1)) < 0 || num >= charWidth.length || !((width = Config.parseFloat(value = props.getProperty(string), -1.0f)) >= 0.0f)) continue;
            charWidth[num] = width;
        }
    }

    public static float readFloat(Properties props, String key, float defOffset) {
        String str = props.getProperty(key);
        if (str == null) {
            return defOffset;
        }
        float offset = Config.parseFloat(str, Float.MIN_VALUE);
        if (offset == Float.MIN_VALUE) {
            Config.warn("Invalid value for " + key + ": " + str);
            return defOffset;
        }
        return offset;
    }

    public static ResourceLocation getHdFontLocation(ResourceLocation fontLoc) {
        if (!Config.isCustomFonts()) {
            return fontLoc;
        }
        if (fontLoc == null) {
            return fontLoc;
        }
        String fontName = fontLoc.getResourcePath();
        String texturesStr = "textures/";
        String mcpatcherStr = "mcpatcher/";
        if (!fontName.startsWith(texturesStr)) {
            return fontLoc;
        }
        fontName = fontName.substring(texturesStr.length());
        fontName = String.valueOf(mcpatcherStr) + fontName;
        ResourceLocation fontLocHD = new ResourceLocation(fontLoc.getResourceDomain(), fontName);
        return Config.hasResource(Config.getResourceManager(), fontLocHD) ? fontLocHD : fontLoc;
    }
}

