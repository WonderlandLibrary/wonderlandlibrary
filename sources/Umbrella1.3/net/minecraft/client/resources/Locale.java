/*
 * Decompiled with CFR 0.150.
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
    private static final Splitter splitter = Splitter.on((char)'=').limit(2);
    private static final Pattern field_135031_c = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    Map field_135032_a = Maps.newHashMap();
    private boolean field_135029_d;
    private static final String __OBFID = "CL_00001097";

    public synchronized void loadLocaleDataFiles(IResourceManager p_135022_1_, List p_135022_2_) {
        this.field_135032_a.clear();
        for (String var4 : p_135022_2_) {
            String var5 = String.format("lang/%s.lang", var4);
            for (String var7 : p_135022_1_.getResourceDomains()) {
                try {
                    this.loadLocaleData(p_135022_1_.getAllResources(new ResourceLocation(var7, var5)));
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
        this.checkUnicode();
    }

    public boolean isUnicode() {
        return this.field_135029_d;
    }

    private void checkUnicode() {
        this.field_135029_d = false;
        int var1 = 0;
        int var2 = 0;
        for (String var4 : this.field_135032_a.values()) {
            int var5 = var4.length();
            var2 += var5;
            for (int var6 = 0; var6 < var5; ++var6) {
                if (var4.charAt(var6) < '\u0100') continue;
                ++var1;
            }
        }
        float var7 = (float)var1 / (float)var2;
        this.field_135029_d = (double)var7 > 0.1;
    }

    private void loadLocaleData(List p_135028_1_) throws IOException {
        for (IResource var3 : p_135028_1_) {
            InputStream var4 = var3.getInputStream();
            try {
                this.loadLocaleData(var4);
            }
            finally {
                IOUtils.closeQuietly((InputStream)var4);
            }
        }
    }

    private void loadLocaleData(InputStream p_135021_1_) throws IOException {
        for (String var3 : IOUtils.readLines((InputStream)p_135021_1_, (Charset)Charsets.UTF_8)) {
            String[] var4;
            if (var3.isEmpty() || var3.charAt(0) == '#' || (var4 = (String[])Iterables.toArray((Iterable)splitter.split((CharSequence)var3), String.class)) == null || var4.length != 2) continue;
            String var5 = var4[0];
            String var6 = field_135031_c.matcher(var4[1]).replaceAll("%$1s");
            this.field_135032_a.put(var5, var6);
        }
    }

    private String translateKeyPrivate(String p_135026_1_) {
        String var2 = (String)this.field_135032_a.get(p_135026_1_);
        return var2 == null ? p_135026_1_ : var2;
    }

    public String formatMessage(String p_135023_1_, Object[] p_135023_2_) {
        String var3 = this.translateKeyPrivate(p_135023_1_);
        try {
            return String.format(var3, p_135023_2_);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + var3;
        }
    }
}

