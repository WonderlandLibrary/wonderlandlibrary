// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.scopedpool;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;
import java.lang.ref.ReferenceQueue;
import java.util.Map;

public class SoftValueHashMap<K, V> implements Map<K, V>
{
    private Map<K, SoftValueRef<K, V>> hash;
    private ReferenceQueue<V> queue;
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        this.processQueue();
        final Set<Entry<K, V>> ret = new HashSet<Entry<K, V>>();
        for (final Entry<K, SoftValueRef<K, V>> e : this.hash.entrySet()) {
            ret.add((Entry<K, Object>)new AbstractMap.SimpleImmutableEntry<K, Object>(e.getKey(), e.getValue().get()));
        }
        return ret;
    }
    
    private void processQueue() {
        if (!this.hash.isEmpty()) {
            Object ref;
            while ((ref = this.queue.poll()) != null) {
                if (ref instanceof SoftValueRef) {
                    final SoftValueRef que = (SoftValueRef)ref;
                    if (ref != this.hash.get(que.key)) {
                        continue;
                    }
                    this.hash.remove(que.key);
                }
            }
        }
    }
    
    public SoftValueHashMap(final int initialCapacity, final float loadFactor) {
        this.queue = new ReferenceQueue<V>();
        this.hash = new ConcurrentHashMap<K, SoftValueRef<K, V>>(initialCapacity, loadFactor);
    }
    
    public SoftValueHashMap(final int initialCapacity) {
        this.queue = new ReferenceQueue<V>();
        this.hash = new ConcurrentHashMap<K, SoftValueRef<K, V>>(initialCapacity);
    }
    
    public SoftValueHashMap() {
        this.queue = new ReferenceQueue<V>();
        this.hash = new ConcurrentHashMap<K, SoftValueRef<K, V>>();
    }
    
    public SoftValueHashMap(final Map<K, V> t) {
        this(Math.max(2 * t.size(), 11), 0.75f);
        this.putAll((Map<? extends K, ? extends V>)t);
    }
    
    @Override
    public int size() {
        this.processQueue();
        return this.hash.size();
    }
    
    @Override
    public boolean isEmpty() {
        this.processQueue();
        return this.hash.isEmpty();
    }
    
    @Override
    public boolean containsKey(final Object key) {
        this.processQueue();
        return this.hash.containsKey(key);
    }
    
    @Override
    public V get(final Object key) {
        this.processQueue();
        return this.valueOrNull(this.hash.get(key));
    }
    
    @Override
    public V put(final K key, final V value) {
        this.processQueue();
        return this.valueOrNull(this.hash.put(key, (SoftValueRef<K, V>)create(key, value, this.queue)));
    }
    
    @Override
    public V remove(final Object key) {
        this.processQueue();
        return this.valueOrNull(this.hash.remove(key));
    }
    
    @Override
    public void clear() {
        this.processQueue();
        this.hash.clear();
    }
    
    @Override
    public boolean containsValue(final Object arg0) {
        this.processQueue();
        if (null == arg0) {
            return false;
        }
        for (final SoftValueRef<K, V> e : this.hash.values()) {
            if (null != e && arg0.equals(e.get())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Set<K> keySet() {
        this.processQueue();
        return this.hash.keySet();
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> arg0) {
        this.processQueue();
        for (final K key : arg0.keySet()) {
            this.put(key, arg0.get(key));
        }
    }
    
    @Override
    public Collection<V> values() {
        this.processQueue();
        final List<V> ret = new ArrayList<V>();
        for (final SoftValueRef<K, V> e : this.hash.values()) {
            ret.add((V)e.get());
        }
        return ret;
    }
    
    private V valueOrNull(final SoftValueRef<K, V> rtn) {
        if (null == rtn) {
            return null;
        }
        return (V)rtn.get();
    }
    
    private static class SoftValueRef<K, V> extends SoftReference<V>
    {
        public K key;
        
        private SoftValueRef(final K key, final V val, final ReferenceQueue<V> q) {
            super(val, q);
            this.key = key;
        }
        
        private static <K, V> SoftValueRef<K, V> create(final K key, final V val, final ReferenceQueue<V> q) {
            if (val == null) {
                return null;
            }
            return new SoftValueRef<K, V>(key, val, q);
        }
    }
}
