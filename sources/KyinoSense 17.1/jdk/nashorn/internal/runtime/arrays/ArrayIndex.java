/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class ArrayIndex {
    private static final int INVALID_ARRAY_INDEX = -1;
    private static final long MAX_ARRAY_INDEX = 0xFFFFFFFEL;

    private ArrayIndex() {
    }

    private static long fromString(String key) {
        long value = 0L;
        int length = key.length();
        if (length == 0 || length > 1 && key.charAt(0) == '0') {
            return -1L;
        }
        for (int i = 0; i < length; ++i) {
            char digit = key.charAt(i);
            if (digit < '0' || digit > '9') {
                return -1L;
            }
            if ((value = value * 10L + (long)digit - 48L) <= 0xFFFFFFFEL) continue;
            return -1L;
        }
        return value;
    }

    public static int getArrayIndex(Object key) {
        if (key instanceof Integer) {
            return ArrayIndex.getArrayIndex((Integer)key);
        }
        if (key instanceof Double) {
            return ArrayIndex.getArrayIndex((Double)key);
        }
        if (key instanceof String) {
            return (int)ArrayIndex.fromString((String)key);
        }
        if (key instanceof Long) {
            return ArrayIndex.getArrayIndex((Long)key);
        }
        if (key instanceof ConsString) {
            return (int)ArrayIndex.fromString(key.toString());
        }
        assert (!(key instanceof ScriptObject));
        return -1;
    }

    public static int getArrayIndex(int key) {
        return key >= 0 ? key : -1;
    }

    public static int getArrayIndex(long key) {
        if (key >= 0L && key <= 0xFFFFFFFEL) {
            return (int)key;
        }
        return -1;
    }

    public static int getArrayIndex(double key) {
        if (JSType.isRepresentableAsInt(key)) {
            return ArrayIndex.getArrayIndex((int)key);
        }
        if (JSType.isRepresentableAsLong(key)) {
            return ArrayIndex.getArrayIndex((long)key);
        }
        return -1;
    }

    public static int getArrayIndex(String key) {
        return (int)ArrayIndex.fromString(key);
    }

    public static boolean isValidArrayIndex(int index) {
        return index != -1;
    }

    public static long toLongIndex(int index) {
        return JSType.toUint32(index);
    }

    public static String toKey(int index) {
        return Long.toString(JSType.toUint32(index));
    }
}

