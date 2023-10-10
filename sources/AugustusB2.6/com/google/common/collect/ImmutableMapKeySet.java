// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import java.util.Map;
import javax.annotation.CheckForNull;
import java.util.Spliterator;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
final class ImmutableMapKeySet<K, V> extends IndexedImmutableSet<K>
{
    private final ImmutableMap<K, V> map;
    
    ImmutableMapKeySet(final ImmutableMap<K, V> map) {
        this.map = map;
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public UnmodifiableIterator<K> iterator() {
        return this.map.keyIterator();
    }
    
    @Override
    public Spliterator<K> spliterator() {
        return this.map.keySpliterator();
    }
    
    @Override
    public boolean contains(@CheckForNull final Object object) {
        return this.map.containsKey(object);
    }
    
    @Override
    K get(final int index) {
        return this.map.entrySet().asList().get(index).getKey();
    }
    
    @Override
    public void forEach(final Consumer<? super K> action) {
        Preconditions.checkNotNull(action);
        this.map.forEach((k, v) -> action.accept((Object)k));
    }
    
    @Override
    boolean isPartialView() {
        return true;
    }
    
    @GwtIncompatible
    private static class KeySetSerializedForm<K> implements Serializable
    {
        final ImmutableMap<K, ?> map;
        private static final long serialVersionUID = 0L;
        
        KeySetSerializedForm(final ImmutableMap<K, ?> map) {
            this.map = map;
        }
        
        Object readResolve() {
            return this.map.keySet();
        }
    }
}
