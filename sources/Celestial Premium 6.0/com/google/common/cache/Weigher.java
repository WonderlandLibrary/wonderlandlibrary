/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Weigher<K, V> {
    public int weigh(K var1, V var2);
}

