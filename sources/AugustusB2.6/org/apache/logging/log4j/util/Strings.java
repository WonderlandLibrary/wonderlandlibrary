// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.function.Supplier;
import java.util.Locale;
import java.util.Objects;
import java.util.Iterator;

public final class Strings
{
    private static final ThreadLocal<StringBuilder> tempStr;
    public static final String EMPTY = "";
    public static final String[] EMPTY_ARRAY;
    public static final String LINE_SEPARATOR;
    
    public static String dquote(final String str) {
        return '\"' + str + '\"';
    }
    
    public static boolean isBlank(final String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
    
    public static boolean isNotBlank(final String s) {
        return !isBlank(s);
    }
    
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
    
    public static String join(final Iterable<?> iterable, final char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }
    
    public static String join(final Iterator<?> iterator, final char separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, "");
        }
        final StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            buf.append(separator);
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }
    
    public static String left(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }
    
    public static String quote(final String str) {
        return '\'' + str + '\'';
    }
    
    public static String trimToNull(final String str) {
        final String ts = (str == null) ? null : str.trim();
        return isEmpty(ts) ? null : ts;
    }
    
    private Strings() {
    }
    
    public static String toRootUpperCase(final String str) {
        return str.toUpperCase(Locale.ROOT);
    }
    
    public static String concat(final String str1, final String str2) {
        if (isEmpty(str1)) {
            return str2;
        }
        if (isEmpty(str2)) {
            return str1;
        }
        final StringBuilder sb = Strings.tempStr.get();
        try {
            return sb.append(str1).append(str2).toString();
        }
        finally {
            sb.setLength(0);
        }
    }
    
    public static String repeat(final String str, final int count) {
        Objects.requireNonNull(str, "str");
        if (count < 0) {
            throw new IllegalArgumentException("count");
        }
        final StringBuilder sb = Strings.tempStr.get();
        try {
            for (int index = 0; index < count; ++index) {
                sb.append(str);
            }
            return sb.toString();
        }
        finally {
            sb.setLength(0);
        }
    }
    
    static {
        tempStr = ThreadLocal.withInitial((Supplier<? extends StringBuilder>)StringBuilder::new);
        EMPTY_ARRAY = new String[0];
        LINE_SEPARATOR = PropertiesUtil.getProperties().getStringProperty("line.separator", "\n");
    }
}
