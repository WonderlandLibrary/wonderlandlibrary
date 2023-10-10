// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.base.Objects;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.util.Set;
import me.gong.mcleaks.util.google.j2objc.annotations.RetainedWith;
import java.util.Map;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(emulated = true)
abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable
{
    private transient Map<K, V> delegate;
    @RetainedWith
    transient AbstractBiMap<V, K> inverse;
    private transient Set<K> keySet;
    private transient Set<V> valueSet;
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
    
    @CanIgnoreReturnValue
    K checkKey(@Nullable final K key) {
        return key;
    }
    
    @CanIgnoreReturnValue
    V checkValue(@Nullable final V value) {
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
    public boolean containsValue(@Nullable final Object value) {
        return this.inverse.containsKey(value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public V put(@Nullable final K key, @Nullable final V value) {
        return this.putInBothMaps(key, value, false);
    }
    
    @CanIgnoreReturnValue
    @Override
    public V forcePut(@Nullable final K key, @Nullable final V value) {
        return this.putInBothMaps(key, value, true);
    }
    
    private V putInBothMaps(@Nullable final K key, @Nullable final V value, final boolean force) {
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
    
    private void updateInverseMap(final K key, final boolean containedKey, final V oldValue, final V newValue) {
        if (containedKey) {
            this.removeFromInverseMap(oldValue);
        }
        this.inverse.delegate.put((K)newValue, (V)key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public V remove(@Nullable final Object key) {
        return this.containsKey(key) ? this.removeFromBothMaps(key) : null;
    }
    
    @CanIgnoreReturnValue
    private V removeFromBothMaps(final Object key) {
        final V oldValue = this.delegate.remove(key);
        this.removeFromInverseMap(oldValue);
        return oldValue;
    }
    
    private void removeFromInverseMap(final V oldValue) {
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
            throw new IllegalArgumentException("value already present: " + broken.getValue());
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
                CollectPreconditions.checkRemove(this.entry != null);
                final V value = this.entry.getValue();
                iterator.remove();
                AbstractBiMap.this.removeFromInverseMap(value);
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
        public boolean remove(final Object key) {
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
        public boolean remove(final Object object) {
            if (!this.esDelegate.contains(object)) {
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
            return this.standardToArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.standardToArray(array);
        }
        
        @Override
        public boolean contains(final Object o) {
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
        
        @Override
        K checkKey(final K key) {
            return this.inverse.checkValue(key);
        }
        
        @Override
        V checkValue(final V value) {
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
