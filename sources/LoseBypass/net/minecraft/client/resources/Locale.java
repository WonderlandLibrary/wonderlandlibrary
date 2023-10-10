/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class Locale {
    private static final Splitter splitter = Splitter.on('=').limit(2);
    private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    Map<String, String> properties = Maps.newHashMap();
    private boolean unicode;

    public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List<String> p_135022_2_) {
        this.properties.clear();
        Iterator<String> iterator = p_135022_2_.iterator();
        block2: while (true) {
            if (!iterator.hasNext()) {
                this.checkUnicode();
                return;
            }
            String s = iterator.next();
            String s1 = String.format("lang/%s.lang", s);
            Iterator<String> iterator2 = resourceManager.getResourceDomains().iterator();
            while (true) {
                if (!iterator2.hasNext()) continue block2;
                String s2 = iterator2.next();
                try {
                    this.loadLocaleData(resourceManager.getAllResources(new ResourceLocation(s2, s1)));
                }
                catch (IOException iOException) {
                }
            }
            break;
        }
    }

    public boolean isUnicode() {
        return this.unicode;
    }

    private void checkUnicode() {
        this.unicode = false;
        int i = 0;
        int j = 0;
        for (String s : this.properties.values()) {
            int k = s.length();
            j += k;
            for (int l = 0; l < k; ++l) {
                if (s.charAt(l) < '\u0100') continue;
                ++i;
            }
        }
        float f = (float)i / (float)j;
        this.unicode = (double)f > 0.1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadLocaleData(List<IResource> p_135028_1_) throws IOException {
        Iterator<IResource> iterator = p_135028_1_.iterator();
        while (iterator.hasNext()) {
            IResource iresource = iterator.next();
            InputStream inputstream = iresource.getInputStream();
            try {
                this.loadLocaleData(inputstream);
            }
            finally {
                IOUtils.closeQuietly(inputstream);
            }
        }
    }

    private void loadLocaleData(InputStream p_135021_1_) throws IOException {
        Iterator<String> iterator = IOUtils.readLines(p_135021_1_, Charsets.UTF_8).iterator();
        while (iterator.hasNext()) {
            String[] astring;
            String s = iterator.next();
            if (s.isEmpty() || s.charAt(0) == '#' || (astring = Iterables.toArray(splitter.split(s), String.class)) == null || astring.length != 2) continue;
            String s1 = astring[0];
            String s2 = pattern.matcher(astring[1]).replaceAll("%$1s");
            this.properties.put(s1, s2);
        }
    }

    private String translateKeyPrivate(String p_135026_1_) {
        String string;
        String s = this.properties.get(p_135026_1_);
        if (s == null) {
            string = p_135026_1_;
            return string;
        }
        string = s;
        return string;
    }

    public String formatMessage(String translateKey, Object[] parameters) {
        String s = this.translateKeyPrivate(translateKey);
        try {
            return String.format(s, parameters);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + s;
        }
    }
}

