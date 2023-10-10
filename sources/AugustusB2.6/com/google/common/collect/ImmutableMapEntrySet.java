// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Spliterator;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
abstract class ImmutableMapEntrySet<K, V> extends CachingAsList<Map.Entry<K, V>>
{
    abstract ImmutableMap<K, V> map();
    
    @Override
    public int size() {
        return this.map().size();
    }
    
    @Override
    public boolean contains(@CheckForNull final Object object) {
        if (object instanceof Map.Entry) {
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
            final V value = this.map().get(entry.getKey());
            return value != null && value.equals(entry.getValue());
        }
        return false;
    }
    
    @Override
    boolean isPartialView() {
        return this.map().isPartialView();
    }
    
    @GwtIncompatible
    @Override
    boolean isHashCodeFast() {
        return this.map().isHashCodeFast();
    }
    
    @Override
    public int hashCode() {
        return this.map().hashCode();
    }
    
    @GwtIncompatible
    @Override
    Object writeReplace() {
        return new EntrySetSerializedForm(this.map());
    }
    
    static final class RegularEntrySet<K, V> extends ImmutableMapEntrySet<K, V>
    {
        private final transient ImmutableMap<K, V> map;
        private final transient ImmutableList<Map.Entry<K, V>> entries;
        
        RegularEntrySet(final ImmutableMap<K, V> map, final Map.Entry<K, V>[] entries) {
            this(map, ImmutableList.asImmutableList(entries));
        }
        
        RegularEntrySet(final ImmutableMap<K, V> map, final ImmutableList<Map.Entry<K, V>> entries) {
            this.map = map;
            this.entries = entries;
        }
        
        @Override
        ImmutableMap<K, V> map() {
            return this.map;
        }
        
        @GwtIncompatible("not used in GWT")
        @Override
        int copyIntoArray(final Object[] dst, final int offset) {
            return this.entries.copyIntoArray(dst, offset);
        }
        
        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.entries.iterator();
        }
        
        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return this.entries.spliterator();
        }
        
        @Override
        public void forEach(final Consumer<? super Map.Entry<K, V>> action) {
            this.entries.forEach(action);
        }
        
        @Override
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new RegularImmutableAsList<Map.Entry<K, V>>((ImmutableCollection<Map.Entry<K, V>>)this, this.entries);
        }
    }
    
    @GwtIncompatible
    private static class EntrySetSerializedForm<K, V> implements Serializable
    {
        final ImmutableMap<K, V> map;
        private static final long serialVersionUID = 0L;
        
        EntrySetSerializedForm(final ImmutableMap<K, V> map) {
            this.map = map;
        }
        
        Object readResolve() {
            return this.map.entrySet();
        }
    }
}
