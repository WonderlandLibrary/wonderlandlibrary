/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.util;

public final class LangUtils {
    public static final int HASH_SEED = 17;
    public static final int HASH_OFFSET = 37;

    private LangUtils() {
    }

    public static int hashCode(int seed, int hashcode) {
        return seed * 37 + hashcode;
    }

    public static int hashCode(int seed, boolean b2) {
        return LangUtils.hashCode(seed, b2 ? 1 : 0);
    }

    public static int hashCode(int seed, Object obj) {
        return LangUtils.hashCode(seed, obj != null ? obj.hashCode() : 0);
    }

    public static boolean equals(Object obj1, Object obj2) {
        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }

    public static boolean equals(Object[] a1, Object[] a2) {
        if (a1 == null) {
            return a2 == null;
        }
        if (a2 != null && a1.length == a2.length) {
            for (int i2 = 0; i2 < a1.length; ++i2) {
                if (LangUtils.equals(a1[i2], a2[i2])) continue;
                return false;
            }
            return true;
        }
        return false;
    }
}

