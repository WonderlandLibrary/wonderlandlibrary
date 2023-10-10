// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.ConcurrentMap;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingConcurrentMap<K, V> extends ForwardingMap<K, V> implements ConcurrentMap<K, V>
{
    protected ForwardingConcurrentMap() {
    }
    
    @Override
    protected abstract ConcurrentMap<K, V> delegate();
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V putIfAbsent(final K key, final V value) {
        return this.delegate().putIfAbsent(key, value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
        return this.delegate().remove(key, value);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V replace(final K key, final V value) {
        return this.delegate().replace(key, value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        return this.delegate().replace(key, oldValue, newValue);
    }
}
