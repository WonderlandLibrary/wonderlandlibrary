/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Hash;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollections;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class IntOpenHashSet
extends AbstractIntSet
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;

    public IntOpenHashSet(int expected, float f2) {
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
    }

    public IntOpenHashSet(int expected) {
        this(expected, 0.75f);
    }

    public IntOpenHashSet() {
        this(16, 0.75f);
    }

    public IntOpenHashSet(Collection<? extends Integer> c2, float f2) {
        this(c2.size(), f2);
        this.addAll(c2);
    }

    public IntOpenHashSet(Collection<? extends Integer> c2) {
        this(c2, 0.75f);
    }

    public IntOpenHashSet(IntCollection c2, float f2) {
        this(c2.size(), f2);
        this.addAll(c2);
    }

    public IntOpenHashSet(IntCollection c2) {
        this(c2, 0.75f);
    }

    public IntOpenHashSet(IntIterator i2, float f2) {
        this(16, f2);
        while (i2.hasNext()) {
            this.add(i2.nextInt());
        }
    }

    public IntOpenHashSet(IntIterator i2) {
        this(i2, 0.75f);
    }

    public IntOpenHashSet(Iterator<?> i2, float f2) {
        this(IntIterators.asIntIterator(i2), f2);
    }

    public IntOpenHashSet(Iterator<?> i2) {
        this(IntIterators.asIntIterator(i2));
    }

    public IntOpenHashSet(int[] a2, int offset, int length, float f2) {
        this(length < 0 ? 0 : length, f2);
        IntArrays.ensureOffsetLength(a2, offset, length);
        for (int i2 = 0; i2 < length; ++i2) {
            this.add(a2[offset + i2]);
        }
    }

    public IntOpenHashSet(int[] a2, int offset, int length) {
        this(a2, offset, length, 0.75f);
    }

    public IntOpenHashSet(int[] a2, float f2) {
        this(a2, 0, a2.length, f2);
    }

    public IntOpenHashSet(int[] a2) {
        this(a2, 0.75f);
    }

    public static IntOpenHashSet of() {
        return new IntOpenHashSet();
    }

    public static IntOpenHashSet of(int e2) {
        IntOpenHashSet result = new IntOpenHashSet(1, 0.75f);
        result.add(e2);
        return result;
    }

    public static IntOpenHashSet of(int e0, int e1) {
        IntOpenHashSet result = new IntOpenHashSet(2, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return result;
    }

    public static IntOpenHashSet of(int e0, int e1, int e2) {
        IntOpenHashSet result = new IntOpenHashSet(3, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!result.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return result;
    }

    public static IntOpenHashSet of(int ... a2) {
        IntOpenHashSet result = new IntOpenHashSet(a2.length, 0.75f);
        for (int element : a2) {
            if (result.add(element)) continue;
            throw new IllegalArgumentException("Duplicate element " + element);
        }
        return result;
    }

    public static IntOpenHashSet toSet(IntStream stream) {
        return stream.collect(IntOpenHashSet::new, IntOpenHashSet::add, IntOpenHashSet::addAll);
    }

    public static IntOpenHashSet toSetWithExpectedSize(IntStream stream, int expectedSize) {
        if (expectedSize <= 16) {
            return IntOpenHashSet.toSet(stream);
        }
        return stream.collect(new IntCollections.SizeDecreasingSupplier<IntOpenHashSet>(expectedSize, size -> size <= 16 ? new IntOpenHashSet() : new IntOpenHashSet(size)), IntOpenHashSet::add, IntOpenHashSet::addAll);
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
    public boolean addAll(IntCollection c2) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(c2.size());
        } else {
            this.tryCapacity(this.size() + c2.size());
        }
        return super.addAll(c2);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c2) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(c2.size());
        } else {
            this.tryCapacity(this.size() + c2.size());
        }
        return super.addAll(c2);
    }

    @Override
    public boolean add(int k2) {
        if (k2 == 0) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        } else {
            int[] key = this.key;
            int pos = HashCommon.mix(k2) & this.mask;
            int curr = key[pos];
            if (curr != 0) {
                if (curr == k2) {
                    return false;
                }
                while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                    if (curr != k2) continue;
                    return false;
                }
            }
            key[pos] = k2;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
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
                    return;
                }
                int slot = HashCommon.mix(curr) & this.mask;
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                pos = pos + 1 & this.mask;
            }
            key[last] = curr;
        }
    }

    private boolean removeEntry(int pos) {
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }

    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = 0;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }

    @Override
    public boolean remove(int k2) {
        if (k2 == 0) {
            if (this.containsNull) {
                return this.removeNullEntry();
            }
            return false;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k2) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (k2 == curr) {
            return this.removeEntry(pos);
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (k2 != curr);
        return this.removeEntry(pos);
    }

    @Override
    public boolean contains(int k2) {
        if (k2 == 0) {
            return this.containsNull;
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
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, 0);
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
    public IntIterator iterator() {
        return new SetIterator();
    }

    @Override
    public IntSpliterator spliterator() {
        return new SetSpliterator();
    }

    @Override
    public void forEach(IntConsumer action) {
        if (this.containsNull) {
            action.accept(this.key[this.n]);
        }
        int[] key = this.key;
        int pos = this.n;
        while (pos-- != 0) {
            if (key[pos] == 0) continue;
            action.accept(key[pos]);
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
        int[] key = this.key;
        int mask = newN - 1;
        int[] newKey = new int[newN + 1];
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
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }

    public IntOpenHashSet clone() {
        IntOpenHashSet c2;
        try {
            c2 = (IntOpenHashSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c2.key = (int[])this.key.clone();
        c2.containsNull = this.containsNull;
        return c2;
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        int j2 = this.realSize();
        int i2 = 0;
        while (j2-- != 0) {
            while (this.key[i2] == 0) {
                ++i2;
            }
            h2 += this.key[i2];
            ++i2;
        }
        return h2;
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        IntIterator i2 = this.iterator();
        s2.defaultWriteObject();
        int j2 = this.size;
        while (j2-- != 0) {
            s2.writeInt(i2.nextInt());
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new int[this.n + 1];
        int[] key = this.key;
        int i2 = this.size;
        while (i2-- != 0) {
            int pos;
            int k2 = s2.readInt();
            if (k2 == 0) {
                pos = this.n;
                this.containsNull = true;
            } else {
                pos = HashCommon.mix(k2) & this.mask;
                if (key[pos] != 0) {
                    while (key[pos = pos + 1 & this.mask] != 0) {
                    }
                }
            }
            key[pos] = k2;
        }
    }

    private void checkTable() {
    }

    private final class SetIterator
    implements IntIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNull;
        IntArrayList wrapped;

        private SetIterator() {
            this.pos = IntOpenHashSet.this.n;
            this.last = -1;
            this.c = IntOpenHashSet.this.size;
            this.mustReturnNull = IntOpenHashSet.this.containsNull;
        }

        @Override
        public boolean hasNext() {
            return this.c != 0;
        }

        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = IntOpenHashSet.this.n;
                return IntOpenHashSet.this.key[IntOpenHashSet.this.n];
            }
            int[] key = IntOpenHashSet.this.key;
            do {
                if (--this.pos >= 0) continue;
                this.last = Integer.MIN_VALUE;
                return this.wrapped.getInt(-this.pos - 1);
            } while (key[this.pos] == 0);
            this.last = this.pos;
            return key[this.last];
        }

        private final void shiftKeys(int pos) {
            int[] key = IntOpenHashSet.this.key;
            while (true) {
                int curr;
                int last = pos;
                pos = last + 1 & IntOpenHashSet.this.mask;
                while (true) {
                    if ((curr = key[pos]) == 0) {
                        key[last] = 0;
                        return;
                    }
                    int slot = HashCommon.mix(curr) & IntOpenHashSet.this.mask;
                    if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                    pos = pos + 1 & IntOpenHashSet.this.mask;
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new IntArrayList(2);
                    }
                    this.wrapped.add(key[pos]);
                }
                key[last] = curr;
            }
        }

        @Override
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == IntOpenHashSet.this.n) {
                IntOpenHashSet.this.containsNull = false;
                IntOpenHashSet.this.key[IntOpenHashSet.this.n] = 0;
            } else if (this.pos >= 0) {
                this.shiftKeys(this.last);
            } else {
                IntOpenHashSet.this.remove(this.wrapped.getInt(-this.pos - 1));
                this.last = -1;
                return;
            }
            --IntOpenHashSet.this.size;
            this.last = -1;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            int[] key = IntOpenHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = IntOpenHashSet.this.n;
                action.accept(key[IntOpenHashSet.this.n]);
                --this.c;
            }
            while (this.c != 0) {
                if (--this.pos < 0) {
                    this.last = Integer.MIN_VALUE;
                    action.accept(this.wrapped.getInt(-this.pos - 1));
                    --this.c;
                    continue;
                }
                if (key[this.pos] == 0) continue;
                this.last = this.pos;
                action.accept(key[this.last]);
                --this.c;
            }
        }
    }

    private final class SetSpliterator
    implements IntSpliterator {
        private static final int POST_SPLIT_CHARACTERISTICS = 257;
        int pos = 0;
        int max;
        int c;
        boolean mustReturnNull;
        boolean hasSplit;

        SetSpliterator() {
            this.max = IntOpenHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = IntOpenHashSet.this.containsNull;
            this.hasSplit = false;
        }

        SetSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            this.max = IntOpenHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = IntOpenHashSet.this.containsNull;
            this.hasSplit = false;
            this.pos = pos;
            this.max = max;
            this.mustReturnNull = mustReturnNull;
            this.hasSplit = hasSplit;
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++this.c;
                action.accept(IntOpenHashSet.this.key[IntOpenHashSet.this.n]);
                return true;
            }
            int[] key = IntOpenHashSet.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != 0) {
                    ++this.c;
                    action.accept(key[this.pos++]);
                    return true;
                }
                ++this.pos;
            }
            return false;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            int[] key = IntOpenHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                action.accept(key[IntOpenHashSet.this.n]);
                ++this.c;
            }
            while (this.pos < this.max) {
                if (key[this.pos] != 0) {
                    action.accept(key[this.pos]);
                    ++this.c;
                }
                ++this.pos;
            }
        }

        @Override
        public int characteristics() {
            return this.hasSplit ? 257 : 321;
        }

        @Override
        public long estimateSize() {
            if (!this.hasSplit) {
                return IntOpenHashSet.this.size - this.c;
            }
            return Math.min((long)(IntOpenHashSet.this.size - this.c), (long)((double)IntOpenHashSet.this.realSize() / (double)IntOpenHashSet.this.n * (double)(this.max - this.pos)) + (long)(this.mustReturnNull ? 1 : 0));
        }

        @Override
        public SetSpliterator trySplit() {
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
            SetSpliterator split = new SetSpliterator(retPos, retMax, this.mustReturnNull, true);
            this.pos = myNewPos;
            this.mustReturnNull = false;
            this.hasSplit = true;
            return split;
        }

        @Override
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
            int[] key = IntOpenHashSet.this.key;
            while (this.pos < this.max && n2 > 0L) {
                if (key[this.pos++] == 0) continue;
                ++skipped;
                --n2;
            }
            return skipped;
        }
    }
}

