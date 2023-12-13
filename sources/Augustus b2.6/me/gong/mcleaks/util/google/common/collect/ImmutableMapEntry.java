// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

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
    
    @Nullable
    ImmutableMapEntry<K, V> getNextInKeyBucket() {
        return null;
    }
    
    @Nullable
    ImmutableMapEntry<K, V> getNextInValueBucket() {
        return null;
    }
    
    boolean isReusable() {
        return true;
    }
    
    static class NonTerminalImmutableMapEntry<K, V> extends ImmutableMapEntry<K, V>
    {
        private final transient ImmutableMapEntry<K, V> nextInKeyBucket;
        
        NonTerminalImmutableMapEntry(final K key, final V value, final ImmutableMapEntry<K, V> nextInKeyBucket) {
            super(key, value);
            this.nextInKeyBucket = nextInKeyBucket;
        }
        
        @Nullable
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
        private final transient ImmutableMapEntry<K, V> nextInValueBucket;
        
        NonTerminalImmutableBiMapEntry(final K key, final V value, final ImmutableMapEntry<K, V> nextInKeyBucket, final ImmutableMapEntry<K, V> nextInValueBucket) {
            super(key, value, nextInKeyBucket);
            this.nextInValueBucket = nextInValueBucket;
        }
        
        @Nullable
        @Override
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return this.nextInValueBucket;
        }
    }
}
