// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.Iterator;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;
import java.util.Set;
import com.google.j2objc.annotations.RetainedWith;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable
{
    private transient Map<K, V> delegate;
    @RetainedWith
    transient AbstractBiMap<V, K> inverse;
    @CheckForNull
    private transient Set<K> keySet;
    @CheckForNull
    private transient Set<V> valueSet;
    @CheckForNull
    private transient Set<Map.Entry<K, V>> entrySet;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    AbstractBiMap(final Map<K, V> forward, final Map<V, K> backward) {
        this.setDelegates(forward, backward);
    }
    
    private AbstractBiMap(final Map<K, V> backward, final AbstractBiMap<V, K> forward) {
        this.delegate = backward;
        this.inverse = forward;
    }
    
    @Override
    protected Map<K, V> delegate() {
        return this.delegate;
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    K checkKey(@ParametricNullness final K key) {
        return key;
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    V checkValue(@ParametricNullness final V value) {
        return value;
    }
    
    void setDelegates(final Map<K, V> forward, final Map<V, K> backward) {
        Preconditions.checkState(this.delegate == null);
        Preconditions.checkState(this.inverse == null);
        Preconditions.checkArgument(forward.isEmpty());
        Preconditions.checkArgument(backward.isEmpty());
        Preconditions.checkArgument(forward != backward);
        this.delegate = forward;
        this.inverse = this.makeInverse(backward);
    }
    
    AbstractBiMap<V, K> makeInverse(final Map<V, K> backward) {
        return new Inverse<V, K>(backward, this);
    }
    
    void setInverse(final AbstractBiMap<V, K> inverse) {
        this.inverse = inverse;
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        return this.inverse.containsKey(value);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V put(@ParametricNullness final K key, @ParametricNullness final V value) {
        return this.putInBothMaps(key, value, false);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V forcePut(@ParametricNullness final K key, @ParametricNullness final V value) {
        return this.putInBothMaps(key, value, true);
    }
    
    @CheckForNull
    private V putInBothMaps(@ParametricNullness final K key, @ParametricNullness final V value, final boolean force) {
        this.checkKey(key);
        this.checkValue(value);
        final boolean containedKey = this.containsKey(key);
        if (containedKey && Objects.equal(value, this.get(key))) {
            return value;
        }
        if (force) {
            this.inverse().remove(value);
        }
        else {
            Preconditions.checkArgument(!this.containsValue(value), "value already present: %s", value);
        }
        final V oldValue = this.delegate.put(key, value);
        this.updateInverseMap(key, containedKey, oldValue, value);
        return oldValue;
    }
    
    private void updateInverseMap(@ParametricNullness final K key, final boolean containedKey, @CheckForNull final V oldValue, @ParametricNullness final V newValue) {
        if (containedKey) {
            this.removeFromInverseMap(NullnessCasts.uncheckedCastNullableTToT(oldValue));
        }
        this.inverse.delegate.put((K)newValue, (V)key);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V remove(@CheckForNull final Object key) {
        return this.containsKey(key) ? this.removeFromBothMaps(key) : null;
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    private V removeFromBothMaps(@CheckForNull final Object key) {
        final V oldValue = NullnessCasts.uncheckedCastNullableTToT(this.delegate.remove(key));
        this.removeFromInverseMap(oldValue);
        return oldValue;
    }
    
    private void removeFromInverseMap(@ParametricNullness final V oldValue) {
        this.inverse.delegate.remove(oldValue);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        this.delegate.replaceAll(function);
        this.inverse.delegate.clear();
        Map.Entry<K, V> broken = null;
        final Iterator<Map.Entry<K, V>> itr = this.delegate.entrySet().iterator();
        while (itr.hasNext()) {
            final Map.Entry<K, V> entry = itr.next();
            final K k = entry.getKey();
            final V v = entry.getValue();
            final K conflict = (K)this.inverse.delegate.putIfAbsent((K)v, (V)k);
            if (conflict != null) {
                broken = entry;
                itr.remove();
            }
        }
        if (broken != null) {
            final String value = String.valueOf(broken.getValue());
            throw new IllegalArgumentException(new StringBuilder(23 + String.valueOf(value).length()).append("value already present: ").append(value).toString());
        }
    }
    
    @Override
    public void clear() {
        this.delegate.clear();
        this.inverse.delegate.clear();
    }
    
    @Override
    public BiMap<V, K> inverse() {
        return (BiMap<V, K>)this.inverse;
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> result = this.keySet;
        return (result == null) ? (this.keySet = new KeySet()) : result;
    }
    
    @Override
    public Set<V> values() {
        final Set<V> result = this.valueSet;
        return (result == null) ? (this.valueSet = new ValueSet()) : result;
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> result = this.entrySet;
        return (result == null) ? (this.entrySet = new EntrySet()) : result;
    }
    
    Iterator<Map.Entry<K, V>> entrySetIterator() {
        final Iterator<Map.Entry<K, V>> iterator = this.delegate.entrySet().iterator();
        return new Iterator<Map.Entry<K, V>>() {
            @CheckForNull
            Map.Entry<K, V> entry;
            
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public Map.Entry<K, V> next() {
                this.entry = iterator.next();
                return new BiMapEntry(this.entry);
            }
            
            @Override
            public void remove() {
                if (this.entry == null) {
                    throw new IllegalStateException("no calls to next() since the last call to remove()");
                }
                final V value = this.entry.getValue();
                iterator.remove();
                AbstractBiMap.this.removeFromInverseMap(value);
                this.entry = null;
            }
        };
    }
    
    private class KeySet extends ForwardingSet<K>
    {
        @Override
        protected Set<K> delegate() {
            return AbstractBiMap.this.delegate.keySet();
        }
        
        @Override
        public void clear() {
            AbstractBiMap.this.clear();
        }
        
        @Override
        public boolean remove(@CheckForNull final Object key) {
            if (!this.contains(key)) {
                return false;
            }
            AbstractBiMap.this.removeFromBothMaps(key);
            return true;
        }
        
        @Override
        public boolean removeAll(final Collection<?> keysToRemove) {
            return this.standardRemoveAll(keysToRemove);
        }
        
        @Override
        public boolean retainAll(final Collection<?> keysToRetain) {
            return this.standardRetainAll(keysToRetain);
        }
        
        @Override
        public Iterator<K> iterator() {
            return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
        }
    }
    
    private class ValueSet extends ForwardingSet<V>
    {
        final Set<V> valuesDelegate;
        
        private ValueSet() {
            this.valuesDelegate = (Set<V>)AbstractBiMap.this.inverse.keySet();
        }
        
        @Override
        protected Set<V> delegate() {
            return this.valuesDelegate;
        }
        
        @Override
        public Iterator<V> iterator() {
            return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
        }
        
        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public String toString() {
            return this.standardToString();
        }
    }
    
    class BiMapEntry extends ForwardingMapEntry<K, V>
    {
        private final Map.Entry<K, V> delegate;
        
        BiMapEntry(final Map.Entry<K, V> delegate) {
            this.delegate = delegate;
        }
        
        @Override
        protected Map.Entry<K, V> delegate() {
            return this.delegate;
        }
        
        @Override
        public V setValue(final V value) {
            AbstractBiMap.this.checkValue(value);
            Preconditions.checkState(AbstractBiMap.this.entrySet().contains(this), (Object)"entry no longer in map");
            if (Objects.equal(value, ((ForwardingMapEntry<K, Object>)this).getValue())) {
                return value;
            }
            Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", value);
            final V oldValue = this.delegate.setValue(value);
            Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(((ForwardingMapEntry<Object, V>)this).getKey())), (Object)"entry no longer in map");
            AbstractBiMap.this.updateInverseMap(((ForwardingMapEntry<Object, V>)this).getKey(), true, oldValue, value);
            return oldValue;
        }
    }
    
    private class EntrySet extends ForwardingSet<Map.Entry<K, V>>
    {
        final Set<Map.Entry<K, V>> esDelegate;
        
        private EntrySet() {
            this.esDelegate = AbstractBiMap.this.delegate.entrySet();
        }
        
        @Override
        protected Set<Map.Entry<K, V>> delegate() {
            return this.esDelegate;
        }
        
        @Override
        public void clear() {
            AbstractBiMap.this.clear();
        }
        
        @Override
        public boolean remove(@CheckForNull final Object object) {
            if (!this.esDelegate.contains(object) || !(object instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
            AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
            this.esDelegate.remove(entry);
            return true;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return AbstractBiMap.this.entrySetIterator();
        }
        
        @Override
        public Object[] toArray() {
            final Object[] result = this.standardToArray();
            return result;
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public boolean contains(@CheckForNull final Object o) {
            return Maps.containsEntryImpl(this.delegate(), o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.standardContainsAll(c);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.standardRemoveAll(c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.standardRetainAll(c);
        }
    }
    
    static class Inverse<K, V> extends AbstractBiMap<K, V>
    {
        @GwtIncompatible
        private static final long serialVersionUID = 0L;
        
        Inverse(final Map<K, V> backward, final AbstractBiMap<V, K> forward) {
            super(backward, (AbstractBiMap<Object, Object>)forward, null);
        }
        
        @ParametricNullness
        @Override
        K checkKey(@ParametricNullness final K key) {
            return this.inverse.checkValue(key);
        }
        
        @ParametricNullness
        @Override
        V checkValue(@ParametricNullness final V value) {
            return this.inverse.checkKey(value);
        }
        
        @GwtIncompatible
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.inverse());
        }
        
        @GwtIncompatible
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.setInverse((AbstractBiMap<V, K>)stream.readObject());
        }
        
        @GwtIncompatible
        Object readResolve() {
            return this.inverse().inverse();
        }
    }
}
