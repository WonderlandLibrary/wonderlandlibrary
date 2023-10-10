// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.SafeMath;
import java.util.function.IntConsumer;
import java.util.Spliterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.io.Serializable;

public class IntArraySet extends AbstractIntSet implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient int[] a;
    private int size;
    
    public IntArraySet(final int[] a) {
        this.a = a;
        this.size = a.length;
    }
    
    public IntArraySet() {
        this.a = IntArrays.EMPTY_ARRAY;
    }
    
    public IntArraySet(final int capacity) {
        this.a = new int[capacity];
    }
    
    public IntArraySet(final IntCollection c) {
        this(c.size());
        this.addAll(c);
    }
    
    public IntArraySet(final Collection<? extends Integer> c) {
        this(c.size());
        this.addAll(c);
    }
    
    public IntArraySet(final IntSet c) {
        this(c.size());
        int i = 0;
        for (final int x : c) {
            this.a[i] = x;
            ++i;
        }
        this.size = i;
    }
    
    public IntArraySet(final Set<? extends Integer> c) {
        this(c.size());
        int i = 0;
        for (final Integer x : c) {
            this.a[i] = x;
            ++i;
        }
        this.size = i;
    }
    
    public IntArraySet(final int[] a, final int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
        }
    }
    
    public static IntArraySet of() {
        return ofUnchecked();
    }
    
    public static IntArraySet of(final int e) {
        return ofUnchecked(e);
    }
    
    public static IntArraySet of(final int... a) {
        if (a.length == 2) {
            if (a[0] == a[1]) {
                throw new IllegalArgumentException("Duplicate element: " + a[1]);
            }
        }
        else if (a.length > 2) {
            IntOpenHashSet.of(a);
        }
        return ofUnchecked(a);
    }
    
    public static IntArraySet ofUnchecked() {
        return new IntArraySet();
    }
    
    public static IntArraySet ofUnchecked(final int... a) {
        return new IntArraySet(a);
    }
    
    private int findKey(final int o) {
        int i = this.size;
        while (i-- != 0) {
            if (this.a[i] == o) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public IntIterator iterator() {
        return new IntIterator() {
            int next = 0;
            
            @Override
            public boolean hasNext() {
                return this.next < IntArraySet.this.size;
            }
            
            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return IntArraySet.this.a[this.next++];
            }
            
            @Override
            public void remove() {
                final int tail = IntArraySet.this.size-- - this.next--;
                System.arraycopy(IntArraySet.this.a, this.next + 1, IntArraySet.this.a, this.next, tail);
            }
            
            @Override
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int remaining = IntArraySet.this.size - this.next;
                if (n < remaining) {
                    this.next += n;
                    return n;
                }
                n = remaining;
                this.next = IntArraySet.this.size;
                return n;
            }
        };
    }
    
    @Override
    public IntSpliterator spliterator() {
        return new Spliterator();
    }
    
    @Override
    public boolean contains(final int k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean remove(final int k) {
        final int pos = this.findKey(k);
        if (pos == -1) {
            return false;
        }
        for (int tail = this.size - pos - 1, i = 0; i < tail; ++i) {
            this.a[pos + i] = this.a[pos + i + 1];
        }
        --this.size;
        return true;
    }
    
    @Override
    public boolean add(final int k) {
        final int pos = this.findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            final int[] b = new int[(this.size == 0) ? 2 : (this.size * 2)];
            int i = this.size;
            while (i-- != 0) {
                b[i] = this.a[i];
            }
            this.a = b;
        }
        this.a[this.size++] = k;
        return true;
    }
    
    @Override
    public void clear() {
        this.size = 0;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public int[] toIntArray() {
        return Arrays.copyOf(this.a, this.size);
    }
    
    @Override
    public int[] toArray(int[] a) {
        if (a == null || a.length < this.size) {
            a = new int[this.size];
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }
    
    public IntArraySet clone() {
        IntArraySet c;
        try {
            c = (IntArraySet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.a = this.a.clone();
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeInt(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new int[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readInt();
        }
    }
    
    private final class Spliterator implements IntSpliterator
    {
        boolean hasSplit;
        int pos;
        int max;
        
        public Spliterator(final IntArraySet x0) {
            this(x0, 0, x0.size, false);
        }
        
        private Spliterator(final int pos, final int max, final boolean hasSplit) {
            this.hasSplit = false;
            assert pos <= max : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }
        
        private int getWorkingMax() {
            return this.hasSplit ? this.max : IntArraySet.this.size;
        }
        
        @Override
        public int characteristics() {
            return 16721;
        }
        
        @Override
        public long estimateSize() {
            return this.getWorkingMax() - this.pos;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            if (this.pos >= this.getWorkingMax()) {
                return false;
            }
            action.accept(IntArraySet.this.a[this.pos++]);
            return true;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            final int max = this.getWorkingMax();
            while (this.pos < max) {
                action.accept(IntArraySet.this.a[this.pos]);
                ++this.pos;
            }
        }
        
        @Override
        public long skip(long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            final int max = this.getWorkingMax();
            if (this.pos >= max) {
                return 0L;
            }
            final int remaining = max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            n = remaining;
            this.pos = max;
            return n;
        }
        
        @Override
        public IntSpliterator trySplit() {
            final int max = this.getWorkingMax();
            final int retLen = max - this.pos >> 1;
            if (retLen <= 1) {
                return null;
            }
            this.max = max;
            final int retMax;
            final int myNewPos = retMax = this.pos + retLen;
            final int oldPos = this.pos;
            this.pos = myNewPos;
            this.hasSplit = true;
            return new Spliterator(oldPos, retMax, true);
        }
    }
}
