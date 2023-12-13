/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WeakIdentityHashMap
implements Map {
    private final ReferenceQueue queue = new ReferenceQueue();
    private Map backingStore = new HashMap();

    public void clear() {
        this.backingStore.clear();
        this.reap();
    }

    public boolean containsKey(Object key) {
        this.reap();
        return this.backingStore.containsKey(new IdentityWeakReference(key));
    }

    public boolean containsValue(Object value) {
        this.reap();
        return this.backingStore.containsValue(value);
    }

    public Set entrySet() {
        this.reap();
        HashSet<1> ret = new HashSet<1>();
        Iterator i2 = this.backingStore.entrySet().iterator();
        while (i2.hasNext()) {
            Map.Entry ref = i2.next();
            final Object key = ((IdentityWeakReference)ref.getKey()).get();
            final Object value = ref.getValue();
            Map.Entry entry = new Map.Entry(){

                public Object getKey() {
                    return key;
                }

                public Object getValue() {
                    return value;
                }

                public Object setValue(Object value2) {
                    throw new UnsupportedOperationException();
                }
            };
            ret.add(entry);
        }
        return Collections.unmodifiableSet(ret);
    }

    public Set keySet() {
        this.reap();
        HashSet ret = new HashSet();
        Iterator i2 = this.backingStore.keySet().iterator();
        while (i2.hasNext()) {
            IdentityWeakReference ref = (IdentityWeakReference)i2.next();
            ret.add(ref.get());
        }
        return Collections.unmodifiableSet(ret);
    }

    public boolean equals(Object o2) {
        return ((Object)this.backingStore).equals(((WeakIdentityHashMap)o2).backingStore);
    }

    public Object get(Object key) {
        this.reap();
        return this.backingStore.get(new IdentityWeakReference(key));
    }

    public Object put(Object key, Object value) {
        this.reap();
        return this.backingStore.put(new IdentityWeakReference(key), value);
    }

    public int hashCode() {
        this.reap();
        return ((Object)this.backingStore).hashCode();
    }

    public boolean isEmpty() {
        this.reap();
        return this.backingStore.isEmpty();
    }

    public void putAll(Map t2) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        this.reap();
        return this.backingStore.remove(new IdentityWeakReference(key));
    }

    public int size() {
        this.reap();
        return this.backingStore.size();
    }

    public Collection values() {
        this.reap();
        return this.backingStore.values();
    }

    private synchronized void reap() {
        Reference zombie = this.queue.poll();
        while (zombie != null) {
            IdentityWeakReference victim = (IdentityWeakReference)zombie;
            this.backingStore.remove(victim);
            zombie = this.queue.poll();
        }
    }

    class IdentityWeakReference
    extends WeakReference {
        int hash;

        IdentityWeakReference(Object obj) {
            super(obj, WeakIdentityHashMap.this.queue);
            this.hash = System.identityHashCode(obj);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            IdentityWeakReference ref = (IdentityWeakReference)o2;
            return this.get() == ref.get();
        }
    }
}

