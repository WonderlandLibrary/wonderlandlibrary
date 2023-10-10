// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Objects;
import java.util.SortedSet;
import java.util.Spliterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.Comparator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.Iterator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import java.util.stream.Collector;
import com.viaversion.viaversion.libs.fastutil.Hash;
import java.io.Serializable;

public class ObjectLinkedOpenHashSet<K> extends AbstractObjectSortedSet<K> implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    private static final Collector<Object, ?, ObjectLinkedOpenHashSet<Object>> TO_SET_COLLECTOR;
    private static final int SPLITERATOR_CHARACTERISTICS = 81;
    
    public ObjectLinkedOpenHashSet(final int expected, final float f) {
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
        this.key = (K[])new Object[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public ObjectLinkedOpenHashSet(final int expected) {
        this(expected, 0.75f);
    }
    
    public ObjectLinkedOpenHashSet() {
        this(16, 0.75f);
    }
    
    public ObjectLinkedOpenHashSet(final Collection<? extends K> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public ObjectLinkedOpenHashSet(final Collection<? extends K> c) {
        this(c, 0.75f);
    }
    
    public ObjectLinkedOpenHashSet(final ObjectCollection<? extends K> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public ObjectLinkedOpenHashSet(final ObjectCollection<? extends K> c) {
        this(c, 0.75f);
    }
    
    public ObjectLinkedOpenHashSet(final Iterator<? extends K> i, final float f) {
        this(16, f);
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ObjectLinkedOpenHashSet(final Iterator<? extends K> i) {
        this(i, 0.75f);
    }
    
    public ObjectLinkedOpenHashSet(final K[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0 : length, f);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public ObjectLinkedOpenHashSet(final K[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public ObjectLinkedOpenHashSet(final K[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public ObjectLinkedOpenHashSet(final K[] a) {
        this(a, 0.75f);
    }
    
    public static <K> ObjectLinkedOpenHashSet<K> of() {
        return new ObjectLinkedOpenHashSet<K>();
    }
    
    public static <K> ObjectLinkedOpenHashSet<K> of(final K e) {
        final ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<K>(1, 0.75f);
        result.add(e);
        return result;
    }
    
    public static <K> ObjectLinkedOpenHashSet<K> of(final K e0, final K e1) {
        final ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<K>(2, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return result;
    }
    
    public static <K> ObjectLinkedOpenHashSet<K> of(final K e0, final K e1, final K e2) {
        final ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<K>(3, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!result.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return result;
    }
    
    @SafeVarargs
    public static <K> ObjectLinkedOpenHashSet<K> of(final K... a) {
        final ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<K>(a.length, 0.75f);
        for (final K element : a) {
            if (!result.add(element)) {
                throw new IllegalArgumentException("Duplicate element " + element);
            }
        }
        return result;
    }
    
    private ObjectLinkedOpenHashSet<K> combine(final ObjectLinkedOpenHashSet<? extends K> toAddFrom) {
        this.addAll(toAddFrom);
        return this;
    }
    
    public static <K> Collector<K, ?, ObjectLinkedOpenHashSet<K>> toSet() {
        return (Collector<K, ?, ObjectLinkedOpenHashSet<K>>)ObjectLinkedOpenHashSet.TO_SET_COLLECTOR;
    }
    
    public static <K> Collector<K, ?, ObjectLinkedOpenHashSet<K>> toSetWithExpectedSize(final int expectedSize) {
        if (expectedSize <= 16) {
            return toSet();
        }
        final Object o3;
        return (Collector<K, ?, ObjectLinkedOpenHashSet<K>>)Collector.of(new ObjectCollections.SizeDecreasingSupplier<Object, Object>(expectedSize, size -> {
            if (size <= 16) {
                // new(com.viaversion.viaversion.libs.fastutil.objects.ObjectLinkedOpenHashSet.class)
                new ObjectLinkedOpenHashSet();
            }
            else {
                // new(com.viaversion.viaversion.libs.fastutil.objects.ObjectLinkedOpenHashSet.class)
                new ObjectLinkedOpenHashSet(size);
            }
            return o3;
        }), ObjectLinkedOpenHashSet::add, ObjectLinkedOpenHashSet::combine, new Collector.Characteristics[0]);
    }
    
    private int realSize() {
        return this.containsNull ? (this.size - 1) : this.size;
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
    
    @Override
    public boolean addAll(final Collection<? extends K> c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean add(final K k) {
        int pos;
        if (k == null) {
            if (this.containsNull) {
                return false;
            }
            pos = this.n;
            this.containsNull = true;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) != null) {
                if (curr.equals(k)) {
                    return false;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (curr.equals(k)) {
                        return false;
                    }
                }
            }
            key[pos] = k;
        }
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
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
    }
    
    public K addOrGet(final K k) {
        int pos;
        if (k == null) {
            if (this.containsNull) {
                return this.key[this.n];
            }
            pos = this.n;
            this.containsNull = true;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) != null) {
                if (curr.equals(k)) {
                    return curr;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (curr.equals(k)) {
                        return curr;
                    }
                }
            }
            key[pos] = k;
        }
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
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return k;
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
                this.fixPointers(pos, last);
                continue Label_0006;
            }
            break;
        }
        key[last] = null;
    }
    
    private boolean removeEntry(final int pos) {
        --this.size;
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = null;
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    @Override
    public boolean remove(final Object k) {
        if (k == null) {
            return this.containsNull && this.removeNullEntry();
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return false;
        }
        if (k.equals(curr)) {
            return this.removeEntry(pos);
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return this.removeEntry(pos);
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final Object k) {
        if (k == null) {
            return this.containsNull;
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
    
    public K get(final Object k) {
        if (k == null) {
            return this.key[this.n];
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return null;
        }
        if (k.equals(curr)) {
            return curr;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return curr;
            }
        }
        return null;
    }
    
    public K removeFirst() {
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
        final K k = this.key[pos];
        --this.size;
        if (k == null) {
            this.containsNull = false;
            this.key[this.n] = null;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return k;
    }
    
    public K removeLast() {
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
        final K k = this.key[pos];
        --this.size;
        if (k == null) {
            this.containsNull = false;
            this.key[this.n] = null;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return k;
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
    
    public boolean addAndMoveToFirst(final K k) {
        int pos;
        if (k == null) {
            if (this.containsNull) {
                this.moveIndexToFirst(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        }
        else {
            K[] key;
            for (key = this.key, pos = (HashCommon.mix(k.hashCode()) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {
                if (k.equals(key[pos])) {
                    this.moveIndexToFirst(pos);
                    return false;
                }
            }
        }
        this.key[pos] = k;
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
        return true;
    }
    
    public boolean addAndMoveToLast(final K k) {
        int pos;
        if (k == null) {
            if (this.containsNull) {
                this.moveIndexToLast(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        }
        else {
            K[] key;
            for (key = this.key, pos = (HashCommon.mix(k.hashCode()) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {
                if (k.equals(key[pos])) {
                    this.moveIndexToLast(pos);
                    return false;
                }
            }
        }
        this.key[pos] = k;
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
        return true;
    }
    
    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, null);
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
    public K first() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    @Override
    public K last() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    @Override
    public ObjectSortedSet<K> tailSet(final K from) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ObjectSortedSet<K> headSet(final K to) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ObjectSortedSet<K> subSet(final K from, final K to) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Comparator<? super K> comparator() {
        return null;
    }
    
    @Override
    public ObjectListIterator<K> iterator(final K from) {
        return new SetIterator(from);
    }
    
    @Override
    public ObjectListIterator<K> iterator() {
        return new SetIterator();
    }
    
    @Override
    public ObjectSpliterator<K> spliterator() {
        return ObjectSpliterators.asSpliterator((ObjectIterator<? extends K>)this.iterator(), Size64.sizeOf(this), 81);
    }
    
    @Override
    public void forEach(final Consumer<? super K> action) {
        int next = this.first;
        while (next != -1) {
            final int curr = next;
            next = (int)this.link[curr];
            action.accept((Object)this.key[curr]);
        }
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
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (key[i] == null) {
                pos = newN;
            }
            else {
                for (pos = (HashCommon.mix(key[i].hashCode()) & mask); newKey[pos] != null; pos = (pos + 1 & mask)) {}
            }
            newKey[pos] = key[i];
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
    }
    
    public ObjectLinkedOpenHashSet<K> clone() {
        ObjectLinkedOpenHashSet<K> c;
        try {
            c = (ObjectLinkedOpenHashSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.containsNull = this.containsNull;
        c.link = this.link.clone();
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        while (j-- != 0) {
            while (this.key[i] == null) {
                ++i;
            }
            if (this != this.key[i]) {
                h += this.key[i].hashCode();
            }
            ++i;
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final ObjectIterator<K> i = this.iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            s.writeObject(i.next());
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
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            int pos;
            if (k == null) {
                pos = this.n;
                this.containsNull = true;
            }
            else if (key[pos = (HashCommon.mix(k.hashCode()) & this.mask)] != null) {
                while (key[pos = (pos + 1 & this.mask)] != null) {}
            }
            key[pos] = k;
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
    
    static {
        TO_SET_COLLECTOR = Collector.of(ObjectLinkedOpenHashSet::new, ObjectLinkedOpenHashSet::add, ObjectLinkedOpenHashSet::combine, new Collector.Characteristics[0]);
    }
    
    private final class SetIterator implements ObjectListIterator<K>
    {
        int prev;
        int next;
        int curr;
        int index;
        
        SetIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = ObjectLinkedOpenHashSet.this.first;
            this.index = 0;
        }
        
        SetIterator(final K from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (from == null) {
                if (ObjectLinkedOpenHashSet.this.containsNull) {
                    this.next = (int)ObjectLinkedOpenHashSet.this.link[ObjectLinkedOpenHashSet.this.n];
                    this.prev = ObjectLinkedOpenHashSet.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this set.");
            }
            else {
                if (Objects.equals(ObjectLinkedOpenHashSet.this.key[ObjectLinkedOpenHashSet.this.last], from)) {
                    this.prev = ObjectLinkedOpenHashSet.this.last;
                    this.index = ObjectLinkedOpenHashSet.this.size;
                    return;
                }
                final K[] key = ObjectLinkedOpenHashSet.this.key;
                for (int pos = HashCommon.mix(from.hashCode()) & ObjectLinkedOpenHashSet.this.mask; key[pos] != null; pos = (pos + 1 & ObjectLinkedOpenHashSet.this.mask)) {
                    if (key[pos].equals(from)) {
                        this.next = (int)ObjectLinkedOpenHashSet.this.link[pos];
                        this.prev = pos;
                        return;
                    }
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this set.");
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.next != -1;
        }
        
        @Override
        public boolean hasPrevious() {
            return this.prev != -1;
        }
        
        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)ObjectLinkedOpenHashSet.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return ObjectLinkedOpenHashSet.this.key[this.curr];
        }
        
        @Override
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(ObjectLinkedOpenHashSet.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return ObjectLinkedOpenHashSet.this.key[this.curr];
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            final K[] key = ObjectLinkedOpenHashSet.this.key;
            final long[] link = ObjectLinkedOpenHashSet.this.link;
            while (this.next != -1) {
                this.curr = this.next;
                this.next = (int)link[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    ++this.index;
                }
                action.accept((Object)key[this.curr]);
            }
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
                this.index = ObjectLinkedOpenHashSet.this.size;
                return;
            }
            int pos = ObjectLinkedOpenHashSet.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)ObjectLinkedOpenHashSet.this.link[pos];
                ++this.index;
            }
        }
        
        @Override
        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }
        
        @Override
        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }
        
        @Override
        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(ObjectLinkedOpenHashSet.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)ObjectLinkedOpenHashSet.this.link[this.curr];
            }
            final ObjectLinkedOpenHashSet this$0 = ObjectLinkedOpenHashSet.this;
            --this$0.size;
            if (this.prev == -1) {
                ObjectLinkedOpenHashSet.this.first = this.next;
            }
            else {
                final long[] link = ObjectLinkedOpenHashSet.this.link;
                final int prev = this.prev;
                link[prev] ^= ((ObjectLinkedOpenHashSet.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                ObjectLinkedOpenHashSet.this.last = this.prev;
            }
            else {
                final long[] link2 = ObjectLinkedOpenHashSet.this.link;
                final int next = this.next;
                link2[next] ^= ((ObjectLinkedOpenHashSet.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == ObjectLinkedOpenHashSet.this.n) {
                ObjectLinkedOpenHashSet.this.containsNull = false;
                ObjectLinkedOpenHashSet.this.key[ObjectLinkedOpenHashSet.this.n] = null;
                return;
            }
            final K[] key = ObjectLinkedOpenHashSet.this.key;
            int last = 0;
        Label_0280:
            while (true) {
                pos = ((last = pos) + 1 & ObjectLinkedOpenHashSet.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(curr.hashCode()) & ObjectLinkedOpenHashSet.this.mask;
                    Label_0373: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0373;
                            }
                            if (slot > pos) {
                                break Label_0373;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0373;
                        }
                        pos = (pos + 1 & ObjectLinkedOpenHashSet.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    ObjectLinkedOpenHashSet.this.fixPointers(pos, last);
                    continue Label_0280;
                }
                break;
            }
            key[last] = null;
        }
    }
}
