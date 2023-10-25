/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.cache.RemovalNotification;

@GwtCompatible
public interface RemovalListener<K, V> {
    public void onRemoval(RemovalNotification<K, V> var1);
}

