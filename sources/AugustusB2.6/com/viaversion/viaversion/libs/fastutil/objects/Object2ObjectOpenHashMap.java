// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.NoSuchElementException;
import com.viaversion.viaversion.libs.fastutil.Pair;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import com.viaversion.viaversion.libs.fastutil.Hash;
import java.io.Serializable;

public class Object2ObjectOpenHashMap<K, V> extends AbstractObject2ObjectMap<K, V> implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Object2ObjectMap.FastEntrySet<K, V> entries;
    protected transient ObjectSet<K> keys;
    protected transient ObjectCollection<V> values;
    
    public Object2ObjectOpenHashMap(final int expected, final float f) {
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
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Object2ObjectOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Object2ObjectOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Object2ObjectOpenHashMap(final Map<? extends K, ? extends V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Object2ObjectOpenHashMap(final Map<? extends K, ? extends V> m) {
        this(m, 0.75f);
    }
    
    public Object2ObjectOpenHashMap(final Object2ObjectMap<K, V> m, final float f) {
        this(m.size(), f);
        this.putAll((Map<? extends K, ? extends V>)m);
    }
    
    public Object2ObjectOpenHashMap(final Object2ObjectMap<K, V> m) {
        this(m, 0.75f);
    }
    
    public Object2ObjectOpenHashMap(final K[] k, final V[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2ObjectOpenHashMap(final K[] k, final V[] v) {
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
        this.key[this.n] = null;
        final V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
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
    
    private void insert(final int pos, final K k, final V v) {
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
    public V put(final K k, final V v) {
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
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
                final int slot = HashCommon.mix(curr.hashCode()) & this.mask;
                Label_0097: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0097;
                        }
                        if (slot > pos) {
                            break Label_0097;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0097;
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
        this.value[last] = null;
    }
    
    @Override
    public V remove(final Object k) {
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
    public V get(final Object k) {
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
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
        final K[] key = this.key;
        if (this.containsNullKey && Objects.equals(value[this.n], v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != null && Objects.equals(value[i], v)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public V getOrDefault(final Object k, final V defaultValue) {
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
    public V putIfAbsent(final K k, final V v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    @Override
    public boolean remove(final Object k, final Object v) {
        if (k == null) {
            if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
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
            if (k.equals(curr) && Objects.equals(v, this.value[pos])) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k.equals(curr) && Objects.equals(v, this.value[pos])) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    @Override
    public boolean replace(final K k, final V oldValue, final V v) {
        final int pos = this.find(k);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    @Override
    public V replace(final K k, final V v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    @Override
    public V computeIfAbsent(final K key, final Object2ObjectFunction<? super K, ? extends V> mappingFunction) {
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
    public V computeIfPresent(final K k, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V newValue = (V)remappingFunction.apply((Object)k, (Object)this.value[pos]);
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
    public V compute(final K k, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final V newValue = (V)remappingFunction.apply((Object)k, (Object)((pos >= 0) ? this.value[pos] : null));
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
        final V newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    @Override
    public V merge(final K k, final V v, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
    }
    
    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, null);
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
    public Object2ObjectMap.FastEntrySet<K, V> object2ObjectEntrySet() {
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
                    if (Object2ObjectOpenHashMap.this.containsNullKey) {
                        consumer.accept((Object)Object2ObjectOpenHashMap.this.value[Object2ObjectOpenHashMap.this.n]);
                    }
                    int pos = Object2ObjectOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Object2ObjectOpenHashMap.this.key[pos] != null) {
                            consumer.accept((Object)Object2ObjectOpenHashMap.this.value[pos]);
                        }
                    }
                }
                
                @Override
                public int size() {
                    return Object2ObjectOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final Object v) {
                    return Object2ObjectOpenHashMap.this.containsValue(v);
                }
                
                @Override
                public void clear() {
                    Object2ObjectOpenHashMap.this.clear();
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
        final V[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
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
    
    public Object2ObjectOpenHashMap<K, V> clone() {
        Object2ObjectOpenHashMap<K, V> c;
        try {
            c = (Object2ObjectOpenHashMap)super.clone();
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
        final K[] key = this.key;
        final V[] value = this.value;
        final EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeObject(value[e]);
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
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final V v = (V)s.readObject();
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
    
    final class MapEntry implements Object2ObjectMap.Entry<K, V>, Map.Entry<K, V>, Pair<K, V>
    {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        @Override
        public K getKey() {
            return Object2ObjectOpenHashMap.this.key[this.index];
        }
        
        @Override
        public K left() {
            return Object2ObjectOpenHashMap.this.key[this.index];
        }
        
        @Override
        public V getValue() {
            return Object2ObjectOpenHashMap.this.value[this.index];
        }
        
        @Override
        public V right() {
            return Object2ObjectOpenHashMap.this.value[this.index];
        }
        
        @Override
        public V setValue(final V v) {
            final V oldValue = Object2ObjectOpenHashMap.this.value[this.index];
            Object2ObjectOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Override
        public Pair<K, V> right(final V v) {
            Object2ObjectOpenHashMap.this.value[this.index] = v;
            return this;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, V> e = (Map.Entry<K, V>)o;
            return Objects.equals(Object2ObjectOpenHashMap.this.key[this.index], e.getKey()) && Objects.equals(Object2ObjectOpenHashMap.this.value[this.index], e.getValue());
        }
        
        @Override
        public int hashCode() {
            return ((Object2ObjectOpenHashMap.this.key[this.index] == null) ? 0 : Object2ObjectOpenHashMap.this.key[this.index].hashCode()) ^ ((Object2ObjectOpenHashMap.this.value[this.index] == null) ? 0 : Object2ObjectOpenHashMap.this.value[this.index].hashCode());
        }
        
        @Override
        public String toString() {
            return Object2ObjectOpenHashMap.this.key[this.index] + "=>" + Object2ObjectOpenHashMap.this.value[this.index];
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
            this.pos = Object2ObjectOpenHashMap.this.n;
            this.last = -1;
            this.c = Object2ObjectOpenHashMap.this.size;
            this.mustReturnNullKey = Object2ObjectOpenHashMap.this.containsNullKey;
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
                return this.last = Object2ObjectOpenHashMap.this.n;
            }
            final K[] key = Object2ObjectOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(k.hashCode()) & Object2ObjectOpenHashMap.this.mask); !k.equals(key[p]); p = (p + 1 & Object2ObjectOpenHashMap.this.mask)) {}
            return p;
        }
        
        public void forEachRemaining(final ConsumerType action) {
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                this.acceptOnIndex(action, this.last = Object2ObjectOpenHashMap.this.n);
                --this.c;
            }
            final K[] key = Object2ObjectOpenHashMap.this.key;
            while (this.c != 0) {
                if (--this.pos < 0) {
                    this.last = Integer.MIN_VALUE;
                    K k;
                    int p;
                    for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(k.hashCode()) & Object2ObjectOpenHashMap.this.mask); !k.equals(key[p]); p = (p + 1 & Object2ObjectOpenHashMap.this.mask)) {}
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
            final K[] key = Object2ObjectOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Object2ObjectOpenHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(curr.hashCode()) & Object2ObjectOpenHashMap.this.mask;
                    Label_0112: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0112;
                            }
                            if (slot > pos) {
                                break Label_0112;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0112;
                        }
                        pos = (pos + 1 & Object2ObjectOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ObjectArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Object2ObjectOpenHashMap.this.value[last] = Object2ObjectOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
            Object2ObjectOpenHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Object2ObjectOpenHashMap.this.n) {
                Object2ObjectOpenHashMap.this.containsNullKey = false;
                Object2ObjectOpenHashMap.this.key[Object2ObjectOpenHashMap.this.n] = null;
                Object2ObjectOpenHashMap.this.value[Object2ObjectOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Object2ObjectOpenHashMap.this.remove(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Object2ObjectOpenHashMap this$0 = Object2ObjectOpenHashMap.this;
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
    
    private final class EntryIterator extends MapIterator<Consumer<? super Object2ObjectMap.Entry<K, V>>> implements ObjectIterator<Object2ObjectMap.Entry<K, V>>
    {
        private MapEntry entry;
        
        @Override
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super Object2ObjectMap.Entry<K, V>> action, final int index) {
            action.accept(this.entry = new MapEntry(index));
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private final class FastEntryIterator extends MapIterator<Consumer<? super Object2ObjectMap.Entry<K, V>>> implements ObjectIterator<Object2ObjectMap.Entry<K, V>>
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
        final void acceptOnIndex(final Consumer<? super Object2ObjectMap.Entry<K, V>> action, final int index) {
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
            this.max = Object2ObjectOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Object2ObjectOpenHashMap.this.containsNullKey;
            this.hasSplit = false;
        }
        
        MapSpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            this.pos = 0;
            this.max = Object2ObjectOpenHashMap.this.n;
            this.c = 0;
            this.mustReturnNull = Object2ObjectOpenHashMap.this.containsNullKey;
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
                this.acceptOnIndex(action, Object2ObjectOpenHashMap.this.n);
                return true;
            }
            final K[] key = Object2ObjectOpenHashMap.this.key;
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
                this.acceptOnIndex(action, Object2ObjectOpenHashMap.this.n);
            }
            final K[] key = Object2ObjectOpenHashMap.this.key;
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
                return Object2ObjectOpenHashMap.this.size - this.c;
            }
            return Math.min(Object2ObjectOpenHashMap.this.size - this.c, (long)(Object2ObjectOpenHashMap.this.realSize() / (double)Object2ObjectOpenHashMap.this.n * (this.max - this.pos)) + (long)(this.mustReturnNull ? 1 : 0));
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
            final K[] key = Object2ObjectOpenHashMap.this.key;
            while (this.pos < this.max && n > 0L) {
                if (key[this.pos++] != null) {
                    ++skipped;
                    --n;
                }
            }
            return skipped;
        }
    }
    
    private final class EntrySpliterator extends MapSpliterator<Consumer<? super Object2ObjectMap.Entry<K, V>>, EntrySpliterator> implements ObjectSpliterator<Object2ObjectMap.Entry<K, V>>
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
        final void acceptOnIndex(final Consumer<? super Object2ObjectMap.Entry<K, V>> action, final int index) {
            action.accept((Object)new MapEntry(index));
        }
        
        @Override
        final EntrySpliterator makeForSplit(final int pos, final int max, final boolean mustReturnNull) {
            return new EntrySpliterator(pos, max, mustReturnNull, true);
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Object2ObjectMap.Entry<K, V>> implements Object2ObjectMap.FastEntrySet<K, V>
    {
        @Override
        public ObjectIterator<Object2ObjectMap.Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Object2ObjectMap.Entry<K, V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectSpliterator<Object2ObjectMap.Entry<K, V>> spliterator() {
            return new EntrySpliterator();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            if (k == null) {
                return Object2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Object2ObjectOpenHashMap.this.value[Object2ObjectOpenHashMap.this.n], v);
            }
            final K[] key = Object2ObjectOpenHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2ObjectOpenHashMap.this.mask)]) == null) {
                return false;
            }
            if (k.equals(curr)) {
                return Objects.equals(Object2ObjectOpenHashMap.this.value[pos], v);
            }
            while ((curr = key[pos = (pos + 1 & Object2ObjectOpenHashMap.this.mask)]) != null) {
                if (k.equals(curr)) {
                    return Objects.equals(Object2ObjectOpenHashMap.this.value[pos], v);
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
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            if (k == null) {
                if (Object2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Object2ObjectOpenHashMap.this.value[Object2ObjectOpenHashMap.this.n], v)) {
                    Object2ObjectOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Object2ObjectOpenHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2ObjectOpenHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!curr.equals(k)) {
                    while ((curr = key[pos = (pos + 1 & Object2ObjectOpenHashMap.this.mask)]) != null) {
                        if (curr.equals(k) && Objects.equals(Object2ObjectOpenHashMap.this.value[pos], v)) {
                            Object2ObjectOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Objects.equals(Object2ObjectOpenHashMap.this.value[pos], v)) {
                    Object2ObjectOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        @Override
        public int size() {
            return Object2ObjectOpenHashMap.this.size;
        }
        
        @Override
        public void clear() {
            Object2ObjectOpenHashMap.this.clear();
        }
        
        @Override
        public void forEach(final Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
            if (Object2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept((Object)new BasicEntry((K)Object2ObjectOpenHashMap.this.key[Object2ObjectOpenHashMap.this.n], (V)Object2ObjectOpenHashMap.this.value[Object2ObjectOpenHashMap.this.n]));
            }
            int pos = Object2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2ObjectOpenHashMap.this.key[pos] != null) {
                    consumer.accept((Object)new BasicEntry((K)Object2ObjectOpenHashMap.this.key[pos], (V)Object2ObjectOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
            final BasicEntry<K, V> entry = new BasicEntry<K, V>();
            if (Object2ObjectOpenHashMap.this.containsNullKey) {
                entry.key = Object2ObjectOpenHashMap.this.key[Object2ObjectOpenHashMap.this.n];
                entry.value = Object2ObjectOpenHashMap.this.value[Object2ObjectOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Object2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2ObjectOpenHashMap.this.key[pos] != null) {
                    entry.key = Object2ObjectOpenHashMap.this.key[pos];
                    entry.value = Object2ObjectOpenHashMap.this.value[pos];
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
            action.accept((Object)Object2ObjectOpenHashMap.this.key[index]);
        }
        
        @Override
        public K next() {
            return Object2ObjectOpenHashMap.this.key[this.nextEntry()];
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
            action.accept((Object)Object2ObjectOpenHashMap.this.key[index]);
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
            if (Object2ObjectOpenHashMap.this.containsNullKey) {
                consumer.accept((Object)Object2ObjectOpenHashMap.this.key[Object2ObjectOpenHashMap.this.n]);
            }
            int pos = Object2ObjectOpenHashMap.this.n;
            while (pos-- != 0) {
                final K k = Object2ObjectOpenHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept((Object)k);
                }
            }
        }
        
        @Override
        public int size() {
            return Object2ObjectOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final Object k) {
            return Object2ObjectOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final Object k) {
            final int oldSize = Object2ObjectOpenHashMap.this.size;
            Object2ObjectOpenHashMap.this.remove(k);
            return Object2ObjectOpenHashMap.this.size != oldSize;
        }
        
        @Override
        public void clear() {
            Object2ObjectOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator<Consumer<? super V>> implements ObjectIterator<V>
    {
        public ValueIterator() {
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super V> action, final int index) {
            action.accept((Object)Object2ObjectOpenHashMap.this.value[index]);
        }
        
        @Override
        public V next() {
            return Object2ObjectOpenHashMap.this.value[this.nextEntry()];
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
            action.accept((Object)Object2ObjectOpenHashMap.this.value[index]);
        }
        
        @Override
        final ValueSpliterator makeForSplit(final int pos, final int max, final boolean mustReturnNull) {
            return new ValueSpliterator(pos, max, mustReturnNull, true);
        }
    }
}
