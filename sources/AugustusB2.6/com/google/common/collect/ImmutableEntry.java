// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true)
class ImmutableEntry<K, V> extends AbstractMapEntry<K, V> implements Serializable
{
    @ParametricNullness
    final K key;
    @ParametricNullness
    final V value;
    private static final long serialVersionUID = 0L;
    
    ImmutableEntry(@ParametricNullness final K key, @ParametricNullness final V value) {
        this.key = key;
        this.value = value;
    }
    
    @ParametricNullness
    @Override
    public final K getKey() {
        return this.key;
    }
    
    @ParametricNullness
    @Override
    public final V getValue() {
        return this.value;
    }
    
    @ParametricNullness
    @Override
    public final V setValue(@ParametricNullness final V value) {
        throw new UnsupportedOperationException();
    }
}
