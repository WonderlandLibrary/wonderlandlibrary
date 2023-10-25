/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.util.Iterator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class FastIntMap<V>
implements Iterable<Entry<V>> {
    private Entry[] table;
    private int size;
    private int mask;
    private int capacity;
    private int threshold;

    FastIntMap() {
        this(16, 0.75f);
    }

    FastIntMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    FastIntMap(int initialCapacity, float loadFactor) {
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

    private int index(int key) {
        return FastIntMap.index(key, this.mask);
    }

    private static int index(int key, int mask) {
        return key & mask;
    }

    public V put(int key, V value) {
        Entry[] table = this.table;
        int index = this.index(key);
        Entry e = table[index];
        while (e != null) {
            if (e.key == key) {
                Object oldValue = e.value;
                e.value = value;
                return (V)oldValue;
            }
            e = e.next;
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
        for (int i = 0; i < table.length; ++i) {
            Entry next;
            Entry<Object> e = table[i];
            if (e == null) continue;
            do {
                next = e.next;
                int index = FastIntMap.index(e.key, newMask);
                e.next = newTable[index];
                newTable[index] = e;
            } while ((e = next) != null);
        }
        this.table = newTable;
        this.capacity = newCapacity;
        this.mask = newMask;
        this.threshold *= 2;
    }

    public V get(int key) {
        int index = this.index(key);
        Entry e = this.table[index];
        while (e != null) {
            if (e.key == key) {
                return (V)e.value;
            }
            e = e.next;
        }
        return null;
    }

    public boolean containsValue(Object value) {
        Entry[] table = this.table;
        for (int i = table.length - 1; i >= 0; --i) {
            Entry e = table[i];
            while (e != null) {
                if (e.value.equals(value)) {
                    return true;
                }
                e = e.next;
            }
        }
        return false;
    }

    public boolean containsKey(int key) {
        int index = this.index(key);
        Entry e = this.table[index];
        while (e != null) {
            if (e.key == key) {
                return true;
            }
            e = e.next;
        }
        return false;
    }

    public V remove(int key) {
        Entry prev;
        int index = this.index(key);
        Entry e = prev = this.table[index];
        while (e != null) {
            Entry next = e.next;
            if (e.key == key) {
                --this.size;
                if (prev == e) {
                    this.table[index] = next;
                } else {
                    prev.next = next;
                }
                return (V)e.value;
            }
            prev = e;
            e = next;
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

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static final class Entry<T> {
        final int key;
        T value;
        Entry<T> next;

        Entry(int key, T value, Entry<T> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public int getKey() {
            return this.key;
        }

        public T getValue() {
            return this.value;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public class EntryIterator
    implements Iterator<Entry<V>> {
        private int nextIndex;
        private Entry<V> current;

        EntryIterator() {
            this.reset();
        }

        public void reset() {
            int i;
            this.current = null;
            Entry[] table = FastIntMap.this.table;
            for (i = table.length - 1; i >= 0 && table[i] == null; --i) {
            }
            this.nextIndex = i;
        }

        @Override
        public boolean hasNext() {
            if (this.nextIndex >= 0) {
                return true;
            }
            Entry e = this.current;
            return e != null && e.next != null;
        }

        @Override
        public Entry<V> next() {
            Entry e = this.current;
            if (e != null && (e = e.next) != null) {
                this.current = e;
                return e;
            }
            Entry[] table = FastIntMap.this.table;
            int i = this.nextIndex;
            e = this.current = table[i];
            while (--i >= 0 && table[i] == null) {
            }
            this.nextIndex = i;
            return e;
        }

        @Override
        public void remove() {
            FastIntMap.this.remove(this.current.key);
        }
    }
}

