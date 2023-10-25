/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.collection;

import io.netty.util.collection.IntObjectMap;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntObjectHashMap<V>
implements IntObjectMap<V>,
Iterable<IntObjectMap.Entry<V>> {
    private static final Object NULL_VALUE = new Object();
    private int size;
    private int[] keys;
    private int maxSize;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private V[] values;
    private static final int DEFAULT_CAPACITY = 11;
    private final float loadFactor;

    private int hashIndex(int n) {
        return n % this.keys.length;
    }

    private int calcMaxSize(int n) {
        int n2 = n - 1;
        return Math.min(n2, (int)((float)n * this.loadFactor));
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterable<IntObjectMap.Entry<V>> entries() {
        return this;
    }

    private static <T> T toInternal(T t) {
        return (T)(t == null ? NULL_VALUE : t);
    }

    @Override
    public V remove(int n) {
        int n2 = this.indexOf(n);
        if (n2 == -1) {
            return null;
        }
        V v = this.values[n2];
        this.removeAt(n2);
        return IntObjectHashMap.toExternal(v);
    }

    private void growSize() {
        ++this.size;
        if (this.size > this.maxSize) {
            this.rehash(IntObjectHashMap.adjustCapacity((int)Math.min((double)this.keys.length * 2.0, 2.147483639E9)));
        } else if (this.size == this.keys.length) {
            this.rehash(this.keys.length);
        }
    }

    private int probeNext(int n) {
        return n == this.values.length - 1 ? 0 : n + 1;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IntObjectMap)) {
            return false;
        }
        IntObjectMap intObjectMap = (IntObjectMap)object;
        if (this.size != intObjectMap.size()) {
            return false;
        }
        for (int i = 0; i < this.values.length; ++i) {
            V v = this.values[i];
            if (v == null) continue;
            int n = this.keys[i];
            Object v2 = intObjectMap.get(n);
            if (!(v == NULL_VALUE ? v2 != null : !v.equals(v2))) continue;
            return false;
        }
        return true;
    }

    private void removeAt(int n) {
        --this.size;
        this.keys[n] = 0;
        this.values[n] = null;
        int n2 = n;
        int n3 = this.probeNext(n);
        while (this.values[n3] != null) {
            int n4 = this.hashIndex(this.keys[n3]);
            if (n3 < n4 && (n4 <= n2 || n2 <= n3) || n4 <= n2 && n2 <= n3) {
                this.keys[n2] = this.keys[n3];
                this.values[n2] = this.values[n3];
                this.keys[n3] = 0;
                this.values[n3] = null;
                n2 = n3;
            }
            n3 = this.probeNext(n3);
        }
    }

    @Override
    public Iterator<IntObjectMap.Entry<V>> iterator() {
        return new IteratorImpl();
    }

    private void rehash(int n) {
        int[] nArray = this.keys;
        V[] VArray = this.values;
        this.keys = new int[n];
        Object[] objectArray = new Object[n];
        this.values = objectArray;
        this.maxSize = this.calcMaxSize(n);
        block0: for (int i = 0; i < VArray.length; ++i) {
            int n2;
            V v = VArray[i];
            if (v == null) continue;
            int n3 = nArray[i];
            int n4 = n2 = this.hashIndex(n3);
            while (true) {
                if (this.values[n4] == null) {
                    this.keys[n4] = n3;
                    this.values[n4] = IntObjectHashMap.toInternal(v);
                    continue block0;
                }
                n4 = this.probeNext(n4);
            }
        }
    }

    public IntObjectHashMap(int n, float f) {
        if (n < 1) {
            throw new IllegalArgumentException("initialCapacity must be >= 1");
        }
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
        }
        this.loadFactor = f;
        int n2 = IntObjectHashMap.adjustCapacity(n);
        this.keys = new int[n2];
        Object[] objectArray = new Object[n2];
        this.values = objectArray;
        this.maxSize = this.calcMaxSize(n2);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void putAll(IntObjectMap<V> intObjectMap) {
        if (intObjectMap instanceof IntObjectHashMap) {
            IntObjectHashMap intObjectHashMap = (IntObjectHashMap)intObjectMap;
            for (int i = 0; i < intObjectHashMap.values.length; ++i) {
                V v = intObjectHashMap.values[i];
                if (v == null) continue;
                this.put(intObjectHashMap.keys[i], v);
            }
            return;
        }
        for (IntObjectMap.Entry<V> entry : intObjectMap.entries()) {
            this.put(entry.key(), entry.value());
        }
    }

    public int hashCode() {
        int n = this.size;
        for (int i = 0; i < this.keys.length; ++i) {
            n ^= this.keys[i];
        }
        return n;
    }

    private static <T> T toExternal(T t) {
        return t == NULL_VALUE ? null : (T)t;
    }

    @Override
    public V put(int n, V v) {
        int n2;
        int n3 = n2 = this.hashIndex(n);
        do {
            if (this.values[n3] == null) {
                this.keys[n3] = n;
                this.values[n3] = IntObjectHashMap.toInternal(v);
                this.growSize();
                return null;
            }
            if (this.keys[n3] != n) continue;
            V v2 = this.values[n3];
            this.values[n3] = IntObjectHashMap.toInternal(v);
            return IntObjectHashMap.toExternal(v2);
        } while ((n3 = this.probeNext(n3)) != n2);
        throw new IllegalStateException("Unable to insert");
    }

    @Override
    public boolean containsValue(V v) {
        V v2 = IntObjectHashMap.toInternal(v);
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] == null || !this.values[i].equals(v2)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsKey(int n) {
        return this.indexOf(n) >= 0;
    }

    @Override
    public int[] keys() {
        int[] nArray = new int[this.size()];
        int n = 0;
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] == null) continue;
            nArray[n++] = this.keys[i];
        }
        return nArray;
    }

    private static int adjustCapacity(int n) {
        return n | 1;
    }

    private int indexOf(int n) {
        int n2;
        int n3 = n2 = this.hashIndex(n);
        do {
            if (this.values[n3] == null) {
                return -1;
            }
            if (n != this.keys[n3]) continue;
            return n3;
        } while ((n3 = this.probeNext(n3)) != n2);
        return -1;
    }

    @Override
    public void clear() {
        Arrays.fill(this.keys, 0);
        Arrays.fill(this.values, null);
        this.size = 0;
    }

    @Override
    public V get(int n) {
        int n2 = this.indexOf(n);
        return n2 == -1 ? null : (V)IntObjectHashMap.toExternal(this.values[n2]);
    }

    @Override
    public V[] values(Class<V> clazz) {
        Object[] objectArray = (Object[])Array.newInstance(clazz, this.size());
        int n = 0;
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] == null) continue;
            objectArray[n++] = this.values[i];
        }
        return objectArray;
    }

    public IntObjectHashMap(int n) {
        this(n, 0.5f);
    }

    public String toString() {
        if (this.size == 0) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(4 * this.size);
        for (int i = 0; i < this.values.length; ++i) {
            V v = this.values[i];
            if (v == null) continue;
            stringBuilder.append(stringBuilder.length() == 0 ? "{" : ", ");
            stringBuilder.append(this.keys[i]).append('=').append((Object)(v == this ? "(this Map)" : v));
        }
        return stringBuilder.append('}').toString();
    }

    public IntObjectHashMap() {
        this(11, 0.5f);
    }

    private final class IteratorImpl
    implements Iterator<IntObjectMap.Entry<V>>,
    IntObjectMap.Entry<V> {
        private int entryIndex = -1;
        private int nextIndex = -1;
        private int prevIndex = -1;

        @Override
        public void setValue(V v) {
            ((IntObjectHashMap)IntObjectHashMap.this).values[this.entryIndex] = IntObjectHashMap.toInternal(v);
        }

        @Override
        public int key() {
            return IntObjectHashMap.this.keys[this.entryIndex];
        }

        private void scanNext() {
            while (++this.nextIndex != IntObjectHashMap.this.values.length && IntObjectHashMap.this.values[this.nextIndex] == null) {
            }
        }

        private IteratorImpl() {
        }

        @Override
        public void remove() {
            if (this.prevIndex < 0) {
                throw new IllegalStateException("next must be called before each remove.");
            }
            IntObjectHashMap.this.removeAt(this.prevIndex);
            this.prevIndex = -1;
        }

        @Override
        public boolean hasNext() {
            if (this.nextIndex == -1) {
                this.scanNext();
            }
            return this.nextIndex < IntObjectHashMap.this.keys.length;
        }

        @Override
        public IntObjectMap.Entry<V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.prevIndex = this.nextIndex;
            this.scanNext();
            this.entryIndex = this.prevIndex;
            return this;
        }

        @Override
        public V value() {
            return IntObjectHashMap.toExternal(IntObjectHashMap.this.values[this.entryIndex]);
        }
    }
}

