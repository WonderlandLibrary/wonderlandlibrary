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
        for (String s2 : p_135022_2_) {
            String s1 = String.format("lang/%s.lang", s2);
            for (String s22 : resourceManager.getResourceDomains()) {
                try {
                    this.loadLocaleData(resourceManager.getAllResources(new ResourceLocation(s22, s1)));
                }
                catch (IOException iOException) {}
            }
        }
        this.checkUnicode();
    }

    public boolean isUnicode() {
        return this.unicode;
    }

    private void checkUnicode() {
        this.unicode = false;
        int i2 = 0;
        int j2 = 0;
        for (String s2 : this.properties.values()) {
            int k2 = s2.length();
            j2 += k2;
            for (int l2 = 0; l2 < k2; ++l2) {
                if (s2.charAt(l2) < '\u0100') continue;
                ++i2;
            }
        }
        float f2 = (float)i2 / (float)j2;
        this.unicode = (double)f2 > 0.1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadLocaleData(List<IResource> p_135028_1_) throws IOException {
        for (IResource iresource : p_135028_1_) {
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
        for (String s2 : IOUtils.readLines(p_135021_1_, Charsets.UTF_8)) {
            String[] astring;
            if (s2.isEmpty() || s2.charAt(0) == '#' || (astring = Iterables.toArray(splitter.split(s2), String.class)) == null || astring.length != 2) continue;
            String s1 = astring[0];
            String s22 = pattern.matcher(astring[1]).replaceAll("%$1s");
            this.properties.put(s1, s22);
        }
    }

    private String translateKeyPrivate(String p_135026_1_) {
        String s2 = this.properties.get(p_135026_1_);
        return s2 == null ? p_135026_1_ : s2;
    }

    public String formatMessage(String translateKey, Object[] parameters) {
        String s2 = this.translateKeyPrivate(translateKey);
        try {
            return String.format(s2, parameters);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + s2;
        }
    }
}

