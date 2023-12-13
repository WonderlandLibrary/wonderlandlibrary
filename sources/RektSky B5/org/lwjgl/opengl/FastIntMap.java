/*
 * Decompiled with CFR 0.152.
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
                int index = FastIntMap.index(e2.key, newMask);
                e2.next = newTable[index];
                newTable[index] = e2;
            } while ((e2 = next) != null);
        }
        this.table = newTable;
        this.capacity = newCapacity;
        this.mask = newMask;
        this.threshold *= 2;
    }

    public V get(int key) {
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

    public boolean containsKey(int key) {
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

    public V remove(int key) {
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
            int i2;
            this.current = null;
            Entry[] table = FastIntMap.this.table;
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
            Entry[] table = FastIntMap.this.table;
            int i2 = this.nextIndex;
            e2 = this.current = table[i2];
            while (--i2 >= 0 && table[i2] == null) {
            }
            this.nextIndex = i2;
            return e2;
        }

        @Override
        public void remove() {
            FastIntMap.this.remove(this.current.key);
        }
    }
}

