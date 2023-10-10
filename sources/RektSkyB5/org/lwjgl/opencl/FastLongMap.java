/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opencl;

import java.util.Iterator;

final class FastLongMap<V>
implements Iterable<Entry<V>> {
    private Entry[] table;
    private int size;
    private int mask;
    private int capacity;
    private int threshold;

    FastLongMap() {
        this(16, 0.75f);
    }

    FastLongMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    FastLongMap(int initialCapacity, float loadFactor) {
        if (initialCapacity > 0x40000000) {
            throw new IllegalArgumentException("initialCapacity is too large.");
        }
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be greater than zero.");
        }
        if (loadFactor <= 0.0f) {
            throw new IllegalArgumentException("initialCapacity must be greater than zero.");
        }
        this.capacity = 1;
        while (this.capacity < initialCapacity) {
            this.capacity <<= 1;
        }
        this.threshold = (int)((float)this.capacity * loadFactor);
        this.table = new Entry[this.capacity];
        this.mask = this.capacity - 1;
    }

    private int index(long key) {
        return FastLongMap.index(key, this.mask);
    }

    private static int index(long key, int mask) {
        int hash = (int)(key ^ key >>> 32);
        return hash & mask;
    }

    public V put(long key, V value) {
        Entry[] table = this.table;
        int index = this.index(key);
        Entry e2 = table[index];
        while (e2 != null) {
            if (e2.key == key) {
                Object oldValue = e2.value;
                e2.value = value;
                return (V)oldValue;
            }
            e2 = e2.next;
        }
        table[index] = new Entry<V>(key, value, table[index]);
        if (this.size++ >= this.threshold) {
            this.rehash(table);
        }
        return null;
    }

    private void rehash(Entry<V>[] table) {
        int newCapacity = 2 * this.capacity;
        int newMask = newCapacity - 1;
        Entry[] newTable = new Entry[newCapacity];
        for (int i2 = 0; i2 < table.length; ++i2) {
            Entry next;
            Entry<Object> e2 = table[i2];
            if (e2 == null) continue;
            do {
                next = e2.next;
                int index = FastLongMap.index(e2.key, newMask);
                e2.next = newTable[index];
                newTable[index] = e2;
            } while ((e2 = next) != null);
        }
        this.table = newTable;
        this.capacity = newCapacity;
        this.mask = newMask;
        this.threshold *= 2;
    }

    public V get(long key) {
        int index = this.index(key);
        Entry e2 = this.table[index];
        while (e2 != null) {
            if (e2.key == key) {
                return (V)e2.value;
            }
            e2 = e2.next;
        }
        return null;
    }

    public boolean containsValue(Object value) {
        Entry[] table = this.table;
        for (int i2 = table.length - 1; i2 >= 0; --i2) {
            Entry e2 = table[i2];
            while (e2 != null) {
                if (e2.value.equals(value)) {
                    return true;
                }
                e2 = e2.next;
            }
        }
        return false;
    }

    public boolean containsKey(long key) {
        int index = this.index(key);
        Entry e2 = this.table[index];
        while (e2 != null) {
            if (e2.key == key) {
                return true;
            }
            e2 = e2.next;
        }
        return false;
    }

    public V remove(long key) {
        Entry prev;
        int index = this.index(key);
        Entry e2 = prev = this.table[index];
        while (e2 != null) {
            Entry next = e2.next;
            if (e2.key == key) {
                --this.size;
                if (prev == e2) {
                    this.table[index] = next;
                } else {
                    prev.next = next;
                }
                return (V)e2.value;
            }
            prev = e2;
            e2 = next;
        }
        return null;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void clear() {
        Entry[] table = this.table;
        for (int index = table.length - 1; index >= 0; --index) {
            table[index] = null;
        }
        this.size = 0;
    }

    public EntryIterator iterator() {
        return new EntryIterator();
    }

    static final class Entry<T> {
        final long key;
        T value;
        Entry<T> next;

        Entry(long key, T value, Entry<T> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public long getKey() {
            return this.key;
        }

        public T getValue() {
            return this.value;
        }
    }

    public class EntryIterator
    implements Iterator<Entry<V>> {
        private int nextIndex;
        private Entry<V> current;

        EntryIterator() {
            this.reset();
        }

        public void reset() {
            int i2;
            this.current = null;
            Entry[] table = FastLongMap.this.table;
            for (i2 = table.length - 1; i2 >= 0 && table[i2] == null; --i2) {
            }
            this.nextIndex = i2;
        }

        @Override
        public boolean hasNext() {
            if (this.nextIndex >= 0) {
                return true;
            }
            Entry e2 = this.current;
            return e2 != null && e2.next != null;
        }

        @Override
        public Entry<V> next() {
            Entry e2 = this.current;
            if (e2 != null && (e2 = e2.next) != null) {
                this.current = e2;
                return e2;
            }
            Entry[] table = FastLongMap.this.table;
            int i2 = this.nextIndex;
            e2 = this.current = table[i2];
            while (--i2 >= 0 && table[i2] == null) {
            }
            this.nextIndex = i2;
            return e2;
        }

        @Override
        public void remove() {
            FastLongMap.this.remove(this.current.key);
        }
    }
}

