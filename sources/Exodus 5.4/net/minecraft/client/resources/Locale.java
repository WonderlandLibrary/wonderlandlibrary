/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Maps
 *  org.apache.commons.io.Charsets
 *  org.apache.commons.io.IOUtils
 */
package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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
    private static final Pattern pattern;
    private static final Splitter splitter;
    Map<String, String> properties = Maps.newHashMap();
    private boolean unicode;

    private void loadLocaleData(List<IResource> list) throws IOException {
        for (IResource iResource : list) {
            InputStream inputStream = iResource.getInputStream();
            this.loadLocaleData(inputStream);
            IOUtils.closeQuietly((InputStream)inputStream);
        }
    }

    private void checkUnicode() {
        this.unicode = false;
        int n = 0;
        int n2 = 0;
        for (String string : this.properties.values()) {
            int n3 = string.length();
            n2 += n3;
            int n4 = 0;
            while (n4 < n3) {
                if (string.charAt(n4) >= '\u0100') {
                    ++n;
                }
                ++n4;
            }
        }
        float f = (float)n / (float)n2;
        this.unicode = (double)f > 0.1;
    }

    private String translateKeyPrivate(String string) {
        String string2 = this.properties.get(string);
        return string2 == null ? string : string2;
    }

    public String formatMessage(String string, Object[] objectArray) {
        String string2 = this.translateKeyPrivate(string);
        try {
            return String.format(string2, objectArray);
        }
        catch (IllegalFormatException illegalFormatException) {
            return "Format error: " + string2;
        }
    }

    public boolean isUnicode() {
        return this.unicode;
    }

    public synchronized void loadLocaleDataFiles(IResourceManager iResourceManager, List<String> list) {
        this.properties.clear();
        for (String string : list) {
            String string2 = String.format("lang/%s.lang", string);
            for (String string3 : iResourceManager.getResourceDomains()) {
                try {
                    this.loadLocaleData(iResourceManager.getAllResources(new ResourceLocation(string3, string2)));
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
        this.checkUnicode();
    }

    static {
        splitter = Splitter.on((char)'=').limit(2);
        pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    }

    private void loadLocaleData(InputStream inputStream) throws IOException {
        for (String string : IOUtils.readLines((InputStream)inputStream, (Charset)Charsets.UTF_8)) {
            String[] stringArray;
            if (string.isEmpty() || string.charAt(0) == '#' || (stringArray = (String[])Iterables.toArray((Iterable)splitter.split((CharSequence)string), String.class)) == null || stringArray.length != 2) continue;
            String string2 = stringArray[0];
            String string3 = pattern.matcher(stringArray[1]).replaceAll("%$1s");
            this.properties.put(string2, string3);
        }
    }
}

