// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.Hash;
import java.io.Serializable;

public class Object2IntOpenHashMap<K> extends AbstractObject2IntMap<K> implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient int[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Object2IntMap.FastEntrySet<K> entries;
    protected transient ObjectSet<K> keys;
    protected transient IntCollection values;
    
    public Object2IntOpenHashMap(final int expected, final float f) {
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
        this.key = (K[])new Object[this.n + 1];
        this.value = new int[this.n + 1];
    }
    
    public Object2IntOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Object2IntOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Object2IntOpenHashMap(final Map<? extends K, ? extends Integer> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Object2IntOpenHashMap(final Map<? extends K, ? extends Integer> m) {
        this(m, 0.75f);
    }
    
    public Object2IntOpenHashMap(final Object2IntMap<K> m, final float f) {
        this(m.size(), f);
        this.putAll((Map<? extends K, ? extends Integer>)m);
    }
    
    public Object2IntOpenHashMap(final Object2IntMap<K> m) {
        this(m, 0.75f);
    }
    
    public Object2IntOpenHashMap(final K[] k, final int[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2IntOpenHashMap(final K[] k, final int[] v) {
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
    
    private int removeEntry(final int pos) {
        final int oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private int removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final int oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Integer> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final K k) {
        if (k == null) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return -(pos + 1);
        }
        if (k.equals(curr)) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final K k, final int v) {
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
    public int put(final K k, final int v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private int addToValue(final int pos, final int incr) {
        final int oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public int addTo(final K k, final int incr) {
        int pos;
        if (k == null) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) != null) {
                if (curr.equals(k)) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (curr.equals(k)) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = this.defRetValue + incr;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }
    
    protected final void shiftKeys(int pos) {
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
                final int slot = HashCommon.mix(curr.hashCode()) & this.mask;
                Label_0090: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0090;
                        }
                        if (slot > pos) {
                            break Label_0090;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0090;
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
        key[last] = null;
    }
    
    @Override
    public int removeInt(final Object k) {
        if (k == null) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (k.equals(curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k.equals(curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    @Override
    public int getInt(final Object k) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return this.defRetValue;
        }
        if (k.equals(curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        if (k == null) {
            return this.containsNullKey;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return false;
        }
        if (k.equals(curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final int v) {
        final int[] value = this.value;
        final K[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != null && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getOrDefault(final Object k, final int defaultValue) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return defaultValue;
        }
        if (k.equals(curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    @Override
    public int putIfAbsent(final K k, final int v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    @Override
    public boolean remove(final Object k, final int v) {
        if (k == null) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
                return false;
            }
            if (k.equals(curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k.equals(curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    @Override
    public boolean replace(final K k, final int oldValue, final int v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    @Override
    public int replace(final K k, final int v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final int oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    @Override
    public int computeIfAbsent(final K k, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final int newValue = mappingFunction.applyAsInt((Object)k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    @Override
    public int computeIfAbsent(final K key, final Object2IntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(key);
        if (pos >= 0) {
            return this.value[pos];
        }
        if (!mappingFunction.containsKey(key)) {
            return this.defRetValue;
        }
        final int newValue = mappingFunction.getInt(key);
        this.insert(-pos - 1, key, newValue);
        return newValue;
    }
    
    @Override
    public int computeIntIfPresent(final K k, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Integer newValue = (Integer)remappingFunction.apply((Object)k, Integer.valueOf(this.value[pos]));
        if (newValue == null) {
            if (k == null) {
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
    public int computeInt(final K k, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Integer newValue = (Integer)remappingFunction.apply((Object)k, (pos >= 0) ? Integer.valueOf(this.value[pos]) : null);
        if (newValue == null) {
            if (pos >= 0) {
                if (k == null) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final int newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    @Override
    public int merge(final K k, final int v, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Integer newValue = (Integer)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (k == null) {
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
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, null);
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
    public Object2IntMap.FastEntrySet<K> object2IntEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ObjectSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public IntCollection values() {
        if (this.values == null) {
            this.values = new AbstractIntCollection() {
                @Override
                public IntIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public IntSpliterator spliterator() {
                    return new ValueSpliterator();
                }
                
                @Override
                public void forEach(final IntConsumer consumer) {
                    if (Object2IntOpenHashMap.this.containsNullKey) {
                        consumer.accept(Object2IntOpenHashMap.this.value[Object2IntOpenHashMap.this.n]);
                    }
                    int pos = Object2IntOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Object2IntOpenHashMap.this.key[pos] != null) {
                            consumer.accept(Object2IntOpenHashMap.this.value[pos]);
                        }
                    }
                }
                
                @Override
                public int size() {
                    return Object2IntOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final int v) {
                    return Object2IntOpenHashMap.this.containsValue(v);
                }
                
                @Override
                public void clear() {
                    Object2IntOpenHashMap.this.clear();
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
        final K[] key = this.key;
        final int[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final int[] newValue = new int[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == null) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(key[i].hashCode()) & mask)] != null) {
                while (newKey[pos = (pos + 1 & mask)] != null) {}
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
    
    public Object2IntOpenHashMap<K> clone() {
        Object2IntOpenHashMap<K> c;
        try {
            c = (Object2IntOpenHashMap)super.clone();
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
            while (this.key[i] == null) {
                ++i;
            }
            if (this != this.key[i]) {
                t = this.key[i].hashCode();
            }
            t ^= this.value[i];
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += this.value[this.n];
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final K[] key = this.key;
        final int[] value = this.value;
        final EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeInt(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final Object[] key2 = new Object[this.n + 1];
        this.key = (K[])key2;
        final K[] key = (K[])key2;
        final int[] value2 = new int[this.n + 1];
        this.value = value2;
        final int[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final int v = s.readInt();
            int pos;
            if (k == null) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(k.hashCode()) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Object2IntMap.Entry<K>, Map.Entry<K, Integer>, ObjectIntPair<K>
    {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        @Override
        public K getKey() {
            return Object2IntOpenHashMap.this.key[this.index];
        }
        
        @Override
        public K left() {
            return Object2IntOpenHashMap.this.key[this.index];
        }
        
        @Override
        public int getIntValue() {
            return Object2IntOpenHashMap.this.value[this.index];
        }
        
        @Override
        public int rightInt() {
            return Object2IntOpenHashMap.this.value[this.index];
        }
        
        @Override
        public int setValue(final int v) {
            final int oldValue = Object2IntOpenHashMap.this.value[this.index];
            Object2IntOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Override
        public ObjectIntPair<K> right(final int v) {
            Object2IntOpenHashMap.this.value[this.index] = v;
            return this;
        }
        
        @Deprecated
        @Override
        public Integer getValue() {
            return Object2IntOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        @Override
        public Integer setValue(final Integer v) {
            return this.setValue((int)v);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, Integer> e = (Map.Entry<K, Integer>)o;
            return Objects.equals(Object2IntOpenHashMap.this.key[this.index], e.getKey()) && Object2IntOpenHashMap.this.value[this.index] == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return ((Object2IntOpenHashMap.this.key[this.index] == null) ? 0 : Object2IntOpenHashMap.this.key[this.index].hashCode()) ^ Object2IntOpenHashMap.this.value[this.index];
        }
        
        @Override
        public String toString() {
            return Object2IntOpenHashMap.this.key[this.index] + "=>" + Object2IntOpenHashMap.this.value[this.index];
        }
    }
    
    private abstract class MapIterator<ConsumerType>
    {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ObjectArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Object2IntOpenHashMap.this.n;
            this.last = -1;
            this.c = Object2IntOpenHashMap.this.size;
            this.mustReturnNullKey = Object2IntOpenHashMap.this.containsNullKey;
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
                return this.last = Object2IntOpenHashMap.this.n;
            }
            final K[] key = Object2IntOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(k.hashCode()) & Object2IntOpenHashMap.this.mask); !k.equals(key[p]); p = (p + 1 & Object2IntOpenHashMap.this.mask)) {}
            return p;
        }
        
        public void forEachRemaining(final ConsumerType action) {
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                this.acceptOnIndex(action, this.last = Object2IntOpenHashMap.this.n);
                --this.c;
            }
            final K[] key = Object2IntOpenHashMap.this.key;
            while (this.c != 0) {
                if (--this.pos < 0) {
                    this.last = Integer.MIN_VALUE;
                    K k;
                    int p;
                    for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(k.hashCode()) & Object2IntOpenHashMap.this.mask); !k.equals(key[p]); p = (p + 1 & Object2IntOpenHashMap.this.mask)) {}
                    this.acceptOnIndex(action, p);
                    --this.c;
                }
                else {
                    if (key[this.pos] == null) {
                        continue;
                    }
                    this.acceptOnIndex(action, this.last = this.pos);
                    --this.c;
                }
            }
        }
        
        private void shiftKeys(int pos) {
            final K[] key = Object2IntOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Object2IntOpenHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(curr.hashCode()) & Object2IntOpenHashMap.this.mask;
                    Label_0102: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0102;
                            }
                            if (slot > pos) {
                                break Label_0102;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0102;
                        }
                        pos = (pos + 1 & Object2IntOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ObjectArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Object2IntOpenHashMap.this.value[last] = Object2IntOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Object2IntOpenHashMap.this.n) {
                Object2IntOpenHashMap.this.containsNullKey = false;
                Object2IntOpenHashMap.this.key[Object2IntOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Object2IntOpenHashMap.this.removeInt(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Object2IntOpenHashMap this$0 = Object2IntOpenHashMap.this;
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
    
    private final class EntryIterator extends MapIterator<Consumer<? super Object2IntMap.Entry<K>>> implements ObjectIterator<Object2IntMap.Entry<K>>
    {
        private MapEntry entry;
        
        @Override
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super Object2IntMap.Entry<K>> action, final int index) {
            action.accept(this.entry = new MapEntry(index));
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private final class FastEntryIterator extends MapIterator<Consumer<? super Object2IntMap.Entry<K>>> implements ObjectIterator<Object2IntMap.Entry<K>>
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
        final void acceptOnIndex(final Consumer<? super Object2IntMap.Entry<K>> action, final int index) {
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
            this.max = Object2IntOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Object2IntOpenHashMap.this.containsNullKey;
            this.hasSplit = false;
        }
        
        MapSpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            this.pos = 0;
            this.max = Object2IntOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Object2IntOpenHashMap.this.containsNullKey;
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
                this.acceptOnIndex(action, Object2IntOpenHashMap.this.n);
                return true;
            }
            final K[] key = Object2IntOpenHashMap.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != null) {
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
                this.acceptOnIndex(action, Object2IntOpenHashMap.this.n);
            }
            final K[] key = Object2IntOpenHashMap.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != null) {
                    this.acceptOnIndex(action, this.pos);
                    ++this.c;
                }
                ++this.pos;
            }
        }
        
        public long estimateSize() {
            if (!this.hasSplit) {
                return Object2IntOpenHashMap.this.size - this.c;
            }
            return Math.min(Object2IntOpenHashMap.this.size - this.c, (long)(Object2IntOpenHashMap.this.realSize() / (double)Object2IntOpenHashMap.this.n * (this.max - this.pos)) + (long)(this.mustReturnNull ? 1 : 0));
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
            final K[] key = Object2IntOpenHashMap.this.key;
            while (this.pos < this.max && n > 0L) {
                if (key[this.pos++] != null) {
                    ++skipped;
                    --n;
                }
            }
            return skipped;
        }
    }
    
    private final class EntrySpliterator extends MapSpliterator<Consumer<? super Object2IntMap.Entry<K>>, EntrySpliterator> implements ObjectSpliterator<Object2IntMap.Entry<K>>
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
        final void acceptOnIndex(final Consumer<? super Object2IntMap.Entry<K>> action, final int index) {
            action.accept((Object)new MapEntry(index));
        }
        
        @Override
        final EntrySpliterator makeForSplit(final int pos, final int max, final boolean mustReturnNull) {
            return new EntrySpliterator(pos, max, mustReturnNull, true);
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Object2IntMap.Entry<K>> implements Object2IntMap.FastEntrySet<K>
    {
        @Override
        public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Object2IntMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectSpliterator<Object2IntMap.Entry<K>> spliterator() {
            return new EntrySpliterator();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final K k = (K)e.getKey();
            final int v = (int)e.getValue();
            if (k == null) {
                return Object2IntOpenHashMap.this.containsNullKey && Object2IntOpenHashMap.this.value[Object2IntOpenHashMap.this.n] == v;
            }
            final K[] key = Object2IntOpenHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2IntOpenHashMap.this.mask)]) == null) {
                return false;
            }
            if (k.equals(curr)) {
                return Object2IntOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Object2IntOpenHashMap.this.mask)]) != null) {
                if (k.equals(curr)) {
                    return Object2IntOpenHashMap.this.value[pos] == v;
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
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final K k = (K)e.getKey();
            final int v = (int)e.getValue();
            if (k == null) {
                if (Object2IntOpenHashMap.this.containsNullKey && Object2IntOpenHashMap.this.value[Object2IntOpenHashMap.this.n] == v) {
                    Object2IntOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Object2IntOpenHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2IntOpenHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!curr.equals(k)) {
                    while ((curr = key[pos = (pos + 1 & Object2IntOpenHashMap.this.mask)]) != null) {
                        if (curr.equals(k) && Object2IntOpenHashMap.this.value[pos] == v) {
                            Object2IntOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Object2IntOpenHashMap.this.value[pos] == v) {
                    Object2IntOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        @Override
        public int size() {
            return Object2IntOpenHashMap.this.size;
        }
        
        @Override
        public void clear() {
            Object2IntOpenHashMap.this.clear();
        }
        
        @Override
        public void forEach(final Consumer<? super Object2IntMap.Entry<K>> consumer) {
            if (Object2IntOpenHashMap.this.containsNullKey) {
                consumer.accept((Object)new BasicEntry((K)Object2IntOpenHashMap.this.key[Object2IntOpenHashMap.this.n], Object2IntOpenHashMap.this.value[Object2IntOpenHashMap.this.n]));
            }
            int pos = Object2IntOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2IntOpenHashMap.this.key[pos] != null) {
                    consumer.accept((Object)new BasicEntry((K)Object2IntOpenHashMap.this.key[pos], Object2IntOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Object2IntMap.Entry<K>> consumer) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            if (Object2IntOpenHashMap.this.containsNullKey) {
                entry.key = Object2IntOpenHashMap.this.key[Object2IntOpenHashMap.this.n];
                entry.value = Object2IntOpenHashMap.this.value[Object2IntOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Object2IntOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2IntOpenHashMap.this.key[pos] != null) {
                    entry.key = Object2IntOpenHashMap.this.key[pos];
                    entry.value = Object2IntOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator<Consumer<? super K>> implements ObjectIterator<K>
    {
        public KeyIterator() {
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super K> action, final int index) {
            action.accept((Object)Object2IntOpenHashMap.this.key[index]);
        }
        
        @Override
        public K next() {
            return Object2IntOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySpliterator extends MapSpliterator<Consumer<? super K>, KeySpliterator> implements ObjectSpliterator<K>
    {
        private static final int POST_SPLIT_CHARACTERISTICS = 1;
        
        KeySpliterator() {
        }
        
        KeySpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }
        
        @Override
        public int characteristics() {
            return this.hasSplit ? 1 : 65;
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super K> action, final int index) {
            action.accept((Object)Object2IntOpenHashMap.this.key[index]);
        }
        
        @Override
        final KeySpliterator makeForSplit(final int pos, final int max, final boolean mustReturnNull) {
            return new KeySpliterator(pos, max, mustReturnNull, true);
        }
    }
    
    private final class KeySet extends AbstractObjectSet<K>
    {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return new KeySpliterator();
        }
        
        @Override
        public void forEach(final Consumer<? super K> consumer) {
            if (Object2IntOpenHashMap.this.containsNullKey) {
                consumer.accept((Object)Object2IntOpenHashMap.this.key[Object2IntOpenHashMap.this.n]);
            }
            int pos = Object2IntOpenHashMap.this.n;
            while (pos-- != 0) {
                final K k = Object2IntOpenHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept((Object)k);
                }
            }
        }
        
        @Override
        public int size() {
            return Object2IntOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final Object k) {
            return Object2IntOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final Object k) {
            final int oldSize = Object2IntOpenHashMap.this.size;
            Object2IntOpenHashMap.this.removeInt(k);
            return Object2IntOpenHashMap.this.size != oldSize;
        }
        
        @Override
        public void clear() {
            Object2IntOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator<IntConsumer> implements IntIterator
    {
        public ValueIterator() {
        }
        
        @Override
        final void acceptOnIndex(final IntConsumer action, final int index) {
            action.accept(Object2IntOpenHashMap.this.value[index]);
        }
        
        @Override
        public int nextInt() {
            return Object2IntOpenHashMap.this.value[this.nextEntry()];
        }
    }
    
    private final class ValueSpliterator extends MapSpliterator<IntConsumer, ValueSpliterator> implements IntSpliterator
    {
        private static final int POST_SPLIT_CHARACTERISTICS = 256;
        
        ValueSpliterator() {
        }
        
        ValueSpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            super(pos, max, mustReturnNull, hasSplit);
        }
        
        @Override
        public int characteristics() {
            return this.hasSplit ? 256 : 320;
        }
        
        @Override
        final void acceptOnIndex(final IntConsumer action, final int index) {
            action.accept(Object2IntOpenHashMap.this.value[index]);
        }
        
        @Override
        final ValueSpliterator makeForSplit(final int pos, final int max, final boolean mustReturnNull) {
            return new ValueSpliterator(pos, max, mustReturnNull, true);
        }
    }
}
