// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Map;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingSetMultimap<K, V> extends ForwardingMultimap<K, V> implements SetMultimap<K, V>
{
    @Override
    protected abstract SetMultimap<K, V> delegate();
    
    @Override
    public Set<Map.Entry<K, V>> entries() {
        return this.delegate().entries();
    }
    
    @Override
    public Set<V> get(@ParametricNullness final K key) {
        return this.delegate().get(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public Set<V> removeAll(@CheckForNull final Object key) {
        return this.delegate().removeAll(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public Set<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return this.delegate().replaceValues(key, values);
    }
}
