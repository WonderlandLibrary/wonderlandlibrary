// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.function.IntConsumer;
import java.util.SortedSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSortedSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import com.viaversion.viaversion.libs.fastutil.Pair;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSortedSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.Hash;
import java.io.Serializable;

public class Int2ObjectLinkedOpenHashMap<V> extends AbstractInt2ObjectSortedMap<V> implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Int2ObjectSortedMap.FastSortedEntrySet<V> entries;
    protected transient IntSortedSet keys;
    protected transient ObjectCollection<V> values;
    
    public Int2ObjectLinkedOpenHashMap(final int expected, final float f) {
        this.first = -1;
        this.last = -1;
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
        this.link = new long[this.n + 1];
    }
    
    public Int2ObjectLinkedOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Int2ObjectLinkedOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Int2ObjectLinkedOpenHashMap(final Map<? extends Integer, ? extends V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Int2ObjectLinkedOpenHashMap(final Map<? extends Integer, ? extends V> m) {
        this(m, 0.75f);
    }
    
    public Int2ObjectLinkedOpenHashMap(final Int2ObjectMap<V> m, final float f) {
        this(m.size(), f);
        this.putAll((Map<? extends Integer, ? extends V>)m);
    }
    
    public Int2ObjectLinkedOpenHashMap(final Int2ObjectMap<V> m) {
        this(m, 0.75f);
    }
    
    public Int2ObjectLinkedOpenHashMap(final int[] k, final V[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Int2ObjectLinkedOpenHashMap(final int[] k, final V[] v) {
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
        this.fixPointers(pos);
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
        this.fixPointers(this.n);
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
        if (this.size == 0) {
            this.last = pos;
            this.first = pos;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
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
                this.fixPointers(pos, last);
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
    
    private V setValue(final int pos, final V v) {
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public V removeFirst() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.first;
        this.first = (int)this.link[pos];
        if (0 <= this.first) {
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        --this.size;
        final V v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.value[this.n] = null;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    public V removeLast() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.last;
        this.last = (int)(this.link[pos] >>> 32);
        if (0 <= this.last) {
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        --this.size;
        final V v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
            this.value[this.n] = null;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    private void moveIndexToFirst(final int i) {
        if (this.size == 1 || this.first == i) {
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int first = this.first;
        link4[first] ^= ((this.link[this.first] ^ ((long)i & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[i] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
        this.first = i;
    }
    
    private void moveIndexToLast(final int i) {
        if (this.size == 1 || this.last == i) {
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int last = this.last;
        link4[last] ^= ((this.link[this.last] ^ ((long)i & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        this.link[i] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
        this.last = i;
    }
    
    public V getAndMoveToFirst(final int k) {
        if (k == 0) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
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
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (k == curr) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public V getAndMoveToLast(final int k) {
        if (k == 0) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
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
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (k == curr) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public V putAndMoveToFirst(final int k, final V v) {
        int pos;
        if (k == 0) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final int[] key = this.key;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != 0) {
                if (curr == k) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (curr == k) {
                        this.moveIndexToFirst(pos);
                        return this.setValue(pos, v);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int first = this.first;
            link[first] ^= ((this.link[this.first] ^ ((long)pos & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[pos] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
            this.first = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }
    
    public V putAndMoveToLast(final int k, final V v) {
        int pos;
        if (k == 0) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final int[] key = this.key;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != 0) {
                if (curr == k) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (curr == k) {
                        this.moveIndexToLast(pos);
                        return this.setValue(pos, v);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
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
        final int n = -1;
        this.last = n;
        this.first = n;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    protected void fixPointers(final int i) {
        if (this.size == 0) {
            final int n = -1;
            this.last = n;
            this.first = n;
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            if (0 <= this.first) {
                final long[] link = this.link;
                final int first = this.first;
                link[first] |= 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            if (0 <= this.last) {
                final long[] link2 = this.link;
                final int last = this.last;
                link2[last] |= 0xFFFFFFFFL;
            }
            return;
        }
        final long linki = this.link[i];
        final int prev = (int)(linki >>> 32);
        final int next = (int)linki;
        final long[] link3 = this.link;
        final int n2 = prev;
        link3[n2] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n3 = next;
        link4[n3] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
    }
    
    protected void fixPointers(final int s, final int d) {
        if (this.size == 1) {
            this.last = d;
            this.first = d;
            this.link[d] = -1L;
            return;
        }
        if (this.first == s) {
            this.first = d;
            final long[] link = this.link;
            final int n = (int)this.link[s];
            link[n] ^= ((this.link[(int)this.link[s]] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[d] = this.link[s];
            return;
        }
        if (this.last == s) {
            this.last = d;
            final long[] link2 = this.link;
            final int n2 = (int)(this.link[s] >>> 32);
            link2[n2] ^= ((this.link[(int)(this.link[s] >>> 32)] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[d] = this.link[s];
            return;
        }
        final long links = this.link[s];
        final int prev = (int)(links >>> 32);
        final int next = (int)links;
        final long[] link3 = this.link;
        final int n3 = prev;
        link3[n3] ^= ((this.link[prev] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n4 = next;
        link4[n4] ^= ((this.link[next] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[d] = links;
    }
    
    @Override
    public int firstIntKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    @Override
    public int lastIntKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    @Override
    public Int2ObjectSortedMap<V> tailMap(final int from) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Int2ObjectSortedMap<V> headMap(final int to) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Int2ObjectSortedMap<V> subMap(final int from, final int to) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public IntComparator comparator() {
        return null;
    }
    
    @Override
    public Int2ObjectSortedMap.FastSortedEntrySet<V> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public IntSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() {
                private static final int SPLITERATOR_CHARACTERISTICS = 80;
                
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public ObjectSpliterator<V> spliterator() {
                    return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(Int2ObjectLinkedOpenHashMap.this), 80);
                }
                
                @Override
                public void forEach(final Consumer<? super V> consumer) {
                    int i = Int2ObjectLinkedOpenHashMap.this.size;
                    int next = Int2ObjectLinkedOpenHashMap.this.first;
                    while (i-- != 0) {
                        final int curr = next;
                        next = (int)Int2ObjectLinkedOpenHashMap.this.link[curr];
                        consumer.accept((Object)Int2ObjectLinkedOpenHashMap.this.value[curr]);
                    }
                }
                
                @Override
                public int size() {
                    return Int2ObjectLinkedOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final Object v) {
                    return Int2ObjectLinkedOpenHashMap.this.containsValue(v);
                }
                
                @Override
                public void clear() {
                    Int2ObjectLinkedOpenHashMap.this.clear();
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
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (key[i] == 0) {
                pos = newN;
            }
            else {
                for (pos = (HashCommon.mix(key[i]) & mask); newKey[pos] != 0; pos = (pos + 1 & mask)) {}
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
            if (prev != -1) {
                final long[] array = newLink;
                final int n = newPrev;
                array[n] ^= ((newLink[newPrev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = newLink;
                final int n2 = pos;
                array2[n2] ^= ((newLink[pos] ^ ((long)newPrev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                newPrev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                newPrev = first;
                newLink[pos] = -1L;
            }
            final int t = i;
            i = (int)link[i];
            prev = t;
        }
        this.link = newLink;
        if ((this.last = newPrev) != -1) {
            final long[] array3 = newLink;
            final int n3 = newPrev;
            array3[n3] |= 0xFFFFFFFFL;
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
    
    public Int2ObjectLinkedOpenHashMap<V> clone() {
        Int2ObjectLinkedOpenHashMap<V> c;
        try {
            c = (Int2ObjectLinkedOpenHashMap)super.clone();
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
        c.link = this.link.clone();
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
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
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
            if (this.first != -1) {
                final long[] array = link;
                final int n2 = prev;
                array[n2] ^= ((link[prev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = link;
                final int n3 = pos;
                array2[n3] ^= ((link[pos] ^ ((long)prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                prev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                prev = first;
                final long[] array3 = link;
                final int n4 = pos;
                array3[n4] |= 0xFFFFFFFF00000000L;
            }
        }
        if ((this.last = prev) != -1) {
            final long[] array4 = link;
            final int n5 = prev;
            array4[n5] |= 0xFFFFFFFFL;
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
            return Int2ObjectLinkedOpenHashMap.this.key[this.index];
        }
        
        @Override
        public int leftInt() {
            return Int2ObjectLinkedOpenHashMap.this.key[this.index];
        }
        
        @Override
        public V getValue() {
            return Int2ObjectLinkedOpenHashMap.this.value[this.index];
        }
        
        @Override
        public V right() {
            return Int2ObjectLinkedOpenHashMap.this.value[this.index];
        }
        
        @Override
        public V setValue(final V v) {
            final V oldValue = Int2ObjectLinkedOpenHashMap.this.value[this.index];
            Int2ObjectLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Override
        public IntObjectPair<V> right(final V v) {
            Int2ObjectLinkedOpenHashMap.this.value[this.index] = v;
            return this;
        }
        
        @Deprecated
        @Override
        public Integer getKey() {
            return Int2ObjectLinkedOpenHashMap.this.key[this.index];
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Integer, V> e = (Map.Entry<Integer, V>)o;
            return Int2ObjectLinkedOpenHashMap.this.key[this.index] == e.getKey() && Objects.equals(Int2ObjectLinkedOpenHashMap.this.value[this.index], e.getValue());
        }
        
        @Override
        public int hashCode() {
            return Int2ObjectLinkedOpenHashMap.this.key[this.index] ^ ((Int2ObjectLinkedOpenHashMap.this.value[this.index] == null) ? 0 : Int2ObjectLinkedOpenHashMap.this.value[this.index].hashCode());
        }
        
        @Override
        public String toString() {
            return Int2ObjectLinkedOpenHashMap.this.key[this.index] + "=>" + Int2ObjectLinkedOpenHashMap.this.value[this.index];
        }
    }
    
    private abstract class MapIterator<ConsumerType>
    {
        int prev;
        int next;
        int curr;
        int index;
        
        abstract void acceptOnIndex(final ConsumerType p0, final int p1);
        
        protected MapIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = Int2ObjectLinkedOpenHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final int from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == 0) {
                if (Int2ObjectLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Int2ObjectLinkedOpenHashMap.this.link[Int2ObjectLinkedOpenHashMap.this.n];
                    this.prev = Int2ObjectLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this map.");
            }
            else {
                if (Int2ObjectLinkedOpenHashMap.this.key[Int2ObjectLinkedOpenHashMap.this.last] == from) {
                    this.prev = Int2ObjectLinkedOpenHashMap.this.last;
                    this.index = Int2ObjectLinkedOpenHashMap.this.size;
                    return;
                }
                for (int pos = HashCommon.mix(from) & Int2ObjectLinkedOpenHashMap.this.mask; Int2ObjectLinkedOpenHashMap.this.key[pos] != 0; pos = (pos + 1 & Int2ObjectLinkedOpenHashMap.this.mask)) {
                    if (Int2ObjectLinkedOpenHashMap.this.key[pos] == from) {
                        this.next = (int)Int2ObjectLinkedOpenHashMap.this.link[pos];
                        this.prev = pos;
                        return;
                    }
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this map.");
            }
        }
        
        public boolean hasNext() {
            return this.next != -1;
        }
        
        public boolean hasPrevious() {
            return this.prev != -1;
        }
        
        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = Int2ObjectLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Int2ObjectLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Int2ObjectLinkedOpenHashMap.this.link[pos];
                ++this.index;
            }
        }
        
        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }
        
        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }
        
        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)Int2ObjectLinkedOpenHashMap.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return this.curr;
        }
        
        public int previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(Int2ObjectLinkedOpenHashMap.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return this.curr;
        }
        
        public void forEachRemaining(final ConsumerType action) {
            while (this.hasNext()) {
                this.curr = this.next;
                this.next = (int)Int2ObjectLinkedOpenHashMap.this.link[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    ++this.index;
                }
                this.acceptOnIndex(action, this.curr);
            }
        }
        
        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(Int2ObjectLinkedOpenHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Int2ObjectLinkedOpenHashMap.this.link[this.curr];
            }
            final Int2ObjectLinkedOpenHashMap this$0 = Int2ObjectLinkedOpenHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Int2ObjectLinkedOpenHashMap.this.first = this.next;
            }
            else {
                final long[] link = Int2ObjectLinkedOpenHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Int2ObjectLinkedOpenHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Int2ObjectLinkedOpenHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Int2ObjectLinkedOpenHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Int2ObjectLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Int2ObjectLinkedOpenHashMap.this.n) {
                Int2ObjectLinkedOpenHashMap.this.containsNullKey = false;
                Int2ObjectLinkedOpenHashMap.this.value[Int2ObjectLinkedOpenHashMap.this.n] = null;
                return;
            }
            final int[] key = Int2ObjectLinkedOpenHashMap.this.key;
            int last = 0;
        Label_0280:
            while (true) {
                pos = ((last = pos) + 1 & Int2ObjectLinkedOpenHashMap.this.mask);
                int curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(curr) & Int2ObjectLinkedOpenHashMap.this.mask;
                    Label_0380: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0380;
                            }
                            if (slot > pos) {
                                break Label_0380;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0380;
                        }
                        pos = (pos + 1 & Int2ObjectLinkedOpenHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Int2ObjectLinkedOpenHashMap.this.value[last] = Int2ObjectLinkedOpenHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Int2ObjectLinkedOpenHashMap.this.fixPointers(pos, last);
                    continue Label_0280;
                }
                break;
            }
            key[last] = 0;
            Int2ObjectLinkedOpenHashMap.this.value[last] = null;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
        
        public int back(final int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
        
        public void set(final Int2ObjectMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final Int2ObjectMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class EntryIterator extends MapIterator<Consumer<? super Int2ObjectMap.Entry<V>>> implements ObjectListIterator<Int2ObjectMap.Entry<V>>
    {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final int from) {
            super(from);
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super Int2ObjectMap.Entry<V>> action, final int index) {
            action.accept((Object)new MapEntry(index));
        }
        
        @Override
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        public MapEntry previous() {
            return this.entry = new MapEntry(this.previousEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private final class FastEntryIterator extends MapIterator<Consumer<? super Int2ObjectMap.Entry<V>>> implements ObjectListIterator<Int2ObjectMap.Entry<V>>
    {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final int from) {
            super(from);
            this.entry = new MapEntry();
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super Int2ObjectMap.Entry<V>> action, final int index) {
            this.entry.index = index;
            action.accept(this.entry);
        }
        
        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
        
        @Override
        public MapEntry previous() {
            this.entry.index = this.previousEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Int2ObjectMap.Entry<V>> implements Int2ObjectSortedMap.FastSortedEntrySet<V>
    {
        private static final int SPLITERATOR_CHARACTERISTICS = 81;
        
        @Override
        public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectSpliterator<Int2ObjectMap.Entry<V>> spliterator() {
            return ObjectSpliterators.asSpliterator((ObjectIterator<? extends Int2ObjectMap.Entry<V>>)this.iterator(), Size64.sizeOf(Int2ObjectLinkedOpenHashMap.this), 81);
        }
        
        @Override
        public Comparator<? super Int2ObjectMap.Entry<V>> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> subSet(final Int2ObjectMap.Entry<V> fromElement, final Int2ObjectMap.Entry<V> toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> headSet(final Int2ObjectMap.Entry<V> toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> tailSet(final Int2ObjectMap.Entry<V> fromElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Int2ObjectMap.Entry<V> first() {
            if (Int2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Int2ObjectLinkedOpenHashMap.this.first);
        }
        
        @Override
        public Int2ObjectMap.Entry<V> last() {
            if (Int2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Int2ObjectLinkedOpenHashMap.this.last);
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
                return Int2ObjectLinkedOpenHashMap.this.containsNullKey && Objects.equals(Int2ObjectLinkedOpenHashMap.this.value[Int2ObjectLinkedOpenHashMap.this.n], v);
            }
            final int[] key = Int2ObjectLinkedOpenHashMap.this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & Int2ObjectLinkedOpenHashMap.this.mask)]) == 0) {
                return false;
            }
            if (k == curr) {
                return Objects.equals(Int2ObjectLinkedOpenHashMap.this.value[pos], v);
            }
            while ((curr = key[pos = (pos + 1 & Int2ObjectLinkedOpenHashMap.this.mask)]) != 0) {
                if (k == curr) {
                    return Objects.equals(Int2ObjectLinkedOpenHashMap.this.value[pos], v);
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
                if (Int2ObjectLinkedOpenHashMap.this.containsNullKey && Objects.equals(Int2ObjectLinkedOpenHashMap.this.value[Int2ObjectLinkedOpenHashMap.this.n], v)) {
                    Int2ObjectLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final int[] key = Int2ObjectLinkedOpenHashMap.this.key;
                int pos;
                int curr;
                if ((curr = key[pos = (HashCommon.mix(k) & Int2ObjectLinkedOpenHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Int2ObjectLinkedOpenHashMap.this.mask)]) != 0) {
                        if (curr == k && Objects.equals(Int2ObjectLinkedOpenHashMap.this.value[pos], v)) {
                            Int2ObjectLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Objects.equals(Int2ObjectLinkedOpenHashMap.this.value[pos], v)) {
                    Int2ObjectLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        @Override
        public int size() {
            return Int2ObjectLinkedOpenHashMap.this.size;
        }
        
        @Override
        public void clear() {
            Int2ObjectLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public ObjectListIterator<Int2ObjectMap.Entry<V>> iterator(final Int2ObjectMap.Entry<V> from) {
            return new EntryIterator(from.getIntKey());
        }
        
        @Override
        public ObjectListIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectListIterator<Int2ObjectMap.Entry<V>> fastIterator(final Int2ObjectMap.Entry<V> from) {
            return new FastEntryIterator(from.getIntKey());
        }
        
        @Override
        public void forEach(final Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            int i = Int2ObjectLinkedOpenHashMap.this.size;
            int next = Int2ObjectLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Int2ObjectLinkedOpenHashMap.this.link[curr];
                consumer.accept((Object)new BasicEntry(Int2ObjectLinkedOpenHashMap.this.key[curr], (V)Int2ObjectLinkedOpenHashMap.this.value[curr]));
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            int i = Int2ObjectLinkedOpenHashMap.this.size;
            int next = Int2ObjectLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Int2ObjectLinkedOpenHashMap.this.link[curr];
                entry.key = Int2ObjectLinkedOpenHashMap.this.key[curr];
                entry.value = Int2ObjectLinkedOpenHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }
    
    private final class KeyIterator extends MapIterator<IntConsumer> implements IntListIterator
    {
        public KeyIterator(final int k) {
            super(k);
        }
        
        @Override
        public int previousInt() {
            return Int2ObjectLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        public KeyIterator() {
        }
        
        @Override
        final void acceptOnIndex(final IntConsumer action, final int index) {
            action.accept(Int2ObjectLinkedOpenHashMap.this.key[index]);
        }
        
        @Override
        public int nextInt() {
            return Int2ObjectLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractIntSortedSet
    {
        private static final int SPLITERATOR_CHARACTERISTICS = 337;
        
        @Override
        public IntListIterator iterator(final int from) {
            return new KeyIterator(from);
        }
        
        @Override
        public IntListIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public IntSpliterator spliterator() {
            return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(Int2ObjectLinkedOpenHashMap.this), 337);
        }
        
        @Override
        public void forEach(final IntConsumer consumer) {
            int i = Int2ObjectLinkedOpenHashMap.this.size;
            int next = Int2ObjectLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                final int curr = next;
                next = (int)Int2ObjectLinkedOpenHashMap.this.link[curr];
                consumer.accept(Int2ObjectLinkedOpenHashMap.this.key[curr]);
            }
        }
        
        @Override
        public int size() {
            return Int2ObjectLinkedOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final int k) {
            return Int2ObjectLinkedOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean remove(final int k) {
            final int oldSize = Int2ObjectLinkedOpenHashMap.this.size;
            Int2ObjectLinkedOpenHashMap.this.remove(k);
            return Int2ObjectLinkedOpenHashMap.this.size != oldSize;
        }
        
        @Override
        public void clear() {
            Int2ObjectLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public int firstInt() {
            if (Int2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Int2ObjectLinkedOpenHashMap.this.key[Int2ObjectLinkedOpenHashMap.this.first];
        }
        
        @Override
        public int lastInt() {
            if (Int2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Int2ObjectLinkedOpenHashMap.this.key[Int2ObjectLinkedOpenHashMap.this.last];
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator<Consumer<? super V>> implements ObjectListIterator<V>
    {
        @Override
        public V previous() {
            return Int2ObjectLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        public ValueIterator() {
        }
        
        @Override
        final void acceptOnIndex(final Consumer<? super V> action, final int index) {
            action.accept((Object)Int2ObjectLinkedOpenHashMap.this.value[index]);
        }
        
        @Override
        public V next() {
            return Int2ObjectLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
