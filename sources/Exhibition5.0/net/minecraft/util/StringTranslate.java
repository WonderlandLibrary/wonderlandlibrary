// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.IllegalFormatException;
import java.util.Iterator;
import java.io.InputStream;
import java.io.IOException;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.Charsets;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.common.base.Splitter;
import java.util.regex.Pattern;

public class StringTranslate
{
    private static final Pattern numericVariablePattern;
    private static final Splitter equalSignSplitter;
    private static StringTranslate instance;
    private final Map languageList;
    private long lastUpdateTimeInMilliseconds;
    private static final String __OBFID = "CL_00001212";
    
    public StringTranslate() {
        this.languageList = Maps.newHashMap();
        try {
            final InputStream var1 = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
            for (final String var3 : IOUtils.readLines(var1, Charsets.UTF_8)) {
                if (!var3.isEmpty() && var3.charAt(0) != '#') {
                    final String[] var4 = (String[])Iterables.toArray(StringTranslate.equalSignSplitter.split((CharSequence)var3), (Class)String.class);
                    if (var4 == null || var4.length != 2) {
                        continue;
                    }
                    final String var5 = var4[0];
                    final String var6 = StringTranslate.numericVariablePattern.matcher(var4[1]).replaceAll("%$1s");
                    this.languageList.put(var5, var6);
                }
            }
            this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
        }
        catch (IOException ex) {}
    }
    
    static StringTranslate getInstance() {
        return StringTranslate.instance;
    }
    
    public static synchronized void replaceWith(final Map p_135063_0_) {
        StringTranslate.instance.languageList.clear();
        StringTranslate.instance.languageList.putAll(p_135063_0_);
        StringTranslate.instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }
    
    public synchronized String translateKey(final String p_74805_1_) {
        return this.tryTranslateKey(p_74805_1_);
    }
    
    public synchronized String translateKeyFormat(final String p_74803_1_, final Object... p_74803_2_) {
        final String var3 = this.tryTranslateKey(p_74803_1_);
        try {
            return String.format(var3, p_74803_2_);
        }
        catch (IllegalFormatException var4) {
            return "Format error: " + var3;
        }
    }
    
    private String tryTranslateKey(final String p_135064_1_) {
        final String var2 = this.languageList.get(p_135064_1_);
        return (var2 == null) ? p_135064_1_ : var2;
    }
    
    public synchronized boolean isKeyTranslated(final String p_94520_1_) {
        return this.languageList.containsKey(p_94520_1_);
    }
    
    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
    
    static {
        numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
        equalSignSplitter = Splitter.on('=').limit(2);
        StringTranslate.instance = new StringTranslate();
    }
}
