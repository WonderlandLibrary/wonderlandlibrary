// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import java.util.Comparator;
import java.util.Iterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;

public abstract class AbstractInt2ObjectSortedMap<V> extends AbstractInt2ObjectMap<V> implements Int2ObjectSortedMap<V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2ObjectSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet
    {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2ObjectSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2ObjectSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2ObjectSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2ObjectSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2ObjectSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2ObjectSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Int2ObjectMap.Entry<?>>)AbstractInt2ObjectSortedMap.this.int2ObjectEntrySet().iterator((Int2ObjectMap.Entry<V>)new BasicEntry<Object>(from, null)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Int2ObjectMap.Entry<?>>)Int2ObjectSortedMaps.fastIterator((Int2ObjectSortedMap<V>)AbstractInt2ObjectSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements IntBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        @Override
        public int nextInt() {
            return this.i.next().getIntKey();
        }
        
        @Override
        public int previousInt() {
            return this.i.previous().getIntKey();
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
            return new ValuesIterator<V>((ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>>)Int2ObjectSortedMaps.fastIterator((Int2ObjectSortedMap<V>)AbstractInt2ObjectSortedMap.this));
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractInt2ObjectSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2ObjectSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        @Override
        public V next() {
            return (V)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
