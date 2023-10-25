/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.util.HashSet;
import java.util.Set;

public final class SetUtils {
    private SetUtils() {
    }

    public static String[] prefixSet(Set<String> set, String prefix) {
        HashSet<String> prefixSet = new HashSet<String>();
        for (String str : set) {
            if (!str.startsWith(prefix)) continue;
            prefixSet.add(str);
        }
        return prefixSet.toArray(new String[prefixSet.size()]);
    }
}

