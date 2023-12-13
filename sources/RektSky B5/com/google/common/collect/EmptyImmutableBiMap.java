/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
final class EmptyImmutableBiMap
extends ImmutableBiMap<Object, Object> {
    static final EmptyImmutableBiMap INSTANCE = new EmptyImmutableBiMap();

    private EmptyImmutableBiMap() {
    }

    @Override
    public ImmutableBiMap<Object, Object> inverse() {
        return this;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Object get(@Nullable Object key) {
        return null;
    }

    @Override
    public ImmutableSet<Map.Entry<Object, Object>> entrySet() {
        return ImmutableSet.of();
    }

    @Override
    ImmutableSet<Map.Entry<Object, Object>> createEntrySet() {
        throw new AssertionError((Object)"should never be called");
    }

    @Override
    public ImmutableSetMultimap<Object, Object> asMultimap() {
        return ImmutableSetMultimap.of();
    }

    @Override
    public ImmutableSet<Object> keySet() {
        return ImmutableSet.of();
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    Object readResolve() {
        return INSTANCE;
    }
}

