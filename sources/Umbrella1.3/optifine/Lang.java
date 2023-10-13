/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Iterables
 *  org.apache.commons.io.Charsets
 *  org.apache.commons.io.IOUtils
 */
package optifine;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class Lang {
    private static final Splitter splitter = Splitter.on((char)'=').limit(2);
    private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");

    public static void resourcesReloaded() {
        Map localeProperties = I18n.getLocaleProperties();
        ArrayList<String> listFiles = new ArrayList<String>();
        String PREFIX = "optifine/lang/";
        String EN_US = "en_US";
        String SUFFIX = ".lang";
        listFiles.add(String.valueOf(PREFIX) + EN_US + SUFFIX);
        if (!Config.getGameSettings().language.equals(EN_US)) {
            listFiles.add(String.valueOf(PREFIX) + Config.getGameSettings().language + SUFFIX);
        }
        String[] files = listFiles.toArray(new String[listFiles.size()]);
        Lang.loadResources(Config.getDefaultResourcePack(), files, localeProperties);
        IResourcePack[] resourcePacks = Config.getResourcePacks();
        for (int i = 0; i < resourcePacks.length; ++i) {
            IResourcePack rp = resourcePacks[i];
            Lang.loadResources(rp, files, localeProperties);
        }
    }

    private static void loadResources(IResourcePack rp, String[] files, Map localeProperties) {
        try {
            for (int e = 0; e < files.length; ++e) {
                InputStream in;
                String file = files[e];
                ResourceLocation loc = new ResourceLocation(file);
                if (!rp.resourceExists(loc) || (in = rp.getInputStream(loc)) == null) continue;
                Lang.loadLocaleData(in, localeProperties);
            }
        }
        catch (IOException var7) {
            var7.printStackTrace();
        }
    }

    public static void loadLocaleData(InputStream is, Map localeProperties) throws IOException {
        for (String line : IOUtils.readLines((InputStream)is, (Charset)Charsets.UTF_8)) {
            String[] parts;
            if (line.isEmpty() || line.charAt(0) == '#' || (parts = (String[])Iterables.toArray((Iterable)splitter.split((CharSequence)line), String.class)) == null || parts.length != 2) continue;
            String key = parts[0];
            String value = pattern.matcher(parts[1]).replaceAll("%$1s");
            localeProperties.put(key, value);
        }
    }

    public static String get(String key) {
        return I18n.format(key, new Object[0]);
    }

    public static String get(String key, String def) {
        String str = I18n.format(key, new Object[0]);
        return str != null && !str.equals(key) ? str : def;
    }

    public static String getOn() {
        return I18n.format("options.on", new Object[0]);
    }

    public static String getOff() {
        return I18n.format("options.off", new Object[0]);
    }

    public static String getFast() {
        return I18n.format("options.graphics.fast", new Object[0]);
    }

    public static String getFancy() {
        return I18n.format("options.graphics.fancy", new Object[0]);
    }

    public static String getDefault() {
        return I18n.format("generator.default", new Object[0]);
    }
}

