/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Weigher;

enum SingletonWeigher implements Weigher<Object, Object>
{
    INSTANCE;


    @Override
    public int weigh(Object key, Object value) {
        return 1;
    }
}

