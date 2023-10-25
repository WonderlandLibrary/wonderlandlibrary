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
    private static StringTranslate instance;
    private long lastUpdateTimeInMilliseconds;
    private static final Splitter equalSignSplitter;
    private static final Pattern numericVariablePattern;
    private final Map<String, String> languageList = Maps.newHashMap();

    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }

    public synchronized String translateKeyFormat(String string, Object ... objectArray) {
        String string2 = this.tryTranslateKey(string);
        try {
            return String.format(string2, objectArray);
        }
        catch (IllegalFormatException illegalFormatException) {
            return "Format error: " + string2;
        }
    }

    public static synchronized void replaceWith(Map<String, String> map) {
        StringTranslate.instance.languageList.clear();
        StringTranslate.instance.languageList.putAll(map);
        StringTranslate.instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public synchronized String translateKey(String string) {
        return this.tryTranslateKey(string);
    }

    static {
        numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
        equalSignSplitter = Splitter.on((char)'=').limit(2);
        instance = new StringTranslate();
    }

    private String tryTranslateKey(String string) {
        String string2 = this.languageList.get(string);
        return string2 == null ? string : string2;
    }

    public StringTranslate() {
        try {
            InputStream inputStream = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
            for (String string : IOUtils.readLines((InputStream)inputStream, (Charset)Charsets.UTF_8)) {
                String[] stringArray;
                if (string.isEmpty() || string.charAt(0) == '#' || (stringArray = (String[])Iterables.toArray((Iterable)equalSignSplitter.split((CharSequence)string), String.class)) == null || stringArray.length != 2) continue;
                String string2 = stringArray[0];
                String string3 = numericVariablePattern.matcher(stringArray[1]).replaceAll("%$1s");
                this.languageList.put(string2, string3);
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

    public synchronized boolean isKeyTranslated(String string) {
        return this.languageList.containsKey(string);
    }
}

