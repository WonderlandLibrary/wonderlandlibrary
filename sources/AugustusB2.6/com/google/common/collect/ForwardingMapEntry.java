// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingMapEntry<K, V> extends ForwardingObject implements Map.Entry<K, V>
{
    protected ForwardingMapEntry() {
    }
    
    @Override
    protected abstract Map.Entry<K, V> delegate();
    
    @ParametricNullness
    @Override
    public K getKey() {
        return this.delegate().getKey();
    }
    
    @ParametricNullness
    @Override
    public V getValue() {
        return this.delegate().getValue();
    }
    
    @ParametricNullness
    @Override
    public V setValue(@ParametricNullness final V value) {
        return this.delegate().setValue(value);
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return this.delegate().equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    protected boolean standardEquals(@CheckForNull final Object object) {
        if (object instanceof Map.Entry) {
            final Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
            return Objects.equal(this.getKey(), that.getKey()) && Objects.equal(this.getValue(), that.getValue());
        }
        return false;
    }
    
    protected int standardHashCode() {
        final K k = this.getKey();
        final V v = this.getValue();
        return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
    }
    
    @Beta
    protected String standardToString() {
        final String value = String.valueOf(this.getKey());
        final String value2 = String.valueOf(this.getValue());
        return new StringBuilder(1 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append("=").append(value2).toString();
    }
}
