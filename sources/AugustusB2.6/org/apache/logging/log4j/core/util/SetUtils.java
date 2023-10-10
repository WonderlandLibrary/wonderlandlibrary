// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.Set;

public final class SetUtils
{
    private static final String[] EMPTY_STRINGS;
    
    private SetUtils() {
    }
    
    public static String[] prefixSet(final Set<String> set, final String prefix) {
        if (set == null) {
            return SetUtils.EMPTY_STRINGS;
        }
        return set.stream().filter(string -> string.startsWith(prefix)).toArray(String[]::new);
    }
    
    static {
        EMPTY_STRINGS = new String[0];
    }
}
