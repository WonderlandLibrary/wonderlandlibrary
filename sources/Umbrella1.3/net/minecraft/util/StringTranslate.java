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
package net.minecraft.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class StringTranslate {
    private static final Pattern numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter equalSignSplitter = Splitter.on((char)'=').limit(2);
    private static StringTranslate instance = new StringTranslate();
    private final Map languageList = Maps.newHashMap();
    private long lastUpdateTimeInMilliseconds;
    private static final String __OBFID = "CL_00001212";

    public StringTranslate() {
        try {
            InputStream var1 = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
            for (String var3 : IOUtils.readLines((InputStream)var1, (Charset)Charsets.UTF_8)) {
                String[] var4;
                if (var3.isEmpty() || var3.charAt(0) == '#' || (var4 = (String[])Iterables.toArray((Iterable)equalSignSplitter.split((CharSequence)var3), String.class)) == null || var4.length != 2) continue;
                String var5 = var4[0];
                String var6 = numericVariablePattern.matcher(var4[1]).replaceAll("%$1s");
                this.languageList.put(var5, var6);
            }
            this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    static StringTranslate getInstance() {
        return instance;
    }

    public static synchronized void replaceWith(Map p_135063_0_) {
        StringTranslate.instance.languageList.clear();
        StringTranslate.instance.languageList.putAll(p_135063_0_);
        StringTranslate.instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public synchronized String translateKey(String p_74805_1_) {
        return this.tryTranslateKey(p_74805_1_);
    }

    public synchronized String translateKeyFormat(String p_74803_1_, Object ... p_74803_2_) {
        String var3 = this.tryTranslateKey(p_74803_1_);
        try {
            return String.format(var3, p_74803_2_);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + var3;
        }
    }

    private String tryTranslateKey(String p_135064_1_) {
        String var2 = (String)this.languageList.get(p_135064_1_);
        return var2 == null ? p_135064_1_ : var2;
    }

    public synchronized boolean isKeyTranslated(String p_94520_1_) {
        return this.languageList.containsKey(p_94520_1_);
    }

    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
}

