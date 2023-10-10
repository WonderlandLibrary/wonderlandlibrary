// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.List;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingListMultimap<K, V> extends ForwardingMultimap<K, V> implements ListMultimap<K, V>
{
    protected ForwardingListMultimap() {
    }
    
    @Override
    protected abstract ListMultimap<K, V> delegate();
    
    @Override
    public List<V> get(@ParametricNullness final K key) {
        return this.delegate().get(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public List<V> removeAll(@CheckForNull final Object key) {
        return this.delegate().removeAll(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public List<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return this.delegate().replaceValues(key, values);
    }
}
