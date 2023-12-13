/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

import java.util.HashMap;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class Classes {
    private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap(13);

    private Classes() {
        throw new UnsupportedOperationException();
    }

    public static String shortNameOf(String className) {
        return className.substring(className.lastIndexOf(46) + 1);
    }

    public static <T> Class<T> wrapperOf(Class<T> clazz) {
        return clazz.isPrimitive() ? WRAPPERS.get(clazz) : clazz;
    }

    static {
        WRAPPERS.put(Boolean.TYPE, Boolean.class);
        WRAPPERS.put(Byte.TYPE, Byte.class);
        WRAPPERS.put(Character.TYPE, Character.class);
        WRAPPERS.put(Double.TYPE, Double.class);
        WRAPPERS.put(Float.TYPE, Float.class);
        WRAPPERS.put(Integer.TYPE, Integer.class);
        WRAPPERS.put(Long.TYPE, Long.class);
        WRAPPERS.put(Short.TYPE, Short.class);
        WRAPPERS.put(Void.TYPE, Void.class);
    }
}

