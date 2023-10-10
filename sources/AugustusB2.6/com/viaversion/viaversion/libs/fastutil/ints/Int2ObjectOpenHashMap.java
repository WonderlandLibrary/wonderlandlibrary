// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.function.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import java.util.NoSuchElementException;
import com.viaversion.viaversion.libs.fastutil.Pair;
import java.util.Set;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.Hash;
import java.io.Serializable;

public class Int2ObjectOpenHashMap<V> extends AbstractInt2ObjectMap<V> implements Serializable, Cloneable, Hash
{
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
    
    public Int2ObjectOpenHashMap(final int expected, final float f) {
        if (f <= 0.0f || f >= 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        final int arraySize = HashCommon.arraySize(expected, f);
        this.n = arraySize;
        this.minN = arraySize;
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = new int[this.n + 1];
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Int2ObjectOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Int2ObjectOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Int2ObjectOpenHashMap(final Map<? extends Integer, ? extends V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Int2ObjectOpenHashMap(final Map<? extends Integer, ? extends V> m) {
        this(m, 0.75f);
    }
    
    public Int2ObjectOpenHashMap(final Int2ObjectMap<V> m, final float f) {
        this(m.size(), f);
        this.putAll((Map<? extends Integer, ? extends V>)m);
    }
    
    public Int2ObjectOpenHashMap(final Int2ObjectMap<V> m) {
        this(m, 0.75f);
    }
    
    public Int2ObjectOpenHashMap(final int[] k, final V[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Int2ObjectOpenHashMap(final int[] k, final V[] v) {
        this(k, v, 0.75f);
    }
    
    private int realSize() {
        return this.containsNullKey ? (this.size - 1) : this.size;
    }
    
    private void ensureCapacity(final int capacity) {
        final int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private void tryCapacity(final long capacity) {
        final int needed = (int)Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(capacity / this.f))));
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private V removeEntry(final int pos) {
        final V oldValue = this.value[pos];
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
        final V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends V> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final int k) {
        if (k == 0) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return -(pos + 1);
        }
        if (k == curr) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final int k, final V v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    @Override
    public V put(final int k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    protected final void shiftKeys(int pos) {
        final int[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            int curr;
            while ((curr = key[pos]) != 0) {
                final int slot = HashCommon.mix(curr) & this.mask;
                Label_0094: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0094;
                        }
                        if (slot > pos) {
                            break Label_0094;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0094;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                this.value[last] = this.value[pos];
                continue Label_0006;
            }
            break;
        }
        key[last] = 0;
        this.value[last] = null;
    }
    
    @Override
    public V remove(final int k) {
        if (k == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final int[] key = this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
                return this.defRetValue;
            }
            if (k == curr) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (k == curr) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    @Override
    public V get(final int k) {
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final int k) {
        if (k == 0) {
            return this.containsNullKey;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return false;
        }
        if (k == curr) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
        final int[] key = this.key;
        if (this.containsNullKey && Objects.equals(value[this.n], v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != 0 && Objects.equals(value[i], v)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public V getOrDefault(final int k, final V defaultValue) {
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return defaultValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    @Override
    public V putIfAbsent(final int k, final V v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    @Override
    public boolean remove(final int k, final Object v) {
        if (k == 0) {
            if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final int[] key = this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
                return false;
            }
            if (k == curr && Objects.equals(v, this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (k == curr && Objects.equals(v, this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    @Override
    public boolean replace(final int k, final V oldValue, final V v) {
        final int pos = this.find(k);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    @Override
    public V replace(final int k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    @Override
    public V computeIfAbsent(final int k, final IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final V newValue = (V)mappingFunction.apply(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    @Override
    public V computeIfAbsent(final int key, final Int2ObjectFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(key);
        if (pos >= 0) {
            return this.value[pos];
        }
        if (!mappingFunction.containsKey(key)) {
            return this.defRetValue;
        }
        final V newValue = (V)mappingFunction.get(key);
        this.insert(-pos - 1, key, newValue);
        return newValue;
    }
    
    @Override
    public V computeIfPresent(final int k, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V newValue = (V)remappingFunction.apply(Integer.valueOf(k), (Object)this.value[pos]);
        if (newValue == null) {
            if (k == 0) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    @Override
    public V compute(final int k, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final V newValue = (V)remappingFunction.apply(Integer.valueOf(k), (Object)((pos >= 0) ? this.value[pos] : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (k == 0) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final V newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    @Override
    public V merge(final int k, final V v, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0 || this.value[pos] == null) {
            if (v == null) {
                return this.defRetValue;
            }
            this.insert(-pos - 1, k, v);
            return v;
        }
        else {
            final V newValue = (V)remappingFunction.apply((Object)this.value[pos], (Object)v);
            if (newValue == null) {
                if (k == 0) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
                return this.defRetValue;
            }
            return this.value[pos] = newValue;
        }
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
    
    @Override
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
            this.values = new AbstractObjectCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public ObjectSpliterator<V> spliterator() {
                    return new ValueSpliterator();
                }
                
                @Override
                public void forEach(final Consumer<? super V> consumer) {
                    if (Int2ObjectOpenHashMap.this.containsNullKey) {
                        consumer.accept((Object)Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n]);
                    }
                    int pos = Int2ObjectOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Int2ObjectOpenHashMap.this.key[pos] != 0) {
                            consumer.accept((Object)Int2ObjectOpenHashMap.this.value[pos]);
                        }
                    }
                }
                
                @Override
                public int size() {
                    return Int2ObjectOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final Object v) {
                    return Int2ObjectOpenHashMap.this.containsValue(v);
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
    
    public boolean trim(final int n) {
        final int l = HashCommon.nextPowerOfTwo((int)Math.ceil(n / this.f));
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }
    
    protected void rehash(final int newN) {
        final int[] key = this.key;
        final V[] value = this.value;
        final int mask = newN - 1;
        final int[] newKey = new int[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(key[i]) & mask)] != 0) {
                while (newKey[pos = (pos + 1 & mask)] != 0) {}
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
        }
        newValue[newN] = value[this.n];
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
    
    public Int2ObjectOpenHashMap<V> clone() {
        Int2ObjectOpenHashMap<V> c;
        try {
            c = (Int2ObjectOpenHashMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = this.key.clone();
        c.value = this.value.clone();
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        int t = 0;
        while (j-- != 0) {
            while (this.key[i] == 0) {
                ++i;
            }
            t = this.key[i];
            if (this != this.value[i]) {
                t ^= ((this.value[i] == null) ? 0 : this.value[i].hashCode());
            }
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += ((this.value[this.n] == null) ? 0 : this.value[this.n].hashCode());
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final int[] key = this.key;
        final V[] value = this.value;
        final EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeInt(key[e]);
            s.writeObject(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final int[] key2 = new int[this.n + 1];
        this.key = key2;
        final int[] key = key2;
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final int k = s.readInt();
            final V v = (V)s.readObject();
            int pos;
            if (k == 0) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(k) & this.mask); key[pos] != 0; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Int2ObjectMap.Entry<V>, Map.Entry<Integer, V>, IntObjectPair<V>
    {
        int index;
        
        MapEntry(final int index) {
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
        public V setValue(final V v) {
            final V oldValue = Int2ObjectOpenHashMap.this.value[this.index];
            Int2ObjectOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Override
        public IntObjectPair<V> right(final V v) {
            Int2ObjectOpenHashMap.this.value[this.index] = v;
            return this;
        }
        
        @Deprecated
        @Override
        public Integer getKey() {
            return Int2ObjectOpenHashMap.this.key[this.index];
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Integer, V> e = (Map.Entry<Integer, V>)o;
            return Int2ObjectOpenHashMap.this.key[this.index] == e.getKey() && Objects.equals(Int2ObjectOpenHashMap.this.value[this.index], e.getValue());
        }
        
        @Override
        public int hashCode() {
            return Int2ObjectOpenHashMap.this.key[this.index] ^ ((Int2ObjectOpenHashMap.this.value[this.index] == null) ? 0 : Int2ObjectOpenHashMap.this.value[this.index].hashCode());
        }
        
        @Override
        public String toString() {
            return Int2ObjectOpenHashMap.this.key[this.index] + "=>" + Int2ObjectOpenHashMap.this.value[this.index];
        }
    }
    
    private abstract class MapIterator<ConsumerType>
    {
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
        
        abstract void acceptOnIndex(final ConsumerType p0, final int p1);
        
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
                return this.last = Int2ObjectOpenHashMap.this.n;
            }
            final int[] key = Int2ObjectOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            int k;
            int p;
            for (k = this.wrapped.getInt(-this.pos - 1), p = (HashCommon.mix(k) & Int2ObjectOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Int2ObjectOpenHashMap.this.mask)) {}
            return p;
        }
        
        public void forEachRemaining(final ConsumerType action) {
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                this.acceptOnIndex(action, this.last = Int2ObjectOpenHashMap.this.n);
                --this.c;
            }
            final int[] key = Int2ObjectOpenHashMap.this.key;
            while (this.c != 0) {
                if (--this.pos < 0) {
                    this.last = Integer.MIN_VALUE;
                    int k;
                    int p;
                    for (k = this.wrapped.getInt(-this.pos - 1), p = (HashCommon.mix(k) & Int2ObjectOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Int2ObjectOpenHashMap.this.mask)) {}
                    this.acceptOnIndex(action, p);
                    --this.c;
                }
                else {
                    if (key[this.pos] == 0) {
                        continue;
                    }
                    this.acceptOnIndex(action, this.last = this.pos);
                    --this.c;
                }
            }
        }
        
        private void shiftKeys(int pos) {
            final int[] key = Int2ObjectOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Int2ObjectOpenHashMap.this.mask);
                int curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(curr) & Int2ObjectOpenHashMap.this.mask;
                    Label_0109: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0109;
                            }
                            if (slot > pos) {
                                break Label_0109;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0109;
                        }
                        pos = (pos + 1 & Int2ObjectOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new IntArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Int2ObjectOpenHashMap.this.value[last] = Int2ObjectOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0;
            Int2ObjectOpenHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Int2ObjectOpenHashMap.this.n) {
                Int2ObjectOpenHashMap.this.containsNullKey = false;
                Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Int2ObjectOpenHashMap.this.remove(this.wrapped.getInt(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Int2ObjectOpenHashMap this$0 = Int2ObjectOpenHashMap.this;
            --this$0.size;
            this.last = -1;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
    }
    
    private final class EntryIterator extends MapIterator<Consumer<? super Int2ObjectMap.Entry<V>>> implements ObjectIterator<Int2ObjectMap.Entry<V>>
    {
        private MapEntry entry;
        
        @Override
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super Int2ObjectMap.Entry<V>> action, final int index) {
            action.accept(this.entry = new MapEntry(index));
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private final class FastEntryIterator extends MapIterator<Consumer<? super Int2ObjectMap.Entry<V>>> implements ObjectIterator<Int2ObjectMap.Entry<V>>
    {
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
        final void acceptOnIndex(final Consumer<? super Int2ObjectMap.Entry<V>> action, final int index) {
            this.entry.index = index;
            action.accept(this.entry);
        }
    }
    
    private abstract class MapSpliterator<ConsumerType, SplitType extends MapSpliterator<ConsumerType, SplitType>>
    {
        int pos;
        int max;
        int c;
        boolean mustReturnNull;
        boolean hasSplit;
        
        MapSpliterator() {
            this.pos = 0;
            this.max = Int2ObjectOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Int2ObjectOpenHashMap.this.containsNullKey;
            this.hasSplit = false;
        }
        
        MapSpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            this.pos = 0;
            this.max = Int2ObjectOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Int2ObjectOpenHashMap.this.containsNullKey;
            this.hasSplit = false;
            this.pos = pos;
            this.max = max;
            this.mustReturnNull = mustReturnNull;
            this.hasSplit = hasSplit;
        }
        
        abstract void acceptOnIndex(final ConsumerType p0, final int p1);
        
        abstract SplitType makeForSplit(final int p0, final int p1, final boolean p2);
        
        public boolean tryAdvance(final ConsumerType action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++this.c;
                this.acceptOnIndex(action, Int2ObjectOpenHashMap.this.n);
                return true;
            }
            final int[] key = Int2ObjectOpenHashMap.this.key;
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
        
        public void forEachRemaining(final ConsumerType action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++this.c;
                this.acceptOnIndex(action, Int2ObjectOpenHashMap.this.n);
            }
            final int[] key = Int2ObjectOpenHashMap.this.key;
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
            return Math.min(Int2ObjectOpenHashMap.this.size - this.c, (long)(Int2ObjectOpenHashMap.this.realSize() / (double)Int2ObjectOpenHashMap.this.n * (this.max - this.pos)) + (long)(this.mustReturnNull ? 1 : 0));
        }
        
        public SplitType trySplit() {
            if (this.pos >= this.max - 1) {
                return null;
            }
            final int retLen = this.max - this.pos >> 1;
            if (retLen <= 1) {
                return null;
            }
            final int myNewPos = this.pos + retLen;
            final int retPos = this.pos;
            final int retMax = myNewPos;
            final SplitType split = this.makeForSplit(retPos, retMax, this.mustReturnNull);
            this.pos = myNewPos;
            this.mustReturnNull = false;
            this.hasSplit = true;
            return split;
        }
        
        public long skip(long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0L) {
                return 0L;
            }
            long skipped = 0L;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++skipped;
                --n;
            }
            final int[] key = Int2ObjectOpenHashMap.this.key;
            while (this.pos < this.max && n > 0L) {
                if (key[this.pos++] != 0) {
                    ++skipped;
                    --n;
                }
            }
            return skipped;
        }
    }
    
    private final class EntrySpliterator extends MapSpliterator<Consumer<? super Int2ObjectMap.Entry<V>>, EntrySpliterator> implements ObjectSpliterator<Int2ObjectMap.Entry<V>>
    {
        private static final int POST_SPLIT_CHARACTERISTICS = 1;
        
        EntrySpliterator() {
        }
        
        EntrySpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }
        
        @Override
        public int characteristics() {
            return this.hasSplit ? 1 : 65;
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super Int2ObjectMap.Entry<V>> action, final int index) {
            action.accept((Object)new MapEntry(index));
        }
        
        @Override
        final EntrySpliterator makeForSplit(final int pos, final int max, final boolean mustReturnNull) {
            return new EntrySpliterator(pos, max, mustReturnNull, true);
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Int2ObjectMap.Entry<V>> implements Int2ObjectMap.FastEntrySet<V>
    {
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
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            final V v = (V)e.getValue();
            if (k == 0) {
                return Int2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n], v);
            }
            final int[] key = Int2ObjectOpenHashMap.this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & Int2ObjectOpenHashMap.this.mask)]) == 0) {
                return false;
            }
            if (k == curr) {
                return Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v);
            }
            while ((curr = key[pos = (pos + 1 & Int2ObjectOpenHashMap.this.mask)]) != 0) {
                if (k == curr) {
                    return Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v);
                }
            }
            return false;
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            final V v = (V)e.getValue();
            if (k == 0) {
                if (Int2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n], v)) {
                    Int2ObjectOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final int[] key = Int2ObjectOpenHashMap.this.key;
                int pos;
                int curr;
                if ((curr = key[pos = (HashCommon.mix(k) & Int2ObjectOpenHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Int2ObjectOpenHashMap.this.mask)]) != 0) {
                        if (curr == k && Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v)) {
                            Int2ObjectOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Objects.equals(Int2ObjectOpenHashMap.this.value[pos], v)) {
                    Int2ObjectOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
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
        public void forEach(final Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept((Object)new BasicEntry(Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n], (V)Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n]));
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Int2ObjectOpenHashMap.this.key[pos] != 0) {
                    consumer.accept((Object)new BasicEntry(Int2ObjectOpenHashMap.this.key[pos], (V)Int2ObjectOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                entry.key = Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n];
                entry.value = Int2ObjectOpenHashMap.this.value[Int2ObjectOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Int2ObjectOpenHashMap.this.key[pos] != 0) {
                    entry.key = Int2ObjectOpenHashMap.this.key[pos];
                    entry.value = Int2ObjectOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator<IntConsumer> implements IntIterator
    {
        public KeyIterator() {
        }
        
        @Override
        final void acceptOnIndex(final IntConsumer action, final int index) {
            action.accept(Int2ObjectOpenHashMap.this.key[index]);
        }
        
        @Override
        public int nextInt() {
            return Int2ObjectOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySpliterator extends MapSpliterator<IntConsumer, KeySpliterator> implements IntSpliterator
    {
        private static final int POST_SPLIT_CHARACTERISTICS = 257;
        
        KeySpliterator() {
        }
        
        KeySpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }
        
        @Override
        public int characteristics() {
            return this.hasSplit ? 257 : 321;
        }
        
        @Override
        final void acceptOnIndex(final IntConsumer action, final int index) {
            action.accept(Int2ObjectOpenHashMap.this.key[index]);
        }
        
        @Override
        final KeySpliterator makeForSplit(final int pos, final int max, final boolean mustReturnNull) {
            return new KeySpliterator(pos, max, mustReturnNull, true);
        }
    }
    
    private final class KeySet extends AbstractIntSet
    {
        @Override
        public IntIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public IntSpliterator spliterator() {
            return new KeySpliterator();
        }
        
        @Override
        public void forEach(final IntConsumer consumer) {
            if (Int2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept(Int2ObjectOpenHashMap.this.key[Int2ObjectOpenHashMap.this.n]);
            }
            int pos = Int2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                final int k = Int2ObjectOpenHashMap.this.key[pos];
                if (k != 0) {
                    consumer.accept(k);
                }
            }
        }
        
        @Override
        public int size() {
            return Int2ObjectOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final int k) {
            return Int2ObjectOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final int k) {
            final int oldSize = Int2ObjectOpenHashMap.this.size;
            Int2ObjectOpenHashMap.this.remove(k);
            return Int2ObjectOpenHashMap.this.size != oldSize;
        }
        
        @Override
        public void clear() {
            Int2ObjectOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator<Consumer<? super V>> implements ObjectIterator<V>
    {
        public ValueIterator() {
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super V> action, final int index) {
            action.accept((Object)Int2ObjectOpenHashMap.this.value[index]);
        }
        
        @Override
        public V next() {
            return Int2ObjectOpenHashMap.this.value[this.nextEntry()];
        }
    }
    
    private final class ValueSpliterator extends MapSpliterator<Consumer<? super V>, ValueSpliterator> implements ObjectSpliterator<V>
    {
        private static final int POST_SPLIT_CHARACTERISTICS = 0;
        
        ValueSpliterator() {
        }
        
        ValueSpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }
        
        @Override
        public int characteristics() {
            return this.hasSplit ? 0 : 64;
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super V> action, final int index) {
            action.accept((Object)Int2ObjectOpenHashMap.this.value[index]);
        }
        
        @Override
        final ValueSpliterator makeForSplit(final int pos, final int max, final boolean mustReturnNull) {
            return new ValueSpliterator(pos, max, mustReturnNull, true);
        }
    }
}
