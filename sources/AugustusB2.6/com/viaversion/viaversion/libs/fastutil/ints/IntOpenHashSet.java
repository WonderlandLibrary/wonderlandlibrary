// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.IntConsumer;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import com.viaversion.viaversion.libs.fastutil.Hash;
import java.io.Serializable;

public class IntOpenHashSet extends AbstractIntSet implements Serializable, Cloneable, Hash
{
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
    
    public IntOpenHashSet(final int expected, final float f) {
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
    }
    
    public IntOpenHashSet(final int expected) {
        this(expected, 0.75f);
    }
    
    public IntOpenHashSet() {
        this(16, 0.75f);
    }
    
    public IntOpenHashSet(final Collection<? extends Integer> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public IntOpenHashSet(final Collection<? extends Integer> c) {
        this(c, 0.75f);
    }
    
    public IntOpenHashSet(final IntCollection c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public IntOpenHashSet(final IntCollection c) {
        this(c, 0.75f);
    }
    
    public IntOpenHashSet(final IntIterator i, final float f) {
        this(16, f);
        while (i.hasNext()) {
            this.add(i.nextInt());
        }
    }
    
    public IntOpenHashSet(final IntIterator i) {
        this(i, 0.75f);
    }
    
    public IntOpenHashSet(final Iterator<?> i, final float f) {
        this(IntIterators.asIntIterator(i), f);
    }
    
    public IntOpenHashSet(final Iterator<?> i) {
        this(IntIterators.asIntIterator(i));
    }
    
    public IntOpenHashSet(final int[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0 : length, f);
        IntArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public IntOpenHashSet(final int[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public IntOpenHashSet(final int[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public IntOpenHashSet(final int[] a) {
        this(a, 0.75f);
    }
    
    public static IntOpenHashSet of() {
        return new IntOpenHashSet();
    }
    
    public static IntOpenHashSet of(final int e) {
        final IntOpenHashSet result = new IntOpenHashSet(1, 0.75f);
        result.add(e);
        return result;
    }
    
    public static IntOpenHashSet of(final int e0, final int e1) {
        final IntOpenHashSet result = new IntOpenHashSet(2, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return result;
    }
    
    public static IntOpenHashSet of(final int e0, final int e1, final int e2) {
        final IntOpenHashSet result = new IntOpenHashSet(3, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!result.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return result;
    }
    
    public static IntOpenHashSet of(final int... a) {
        final IntOpenHashSet result = new IntOpenHashSet(a.length, 0.75f);
        for (final int element : a) {
            if (!result.add(element)) {
                throw new IllegalArgumentException("Duplicate element " + element);
            }
        }
        return result;
    }
    
    public static IntOpenHashSet toSet(final IntStream stream) {
        return stream.collect(IntOpenHashSet::new, IntOpenHashSet::add, IntOpenHashSet::addAll);
    }
    
    public static IntOpenHashSet toSetWithExpectedSize(final IntStream stream, final int expectedSize) {
        if (expectedSize <= 16) {
            return toSet(stream);
        }
        final Object o3;
        return stream.collect(new IntCollections.SizeDecreasingSupplier<IntOpenHashSet>(expectedSize, size -> {
            if (size <= 16) {
                // new(com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet.class)
                new IntOpenHashSet();
            }
            else {
                // new(com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet.class)
                new IntOpenHashSet(size);
            }
            return o3;
        }), IntOpenHashSet::add, IntOpenHashSet::addAll);
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
    public boolean addAll(final IntCollection c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Integer> c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean add(final int k) {
        if (k == 0) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        }
        else {
            final int[] key = this.key;
            int pos;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) != 0) {
                if (curr == k) {
                    return false;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (curr == k) {
                        return false;
                    }
                }
            }
            key[pos] = k;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
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
                Label_0087: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0087;
                        }
                        if (slot > pos) {
                            break Label_0087;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0087;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                continue Label_0006;
            }
            break;
        }
        key[last] = 0;
    }
    
    private boolean removeEntry(final int pos) {
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
    public boolean remove(final int k) {
        if (k == 0) {
            return this.containsNull && this.removeNullEntry();
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(k) & this.mask)]) == 0) {
            return false;
        }
        if (k == curr) {
            return this.removeEntry(pos);
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (k == curr) {
                return this.removeEntry(pos);
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final int k) {
        if (k == 0) {
            return this.containsNull;
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
    public void forEach(final IntConsumer action) {
        if (this.containsNull) {
            action.accept(this.key[this.n]);
        }
        final int[] key = this.key;
        int pos = this.n;
        while (pos-- != 0) {
            if (key[pos] != 0) {
                action.accept(key[pos]);
            }
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
        final int[] key = this.key;
        final int mask = newN - 1;
        final int[] newKey = new int[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(key[i]) & mask)] != 0) {
                while (newKey[pos = (pos + 1 & mask)] != 0) {}
            }
            newKey[pos] = key[i];
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }
    
    public IntOpenHashSet clone() {
        IntOpenHashSet c;
        try {
            c = (IntOpenHashSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.containsNull = this.containsNull;
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        while (j-- != 0) {
            while (this.key[i] == 0) {
                ++i;
            }
            h += this.key[i];
            ++i;
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final IntIterator i = this.iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            s.writeInt(i.nextInt());
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
        int i = this.size;
        while (i-- != 0) {
            final int k = s.readInt();
            int pos;
            if (k == 0) {
                pos = this.n;
                this.containsNull = true;
            }
            else if (key[pos = (HashCommon.mix(k) & this.mask)] != 0) {
                while (key[pos = (pos + 1 & this.mask)] != 0) {}
            }
            key[pos] = k;
        }
    }
    
    private void checkTable() {
    }
    
    private final class SetIterator implements IntIterator
    {
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
            final int[] key = IntOpenHashSet.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != 0) {
                    final int[] array = key;
                    final int pos = this.pos;
                    this.last = pos;
                    return array[pos];
                }
            }
            this.last = Integer.MIN_VALUE;
            return this.wrapped.getInt(-this.pos - 1);
        }
        
        private final void shiftKeys(int pos) {
            final int[] key = IntOpenHashSet.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & IntOpenHashSet.this.mask);
                int curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(curr) & IntOpenHashSet.this.mask;
                    Label_0099: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0099;
                            }
                            if (slot > pos) {
                                break Label_0099;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0099;
                        }
                        pos = (pos + 1 & IntOpenHashSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new IntArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0;
        }
        
        @Override
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == IntOpenHashSet.this.n) {
                IntOpenHashSet.this.containsNull = false;
                IntOpenHashSet.this.key[IntOpenHashSet.this.n] = 0;
            }
            else {
                if (this.pos < 0) {
                    IntOpenHashSet.this.remove(this.wrapped.getInt(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final IntOpenHashSet this$0 = IntOpenHashSet.this;
            --this$0.size;
            this.last = -1;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            final int[] key = IntOpenHashSet.this.key;
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
                }
                else {
                    if (key[this.pos] == 0) {
                        continue;
                    }
                    final int[] array = key;
                    final int pos = this.pos;
                    this.last = pos;
                    action.accept(array[pos]);
                    --this.c;
                }
            }
        }
    }
    
    private final class SetSpliterator implements IntSpliterator
    {
        private static final int POST_SPLIT_CHARACTERISTICS = 257;
        int pos;
        int max;
        int c;
        boolean mustReturnNull;
        boolean hasSplit;
        
        SetSpliterator() {
            this.pos = 0;
            this.max = IntOpenHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = IntOpenHashSet.this.containsNull;
            this.hasSplit = false;
        }
        
        SetSpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            this.pos = 0;
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
        public boolean tryAdvance(final IntConsumer action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++this.c;
                action.accept(IntOpenHashSet.this.key[IntOpenHashSet.this.n]);
                return true;
            }
            final int[] key = IntOpenHashSet.this.key;
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
        public void forEachRemaining(final IntConsumer action) {
            final int[] key = IntOpenHashSet.this.key;
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
            return Math.min(IntOpenHashSet.this.size - this.c, (long)(IntOpenHashSet.this.realSize() / (double)IntOpenHashSet.this.n * (this.max - this.pos)) + (long)(this.mustReturnNull ? 1 : 0));
        }
        
        @Override
        public SetSpliterator trySplit() {
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
            final SetSpliterator split = new SetSpliterator(retPos, retMax, this.mustReturnNull, true);
            this.pos = myNewPos;
            this.mustReturnNull = false;
            this.hasSplit = true;
            return split;
        }
        
        @Override
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
            final int[] key = IntOpenHashSet.this.key;
            while (this.pos < this.max && n > 0L) {
                if (key[this.pos++] != 0) {
                    ++skipped;
                    --n;
                }
            }
            return skipped;
        }
    }
}
