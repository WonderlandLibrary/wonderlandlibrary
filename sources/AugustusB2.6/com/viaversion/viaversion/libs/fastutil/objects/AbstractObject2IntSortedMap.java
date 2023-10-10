// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;

public abstract class AbstractObject2IntSortedMap<K> extends AbstractObject2IntMap<K> implements Object2IntSortedMap<K>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2IntSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractObject2IntSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2IntSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractObject2IntSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractObject2IntSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractObject2IntSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2IntMap.Entry<K>>)AbstractObject2IntSortedMap.this.object2IntEntrySet().iterator((Object2IntMap.Entry<K>)new BasicEntry<Object>((K)from, 0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2IntMap.Entry<K>>)Object2IntSortedMaps.fastIterator((Object2IntSortedMap<K>)AbstractObject2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i) {
            this.i = i;
        }
        
        @Override
        public K next() {
            return (K)this.i.next().getKey();
        }
        
        @Override
        public K previous() {
            return (K)this.i.previous().getKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection
    {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator<Object>((ObjectBidirectionalIterator<Object2IntMap.Entry<?>>)Object2IntSortedMaps.fastIterator((Object2IntSortedMap<K>)AbstractObject2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractObject2IntSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements IntIterator
    {
        protected final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i) {
            this.i = i;
        }
        
        @Override
        public int nextInt() {
            return this.i.next().getIntValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
