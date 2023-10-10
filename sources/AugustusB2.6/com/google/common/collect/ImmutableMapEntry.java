// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
class ImmutableMapEntry<K, V> extends ImmutableEntry<K, V>
{
    static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(final int size) {
        return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[size];
    }
    
    ImmutableMapEntry(final K key, final V value) {
        super(key, value);
        CollectPreconditions.checkEntryNotNull(key, value);
    }
    
    ImmutableMapEntry(final ImmutableMapEntry<K, V> contents) {
        super(contents.getKey(), contents.getValue());
    }
    
    @CheckForNull
    ImmutableMapEntry<K, V> getNextInKeyBucket() {
        return null;
    }
    
    @CheckForNull
    ImmutableMapEntry<K, V> getNextInValueBucket() {
        return null;
    }
    
    boolean isReusable() {
        return true;
    }
    
    static class NonTerminalImmutableMapEntry<K, V> extends ImmutableMapEntry<K, V>
    {
        @CheckForNull
        private final transient ImmutableMapEntry<K, V> nextInKeyBucket;
        
        NonTerminalImmutableMapEntry(final K key, final V value, @CheckForNull final ImmutableMapEntry<K, V> nextInKeyBucket) {
            super(key, value);
            this.nextInKeyBucket = nextInKeyBucket;
        }
        
        @CheckForNull
        @Override
        final ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }
        
        @Override
        final boolean isReusable() {
            return false;
        }
    }
    
    static final class NonTerminalImmutableBiMapEntry<K, V> extends NonTerminalImmutableMapEntry<K, V>
    {
        @CheckForNull
        private final transient ImmutableMapEntry<K, V> nextInValueBucket;
        
        NonTerminalImmutableBiMapEntry(final K key, final V value, @CheckForNull final ImmutableMapEntry<K, V> nextInKeyBucket, @CheckForNull final ImmutableMapEntry<K, V> nextInValueBucket) {
            super(key, value, nextInKeyBucket);
            this.nextInValueBucket = nextInValueBucket;
        }
        
        @CheckForNull
        @Override
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return this.nextInValueBucket;
        }
    }
}
