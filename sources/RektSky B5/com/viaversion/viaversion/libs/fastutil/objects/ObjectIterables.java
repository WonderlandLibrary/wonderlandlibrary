/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

public final class ObjectIterables {
    private ObjectIterables() {
    }

    public static <K> long size(Iterable<K> iterable) {
        long c2 = 0L;
        for (K dummy : iterable) {
            ++c2;
        }
        return c2;
    }
}

