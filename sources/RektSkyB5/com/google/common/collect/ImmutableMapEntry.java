/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableEntry;
import javax.annotation.Nullable;

@GwtIncompatible(value="unnecessary")
abstract class ImmutableMapEntry<K, V>
extends ImmutableEntry<K, V> {
    ImmutableMapEntry(K key, V value) {
        super(key, value);
        CollectPreconditions.checkEntryNotNull(key, value);
    }

    ImmutableMapEntry(ImmutableMapEntry<K, V> contents) {
        super(contents.getKey(), contents.getValue());
    }

    @Nullable
    abstract ImmutableMapEntry<K, V> getNextInKeyBucket();

    @Nullable
    abstract ImmutableMapEntry<K, V> getNextInValueBucket();

    static final class TerminalEntry<K, V>
    extends ImmutableMapEntry<K, V> {
        TerminalEntry(ImmutableMapEntry<K, V> contents) {
            super(contents);
        }

        TerminalEntry(K key, V value) {
            super(key, value);
        }

        @Override
        @Nullable
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return null;
        }

        @Override
        @Nullable
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return null;
        }
    }
}

