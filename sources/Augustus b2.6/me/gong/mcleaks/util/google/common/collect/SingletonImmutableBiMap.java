// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Map;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.j2objc.annotations.RetainedWith;
import me.gong.mcleaks.util.google.errorprone.annotations.concurrent.LazyInit;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableBiMap<K, V> extends ImmutableBiMap<K, V>
{
    final transient K singleKey;
    final transient V singleValue;
    @LazyInit
    @RetainedWith
    transient ImmutableBiMap<V, K> inverse;
    
    SingletonImmutableBiMap(final K singleKey, final V singleValue) {
        CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
        this.singleKey = singleKey;
        this.singleValue = singleValue;
    }
    
    private SingletonImmutableBiMap(final K singleKey, final V singleValue, final ImmutableBiMap<V, K> inverse) {
        this.singleKey = singleKey;
        this.singleValue = singleValue;
        this.inverse = inverse;
    }
    
    @Override
    public V get(@Nullable final Object key) {
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
    public boolean containsKey(@Nullable final Object key) {
        return this.singleKey.equals(key);
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
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
        final ImmutableBiMap<V, K> result = this.inverse;
        if (result == null) {
            return this.inverse = (ImmutableBiMap<V, K>)new SingletonImmutableBiMap(this.singleValue, this.singleKey, (ImmutableBiMap<Object, Object>)this);
        }
        return result;
    }
}
