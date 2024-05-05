package net.optifine;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class Lang {
    private static final Splitter splitter = Splitter.on((char)'=').limit(2);
    private static final Pattern pattern = Pattern.compile((String)"%(\\d+\\$)?[\\d\\.]*[df]");

    public static void resourcesReloaded() {
        Map map = I18n.getLocaleProperties();
        ArrayList list = new ArrayList();
        String s = "optifine/lang/";
        String s1 = "en_US";
        String s2 = ".lang";
        list.add((Object)(s + s1 + s2));
        if (!Config.getGameSettings().language.equals((Object)s1)) {
            list.add((Object)(s + Config.getGameSettings().language + s2));
        }
        String[] astring = (String[])list.toArray((Object[])new String[list.size()]);
        Lang.loadResources((IResourcePack)Config.getDefaultResourcePack(), astring, map);
        IResourcePack[] airesourcepack = Config.getResourcePacks();
        for (int i = 0; i < airesourcepack.length; ++i) {
            IResourcePack iresourcepack = airesourcepack[i];
            Lang.loadResources(iresourcepack, astring, map);
        }
    }

    private static void loadResources(IResourcePack rp, String[] files, Map localeProperties) {
        try {
            for (int i = 0; i < files.length; ++i) {
                InputStream inputstream;
                String s = files[i];
                ResourceLocation resourcelocation = new ResourceLocation(s);
                if (!rp.resourceExists(resourcelocation) || (inputstream = rp.getInputStream(resourcelocation)) == null) continue;
                Lang.loadLocaleData(inputstream, localeProperties);
            }
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    public static void loadLocaleData(InputStream is, Map localeProperties) throws IOException {
        Iterator iterator = IOUtils.readLines((InputStream)is, (Charset)Charsets.UTF_8).iterator();
        is.close();
        while (iterator.hasNext()) {
            String[] astring;
            String s = (String)iterator.next();
            if (s.isEmpty() || s.charAt(0) == '#' || (astring = (String[])Iterables.toArray((Iterable)splitter.split((CharSequence)s), String.class)) == null || astring.length != 2) continue;
            String s1 = astring[0];
            String s2 = pattern.matcher((CharSequence)astring[1]).replaceAll("%$1s");
            localeProperties.put((Object)s1, (Object)s2);
        }
    }

    public static String get(String key) {
        return I18n.format((String)key, (Object[])new Object[0]);
    }

    public static String get(String key, String def) {
        String s = I18n.format((String)key, (Object[])new Object[0]);
        return s != null && !s.equals((Object)key) ? s : def;
    }

    public static String getOn() {
        return I18n.format((String)"options.on", (Object[])new Object[0]);
    }

    public static String getOff() {
        return I18n.format((String)"options.off", (Object[])new Object[0]);
    }

    public static String getFast() {
        return I18n.format((String)"options.graphics.fast", (Object[])new Object[0]);
    }

    public static String getFancy() {
        return I18n.format((String)"options.graphics.fancy", (Object[])new Object[0]);
    }

    public static String getDefault() {
        return I18n.format((String)"generator.default", (Object[])new Object[0]);
    }
}
