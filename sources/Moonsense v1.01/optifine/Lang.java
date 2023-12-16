// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Iterator;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.Charsets;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.client.resources.IResourcePack;
import java.util.ArrayList;
import net.minecraft.client.resources.I18n;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;

public class Lang
{
    private static final Splitter splitter;
    private static final Pattern pattern;
    
    static {
        splitter = Splitter.on('=').limit(2);
        pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    }
    
    public static void resourcesReloaded() {
        final Map localeProperties = I18n.getLocaleProperties();
        final ArrayList listFiles = new ArrayList();
        final String PREFIX = "optifine/lang/";
        final String EN_US = "en_US";
        final String SUFFIX = ".lang";
        listFiles.add(String.valueOf(PREFIX) + EN_US + SUFFIX);
        if (!Config.getGameSettings().language.equals(EN_US)) {
            listFiles.add(String.valueOf(PREFIX) + Config.getGameSettings().language + SUFFIX);
        }
        final String[] files = listFiles.toArray(new String[listFiles.size()]);
        loadResources(Config.getDefaultResourcePack(), files, localeProperties);
        final IResourcePack[] resourcePacks = Config.getResourcePacks();
        for (int i = 0; i < resourcePacks.length; ++i) {
            final IResourcePack rp = resourcePacks[i];
            loadResources(rp, files, localeProperties);
        }
    }
    
    private static void loadResources(final IResourcePack rp, final String[] files, final Map localeProperties) {
        try {
            for (int e = 0; e < files.length; ++e) {
                final String file = files[e];
                final ResourceLocation loc = new ResourceLocation(file);
                if (rp.resourceExists(loc)) {
                    final InputStream in = rp.getInputStream(loc);
                    if (in != null) {
                        loadLocaleData(in, localeProperties);
                    }
                }
            }
        }
        catch (IOException var7) {
            var7.printStackTrace();
        }
    }
    
    public static void loadLocaleData(final InputStream is, final Map localeProperties) throws IOException {
        for (final String line : IOUtils.readLines(is, Charsets.UTF_8)) {
            if (!line.isEmpty() && line.charAt(0) != '#') {
                final String[] parts = (String[])Iterables.toArray(Lang.splitter.split((CharSequence)line), (Class)String.class);
                if (parts == null || parts.length != 2) {
                    continue;
                }
                final String key = parts[0];
                final String value = Lang.pattern.matcher(parts[1]).replaceAll("%$1s");
                localeProperties.put(key, value);
            }
        }
    }
    
    public static String get(final String key) {
        return I18n.format(key, new Object[0]);
    }
    
    public static String get(final String key, final String def) {
        final String str = I18n.format(key, new Object[0]);
        return (str != null && !str.equals(key)) ? str : def;
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
