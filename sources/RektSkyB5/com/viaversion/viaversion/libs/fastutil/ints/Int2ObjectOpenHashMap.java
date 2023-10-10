/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Hash;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractInt2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntSet;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntObjectPair;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

public class Int2ObjectOpenHashMap<V>
extends AbstractInt2ObjectMap<V>
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Int2ObjectMap.FastEntrySet<V> entries;
    protected transient IntSet keys;
    protected transient ObjectCollection<V> values;

    public Int2ObjectOpenHashMap(int expected, float f2) {
        if (f2 <= 0.0f || f2 >= 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f2;
        this.minN = this.n = HashCommon.arraySize(expected, f2);
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f2);
        this.key = new int[this.n + 1];
        this.value = new Object[this.n + 1];
    }

    public Int2ObjectOpenHashMap(int expected) {
        this(expected, 0.75f);
    }

    public Int2ObjectOpenHashMap() {
        this(16, 0.75f);
    }

    public Int2ObjectOpenHashMap(Map<? extends Integer, ? extends V> m2, float f2) {
        this(m2.size(), f2);
        this.putAll(m2);
    }

    public Int2ObjectOpenHashMap(Map<? extends Integer, ? extends V> m2) {
        this(m2, 0.75f);
    }

    public Int2ObjectOpenHashMap(Int2ObjectMap<V> m2, float f2) {
        this(m2.size(), f2);
        this.putAll(m2);
    }

    public Int2ObjectOpenHashMap(Int2ObjectMap<V> m2) {
        this(m2, 0.75f);
    }

    public Int2ObjectOpenHashMap(int[] k2, V[] v2, float f2) {
        this(k2.length, f2);
        if (k2.length != v2.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k2.length + " and " + v2.length + ")");
        }
        for (int i2 = 0; i2 < k2.length; ++i2) {
            this.put(k2[i2], v2[i2]);
        }
    }

    public Int2ObjectOpenHashMap(int[] k2, V[] v2) {
        this(k2, v2, 0.75f);
    }

    private int realSize() {
        return this.containsNullKey ? this.size - 1 : this.size;
    }

    private void ensureCapacity(int capacity) {
        int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }

    private void tryCapacity(long capacity) {
        int needed = (int)Math.min(0x40000000L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((float)capacity / this.f))));
        if (needed > this.n) {
            this.rehash(needed);
        }
    }

    private V removeEntry(int pos) {
        V oldValue = this.value[pos];
        this.value[pos] = null;
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    private V removeNullEntry() {
        this.containsNullKey = false;
        V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends V> m2) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(m2.size());
        } else {
            this.tryCapacity(this.size() + m2.size());
        }
        super.putAll(m2);
    }

    private int find(int k2) {
        if (k2 == 0) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k2) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return -(pos + 1);
        }
        if (k2 == curr) {
            return pos;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return -(pos + 1);
        } while (k2 != curr);
        return pos;
    }

    private void insert(int pos, int k2, V v2) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k2;
        this.value[pos] = v2;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }

    @Override
    public V put(int k2, V v2) {
        int pos = this.find(k2);
        if (pos < 0) {
            this.insert(-pos - 1, k2, v2);
            return (V)this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v2;
        return oldValue;
    }

    protected final void shiftKeys(int pos) {
        int[] key = this.key;
        while (true) {
            int curr;
            int last = pos;
            pos = last + 1 & this.mask;
            while (true) {
                if ((curr = key[pos]) == 0) {
                    key[last] = 0;
                    this.value[last] = null;
                    return;
                }
                int slot = HashCommon.mix(curr) & this.mask;
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                pos = pos + 1 & this.mask;
            }
            key[last] = curr;
            this.value[last] = this.value[pos];
        }
    }

    @Override
    public V remove(int k2) {
        if (k2 == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return (V)this.defRetValue;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k2) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return (V)this.defRetValue;
        }
        if (k2 == curr) {
            return this.removeEntry(pos);
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return (V)this.defRetValue;
        } while (k2 != curr);
        return this.removeEntry(pos);
    }

    @Override
    public V get(int k2) {
        if (k2 == 0) {
            return (V)(this.containsNullKey ? this.value[this.n] : this.defRetValue);
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k2) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return (V)this.defRetValue;
        }
        if (k2 == curr) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return (V)this.defRetValue;
        } while (k2 != curr);
        return this.value[pos];
    }

    @Override
    public boolean containsKey(int k2) {
        if (k2 == 0) {
            return this.containsNullKey;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k2) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (k2 == curr) {
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (k2 != curr);
        return true;
    }

    @Override
    public boolean containsValue(Object v2) {
        V[] value = this.value;
        int[] key = this.key;
        if (this.containsNullKey && Objects.equals(value[this.n], v2)) {
            return true;
        }
        int i2 = this.n;
        while (i2-- != 0) {
            if (key[i2] == 0 || !Objects.equals(value[i2], v2)) continue;
            return true;
        }
        return false;
    }

    @Override
    public V getOrDefault(int k2, V defaultValue) {
        if (k2 == 0) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k2) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return defaultValue;
        }
        if (k2 == curr) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return defaultValue;
        } while (k2 != curr);
        return this.value[pos];
    }

    @Override
    public V putIfAbsent(int k2, V v2) {
        int pos = this.find(k2);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k2, v2);
        return (V)this.defRetValue;
    }

    @Override
    public boolean remove(int k2, Object v2) {
        if (k2 == 0) {
            if (this.containsNullKey && Objects.equals(v2, this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k2) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (k2 == curr && Objects.equals(v2, this.value[pos])) {
            this.removeEntry(pos);
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (k2 != curr || !Objects.equals(v2, this.value[pos]));
        this.removeEntry(pos);
        return true;
    }

    @Override
    public boolean replace(int k2, V oldValue, V v2) {
        int pos = this.find(k2);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
            return false;
        }
        this.value[pos] = v2;
        return true;
    }

    @Override
    public V replace(int k2, V v2) {
        int pos = this.find(k2);
        if (pos < 0) {
            return (V)this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v2;
        return oldValue;
    }

    @Override
    public V computeIfAbsent(int k2, IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(k2);
        if (pos >= 0) {
            return this.value[pos];
        }
        V newValue = mappingFunction.apply(k2);
        this.insert(-pos - 1, k2, newValue);
        return newValue;
    }

    @Override
    public V computeIfAbsent(int key, Int2ObjectFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(key);
        if (pos >= 0) {
            return this.value[pos];
        }
        if (!mappingFunction.containsKey(key)) {
            return (V)this.defRetValue;
        }
        V newValue = mappingFunction.get(key);
        this.insert(-pos - 1, key, newValue);
        return newValue;
    }

    @Override
    public V computeIfPresent(int k2, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k2);
        if (pos < 0) {
            return (V)this.defRetValue;
        }
        V newValue = remappingFunction.apply(k2, this.value[pos]);
        if (newValue == null) {
            if (k2 == 0) {
                this.removeNullEntry();
            } else {
                this.removeEntry(pos);
            }
            return (V)this.defRetValue;
        }
        this.value[pos] = newValue;
        return this.value[pos];
    }

    @Override
    public V compute(int k2, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k2);
        V newValue = remappingFunction.apply(k2, pos >= 0 ? (Object)this.value[pos] : null);
        if (newValue == null) {
            if (pos >= 0) {
                if (k2 == 0) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(pos);
                }
            }
            return (V)this.defRetValue;
        }
        V newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k2, newVal);
            return newVal;
        }
        this.value[pos] = newVal;
        return this.value[pos];
    }

    @Override
    public V merge(int k2, V v2, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k2);
        if (pos < 0 || this.value[pos] == null) {
            if (v2 == null) {
                return (V)this.defRetValue;
            }
            this.insert(-pos - 1, k2, v2);
            return v2;
        }
        V newValue = remappingFunction.apply(this.value[pos], v2);
        if (newValue == null) {
            if (k2 == 0) {
                this.removeNullEntry();
            } else {
                this.removeEntry(pos);
            }
            return (V)this.defRetValue;
        }
        this.value[pos] = newValue;
        return this.value[pos];
    }

    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, 0);
        Arrays.fill(this.value, null);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    public Int2ObjectMap.FastEntrySet<V> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    @Override
    public IntSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>(){

                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }

                @Override
                public ObjectSpliterator<V> spliterator() {
                    return new ValueSpliterator();
                }

                @Override
                public void forEach(Consumer<? super V> consumer) {
                    if (Int2ObjectOpenHashMap.this.containsNullKey) {
                        consumer.accept(Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n]);
                    }
                    int pos = Int2ObjectOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Int2ObjectOpenHashMap.this.key[pos] == 0) continue;
                        consumer.accept(Int2ObjectOpenHashMap.this.value[pos]);
                    }
                }

                @Override
                public int size() {
                    return Int2ObjectOpenHashMap.this.size;
                }

                @Override
                public boolean contains(Object v2) {
                    return Int2ObjectOpenHashMap.this.containsValue(v2);
                }

                @Override
                public void clear() {
                    Int2ObjectOpenHashMap.this.clear();
                }
            };
        }
        return this.values;
    }

    public boolean trim() {
        return this.trim(this.size);
    }

    public boolean trim(int n2) {
        int l2 = HashCommon.nextPowerOfTwo((int)Math.ceil((float)n2 / this.f));
        if (l2 >= this.n || this.size > HashCommon.maxFill(l2, this.f)) {
            return true;
        }
        try {
            this.rehash(l2);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }

    protected void rehash(int newN) {
        int[] key = this.key;
        V[] value = this.value;
        int mask = newN - 1;
        int[] newKey = new int[newN + 1];
        Object[] newValue = new Object[newN + 1];
        int i2 = this.n;
        int j2 = this.realSize();
        while (j2-- != 0) {
            while (key[--i2] == 0) {
            }
            int pos = HashCommon.mix(key[i2]) & mask;
            if (newKey[pos] != 0) {
                while (newKey[pos = pos + 1 & mask] != 0) {
                }
            }
            newKey[pos] = key[i2];
            newValue[pos] = value[i2];
        }
        newValue[newN] = value[this.n];
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }

    public Int2ObjectOpenHashMap<V> clone() {
        Int2ObjectOpenHashMap c2;
        try {
            c2 = (Int2ObjectOpenHashMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c2.keys = null;
        c2.values = null;
        c2.entries = null;
        c2.containsNullKey = this.containsNullKey;
        c2.key = (int[])this.key.clone();
        c2.value = (Object[])this.value.clone();
        return c2;
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        int j2 = this.realSize();
        int i2 = 0;
        int t2 = 0;
        while (j2-- != 0) {
            while (this.key[i2] == 0) {
                ++i2;
            }
            t2 = this.key[i2];
            if (this != this.value[i2]) {
                t2 ^= this.value[i2] == null ? 0 : this.value[i2].hashCode();
            }
            h2 += t2;
            ++i2;
        }
        if (this.containsNullKey) {
            h2 += this.value[this.n] == null ? 0 : this.value[this.n].hashCode();
        }
        return h2;
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        int[] key = this.key;
        V[] value = this.value;
        EntryIterator i2 = new EntryIterator();
        s2.defaultWriteObject();
        int j2 = this.size;
        while (j2-- != 0) {
            int e2 = i2.nextEntry();
            s2.writeInt(key[e2]);
            s2.writeObject(value[e2]);
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new int[this.n + 1];
        int[] key = this.key;
        this.value = new Object[this.n + 1];
        Object[] value = this.value;
        int i2 = this.size;
        while (i2-- != 0) {
            int pos;
            int k2 = s2.readInt();
            Object v2 = s2.readObject();
            if (k2 == 0) {
                pos = this.n;
                this.containsNullKey = true;
            } else {
                pos = HashCommon.mix(k2) & this.mask;
                while (key[pos] != 0) {
                    pos = pos + 1 & this.mask;
                }
            }
            key[pos] = k2;
            value[pos] = v2;
        }
    }

    private void checkTable() {
    }

    private final class MapEntrySet
    extends AbstractObjectSet<Int2ObjectMap.Entry<V>>
    implements Int2ObjectMap.FastEntrySet<V> {
        private MapEntrySet() {
        }

        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }

        @Override
        public ObjectSpliterator<Int2ObjectMap.Entry<V>> spliterator() {
            return new EntrySpliterator();
        }

        @Override
        public boolean contains(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            if (e2.getKey() == null || !(e2.getKey() instanceof Integer)) {
                return false;
            }
            int k2 = (Integer)e2.getKey();
            Object v2 = e2.getValue();
            if (k2 == 0) {
                return Int2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n], v2);
            }
            int[] key = Int2ObjectOpenHashMap.this.key;
            int pos = HashCommon.mix(k2) & Int2ObjectOpenHashMap.this.mask;
            int curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (k2 == curr) {
                return Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v2);
            }
            do {
                if ((curr = key[pos = pos + 1 & Int2ObjectOpenHashMap.this.mask]) != 0) continue;
                return false;
            } while (k2 != curr);
            return Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v2);
        }

        @Override
        public boolean remove(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            if (e2.getKey() == null || !(e2.getKey() instanceof Integer)) {
                return false;
            }
            int k2 = (Integer)e2.getKey();
            Object v2 = e2.getValue();
            if (k2 == 0) {
                if (Int2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n], v2)) {
                    Int2ObjectOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            int[] key = Int2ObjectOpenHashMap.this.key;
            int pos = HashCommon.mix(k2) & Int2ObjectOpenHashMap.this.mask;
            int curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (curr == k2) {
                if (Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v2)) {
                    Int2ObjectOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
            do {
                if ((curr = key[pos = pos + 1 & Int2ObjectOpenHashMap.this.mask]) != 0) continue;
                return false;
            } while (curr != k2 || !Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v2));
            Int2ObjectOpenHashMap.this.removeEntry(pos);
            return true;
        }

        @Override
        public int size() {
            return Int2ObjectOpenHashMap.this.size;
        }

        @Override
        public void clear() {
            Int2ObjectOpenHashMap.this.clear();
        }

        @Override
        public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept(new AbstractInt2ObjectMap.BasicEntry(Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n], Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n]));
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Int2ObjectOpenHashMap.this.key[pos] == 0) continue;
                consumer.accept(new AbstractInt2ObjectMap.BasicEntry(Int2ObjectOpenHashMap.this.key[pos], Int2ObjectOpenHashMap.this.value[pos]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            AbstractInt2ObjectMap.BasicEntry entry = new AbstractInt2ObjectMap.BasicEntry();
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                entry.key = Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n];
                entry.value = Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Int2ObjectOpenHashMap.this.key[pos] == 0) continue;
                entry.key = Int2ObjectOpenHashMap.this.key[pos];
                entry.value = Int2ObjectOpenHashMap.this.value[pos];
                consumer.accept(entry);
            }
        }
    }

    private final class KeySet
    extends AbstractIntSet {
        private KeySet() {
        }

        @Override
        public IntIterator iterator() {
            return new KeyIterator();
        }

        @Override
        public IntSpliterator spliterator() {
            return new KeySpliterator();
        }

        @Override
        public void forEach(IntConsumer consumer) {
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept(Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n]);
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                int k2 = Int2ObjectOpenHashMap.this.key[pos];
                if (k2 == 0) continue;
                consumer.accept(k2);
            }
        }

        @Override
        public int size() {
            return Int2ObjectOpenHashMap.this.size;
        }

        @Override
        public boolean contains(int k2) {
            return Int2ObjectOpenHashMap.this.containsKey(k2);
        }

        @Override
        public boolean remove(int k2) {
            int oldSize = Int2ObjectOpenHashMap.this.size;
            Int2ObjectOpenHashMap.this.remove(k2);
            return Int2ObjectOpenHashMap.this.size != oldSize;
        }

        @Override
        public void clear() {
            Int2ObjectOpenHashMap.this.clear();
        }
    }

    private final class EntryIterator
    extends MapIterator<Consumer<? super Int2ObjectMap.Entry<V>>>
    implements ObjectIterator<Int2ObjectMap.Entry<V>> {
        private MapEntry entry;

        private EntryIterator() {
        }

        @Override
        public MapEntry next() {
            this.entry = new MapEntry(this.nextEntry());
            return this.entry;
        }

        @Override
        final void acceptOnIndex(Consumer<? super Int2ObjectMap.Entry<V>> action, int index) {
            this.entry = new MapEntry(index);
            action.accept(this.entry);
        }

        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    private final class ValueSpliterator
    extends MapSpliterator<Consumer<? super V>, ValueSpliterator>
    implements ObjectSpliterator<V> {
        private static final int POST_SPLIT_CHARACTERISTICS = 0;

        ValueSpliterator() {
        }

        ValueSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }

        @Override
        public int characteristics() {
            return this.hasSplit ? 0 : 64;
        }

        @Override
        final void acceptOnIndex(Consumer<? super V> action, int index) {
            action.accept(Int2ObjectOpenHashMap.this.value[index]);
        }

        @Override
        final ValueSpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new ValueSpliterator(pos, max, mustReturnNull, true);
        }
    }

    private final class ValueIterator
    extends MapIterator<Consumer<? super V>>
    implements ObjectIterator<V> {
        @Override
        final void acceptOnIndex(Consumer<? super V> action, int index) {
            action.accept(Int2ObjectOpenHashMap.this.value[index]);
        }

        @Override
        public V next() {
            return Int2ObjectOpenHashMap.this.value[this.nextEntry()];
        }
    }

    private final class KeySpliterator
    extends MapSpliterator<IntConsumer, KeySpliterator>
    implements IntSpliterator {
        private static final int POST_SPLIT_CHARACTERISTICS = 257;

        KeySpliterator() {
        }

        KeySpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }

        @Override
        public int characteristics() {
            return this.hasSplit ? 257 : 321;
        }

        @Override
        final void acceptOnIndex(IntConsumer action, int index) {
            action.accept(Int2ObjectOpenHashMap.this.key[index]);
        }

        @Override
        final KeySpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new KeySpliterator(pos, max, mustReturnNull, true);
        }
    }

    private final class KeyIterator
    extends MapIterator<IntConsumer>
    implements IntIterator {
        @Override
        final void acceptOnIndex(IntConsumer action, int index) {
            action.accept(Int2ObjectOpenHashMap.this.key[index]);
        }

        @Override
        public int nextInt() {
            return Int2ObjectOpenHashMap.this.key[this.nextEntry()];
        }
    }

    private final class EntrySpliterator
    extends MapSpliterator<Consumer<? super Int2ObjectMap.Entry<V>>, EntrySpliterator>
    implements ObjectSpliterator<Int2ObjectMap.Entry<V>> {
        private static final int POST_SPLIT_CHARACTERISTICS = 1;

        EntrySpliterator() {
        }

        EntrySpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }

        @Override
        public int characteristics() {
            return this.hasSplit ? 1 : 65;
        }

        @Override
        final void acceptOnIndex(Consumer<? super Int2ObjectMap.Entry<V>> action, int index) {
            action.accept(new MapEntry(index));
        }

        @Override
        final EntrySpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
            return new EntrySpliterator(pos, max, mustReturnNull, true);
        }
    }

    private abstract class MapSpliterator<ConsumerType, SplitType extends MapSpliterator<ConsumerType, SplitType>> {
        int pos = 0;
        int max;
        int c;
        boolean mustReturnNull;
        boolean hasSplit;

        MapSpliterator() {
            this.max = Int2ObjectOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Int2ObjectOpenHashMap.this.containsNullKey;
            this.hasSplit = false;
        }

        MapSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            this.max = Int2ObjectOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Int2ObjectOpenHashMap.this.containsNullKey;
            this.hasSplit = false;
            this.pos = pos;
            this.max = max;
            this.mustReturnNull = mustReturnNull;
            this.hasSplit = hasSplit;
        }

        abstract void acceptOnIndex(ConsumerType var1, int var2);

        abstract SplitType makeForSplit(int var1, int var2, boolean var3);

        public boolean tryAdvance(ConsumerType action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++this.c;
                this.acceptOnIndex(action, Int2ObjectOpenHashMap.this.n);
                return true;
            }
            int[] key = Int2ObjectOpenHashMap.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != 0) {
                    ++this.c;
                    this.acceptOnIndex(action, this.pos++);
                    return true;
                }
                ++this.pos;
            }
            return false;
        }

        public void forEachRemaining(ConsumerType action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++this.c;
                this.acceptOnIndex(action, Int2ObjectOpenHashMap.this.n);
            }
            int[] key = Int2ObjectOpenHashMap.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != 0) {
                    this.acceptOnIndex(action, this.pos);
                    ++this.c;
                }
                ++this.pos;
            }
        }

        public long estimateSize() {
            if (!this.hasSplit) {
                return Int2ObjectOpenHashMap.this.size - this.c;
            }
            return Math.min((long)(Int2ObjectOpenHashMap.this.size - this.c), (long)((double)Int2ObjectOpenHashMap.this.realSize() / (double)Int2ObjectOpenHashMap.this.n * (double)(this.max - this.pos)) + (long)(this.mustReturnNull ? 1 : 0));
        }

        public SplitType trySplit() {
            if (this.pos >= this.max - 1) {
                return null;
            }
            int retLen = this.max - this.pos >> 1;
            if (retLen <= 1) {
                return null;
            }
            int myNewPos = this.pos + retLen;
            int retPos = this.pos;
            int retMax = myNewPos;
            SplitType split = this.makeForSplit(retPos, retMax, this.mustReturnNull);
            this.pos = myNewPos;
            this.mustReturnNull = false;
            this.hasSplit = true;
            return split;
        }

        public long skip(long n2) {
            if (n2 < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            if (n2 == 0L) {
                return 0L;
            }
            long skipped = 0L;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++skipped;
                --n2;
            }
            int[] key = Int2ObjectOpenHashMap.this.key;
            while (this.pos < this.max && n2 > 0L) {
                if (key[this.pos++] == 0) continue;
                ++skipped;
                --n2;
            }
            return skipped;
        }
    }

    private final class FastEntryIterator
    extends MapIterator<Consumer<? super Int2ObjectMap.Entry<V>>>
    implements ObjectIterator<Int2ObjectMap.Entry<V>> {
        private final MapEntry entry;

        private FastEntryIterator() {
            this.entry = new MapEntry();
        }

        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }

        @Override
        final void acceptOnIndex(Consumer<? super Int2ObjectMap.Entry<V>> action, int index) {
            this.entry.index = index;
            action.accept(this.entry);
        }
    }

    private abstract class MapIterator<ConsumerType> {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        IntArrayList wrapped;

        private MapIterator() {
            this.pos = Int2ObjectOpenHashMap.this.n;
            this.last = -1;
            this.c = Int2ObjectOpenHashMap.this.size;
            this.mustReturnNullKey = Int2ObjectOpenHashMap.this.containsNullKey;
        }

        abstract void acceptOnIndex(ConsumerType var1, int var2);

        public boolean hasNext() {
            return this.c != 0;
        }

        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                this.last = Int2ObjectOpenHashMap.this.n;
                return this.last;
            }
            int[] key = Int2ObjectOpenHashMap.this.key;
            do {
                if (--this.pos >= 0) continue;
                this.last = Integer.MIN_VALUE;
                int k2 = this.wrapped.getInt(-this.pos - 1);
                int p2 = HashCommon.mix(k2) & Int2ObjectOpenHashMap.this.mask;
                while (k2 != key[p2]) {
                    p2 = p2 + 1 & Int2ObjectOpenHashMap.this.mask;
                }
                return p2;
            } while (key[this.pos] == 0);
            this.last = this.pos;
            return this.last;
        }

        public void forEachRemaining(ConsumerType action) {
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                this.last = Int2ObjectOpenHashMap.this.n;
                this.acceptOnIndex(action, this.last);
                --this.c;
            }
            int[] key = Int2ObjectOpenHashMap.this.key;
            while (this.c != 0) {
                if (--this.pos < 0) {
                    this.last = Integer.MIN_VALUE;
                    int k2 = this.wrapped.getInt(-this.pos - 1);
                    int p2 = HashCommon.mix(k2) & Int2ObjectOpenHashMap.this.mask;
                    while (k2 != key[p2]) {
                        p2 = p2 + 1 & Int2ObjectOpenHashMap.this.mask;
                    }
                    this.acceptOnIndex(action, p2);
                    --this.c;
                    continue;
                }
                if (key[this.pos] == 0) continue;
                this.last = this.pos;
                this.acceptOnIndex(action, this.last);
                --this.c;
            }
        }

        private void shiftKeys(int pos) {
            int[] key = Int2ObjectOpenHashMap.this.key;
            while (true) {
                int curr;
                int last = pos;
                pos = last + 1 & Int2ObjectOpenHashMap.this.mask;
                while (true) {
                    if ((curr = key[pos]) == 0) {
                        key[last] = 0;
                        Int2ObjectOpenHashMap.this.value[last] = null;
                        return;
                    }
                    int slot = HashCommon.mix(curr) & Int2ObjectOpenHashMap.this.mask;
                    if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                    pos = pos + 1 & Int2ObjectOpenHashMap.this.mask;
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new IntArrayList(2);
                    }
                    this.wrapped.add(key[pos]);
                }
                key[last] = curr;
                Int2ObjectOpenHashMap.this.value[last] = Int2ObjectOpenHashMap.this.value[pos];
            }
        }

        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Int2ObjectOpenHashMap.this.n) {
                Int2ObjectOpenHashMap.this.containsNullKey = false;
                Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n] = null;
            } else if (this.pos >= 0) {
                this.shiftKeys(this.last);
            } else {
                Int2ObjectOpenHashMap.this.remove(this.wrapped.getInt(-this.pos - 1));
                this.last = -1;
                return;
            }
            --Int2ObjectOpenHashMap.this.size;
            this.last = -1;
        }

        public int skip(int n2) {
            int i2 = n2;
            while (i2-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n2 - i2 - 1;
        }
    }

    final class MapEntry
    implements Int2ObjectMap.Entry<V>,
    Map.Entry<Integer, V>,
    IntObjectPair<V> {
        int index;

        MapEntry(int index) {
            this.index = index;
        }

        MapEntry() {
        }

        @Override
        public int getIntKey() {
            return Int2ObjectOpenHashMap.this.key[this.index];
        }

        @Override
        public int leftInt() {
            return Int2ObjectOpenHashMap.this.key[this.index];
        }

        @Override
        public V getValue() {
            return Int2ObjectOpenHashMap.this.value[this.index];
        }

        @Override
        public V right() {
            return Int2ObjectOpenHashMap.this.value[this.index];
        }

        @Override
        public V setValue(V v2) {
            Object oldValue = Int2ObjectOpenHashMap.this.value[this.index];
            Int2ObjectOpenHashMap.this.value[this.index] = v2;
            return oldValue;
        }

        public IntObjectPair<V> right(V v2) {
            Int2ObjectOpenHashMap.this.value[this.index] = v2;
            return this;
        }

        @Override
        @Deprecated
        public Integer getKey() {
            return Int2ObjectOpenHashMap.this.key[this.index];
        }

        @Override
        public boolean equals(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            return Int2ObjectOpenHashMap.this.key[this.index] == (Integer)e2.getKey() && Objects.equals(Int2ObjectOpenHashMap.this.value[this.index], e2.getValue());
        }

        @Override
        public int hashCode() {
            return Int2ObjectOpenHashMap.this.key[this.index] ^ (Int2ObjectOpenHashMap.this.value[this.index] == null ? 0 : Int2ObjectOpenHashMap.this.value[this.index].hashCode());
        }

        public String toString() {
            return Int2ObjectOpenHashMap.this.key[this.index] + "=>" + Int2ObjectOpenHashMap.this.value[this.index];
        }
    }
}

