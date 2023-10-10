/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil;

import java.util.Collection;
import java.util.Map;

public interface Size64 {
    public long size64();

    @Deprecated
    default public int size() {
        return (int)Math.min(Integer.MAX_VALUE, this.size64());
    }

    public static long sizeOf(Collection<?> c2) {
        return c2 instanceof Size64 ? ((Size64)((Object)c2)).size64() : (long)c2.size();
    }

    public static long sizeOf(Map<?, ?> m2) {
        return m2 instanceof Size64 ? ((Size64)((Object)m2)).size64() : (long)m2.size();
    }
}

