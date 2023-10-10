// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Map;
import com.google.common.base.Preconditions;
import java.util.function.BiConsumer;
import com.google.j2objc.annotations.RetainedWith;
import com.google.errorprone.annotations.concurrent.LazyInit;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableBiMap<K, V> extends ImmutableBiMap<K, V>
{
    final transient K singleKey;
    final transient V singleValue;
    @CheckForNull
    private final transient ImmutableBiMap<V, K> inverse;
    @LazyInit
    @CheckForNull
    @RetainedWith
    private transient ImmutableBiMap<V, K> lazyInverse;
    
    SingletonImmutableBiMap(final K singleKey, final V singleValue) {
        CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
        this.singleKey = singleKey;
        this.singleValue = singleValue;
        this.inverse = null;
    }
    
    private SingletonImmutableBiMap(final K singleKey, final V singleValue, final ImmutableBiMap<V, K> inverse) {
        this.singleKey = singleKey;
        this.singleValue = singleValue;
        this.inverse = inverse;
    }
    
    @CheckForNull
    @Override
    public V get(@CheckForNull final Object key) {
        return this.singleKey.equals(key) ? this.singleValue : null;
    }
    
    @Override
    public int size() {
        return 1;
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action).accept((Object)this.singleKey, (Object)this.singleValue);
    }
    
    @Override
    public boolean containsKey(@CheckForNull final Object key) {
        return this.singleKey.equals(key);
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        return this.singleValue.equals(value);
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
    }
    
    @Override
    ImmutableSet<K> createKeySet() {
        return ImmutableSet.of(this.singleKey);
    }
    
    @Override
    public ImmutableBiMap<V, K> inverse() {
        if (this.inverse != null) {
            return this.inverse;
        }
        final ImmutableBiMap<V, K> result = this.lazyInverse;
        if (result == null) {
            return this.lazyInverse = (ImmutableBiMap<V, K>)new SingletonImmutableBiMap(this.singleValue, this.singleKey, (ImmutableBiMap<Object, Object>)this);
        }
        return result;
    }
}
