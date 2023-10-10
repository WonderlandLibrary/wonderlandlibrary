// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractObject2ObjectSortedMap<K, V> extends AbstractObject2ObjectMap<K, V> implements Object2ObjectSortedMap<K, V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2ObjectSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractObject2ObjectSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2ObjectSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2ObjectSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractObject2ObjectSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractObject2ObjectSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractObject2ObjectSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K, Object>((ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, ?>>)AbstractObject2ObjectSortedMap.this.object2ObjectEntrySet().iterator((Object2ObjectMap.Entry<K, V>)new BasicEntry<Object, Object>((K)from, null)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K, Object>((ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, ?>>)Object2ObjectSortedMaps.fastIterator((Object2ObjectSortedMap<K, V>)AbstractObject2ObjectSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K, V> implements ObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> i) {
            this.i = i;
        }
        
        @Override
        public K next() {
            return this.i.next().getKey();
        }
        
        @Override
        public K previous() {
            return this.i.previous().getKey();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }
    
    protected class ValuesCollection extends AbstractObjectCollection<V>
    {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<Object, V>((ObjectBidirectionalIterator<Object2ObjectMap.Entry<?, V>>)Object2ObjectSortedMaps.fastIterator((Object2ObjectSortedMap<K, V>)AbstractObject2ObjectSortedMap.this));
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractObject2ObjectSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2ObjectSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K, V> implements ObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> i) {
            this.i = i;
        }
        
        @Override
        public V next() {
            return this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
