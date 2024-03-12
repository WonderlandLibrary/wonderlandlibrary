// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.Iterator;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.Charsets;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.src.Config;
import java.util.ArrayList;
import net.minecraft.client.resources.I18n;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;

public class Lang
{
    private static final Splitter splitter;
    private static final Pattern pattern;
    
    public static void resourcesReloaded() {
        final Map map = I18n.getLocaleProperties();
        final List<String> list = new ArrayList<String>();
        final String s = "optifine/lang/";
        final String s2 = "en_US";
        final String s3 = ".lang";
        list.add(s + s2 + s3);
        if (!Config.getGameSettings().language.equals(s2)) {
            list.add(s + Config.getGameSettings().language + s3);
        }
        final String[] astring = list.toArray(new String[list.size()]);
        loadResources(Config.getDefaultResourcePack(), astring, map);
        final IResourcePack[] airesourcepack = Config.getResourcePacks();
        for (int i = 0; i < airesourcepack.length; ++i) {
            final IResourcePack iresourcepack = airesourcepack[i];
            loadResources(iresourcepack, astring, map);
        }
    }
    
    private static void loadResources(final IResourcePack rp, final String[] files, final Map localeProperties) {
        try {
            for (int i = 0; i < files.length; ++i) {
                final String s = files[i];
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                if (rp.resourceExists(resourcelocation)) {
                    final InputStream inputstream = rp.getInputStream(resourcelocation);
                    if (inputstream != null) {
                        loadLocaleData(inputstream, localeProperties);
                    }
                }
            }
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    public static void loadLocaleData(final InputStream is, final Map localeProperties) throws IOException {
        final Iterator iterator = IOUtils.readLines(is, Charsets.UTF_8).iterator();
        is.close();
        while (iterator.hasNext()) {
            final String s = iterator.next();
            if (!s.isEmpty() && s.charAt(0) != '#') {
                final String[] astring = (String[])Iterables.toArray(Lang.splitter.split((CharSequence)s), (Class)String.class);
                if (astring == null || astring.length != 2) {
                    continue;
                }
                final String s2 = astring[0];
                final String s3 = Lang.pattern.matcher(astring[1]).replaceAll("%$1s");
                localeProperties.put(s2, s3);
            }
        }
    }
    
    public static String get(final String key) {
        return I18n.format(key, new Object[0]);
    }
    
    public static String get(final String key, final String def) {
        final String s = I18n.format(key, new Object[0]);
        return (s != null && !s.equals(key)) ? s : def;
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
    
    static {
        splitter = Splitter.on('=').limit(2);
        pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    }
}
