/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class Strings {
    public static final String EMPTY = "";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Strings() {
        throw new UnsupportedOperationException();
    }

    public static String repeat(char ch, int count) {
        StringBuilder buffer = new StringBuilder();
        for (int i2 = 0; i2 < count; ++i2) {
            buffer.append(ch);
        }
        return buffer.toString();
    }

    public static boolean isNullOrEmpty(String target) {
        return target == null || EMPTY.equals(target);
    }

    public static String surround(String target, char begin, char end) {
        return begin + target + end;
    }

    public static String join(String[] pieces, String separator) {
        return Strings.join(Arrays.asList(pieces), separator);
    }

    public static String join(List<String> pieces, String separator) {
        StringBuilder buffer = new StringBuilder();
        Iterator<String> iter = pieces.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (!iter.hasNext()) continue;
            buffer.append(separator);
        }
        return buffer.toString();
    }
}

