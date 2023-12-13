/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class StringTranslate {
    private static final Pattern numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter equalSignSplitter = Splitter.on('=').limit(2);
    private static StringTranslate instance = new StringTranslate();
    private final Map<String, String> languageList = Maps.newHashMap();
    private long lastUpdateTimeInMilliseconds;

    public StringTranslate() {
        try {
            InputStream inputstream = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
            for (String s2 : IOUtils.readLines(inputstream, Charsets.UTF_8)) {
                String[] astring;
                if (s2.isEmpty() || s2.charAt(0) == '#' || (astring = Iterables.toArray(equalSignSplitter.split(s2), String.class)) == null || astring.length != 2) continue;
                String s1 = astring[0];
                String s22 = numericVariablePattern.matcher(astring[1]).replaceAll("%$1s");
                this.languageList.put(s1, s22);
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

    public static synchronized void replaceWith(Map<String, String> p_135063_0_) {
        StringTranslate.instance.languageList.clear();
        StringTranslate.instance.languageList.putAll(p_135063_0_);
        StringTranslate.instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public synchronized String translateKey(String key) {
        return this.tryTranslateKey(key);
    }

    public synchronized String translateKeyFormat(String key, Object ... format) {
        String s2 = this.tryTranslateKey(key);
        try {
            return String.format(s2, format);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + s2;
        }
    }

    private String tryTranslateKey(String key) {
        String s2 = this.languageList.get(key);
        return s2 == null ? key : s2;
    }

    public synchronized boolean isKeyTranslated(String key) {
        return this.languageList.containsKey(key);
    }

    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
}

