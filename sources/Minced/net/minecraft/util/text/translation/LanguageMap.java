// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text.translation;

import java.util.IllegalFormatException;
import java.util.Iterator;
import java.io.InputStream;
import java.io.IOException;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.common.base.Splitter;
import java.util.regex.Pattern;

public class LanguageMap
{
    private static final Pattern NUMERIC_VARIABLE_PATTERN;
    private static final Splitter EQUAL_SIGN_SPLITTER;
    private static final LanguageMap instance;
    private final Map<String, String> languageList;
    private long lastUpdateTimeInMilliseconds;
    
    public LanguageMap() {
        this.languageList = (Map<String, String>)Maps.newHashMap();
        try {
            final InputStream inputstream = LanguageMap.class.getResourceAsStream("/assets/minecraft/lang/en_us.lang");
            for (final String s : IOUtils.readLines(inputstream, StandardCharsets.UTF_8)) {
                if (!s.isEmpty() && s.charAt(0) != '#') {
                    final String[] astring = (String[])Iterables.toArray(LanguageMap.EQUAL_SIGN_SPLITTER.split((CharSequence)s), (Class)String.class);
                    if (astring == null || astring.length != 2) {
                        continue;
                    }
                    final String s2 = astring[0];
                    final String s3 = LanguageMap.NUMERIC_VARIABLE_PATTERN.matcher(astring[1]).replaceAll("%$1s");
                    this.languageList.put(s2, s3);
                }
            }
            this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
        }
        catch (IOException ex) {}
    }
    
    static LanguageMap getInstance() {
        return LanguageMap.instance;
    }
    
    public static synchronized void replaceWith(final Map<String, String> p_135063_0_) {
        LanguageMap.instance.languageList.clear();
        LanguageMap.instance.languageList.putAll(p_135063_0_);
        LanguageMap.instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }
    
    public synchronized String translateKey(final String key) {
        return this.tryTranslateKey(key);
    }
    
    public synchronized String translateKeyFormat(final String key, final Object... format) {
        final String s = this.tryTranslateKey(key);
        try {
            return String.format(s, format);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + s;
        }
    }
    
    private String tryTranslateKey(final String key) {
        final String s = this.languageList.get(key);
        return (s == null) ? key : s;
    }
    
    public synchronized boolean isKeyTranslated(final String key) {
        return this.languageList.containsKey(key);
    }
    
    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
    
    static {
        NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
        EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
        instance = new LanguageMap();
    }
}
