// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import java.util.Base64;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import dev.tenacity.module.Module;
import java.util.List;

public class StringUtils
{
    public static String findLongestModuleName(final List<Module> modules) {
        String string;
        final StringBuilder sb;
        return Collections.max((Collection<? extends Module>)modules, Comparator.comparing(module -> {
            new StringBuilder().append(module.getName());
            if (module.hasMode()) {
                string = " " + module.getSuffix();
            }
            else {
                string = "";
            }
            return Integer.valueOf(sb.append(string).toString().length());
        })).getName();
    }
    
    public static String getLongestModeName(final List<String> listOfWords) {
        String longestWord = null;
        for (final String word : listOfWords) {
            if (longestWord == null || word.length() > longestWord.length()) {
                longestWord = word;
            }
        }
        return (longestWord != null) ? longestWord : "";
    }
    
    public static String b64(final Object o) {
        return Base64.getEncoder().encodeToString(String.valueOf(o).getBytes());
    }
}
