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
    private static final Splitter splitter = Splitter.on((char)'=').limit(2);
    private static final Pattern pattern = Pattern.compile((String)"%(\\d+\\$)?[\\d\\.]*[df]");
    Map<String, String> properties = Maps.newHashMap();
    private boolean unicode;

    public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List<String> languageList) {
        this.properties.clear();
        for (String s : languageList) {
            String s1 = String.format((String)"lang/%s.lang", (Object[])new Object[]{s});
            for (String s2 : resourceManager.getResourceDomains()) {
                try {
                    this.loadLocaleData((List<IResource>)resourceManager.getAllResources(new ResourceLocation(s2, s1)));
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
    private void loadLocaleData(List<IResource> resourcesList) throws IOException {
        for (IResource iresource : resourcesList) {
            InputStream inputstream = iresource.getInputStream();
            try {
                this.loadLocaleData(inputstream);
            }
            finally {
                IOUtils.closeQuietly((InputStream)inputstream);
            }
        }
    }

    private void loadLocaleData(InputStream inputStreamIn) throws IOException {
        for (String s : IOUtils.readLines((InputStream)inputStreamIn, (Charset)Charsets.UTF_8)) {
            String[] astring;
            if (s.isEmpty() || s.charAt(0) == '#' || (astring = (String[])Iterables.toArray((Iterable)splitter.split((CharSequence)s), String.class)) == null || astring.length != 2) continue;
            String s1 = astring[0];
            String s2 = pattern.matcher((CharSequence)astring[1]).replaceAll("%$1s");
            this.properties.put((Object)s1, (Object)s2);
        }
    }

    private String translateKeyPrivate(String translateKey) {
        String s = (String)this.properties.get((Object)translateKey);
        return s == null ? translateKey : s;
    }

    public String formatMessage(String translateKey, Object[] parameters) {
        String s = this.translateKeyPrivate(translateKey);
        try {
            return String.format((String)s, (Object[])parameters);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + s;
        }
    }
}
