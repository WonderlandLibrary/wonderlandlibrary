// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.util.IllegalFormatException;
import com.google.common.collect.Iterables;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;

public class Locale
{
    private static final Splitter SPLITTER;
    private static final Pattern PATTERN;
    Map<String, String> properties;
    private boolean unicode;
    
    public Locale() {
        this.properties = (Map<String, String>)Maps.newHashMap();
    }
    
    public synchronized void loadLocaleDataFiles(final IResourceManager resourceManager, final List<String> languageList) {
        this.properties.clear();
        for (final String s : languageList) {
            final String s2 = String.format("lang/%s.lang", s);
            for (final String s3 : resourceManager.getResourceDomains()) {
                try {
                    this.loadLocaleData(resourceManager.getAllResources(new ResourceLocation(s3, s2)));
                }
                catch (IOException ex) {}
            }
        }
        this.checkUnicode();
    }
    
    public boolean isUnicode() {
        return this.unicode;
    }
    
    private void checkUnicode() {
        this.unicode = false;
        int i = 0;
        int j = 0;
        for (final String s : this.properties.values()) {
            final int k = s.length();
            j += k;
            for (int l = 0; l < k; ++l) {
                if (s.charAt(l) >= '\u0100') {
                    ++i;
                }
            }
        }
        final float f = i / (float)j;
        this.unicode = (f > 0.1);
    }
    
    private void loadLocaleData(final List<IResource> resourcesList) throws IOException {
        for (final IResource iresource : resourcesList) {
            final InputStream inputstream = iresource.getInputStream();
            try {
                this.loadLocaleData(inputstream);
            }
            finally {
                IOUtils.closeQuietly(inputstream);
            }
        }
    }
    
    private void loadLocaleData(final InputStream inputStreamIn) throws IOException {
        for (final String s : IOUtils.readLines(inputStreamIn, StandardCharsets.UTF_8)) {
            if (!s.isEmpty() && s.charAt(0) != '#') {
                final String[] astring = (String[])Iterables.toArray(Locale.SPLITTER.split((CharSequence)s), (Class)String.class);
                if (astring == null || astring.length != 2) {
                    continue;
                }
                final String s2 = astring[0];
                final String s3 = Locale.PATTERN.matcher(astring[1]).replaceAll("%$1s");
                this.properties.put(s2, s3);
            }
        }
    }
    
    private String translateKeyPrivate(final String translateKey) {
        final String s = this.properties.get(translateKey);
        return (s == null) ? translateKey : s;
    }
    
    public String formatMessage(final String translateKey, final Object[] parameters) {
        final String s = this.translateKeyPrivate(translateKey);
        try {
            return String.format(s, parameters);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + s;
        }
    }
    
    public boolean hasKey(final String key) {
        return this.properties.containsKey(key);
    }
    
    static {
        SPLITTER = Splitter.on('=').limit(2);
        PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    }
}
