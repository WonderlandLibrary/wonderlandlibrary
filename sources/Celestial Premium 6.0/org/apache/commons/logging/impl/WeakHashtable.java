/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.logging.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class WeakHashtable
extends Hashtable {
    private static final long serialVersionUID = -1546036869799732453L;
    private static final int MAX_CHANGES_BEFORE_PURGE = 100;
    private static final int PARTIAL_PURGE_COUNT = 10;
    private final ReferenceQueue queue = new ReferenceQueue();
    private int changeCount = 0;

    public boolean containsKey(Object key) {
        Referenced referenced = new Referenced(key);
        return super.containsKey(referenced);
    }

    public Enumeration elements() {
        this.purge();
        return super.elements();
    }

    public Set entrySet() {
        this.purge();
        Set referencedEntries = super.entrySet();
        HashSet<Entry> unreferencedEntries = new HashSet<Entry>();
        Iterator it = referencedEntries.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            Referenced referencedKey = (Referenced)entry.getKey();
            Object key = referencedKey.getValue();
            Object value = entry.getValue();
            if (key == null) continue;
            Entry dereferencedEntry = new Entry(key, value);
            unreferencedEntries.add(dereferencedEntry);
        }
        return unreferencedEntries;
    }

    public Object get(Object key) {
        Referenced referenceKey = new Referenced(key);
        return super.get(referenceKey);
    }

    public Enumeration keys() {
        this.purge();
        final Enumeration enumer = super.keys();
        return new Enumeration(){

            public boolean hasMoreElements() {
                return enumer.hasMoreElements();
            }

            public Object nextElement() {
                Referenced nextReference = (Referenced)enumer.nextElement();
                return nextReference.getValue();
            }
        };
    }

    public Set keySet() {
        this.purge();
        Set referencedKeys = super.keySet();
        HashSet<Object> unreferencedKeys = new HashSet<Object>();
        Iterator it = referencedKeys.iterator();
        while (it.hasNext()) {
            Referenced referenceKey = (Referenced)it.next();
            Object keyValue = referenceKey.getValue();
            if (keyValue == null) continue;
            unreferencedKeys.add(keyValue);
        }
        return unreferencedKeys;
    }

    public synchronized Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("Null keys are not allowed");
        }
        if (value == null) {
            throw new NullPointerException("Null values are not allowed");
        }
        if (this.changeCount++ > 100) {
            this.purge();
            this.changeCount = 0;
        } else if (this.changeCount % 10 == 0) {
            this.purgeOne();
        }
        Referenced keyRef = new Referenced(key, this.queue);
        return super.put(keyRef, value);
    }

    public void putAll(Map t) {
        if (t != null) {
            Set entrySet = t.entrySet();
            Iterator it = entrySet.iterator();
            while (it.hasNext()) {
                Map.Entry entry = it.next();
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public Collection values() {
        this.purge();
        return super.values();
    }

    public synchronized Object remove(Object key) {
        if (this.changeCount++ > 100) {
            this.purge();
            this.changeCount = 0;
        } else if (this.changeCount % 10 == 0) {
            this.purgeOne();
        }
        return super.remove(new Referenced(key));
    }

    public boolean isEmpty() {
        this.purge();
        return super.isEmpty();
    }

    public int size() {
        this.purge();
        return super.size();
    }

    public String toString() {
        this.purge();
        return super.toString();
    }

    protected void rehash() {
        this.purge();
        super.rehash();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void purge() {
        ArrayList<Referenced> toRemove = new ArrayList<Referenced>();
        ReferenceQueue referenceQueue = this.queue;
        synchronized (referenceQueue) {
            WeakKey key;
            while ((key = (WeakKey)this.queue.poll()) != null) {
                toRemove.add(key.getReferenced());
            }
        }
        int size = toRemove.size();
        for (int i = 0; i < size; ++i) {
            super.remove(toRemove.get(i));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void purgeOne() {
        ReferenceQueue referenceQueue = this.queue;
        synchronized (referenceQueue) {
            WeakKey key = (WeakKey)this.queue.poll();
            if (key != null) {
                super.remove(key.getReferenced());
            }
        }
    }

    private static final class WeakKey
    extends WeakReference {
        private final Referenced referenced;

        private WeakKey(Object key, ReferenceQueue queue, Referenced referenced) {
            super(key, queue);
            this.referenced = referenced;
        }

        private Referenced getReferenced() {
            return this.referenced;
        }
    }

    private static final class Referenced {
        private final WeakReference reference;
        private final int hashCode;

        private Referenced(Object referant) {
            this.reference = new WeakReference<Object>(referant);
            this.hashCode = referant.hashCode();
        }

        private Referenced(Object key, ReferenceQueue queue) {
            this.reference = new WeakKey(key, queue, this);
            this.hashCode = key.hashCode();
        }

        public int hashCode() {
            return this.hashCode;
        }

        private Object getValue() {
            return this.reference.get();
        }

        public boolean equals(Object o) {
            boolean result = false;
            if (o instanceof Referenced) {
                Referenced otherKey = (Referenced)o;
                Object thisKeyValue = this.getValue();
                Object otherKeyValue = otherKey.getValue();
                if (thisKeyValue == null) {
                    result = otherKeyValue == null;
                    result = result && this.hashCode() == otherKey.hashCode();
                } else {
                    result = thisKeyValue.equals(otherKeyValue);
                }
            }
            return result;
        }
    }

    private static final class Entry
    implements Map.Entry {
        private final Object key;
        private final Object value;

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public boolean equals(Object o) {
            boolean result = false;
            if (o != null && o instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry)o;
                result = (this.getKey() == null ? entry.getKey() == null : this.getKey().equals(entry.getKey())) && (this.getValue() == null ? entry.getValue() == null : this.getValue().equals(entry.getValue()));
            }
            return result;
        }

        public int hashCode() {
            return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
        }

        public Object setValue(Object value) {
            throw new UnsupportedOperationException("Entry.setValue is not supported.");
        }

        public Object getValue() {
            return this.value;
        }

        public Object getKey() {
            return this.key;
        }
    }
}

