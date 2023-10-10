// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.Map;
import java.util.Collection;

public final class Assert
{
    private Assert() {
    }
    
    public static boolean isEmpty(final Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof CharSequence) {
            return ((CharSequence)o).length() == 0;
        }
        if (o.getClass().isArray()) {
            return ((Object[])o).length == 0;
        }
        if (o instanceof Collection) {
            return ((Collection)o).isEmpty();
        }
        return o instanceof Map && ((Map)o).isEmpty();
    }
    
    public static boolean isNonEmpty(final Object o) {
        return !isEmpty(o);
    }
    
    public static <T> T requireNonEmpty(final T value) {
        return requireNonEmpty(value, "");
    }
    
    public static <T> T requireNonEmpty(final T value, final String message) {
        if (isEmpty(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
    
    public static int valueIsAtLeast(final int value, final int minValue) {
        if (value < minValue) {
            throw new IllegalArgumentException("Value should be at least " + minValue + " but was " + value);
        }
        return value;
    }
}
