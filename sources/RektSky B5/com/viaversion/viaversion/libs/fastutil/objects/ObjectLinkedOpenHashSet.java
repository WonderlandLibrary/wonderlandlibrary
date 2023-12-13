/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Hash;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSortedSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollections;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSortedSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collector;

public class ObjectLinkedOpenHashSet<K>
extends AbstractObjectSortedSet<K>
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected transient int first = -1;
    protected transient int last = -1;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    private static final Collector<Object, ?, ObjectLinkedOpenHashSet<Object>> TO_SET_COLLECTOR = Collector.of(ObjectLinkedOpenHashSet::new, ObjectLinkedOpenHashSet::add, ObjectLinkedOpenHashSet::combine, new Collector.Characteristics[0]);
    private static final int SPLITERATOR_CHARACTERISTICS = 81;

    public ObjectLinkedOpenHashSet(int expected, float f2) {
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
        this.key = new Object[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public ObjectLinkedOpenHashSet(int expected) {
        this(expected, 0.75f);
    }

    public ObjectLinkedOpenHashSet() {
        this(16, 0.75f);
    }

    public ObjectLinkedOpenHashSet(Collection<? extends K> c2, float f2) {
        this(c2.size(), f2);
        this.addAll(c2);
    }

    public ObjectLinkedOpenHashSet(Collection<? extends K> c2) {
        this(c2, 0.75f);
    }

    public ObjectLinkedOpenHashSet(ObjectCollection<? extends K> c2, float f2) {
        this(c2.size(), f2);
        this.addAll(c2);
    }

    public ObjectLinkedOpenHashSet(ObjectCollection<? extends K> c2) {
        this(c2, 0.75f);
    }

    public ObjectLinkedOpenHashSet(Iterator<? extends K> i2, float f2) {
        this(16, f2);
        while (i2.hasNext()) {
            this.add(i2.next());
        }
    }

    public ObjectLinkedOpenHashSet(Iterator<? extends K> i2) {
        this(i2, 0.75f);
    }

    public ObjectLinkedOpenHashSet(K[] a2, int offset, int length, float f2) {
        this(length < 0 ? 0 : length, f2);
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        for (int i2 = 0; i2 < length; ++i2) {
            this.add(a2[offset + i2]);
        }
    }

    public ObjectLinkedOpenHashSet(K[] a2, int offset, int length) {
        this(a2, offset, length, 0.75f);
    }

    public ObjectLinkedOpenHashSet(K[] a2, float f2) {
        this(a2, 0, a2.length, f2);
    }

    public ObjectLinkedOpenHashSet(K[] a2) {
        this(a2, 0.75f);
    }

    public static <K> ObjectLinkedOpenHashSet<K> of() {
        return new ObjectLinkedOpenHashSet<K>();
    }

    public static <K> ObjectLinkedOpenHashSet<K> of(K e2) {
        ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<K>(1, 0.75f);
        result.add(e2);
        return result;
    }

    public static <K> ObjectLinkedOpenHashSet<K> of(K e0, K e1) {
        ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<K>(2, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return result;
    }

    public static <K> ObjectLinkedOpenHashSet<K> of(K e0, K e1, K e2) {
        ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<K>(3, 0.75f);
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
    public static <K> ObjectLinkedOpenHashSet<K> of(K ... a2) {
        ObjectLinkedOpenHashSet<K> result = new ObjectLinkedOpenHashSet<K>(a2.length, 0.75f);
        for (K element : a2) {
            if (result.add(element)) continue;
            throw new IllegalArgumentException("Duplicate element " + element);
        }
        return result;
    }

    private ObjectLinkedOpenHashSet<K> combine(ObjectLinkedOpenHashSet<? extends K> toAddFrom) {
        this.addAll((Collection<? extends K>)toAddFrom);
        return this;
    }

    public static <K> Collector<K, ?, ObjectLinkedOpenHashSet<K>> toSet() {
        return TO_SET_COLLECTOR;
    }

    public static <K> Collector<K, ?, ObjectLinkedOpenHashSet<K>> toSetWithExpectedSize(int expectedSize) {
        if (expectedSize <= 16) {
            return ObjectLinkedOpenHashSet.toSet();
        }
        return Collector.of(new ObjectCollections.SizeDecreasingSupplier(expectedSize, size -> size <= 16 ? new ObjectLinkedOpenHashSet() : new ObjectLinkedOpenHashSet(size)), ObjectLinkedOpenHashSet::add, ObjectLinkedOpenHashSet::combine, new Collector.Characteristics[0]);
    }

    private int realSize() {
        return this.containsNull ? this.size - 1 : this.size;
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

    @Override
    public boolean addAll(Collection<? extends K> c2) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(c2.size());
        } else {
            this.tryCapacity(this.size() + c2.size());
        }
        return super.addAll(c2);
    }

    @Override
    public boolean add(K k2) {
        int pos;
        if (k2 == null) {
            if (this.containsNull) {
                return false;
            }
            pos = this.n;
            this.containsNull = true;
        } else {
            K[] key = this.key;
            pos = HashCommon.mix(k2.hashCode()) & this.mask;
            K curr = key[pos];
            if (curr != null) {
                if (curr.equals(k2)) {
                    return false;
                }
                while ((curr = key[pos = pos + 1 & this.mask]) != null) {
                    if (!curr.equals(k2)) continue;
                    return false;
                }
            }
            key[pos] = k2;
        }
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            int n2 = this.last;
            this.link[n2] = this.link[n2] ^ (this.link[this.last] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[pos] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
    }

    public K addOrGet(K k2) {
        int pos;
        if (k2 == null) {
            if (this.containsNull) {
                return this.key[this.n];
            }
            pos = this.n;
            this.containsNull = true;
        } else {
            K[] key = this.key;
            pos = HashCommon.mix(k2.hashCode()) & this.mask;
            K curr = key[pos];
            if (curr != null) {
                if (curr.equals(k2)) {
                    return curr;
                }
                while ((curr = key[pos = pos + 1 & this.mask]) != null) {
                    if (!curr.equals(k2)) continue;
                    return curr;
                }
            }
            key[pos] = k2;
        }
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            int n2 = this.last;
            this.link[n2] = this.link[n2] ^ (this.link[this.last] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[pos] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return k2;
    }

    protected final void shiftKeys(int pos) {
        K[] key = this.key;
        while (true) {
            K curr;
            int last = pos;
            pos = last + 1 & this.mask;
            while (true) {
                if ((curr = key[pos]) == null) {
                    key[last] = null;
                    return;
                }
                int slot = HashCommon.mix(curr.hashCode()) & this.mask;
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                pos = pos + 1 & this.mask;
            }
            key[last] = curr;
            this.fixPointers(pos, last);
        }
    }

    private boolean removeEntry(int pos) {
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
    public boolean remove(Object k2) {
        if (k2 == null) {
            if (this.containsNull) {
                return this.removeNullEntry();
            }
            return false;
        }
        K[] key = this.key;
        int pos = HashCommon.mix(k2.hashCode()) & this.mask;
        K curr = key[pos];
        if (curr == null) {
            return false;
        }
        if (k2.equals(curr)) {
            return this.removeEntry(pos);
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != null) continue;
            return false;
        } while (!k2.equals(curr));
        return this.removeEntry(pos);
    }

    @Override
    public boolean contains(Object k2) {
        if (k2 == null) {
            return this.containsNull;
        }
        K[] key = this.key;
        int pos = HashCommon.mix(k2.hashCode()) & this.mask;
        K curr = key[pos];
        if (curr == null) {
            return false;
        }
        if (k2.equals(curr)) {
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != null) continue;
            return false;
        } while (!k2.equals(curr));
        return true;
    }

    public K get(Object k2) {
        if (k2 == null) {
            return this.key[this.n];
        }
        K[] key = this.key;
        int pos = HashCommon.mix(k2.hashCode()) & this.mask;
        K curr = key[pos];
        if (curr == null) {
            return null;
        }
        if (k2.equals(curr)) {
            return curr;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != null) continue;
            return null;
        } while (!k2.equals(curr));
        return curr;
    }

    public K removeFirst() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int pos = this.first;
        this.first = (int)this.link[pos];
        if (0 <= this.first) {
            int n2 = this.first;
            this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
        }
        K k2 = this.key[pos];
        --this.size;
        if (k2 == null) {
            this.containsNull = false;
            this.key[this.n] = null;
        } else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return k2;
    }

    public K removeLast() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int pos = this.last;
        this.last = (int)(this.link[pos] >>> 32);
        if (0 <= this.last) {
            int n2 = this.last;
            this.link[n2] = this.link[n2] | 0xFFFFFFFFL;
        }
        K k2 = this.key[pos];
        --this.size;
        if (k2 == null) {
            this.containsNull = false;
            this.key[this.n] = null;
        } else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return k2;
    }

    private void moveIndexToFirst(int i2) {
        if (this.size == 1 || this.first == i2) {
            return;
        }
        if (this.last == i2) {
            int n2 = this.last = (int)(this.link[i2] >>> 32);
            this.link[n2] = this.link[n2] | 0xFFFFFFFFL;
        } else {
            long linki = this.link[i2];
            int prev = (int)(linki >>> 32);
            int next = (int)linki;
            int n3 = prev;
            this.link[n3] = this.link[n3] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            int n4 = next;
            this.link[n4] = this.link[n4] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
        }
        int n5 = this.first;
        this.link[n5] = this.link[n5] ^ (this.link[this.first] ^ ((long)i2 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
        this.link[i2] = 0xFFFFFFFF00000000L | (long)this.first & 0xFFFFFFFFL;
        this.first = i2;
    }

    private void moveIndexToLast(int i2) {
        if (this.size == 1 || this.last == i2) {
            return;
        }
        if (this.first == i2) {
            int n2 = this.first = (int)this.link[i2];
            this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
        } else {
            long linki = this.link[i2];
            int prev = (int)(linki >>> 32);
            int next = (int)linki;
            int n3 = prev;
            this.link[n3] = this.link[n3] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            int n4 = next;
            this.link[n4] = this.link[n4] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
        }
        int n5 = this.last;
        this.link[n5] = this.link[n5] ^ (this.link[this.last] ^ (long)i2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        this.link[i2] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
        this.last = i2;
    }

    public boolean addAndMoveToFirst(K k2) {
        int pos;
        if (k2 == null) {
            if (this.containsNull) {
                this.moveIndexToFirst(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            K[] key = this.key;
            pos = HashCommon.mix(k2.hashCode()) & this.mask;
            while (key[pos] != null) {
                if (k2.equals(key[pos])) {
                    this.moveIndexToFirst(pos);
                    return false;
                }
                pos = pos + 1 & this.mask;
            }
        }
        this.key[pos] = k2;
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            int n2 = this.first;
            this.link[n2] = this.link[n2] ^ (this.link[this.first] ^ ((long)pos & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            this.link[pos] = 0xFFFFFFFF00000000L | (long)this.first & 0xFFFFFFFFL;
            this.first = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return true;
    }

    public boolean addAndMoveToLast(K k2) {
        int pos;
        if (k2 == null) {
            if (this.containsNull) {
                this.moveIndexToLast(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        } else {
            K[] key = this.key;
            pos = HashCommon.mix(k2.hashCode()) & this.mask;
            while (key[pos] != null) {
                if (k2.equals(key[pos])) {
                    this.moveIndexToLast(pos);
                    return false;
                }
                pos = pos + 1 & this.mask;
            }
        }
        this.key[pos] = k2;
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            int n2 = this.last;
            this.link[n2] = this.link[n2] ^ (this.link[this.last] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[pos] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
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
        this.last = -1;
        this.first = -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    protected void fixPointers(int i2) {
        if (this.size == 0) {
            this.last = -1;
            this.first = -1;
            return;
        }
        if (this.first == i2) {
            this.first = (int)this.link[i2];
            if (0 <= this.first) {
                int n2 = this.first;
                this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == i2) {
            this.last = (int)(this.link[i2] >>> 32);
            if (0 <= this.last) {
                int n3 = this.last;
                this.link[n3] = this.link[n3] | 0xFFFFFFFFL;
            }
            return;
        }
        long linki = this.link[i2];
        int prev = (int)(linki >>> 32);
        int next = (int)linki;
        int n4 = prev;
        this.link[n4] = this.link[n4] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        int n5 = next;
        this.link[n5] = this.link[n5] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
    }

    protected void fixPointers(int s2, int d2) {
        if (this.size == 1) {
            this.first = this.last = d2;
            this.link[d2] = -1L;
            return;
        }
        if (this.first == s2) {
            this.first = d2;
            int n2 = (int)this.link[s2];
            this.link[n2] = this.link[n2] ^ (this.link[(int)this.link[s2]] ^ ((long)d2 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            this.link[d2] = this.link[s2];
            return;
        }
        if (this.last == s2) {
            this.last = d2;
            int n3 = (int)(this.link[s2] >>> 32);
            this.link[n3] = this.link[n3] ^ (this.link[(int)(this.link[s2] >>> 32)] ^ (long)d2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[d2] = this.link[s2];
            return;
        }
        long links = this.link[s2];
        int prev = (int)(links >>> 32);
        int next = (int)links;
        int n4 = prev;
        this.link[n4] = this.link[n4] ^ (this.link[prev] ^ (long)d2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        int n5 = next;
        this.link[n5] = this.link[n5] ^ (this.link[next] ^ ((long)d2 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
        this.link[d2] = links;
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
    public ObjectSortedSet<K> tailSet(K from) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectSortedSet<K> headSet(K to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectSortedSet<K> subSet(K from, K to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super K> comparator() {
        return null;
    }

    @Override
    public ObjectListIterator<K> iterator(K from) {
        return new SetIterator(from);
    }

    @Override
    public ObjectListIterator<K> iterator() {
        return new SetIterator();
    }

    @Override
    public ObjectSpliterator<K> spliterator() {
        return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 81);
    }

    @Override
    public void forEach(Consumer<? super K> action) {
        int next = this.first;
        while (next != -1) {
            int curr = next;
            next = (int)this.link[curr];
            action.accept(this.key[curr]);
        }
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
        K[] key = this.key;
        int mask = newN - 1;
        Object[] newKey = new Object[newN + 1];
        int i2 = this.first;
        int prev = -1;
        int newPrev = -1;
        long[] link = this.link;
        long[] newLink = new long[newN + 1];
        this.first = -1;
        int j2 = this.size;
        while (j2-- != 0) {
            int pos;
            if (key[i2] == null) {
                pos = newN;
            } else {
                pos = HashCommon.mix(key[i2].hashCode()) & mask;
                while (newKey[pos] != null) {
                    pos = pos + 1 & mask;
                }
            }
            newKey[pos] = key[i2];
            if (prev != -1) {
                int n2 = newPrev;
                newLink[n2] = newLink[n2] ^ (newLink[newPrev] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
                int n3 = pos;
                newLink[n3] = newLink[n3] ^ (newLink[pos] ^ ((long)newPrev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
                newPrev = pos;
            } else {
                newPrev = this.first = pos;
                newLink[pos] = -1L;
            }
            int t2 = i2;
            i2 = (int)link[i2];
            prev = t2;
        }
        this.link = newLink;
        this.last = newPrev;
        if (newPrev != -1) {
            int n4 = newPrev;
            newLink[n4] = newLink[n4] | 0xFFFFFFFFL;
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }

    public ObjectLinkedOpenHashSet<K> clone() {
        ObjectLinkedOpenHashSet c2;
        try {
            c2 = (ObjectLinkedOpenHashSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c2.key = (Object[])this.key.clone();
        c2.containsNull = this.containsNull;
        c2.link = (long[])this.link.clone();
        return c2;
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        int j2 = this.realSize();
        int i2 = 0;
        while (j2-- != 0) {
            while (this.key[i2] == null) {
                ++i2;
            }
            if (this != this.key[i2]) {
                h2 += this.key[i2].hashCode();
            }
            ++i2;
        }
        return h2;
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        ObjectBidirectionalIterator i2 = this.iterator();
        s2.defaultWriteObject();
        int j2 = this.size;
        while (j2-- != 0) {
            s2.writeObject(i2.next());
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new Object[this.n + 1];
        Object[] key = this.key;
        this.link = new long[this.n + 1];
        long[] link = this.link;
        int prev = -1;
        this.last = -1;
        this.first = -1;
        int i2 = this.size;
        while (i2-- != 0) {
            int pos;
            Object k2 = s2.readObject();
            if (k2 == null) {
                pos = this.n;
                this.containsNull = true;
            } else {
                pos = HashCommon.mix(k2.hashCode()) & this.mask;
                if (key[pos] != null) {
                    while (key[pos = pos + 1 & this.mask] != null) {
                    }
                }
            }
            key[pos] = k2;
            if (this.first != -1) {
                int n2 = prev;
                link[n2] = link[n2] ^ (link[prev] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
                int n3 = pos;
                link[n3] = link[n3] ^ (link[pos] ^ ((long)prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
                prev = pos;
                continue;
            }
            prev = this.first = pos;
            int n4 = pos;
            link[n4] = link[n4] | 0xFFFFFFFF00000000L;
        }
        this.last = prev;
        if (prev != -1) {
            int n5 = prev;
            link[n5] = link[n5] | 0xFFFFFFFFL;
        }
    }

    private void checkTable() {
    }

    private final class SetIterator
    implements ObjectListIterator<K> {
        int prev = -1;
        int next = -1;
        int curr = -1;
        int index = -1;

        SetIterator() {
            this.next = ObjectLinkedOpenHashSet.this.first;
            this.index = 0;
        }

        SetIterator(K from) {
            if (from == null) {
                if (ObjectLinkedOpenHashSet.this.containsNull) {
                    this.next = (int)ObjectLinkedOpenHashSet.this.link[ObjectLinkedOpenHashSet.this.n];
                    this.prev = ObjectLinkedOpenHashSet.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this set.");
            }
            if (Objects.equals(ObjectLinkedOpenHashSet.this.key[ObjectLinkedOpenHashSet.this.last], from)) {
                this.prev = ObjectLinkedOpenHashSet.this.last;
                this.index = ObjectLinkedOpenHashSet.this.size;
                return;
            }
            K[] key = ObjectLinkedOpenHashSet.this.key;
            int pos = HashCommon.mix(from.hashCode()) & ObjectLinkedOpenHashSet.this.mask;
            while (key[pos] != null) {
                if (key[pos].equals(from)) {
                    this.next = (int)ObjectLinkedOpenHashSet.this.link[pos];
                    this.prev = pos;
                    return;
                }
                pos = pos + 1 & ObjectLinkedOpenHashSet.this.mask;
            }
            throw new NoSuchElementException("The key " + from + " does not belong to this set.");
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
        public void forEachRemaining(Consumer<? super K> action) {
            K[] key = ObjectLinkedOpenHashSet.this.key;
            long[] link = ObjectLinkedOpenHashSet.this.link;
            while (this.next != -1) {
                this.curr = this.next;
                this.next = (int)link[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    ++this.index;
                }
                action.accept(key[this.curr]);
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
            } else {
                this.next = (int)ObjectLinkedOpenHashSet.this.link[this.curr];
            }
            --ObjectLinkedOpenHashSet.this.size;
            if (this.prev == -1) {
                ObjectLinkedOpenHashSet.this.first = this.next;
            } else {
                int n2 = this.prev;
                ObjectLinkedOpenHashSet.this.link[n2] = ObjectLinkedOpenHashSet.this.link[n2] ^ (ObjectLinkedOpenHashSet.this.link[this.prev] ^ (long)this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            }
            if (this.next == -1) {
                ObjectLinkedOpenHashSet.this.last = this.prev;
            } else {
                int n3 = this.next;
                ObjectLinkedOpenHashSet.this.link[n3] = ObjectLinkedOpenHashSet.this.link[n3] ^ (ObjectLinkedOpenHashSet.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos != ObjectLinkedOpenHashSet.this.n) {
                K[] key = ObjectLinkedOpenHashSet.this.key;
                while (true) {
                    Object curr;
                    int last = pos;
                    pos = last + 1 & ObjectLinkedOpenHashSet.this.mask;
                    while (true) {
                        if ((curr = key[pos]) == null) {
                            key[last] = null;
                            return;
                        }
                        int slot = HashCommon.mix(curr.hashCode()) & ObjectLinkedOpenHashSet.this.mask;
                        if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                        pos = pos + 1 & ObjectLinkedOpenHashSet.this.mask;
                    }
                    key[last] = curr;
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    ObjectLinkedOpenHashSet.this.fixPointers(pos, last);
                }
            }
            ObjectLinkedOpenHashSet.this.containsNull = false;
            ObjectLinkedOpenHashSet.this.key[ObjectLinkedOpenHashSet.this.n] = null;
        }
    }
}

